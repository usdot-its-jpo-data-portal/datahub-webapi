package gov.dot.its.datahub.webapi.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

		ApiResponse<List<DHMetrics>> apiResponse = dhMetricsService.getMetrics(request);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/metrics/{dhSourceName}", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<DHMetrics>>> metric(HttpServletRequest request, @RequestParam String dhSourceName) throws IOException {

		ApiResponse<List<DHMetrics>> apiResponse = dhMetricsService.getMetricsBySource(request, dhSourceName);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
    
	@GetMapping(value="/v1/metrics/getbydate/{beginDate}/{endDate}", headers="Accept=application/json", produces="application/json")
	//public ResponseEntity<ApiResponse<List<DHMetrics>>> metric(HttpServletRequest request, @RequestParam String beginDate, @RequestParam int endDate) throws IOException {
	public ResponseEntity<ApiResponse<List<DHMetrics>>> metric(HttpServletRequest request, @PathVariable String beginDate, @PathVariable String endDate) throws IOException, ParseException {

		ApiResponse<List<DHMetrics>> apiResponse = dhMetricsService.getMetricsByDate(request, beginDate, endDate);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
