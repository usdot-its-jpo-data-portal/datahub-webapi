package gov.dot.its.datahub.webapi.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.dot.its.datahub.webapi.Utils.ApiUtils;
import gov.dot.its.datahub.webapi.dao.MetricsDao;
import gov.dot.its.datahub.webapi.model.ApiError;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHMetrics;

@Service
public class MetricsServiceImpl implements MetricsService {

	private static final Logger logger = LoggerFactory.getLogger(DataAssetServiceImpl.class);
	private static final String MESSAGE_TEMPLATE = "%s : %s %s";

	@Value("${datahub.webapi.es.limit}")
	private int esDefaultLimit;

	@Autowired
	private ApiUtils apiUtils;

	@Autowired
	private MetricsDao metricsDao;
	
	@Override
	public ApiResponse<List<DHMetrics>> getMetrics(HttpServletRequest request) {

		final String RESPONSE_FIND_ALL = "Response: Find all, {}";
		ApiResponse<List<DHMetrics> > apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {
			List<DHMetrics> data = metricsDao.getMetrics(esDefaultLimit); // DHRepository or use DataAssets

			if (data != null && !data.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, data, null, errors, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.OK);
				
			} else {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.NO_CONTENT);
				
			}

		} catch (ElasticsearchStatusException | IOException e) {
			logger.info(apiUtils.stringFormat(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
		return apiResponse;

	}

	@Override
	public ApiResponse<List<DHMetrics>> getMetricsBySource(HttpServletRequest request, String dhSourceName) throws IOException {
		// TODO Auto-generated method stub

		final String RESPONSE_FIND_ALL = "Response: Find by dhSourceName, {}";
		ApiResponse<List<DHMetrics> > apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {
			List<DHMetrics> data = metricsDao.getMetrics(esDefaultLimit, dhSourceName); // DHRepository or use DataAssets

			if (data != null && !data.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, data, null, errors, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.OK);
				
			} else {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.NO_CONTENT);
				
			}

		} catch (ElasticsearchStatusException e) {
			logger.info(apiUtils.stringFormat(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}

		return apiResponse;
	}
}
