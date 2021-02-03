package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

@Repository
public class DataAssetDaoImpl implements DataAssetDao {
	@Value("${datahub.webapi.es.index}")
	private String index;

	private static final String HIGHLIGHTER_TYPE = "plain";
	private static final int FRAGMENT_SIZE = 5000;
	private static final int NUMBER_OF_FRAGMENTS = 5;
	private static final String MASK_TAG = "its-datahub-hide";

	@Autowired
	private ESClientDao esClientDao;

	@Override
	public List<DataAsset> getDataAssets(String sortBy, String sortDirection, Integer limit) throws IOException {
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		searchSourceBuilder.size(limit);
		if (!StringUtils.isEmpty(sortBy)) {
			SortOrder sortOrder = SortOrder.ASC;
			if (!StringUtils.isEmpty(sortDirection) && sortDirection.equalsIgnoreCase("desc")) {
				sortOrder = SortOrder.DESC;
			}
			searchSourceBuilder.sort(sortBy, sortOrder);
		}
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = null;

		searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();

		SearchHit[] searchHits = hits.getHits();

		List<DataAsset> result = new ArrayList<>();
		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataAsset dataAsset = mapper.convertValue(sourceAsMap, DataAsset.class);
			dataAsset.setEsScore(getHitScore(hit));
			if (dataAsset.getTags().contains(MASK_TAG)){
				continue;
			}
			result.add(dataAsset);
		}

		return result;
	}

	@Override
	public DataAsset getDataAsset(String id) throws IOException {
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("dhId", id));

		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = null;

		searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();

		SearchHit[] searchHits = hits.getHits();

		DataAsset result = null;
		if (searchHits.length > 0) {
			SearchHit hit = searchHits[0];
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			result = mapper.convertValue(sourceAsMap, DataAsset.class);
			result.setEsScore(getHitScore(hit));
			if (result.getTags().contains(MASK_TAG)){
				result = null;
			}
		}

		return result;
	}

	@Override
	public SearchResponseModel<List<DataAsset>> searchDataAssetsByWords(SearchRequestModel searchRequestModel) throws IOException {

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchRequestModel.getTerm(), "name", "description", "tags"));

		searchSourceBuilder.size(searchRequestModel.getLimit());
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<em class=\"search-highlight\">");
		highlightBuilder.postTags("</em>");

		HighlightBuilder.Field highlightName = new HighlightBuilder.Field("name");
		highlightName.highlighterType(HIGHLIGHTER_TYPE);
		highlightName.fragmentSize(FRAGMENT_SIZE);
		highlightName.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightName);

		HighlightBuilder.Field highlightDescription = new HighlightBuilder.Field("description");
		highlightDescription.highlighterType(HIGHLIGHTER_TYPE);
		highlightDescription.fragmentSize(FRAGMENT_SIZE);
		highlightDescription.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightDescription);

		HighlightBuilder.Field highlightTags = new HighlightBuilder.Field("tags");
		highlightTags.highlighterType(HIGHLIGHTER_TYPE);
		highlightTags.fragmentSize(FRAGMENT_SIZE);
		highlightTags.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightTags);

		searchSourceBuilder.highlighter(highlightBuilder);

		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = null;

		searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = searchResponse.getHits();

		return this.getSearchResponseData(hits);
	}

	@Override
	public SearchResponseModel<List<DataAsset>> searchDataAssetsByPhrase(SearchRequestModel searchRequestModel) throws IOException {

		SearchTemplateRequest searchTemplateRequest = new SearchTemplateRequest();
		searchTemplateRequest.setRequest(new SearchRequest(index));
		searchTemplateRequest.setScriptType(ScriptType.INLINE);
		String queryTemplate = "{\n" +
				"    \"query\": {\n" +
				"      \"bool\": {\n" +
				"        \"should\": [\n" +
				"          {\"match\": {\n" +
				"            \"_all\": {\n" +
				"              \"query\": \"{{term}}\"\n" +
				"            }\n" +
				"          }\n" +
				"          },\n" +
				"          {\"match_phrase\": { \"name\": { \"query\": \"{{term}}\" } } },\n" +
				"          {\"match_phrase\": { \"description\": { \"query\": \"{{term}}\" } } },\n" +
				"          {\"match_phrase\": { \"tags\": { \"query\": \"{{term}}\"} } }\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    \"highlight\" : {\n" +
				"      \"pre_tags\": [\"<em class=\\\"search-highlight\\\">\"],\n" +
				"      \"post_tags\": [\"</em>\"],\n" +
				"      \"fields\" : {\n" +
				"        \"description\": {\"fragment_size\" : {{fragmentSize}}, \"number_of_fragments\" : {{numFragments}}, \"type\": \"{{highliterType}}\"},\n" +
				"        \"name\": {\"fragment_size\" : {{fragmentSize}}, \"number_of_fragments\" : {{numFragments}}, \"type\": \"{{highliterType}}\"},\n" +
				"        \"tags\": {\"fragment_size\" : {{fragmentSize}}, \"number_of_fragments\" : {{numFragments}}, \"type\": \"{{highliterType}}\"}\n" +
				"      }\n" +
				"    },\n" +
				"    \"size\": \"{{size}}\"\n" +
				"}";
		searchTemplateRequest.setScript(queryTemplate);
		Map<String, Object> scriptParams = new HashMap<>();
		scriptParams.put("term", searchRequestModel.getTerm());
		scriptParams.put("size", searchRequestModel.getLimit());
		scriptParams.put("fragmentSize", FRAGMENT_SIZE);
		scriptParams.put("numFragments", NUMBER_OF_FRAGMENTS);
		scriptParams.put("highliterType", HIGHLIGHTER_TYPE);
		searchTemplateRequest.setScriptParams(scriptParams);

		SearchTemplateResponse searchTemplateResponse = esClientDao.searchTemplate(searchTemplateRequest, RequestOptions.DEFAULT);

		SearchHits hits = searchTemplateResponse.getResponse().getHits();

		return this.getSearchResponseData(hits);
	}

	private SearchResponseModel<List<DataAsset>> getSearchResponseData(SearchHits hits) {
		SearchResponseModel<List<DataAsset>> searchResponseModel = new SearchResponseModel<>();

		searchResponseModel.setMaxScore(hits.getMaxScore());

		SearchHit[] searchHits = hits.getHits();
		searchResponseModel.setNumHits(searchHits.length);

		List<DataAsset> result = new ArrayList<>();
		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataAsset dataAsset = mapper.convertValue(sourceAsMap, DataAsset.class);
			if (dataAsset.getTags().contains(MASK_TAG)){
				continue;
			}
			dataAsset.setEsScore(getHitScore(hit));
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for(Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				HighlightField field = entry.getValue();
				for(Text text : field.fragments()) {
					if (dataAsset.getHighlights() != null && dataAsset.getHighlights().containsKey(entry.getKey())) {
						dataAsset.getHighlights().get(entry.getKey()).add(text.toString());
					} else {
						List<String> frags = new ArrayList<>();
						frags.add(text.toString());
						dataAsset.getHighlights().put(entry.getKey(), frags);
					}
				}
			}

			result.add(dataAsset);
		}

		searchResponseModel.setResult(result);

		return searchResponseModel;
	}

	private Float getHitScore(SearchHit hit) {
		return Float.isNaN(hit.getScore()) ? null : hit.getScore();
	}
}
