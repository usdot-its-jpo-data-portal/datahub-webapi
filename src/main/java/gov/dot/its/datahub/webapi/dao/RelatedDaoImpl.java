package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
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

	@Autowired
	private ESClientDao esClientDao;

	@Override
	public List<RelatedItemModel> getRelatedItems(String id) throws IOException {

		GetRequest getRequest = new GetRequest(relatedIndex, id);
		GetResponse getResponse = esClientDao.get(getRequest, RequestOptions.DEFAULT);


		if (getResponse.isExists()) {

			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			RelatedModel relatedModel = mapper.convertValue(sourceAsMap, RelatedModel.class);

			this.buildTargetUrls(relatedModel);

			return relatedModel.getUrls();
		}

		return new ArrayList<>();
	}

	private void buildTargetUrls(RelatedModel relatedModel) {
		if (relatedModel == null || relatedModel.getUrls().isEmpty()) {
			return;
		}

		for(RelatedItemModel item: relatedModel.getUrls()) {
			String url = this.codeHubEndPoint + this.codeHubQueryString + item.getId();
			item.setUrl(url);
		}
	}

}
