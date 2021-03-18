package gov.dot.its.datahub.webapi.business;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHMetrics;

public interface MetricsService {

	ApiResponse<List<DHMetrics>> getMetrics(HttpServletRequest request);
	ApiResponse<List<DHMetrics>> getMetricsBySource(HttpServletRequest request, String dhSourceName) throws IOException;
}
