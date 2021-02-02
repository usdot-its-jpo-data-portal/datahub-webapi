package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import gov.dot.its.datahub.webapi.model.DHMetrics;

@Repository
public class MetricsDaoImpl implements MetricsDao {

	@Value("${datahub.webapi.es.metrics.fields}")
	private String[] includedFieldsMetrics;
	@Value("${datahub.webapi.es.limit}")
    private int esDefaultLimit;
    @Value("${datahub.webapi.es.metrics.index}")
	private String index;


    @Autowired
    private ESClientDao esClientDao;
    
	@Override
	public List<DHMetrics> getMetrics(int esDefaultLimit, String[] sourceName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(esDefaultLimit);
		
		//the queryBuiklder ?
		QueryBuilder queryBuilder = this.generateQueryForSourceNames(sourceName);
		searchSourceBuilder.query(queryBuilder);
		searchSourceBuilder.fetchSource(includedFieldsMetrics, new String[] {});  //includeFieldsMetrics
		searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        
        List<DHMetrics> result = new ArrayList<>();
        for(SearchHit hit : searchHits)
        {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DHMetrics dataMetrics = mapper.convertValue(sourceMap, DHMetrics.class);
            result.add(dataMetrics);

        }


        return result;
	}
    
    private QueryBuilder generateQueryForSourceNames(String[] sourceName) {
		if (sourceName == null || sourceName.length==0) {
			return QueryBuilders.boolQuery();
		}

		return QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("dhSourceName.keyword", sourceName));
	}
}
