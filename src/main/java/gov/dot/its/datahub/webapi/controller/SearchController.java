package gov.dot.its.datahub.webapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.datahub.webapi.business.SearchService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;

	@PostMapping(value = "/v1/search", headers = "Accept=application/json")
	public ResponseEntity<ApiResponse<SearchResponseModel<List<DataAsset>>>> searchDataAssets(HttpServletRequest request, @RequestBody SearchRequestModel searchRequestModel) {

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
