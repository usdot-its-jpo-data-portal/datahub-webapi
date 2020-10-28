package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import gov.dot.its.datahub.webapi.model.DHConfiguration;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

	@Value("${datahub.webapi.configurations.index}")
	private String configurationIndex;

	@Value("${datahub.webapi.configurations.default}")
	private String configurationId;

	@Autowired
	private ESClientDao esClientDao;

	@Override
	public List<DHEngagementPopup> getEngagementPopups() throws IOException {
		GetRequest getRequest = new GetRequest(configurationIndex, configurationId);
		GetResponse getResponse = esClientDao.get(getRequest, RequestOptions.DEFAULT);

		if (!getResponse.isExists()) {
			return new ArrayList<>();
		}

		Map<String, Object> sourceMap = getResponse.getSourceAsMap();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DHConfiguration configuration = mapper.convertValue(sourceMap, DHConfiguration.class);

		return configuration.getEngagementPopups().stream().filter(DHEngagementPopup::isActive).collect(Collectors.toList());
	}
}
