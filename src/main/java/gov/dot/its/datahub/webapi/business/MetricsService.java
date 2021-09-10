package gov.dot.its.datahub.webapi.business;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHMetrics;

public interface MetricsService {

	ApiResponse<List<DHMetrics>> getMetrics(HttpServletRequest request);
	ApiResponse<List<DHMetrics>> getMetricsBySource(HttpServletRequest request, String dhSourceName) throws IOException;
	ApiResponse<List<DHMetrics>> getMetricsByDate(HttpServletRequest request, String beginDate, String endDate) throws IOException, ParseException;
}
