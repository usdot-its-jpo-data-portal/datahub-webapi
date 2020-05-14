package gov.dot.its.datahub.webapi.model;

import java.util.ArrayList;
import java.util.List;

public class DHConfiguration {
	private String id;
	private String name;
	private List<DHEngagementPopup> engagementPopups;

	public DHConfiguration() {
		this.engagementPopups = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DHEngagementPopup> getEngagementPopups() {
		return engagementPopups;
	}

	public void setEngagementPopups(List<DHEngagementPopup> engagementPopups) {
		this.engagementPopups = engagementPopups;
	}

}
