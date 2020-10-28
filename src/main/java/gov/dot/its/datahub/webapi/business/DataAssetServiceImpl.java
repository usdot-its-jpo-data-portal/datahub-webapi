package gov.dot.its.datahub.webapi.business;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gov.dot.its.datahub.webapi.dao.DataAssetDao;
import gov.dot.its.datahub.webapi.model.ApiError;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;

@Service
public class DataAssetServiceImpl implements DataAssetService {

	private static final Logger logger = LoggerFactory.getLogger(DataAssetServiceImpl.class);

	@Autowired
	private DataAssetDao dataAssetDao;

	@Value("${datahub.webapi.debug}")
	private boolean isDebug;

	@Override
	public ApiResponse<List<DataAsset>> findAll(HttpServletRequest request, Map<String, String> params) {
		logger.info("Request: Find all.");
		final String RESPONSE_FIND_ALL = "Response: Find all, {}";

		ApiResponse<List<DataAsset>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		String sortBy = getQueryParamString(params, "sortBy", null);
		String sortOrder = getQueryParamString(params, "sortOrder", null);
		int limit = getQueryParamInteger(params, "limit", 10);

		if (sortBy != null && !StringUtils.isEmpty(sortBy) && !orderByFieldIsValid(sortBy)) {
			errors.add(new ApiError(String.format("Invalid sortBy field [%s].", sortBy)));
			apiResponse.setResponse(HttpStatus.BAD_REQUEST, null, null, errors, request);
			logger.warn(RESPONSE_FIND_ALL, HttpStatus.BAD_REQUEST);
			return apiResponse;
		}

		try {

			List<DataAsset> data = dataAssetDao.getDataAssets(sortBy, sortOrder, limit);

			if (data != null && !data.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, data, null, null, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.OK);
				return apiResponse;
			} else {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
				logger.info(RESPONSE_FIND_ALL, HttpStatus.NO_CONTENT);
				return apiResponse;
			}

		} catch(ElasticsearchStatusException | IOException e) {
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

	@Override
	public ApiResponse<DataAsset> findById(HttpServletRequest request, String id) {
		logger.info("Request: Find by id.");
		final String RESPONSE_BY_ID = "Response: Find by id, {}";
		ApiResponse<DataAsset> apiResponse = new ApiResponse<>();

		try {

			DataAsset data = dataAssetDao.getDataAsset(id);

			if (data != null) {
				apiResponse.setResponse(HttpStatus.OK, data, null, null, request);
				logger.info(RESPONSE_BY_ID, HttpStatus.OK);
				return apiResponse;
			} else {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
				logger.info(RESPONSE_BY_ID, HttpStatus.NO_CONTENT);
				return apiResponse;
			}

		} catch(IOException e) {
			logger.error(e.getMessage());
			if (isDebug) {
				logger.error(e.toString());
			}
			ArrayList<ApiError> errors = new ArrayList<>();
			errors.add(new ApiError(e.getMessage()));
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

			return apiResponse;
		}
	}

	private boolean orderByFieldIsValid(String sortBy) {
		if (sortBy == null || StringUtils.isEmpty(sortBy)) {
			return false;
		}

		Field[] fields = DataAsset.class.getDeclaredFields();
		for(Field field : fields) {
			if (sortBy.equals(field.getName())) {
				return this.allowedFieldType(field);
			}
		}
		return false;
	}

	private boolean allowedFieldType(Field field) {
		return field.getType() == Integer.class || field.getType() == java.sql.Timestamp.class;
	}

	private String getQueryParamString(Map<String, String> params,String name, String defaultValue) {
		for(Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return defaultValue;
	}

	private int getQueryParamInteger(Map<String, String> params, String name, int defaultValue) {
		String val = this.getQueryParamString(params, name, null);
		if (val == null) {
			return defaultValue;
		}

		int result = -1;
		try {
			result = Integer.parseInt(val);
		} catch(NumberFormatException e) {
			result = defaultValue;
		}

		return result;
	}
}
