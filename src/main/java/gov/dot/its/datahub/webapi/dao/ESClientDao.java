package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;

public interface ESClientDao {
	SearchResponse search(SearchRequest searchRequest, RequestOptions requestOptions) throws IOException;
	GetResponse get(GetRequest getRequest, RequestOptions requestOptions) throws IOException;
	SearchTemplateResponse searchTemplate(SearchTemplateRequest searchTemplateRequest, RequestOptions requestOptions);
}
