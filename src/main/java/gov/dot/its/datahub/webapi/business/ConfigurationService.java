package gov.dot.its.datahub.webapi.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

public interface ConfigurationService {

	ApiResponse<List<DHEngagementPopup>> engagementPopups(HttpServletRequest request);

}
