package gov.dot.its.datahub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DHConfigurationTest {

	final String TEST_ID = "TEST_ID";

	@Test
	public void testInstance() {
		DHConfiguration dhConfiguration = new DHConfiguration();
		assertNotNull(dhConfiguration);
	}

	@Test
	public void testId() {
		DHConfiguration dhConfiguration = new DHConfiguration();
		dhConfiguration.setId(TEST_ID);
		assertEquals(TEST_ID, dhConfiguration.getId());
	}

	@Test
	public void testName() {
		DHConfiguration dhConfiguration = new DHConfiguration();
		dhConfiguration.setName(TEST_ID);
		assertEquals(TEST_ID, dhConfiguration.getName());
	}

	@Test
	public void testEngagementPopups() {
		DHEngagementPopup engagementPopup = new DHEngagementPopup();
		engagementPopup.setId(TEST_ID);
		engagementPopup.setName(TEST_ID);

		List<DHEngagementPopup> engagementPopups = new ArrayList<>();
		engagementPopups.add(engagementPopup);

		DHConfiguration dhConfiguration = new DHConfiguration();
		dhConfiguration.setEngagementPopups(engagementPopups);
		assertTrue(dhConfiguration.getEngagementPopups().size()>0);
		assertEquals(TEST_ID, dhConfiguration.getEngagementPopups().get(0).getId());
	}

}
