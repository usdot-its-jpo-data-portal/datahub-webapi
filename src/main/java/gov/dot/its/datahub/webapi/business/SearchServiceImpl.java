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
import org.springframework.util.StringUtils;

import gov.dot.its.datahub.webapi.dao.DataAssetDao;
import gov.dot.its.datahub.webapi.dao.RelatedDao;
import gov.dot.its.datahub.webapi.model.ApiError;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

@Service
public class SearchServiceImpl implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	private DataAssetDao dataAssetDao;

	@Autowired
	private RelatedDao relatedDao;

	@Value("${datahub.webapi.debug}")
	private boolean isDebug;

	@Override
	public ApiResponse<SearchResponseModel<List<DataAsset>>> searchDataAssets(HttpServletRequest request, SearchRequestModel searchRequestModel) {
		logger.info("Request: Search Data Asset.");
		final String RESPONSE_SEARCH_DATA = "Response: Search Data Asset, {}";

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		if (searchRequestModel == null || StringUtils.isEmpty(searchRequestModel.getTerm())) {
			errors.add(new ApiError("Invalid search request."));
			apiResponse.setResponse(HttpStatus.BAD_REQUEST, null, null, errors, request);
			logger.warn(RESPONSE_SEARCH_DATA, HttpStatus.BAD_REQUEST);
			return apiResponse;
		}

		try {
			SearchResponseModel<List<DataAsset>> searchResponse = null;

			if (!searchRequestModel.isPhrase()) {
				searchResponse = dataAssetDao.searchDataAssetsByWords(searchRequestModel);
			} else {
				searchResponse = dataAssetDao.searchDataAssetsByPhrase(searchRequestModel);
			}

			searchResponse.setSearchRequest(searchRequestModel);
			if (searchResponse.getResult() != null && !searchResponse.getResult().isEmpty()) {

				this.getRelatedInformation(searchResponse);

				apiResponse.setResponse(HttpStatus.OK, searchResponse, null, null, request);
				logger.info(RESPONSE_SEARCH_DATA, HttpStatus.OK);
				return apiResponse;
			} else {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, searchResponse, null, null, request);
				logger.info(RESPONSE_SEARCH_DATA, HttpStatus.NO_CONTENT);
				return apiResponse;
			}

		} catch(IOException |ElasticsearchStatusException e) {
			logger.error(e.getMessage());
			if (isDebug) {
				logger.error(e.toString());
			}
			errors.add(new ApiError(e.getMessage()));
			if (e.getSuppressed().length > 0) {
				for (Throwable x : e.getSuppressed()) {
					errors.add(new ApiError(x.toString()));
				}
			}
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);
			return apiResponse;
		}
	}

	private void getRelatedInformation(SearchResponseModel<List<DataAsset>> searchResponse) {
		if (searchResponse == null || searchResponse.getResult() == null || searchResponse.getResult().isEmpty()) {
			return;
		}

		for(DataAsset dataAsset: searchResponse.getResult()) {
			List<RelatedItemModel> relatedItems;
			try {
				relatedItems = relatedDao.getRelatedItems(dataAsset.getDhId());
				if (relatedItems.isEmpty()){
					continue;
				}
				dataAsset.setRelated(relatedItems);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

}
