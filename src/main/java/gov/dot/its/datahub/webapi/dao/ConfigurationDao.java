package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

public interface ConfigurationDao {

	List<DHEngagementPopup> getEngagementPopups() throws IOException;

}
