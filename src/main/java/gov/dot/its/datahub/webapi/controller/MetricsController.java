package gov.dot.its.datahub.webapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.datahub.webapi.business.MetricsService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHMetrics;

@RestController
public class MetricsController {  

    @Autowired
    private MetricsService dhMetricsService;

    @GetMapping(value="/v1/metrics", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<DHMetrics>>> metrics(HttpServletRequest request) {

		ApiResponse<List<DHMetrics>> apiResponse = dhMetricsService.getMetrics(request, null);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
    
}
