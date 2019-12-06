package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.RelatedModel;

@Repository
public class RelatedDaoImpl implements RelatedDao {

	@Value("${datahub.webapi.es.related}")
	private String relatedIndex;

	@Value("${codehub.ui.url.endpoint}")
	private String codeHubEndPoint;

	@Value("${codehub.ui.url.questring}")
	private String codeHubQueryString;

	private RestHighLevelClient restHighLevelClient;

	public RelatedDaoImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public List<RelatedItemModel> getRelatedItems(String id) throws IOException {

		GetRequest getRequest = new GetRequest(relatedIndex, "_doc", id);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);


		if (getResponse.isExists()) {

			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			RelatedModel relatedModel = mapper.convertValue(sourceAsMap, RelatedModel.class);

			this.buildTargetUrls(relatedModel);

			return relatedModel.getUrls();
		}

		return null;
	}

	private void buildTargetUrls(RelatedModel relatedModel) {
		if (relatedModel == null || relatedModel.getUrls() == null || relatedModel.getUrls().isEmpty()) {
			return;
		}

		for(RelatedItemModel item: relatedModel.getUrls()) {
			String url = this.codeHubEndPoint + this.codeHubQueryString + item.getId();
			item.setUrl(url);
		}
	}

}