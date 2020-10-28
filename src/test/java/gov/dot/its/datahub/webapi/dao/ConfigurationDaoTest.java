package gov.dot.its.datahub.webapi.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import gov.dot.its.datahub.webapi.MockDataConfiguration;
import gov.dot.its.datahub.webapi.MockESUtils;
import gov.dot.its.datahub.webapi.model.DHConfiguration;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@RunWith(SpringRunner.class)
public class ConfigurationDaoTest {

	private MockESUtils mockESUtils;
	private MockDataConfiguration mockData;

	@InjectMocks
	ConfigurationDaoImpl configurationDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(configurationDao, "configurationIndex", "unitTestIndex");
		ReflectionTestUtils.setField(configurationDao, "configurationId", "defaultId");

		this.mockData = new MockDataConfiguration();
		this.mockESUtils = new MockESUtils();
	}

	@Test
	public void testGetEngagementPopupsNoData() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<DHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();
		assertTrue(engagementPopups.isEmpty());
	}

	@Test
	public void testGetEngagementPopupsData() throws IOException {
		DHConfiguration configuration = mockData.getFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(configuration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<DHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();
		assertFalse(engagementPopups.isEmpty());
	}

}
