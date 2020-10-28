package gov.dot.its.datahub.webapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.datahub.webapi.business.DataAssetService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;

@RestController
public class DataAssetController {

	@Autowired
	private DataAssetService dataAssetService;

	@GetMapping(value="/v1/dataassets", headers = "Accept=application/json")
	public ResponseEntity<ApiResponse<List<DataAsset>>> datasets(HttpServletRequest request, @RequestParam Map<String, String> params) { 

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}

	@GetMapping(value="/v1/dataassets/{id}", headers = "Accept=application/json")
	public ResponseEntity<ApiResponse<DataAsset>> dataset(HttpServletRequest request, @PathVariable String id) {

		 ApiResponse<DataAsset> apiResponse = dataAssetService.findById(request, id);

		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}

}
