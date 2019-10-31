package gov.dot.its.datahub.webapi.business;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;

public interface DataAssetService {
	ApiResponse<List<DataAsset>> findAll(HttpServletRequest request, Map<String, String> params);

	ApiResponse<DataAsset> findById(HttpServletRequest request, String id);
}
