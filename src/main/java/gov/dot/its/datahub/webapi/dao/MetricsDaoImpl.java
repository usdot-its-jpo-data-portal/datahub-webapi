package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

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
    public List<DHMetrics> getMetrics(int esDefaultLimit) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(esDefaultLimit);

        //QueryBuilder queryBuilder = this.generateQueryForSourceNames(sourceName);
        //searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.fetchSource(includedFieldsMetrics, new String[] {}); // includeFieldsMetrics
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<DHMetrics> result = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new StdDateFormat());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DHMetrics dataMetrics = mapper.convertValue(sourceMap, DHMetrics.class);
            dataMetrics.setTimeStamp((String) sourceMap.get("timestamp"));
            result.add(dataMetrics);

        }


        return result;
	}

    @Override
    public List<DHMetrics> getMetrics(int esDefaultLimit, String dhSourceName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(esDefaultLimit);

        QueryBuilder queryBuilder = this.generateQueryForSourceNames(dhSourceName);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.fetchSource(includedFieldsMetrics, new String[] {}); // includeFieldsMetrics
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<DHMetrics> result = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new StdDateFormat());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DHMetrics dataMetrics = mapper.convertValue(sourceMap, DHMetrics.class);
            dataMetrics.setTimeStamp((String) sourceMap.get("timestamp"));
            result.add(dataMetrics);

        }


        return result;
    }

    private QueryBuilder generateQueryForSourceNames(String dhSourceName) {
		if (dhSourceName == null) {
			return QueryBuilders.boolQuery();
		}

		return QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("dhSourceName", dhSourceName));
	}

    @Override
    public List<DHMetrics> getMetrics(int esDefaultLimit, String beginDate, String endDate) throws IOException, ParseException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(esDefaultLimit);


        QueryBuilder queryBuilder = this.generateQueryForDateRange(beginDate, endDate);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.fetchSource(includedFieldsMetrics, new String[] {}); // includeFieldsMetrics
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = esClientDao.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<DHMetrics> result = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new StdDateFormat());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DHMetrics dataMetrics = mapper.convertValue(sourceMap, DHMetrics.class);
            dataMetrics.setTimeStamp((String) sourceMap.get("timestamp"));
            result.add(dataMetrics);

        }


        return result;
    }

    private QueryBuilder generateQueryForDateRange(String beginDate, String endDate) throws ParseException {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        Date bdate = simpleDate.parse(beginDate);
        Date eDate = simpleDate.parse(endDate);

        System.out.println(simpleDate.format(bdate));
        System.out.println(simpleDate.format(eDate));

        return QueryBuilders.rangeQuery("timestamp").to(eDate).from(bdate);
        //return QueryBuilders.rangeQuery("timestamp").gte(simpleDate.format(bdate));
    }

}

