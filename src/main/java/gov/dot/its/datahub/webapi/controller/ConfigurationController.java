package gov.dot.its.datahub.webapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.datahub.webapi.business.ConfigurationService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@RestController
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@GetMapping(value="/v1/configurations/engagementpopups", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<DHEngagementPopup>>> engagementPopups(HttpServletRequest request) {

		ApiResponse<List<DHEngagementPopup>> apiResponse = configurationService.engagementPopups(request);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
