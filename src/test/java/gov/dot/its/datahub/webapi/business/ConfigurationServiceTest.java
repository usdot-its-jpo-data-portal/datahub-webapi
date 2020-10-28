package gov.dot.its.datahub.webapi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.datahub.webapi.MockDataConfiguration;
import gov.dot.its.datahub.webapi.dao.ConfigurationDao;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@RunWith(SpringRunner.class)
public class ConfigurationServiceTest {

	private MockDataConfiguration mockData;

	@InjectMocks
	private ConfigurationServiceImpl configurationService;

	@Mock
	private ConfigurationDao configurationDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	public ConfigurationServiceTest() {
		this.mockData = new MockDataConfiguration();
	}

	@Test
	public void testEngagementPopupsData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		List<DHEngagementPopup> engagementPopups = new ArrayList<>();
		for(int i=0; i<2; i++) {
			DHEngagementPopup engagementPopup = this.mockData.getFakeEngagementPopup(i);
			engagementPopups.add(engagementPopup);
		}

		when(configurationDao.getEngagementPopups()).thenReturn(engagementPopups);

		ApiResponse<List<DHEngagementPopup>> apiResponse = configurationService.engagementPopups(request);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getResult().isEmpty());
	}

	@Test
	public void testEngagementPopupsNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		when(configurationDao.getEngagementPopups()).thenReturn(new ArrayList<>());

		ApiResponse<List<DHEngagementPopup>> apiResponse = configurationService.engagementPopups(request);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testEngagementPopupsError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		final String EXCEPTION_MSG = "Exception Test";

		when(configurationDao.getEngagementPopups()).thenThrow(new IOException(EXCEPTION_MSG));

		ApiResponse<List<DHEngagementPopup>> apiResponse = configurationService.engagementPopups(request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertTrue(!apiResponse.getErrors().isEmpty());
	}
}
