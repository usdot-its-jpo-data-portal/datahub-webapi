package gov.dot.its.datahub.webapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gov.dot.its.datahub.webapi.model.DHConfiguration;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

public class MockDataConfiguration {
	public DHEngagementPopup getFakeEngagementPopup(int index) {
		DHEngagementPopup engagementPopup = new DHEngagementPopup();
		engagementPopup.setActive(true);
		engagementPopup.setContent(String.format("<h1>Content-%s</h1>", index));
		engagementPopup.setControlsColor("white");
		engagementPopup.setControlsShadow("black");
		engagementPopup.setDescription(String.format("Description-%s", index));
		engagementPopup.setId(UUID.randomUUID().toString());
		engagementPopup.setLastModified(new Date());
		engagementPopup.setName(String.format("Engagement Popup %s", index));

		return engagementPopup;
	}

	public DHConfiguration getFakeConfiguration() {
		DHConfiguration configuration = new DHConfiguration();
		configuration.setId("configuration-id");
		configuration.setName("configuration-name");

		List<DHEngagementPopup> engagementPopups = new ArrayList<>();
		for(int i=1; i<2; i++) {
			DHEngagementPopup engagementPopup = this.getFakeEngagementPopup(i);
			engagementPopups.add(engagementPopup);
		}
		configuration.setEngagementPopups(engagementPopups);

		return configuration;
	}
}
