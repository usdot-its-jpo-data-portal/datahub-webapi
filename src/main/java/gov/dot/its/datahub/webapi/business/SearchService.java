package gov.dot.its.datahub.webapi.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

public interface SearchService {

	ApiResponse<SearchResponseModel<List<DataAsset>>> searchDataAssets(HttpServletRequest request, SearchRequestModel searchRequestModel);

}
