package gov.dot.its.datahub.webapi;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

public class MockESUtils {
	private ObjectMapper mapper;

	public MockESUtils() {
		this.mapper = new ObjectMapper();
	}

	public SearchResponse generateFakeSearchResponse(Object sourceObject) throws JsonProcessingException {
		String jsonObject = sourceObject != null ? mapper.writeValueAsString(sourceObject) : "";
		BytesReference source = new BytesArray(jsonObject);
		Map<String, HighlightField> highlightFields = new HashMap<>();
		Text[] fragments = new Text[]{new Text("one"), new Text("two")};
		HighlightField highLightField = new HighlightField("test1", fragments);
		highlightFields.put("one", highLightField);
		SearchHit hit = new SearchHit(sourceObject != null ? 1 : 0);
		hit.sourceRef( source );
		hit.highlightFields(highlightFields);
		SearchHit[] searchHitArray = sourceObject != null ? new SearchHit[] { hit } : new SearchHit[] {};
		SearchHits hits = new SearchHits( searchHitArray, null, 1 );
		SearchResponseSections searchResponseSections = new SearchResponseSections( hits, null, null, false, null, null, 5 );
		SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 8, 8, 0, 8, new ShardSearchFailure[] {}, null);

		return searchResponse;
	}

	public GetResponse generateFakeGetResponse(Object sourceObject) throws JsonProcessingException {
		String index = "index";
		String type = "type";
		String id = "id";
		long seqNo = sourceObject != null ? 1L : -2;
		long primaryTerm = sourceObject != null ? 1L : 0;
		long version = 1;
		boolean exists = sourceObject != null;

		String jsonObject = sourceObject != null ? mapper.writeValueAsString(sourceObject) : "";
		BytesReference source = new BytesArray(jsonObject);

		Map<String, DocumentField> fields = sourceObject != null ? new HashMap<>() : null;
		Map<String, DocumentField> metaFields = sourceObject != null ? new HashMap<>() : null;
		GetResult getResult = new GetResult(index, type, id, seqNo, primaryTerm, version, exists, source, fields, metaFields);
		GetResponse getResponse = new GetResponse(getResult);
		return getResponse;
	}

}
