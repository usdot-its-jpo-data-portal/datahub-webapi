package gov.dot.its.datahub.webapi.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.dot.its.datahub.webapi.dao.ConfigurationDao;
import gov.dot.its.datahub.webapi.model.ApiError;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	@Autowired
	private ConfigurationDao configurationDao;

	@Value("${datahub.webapi.debug}")
	private boolean isDebug;

	@Override
	public ApiResponse<List<DHEngagementPopup>> engagementPopups(HttpServletRequest request) {
		logger.info("Request: Engagement Popups.");
		final String RESPONSE_MSG = "Response: GET Engagement Popups. {}";

		ApiResponse<List<DHEngagementPopup>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {
			List<DHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();

			if (engagementPopups != null && !engagementPopups.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, engagementPopups, null, null, request);
				logger.info(RESPONSE_MSG, HttpStatus.OK);
				return apiResponse;
			}

			apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			logger.info(RESPONSE_MSG, HttpStatus.NO_CONTENT);
			return apiResponse;
		} catch(ElasticsearchException | IOException e) {
			errors.add(new ApiError(e.getMessage()));
			logger.error(e.getMessage());
			if (isDebug) {
				logger.error(e.toString());
			}
			if (e.getSuppressed().length > 0) {
				for (Throwable x : e.getSuppressed()) {
					errors.add(new ApiError(x.toString()));
				}
			}
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);
			return apiResponse;
		}
	}
}
