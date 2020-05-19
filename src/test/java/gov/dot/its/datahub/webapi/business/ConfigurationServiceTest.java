package gov.dot.its.datahub.webapi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.datahub.webapi.dao.ConfigurationDao;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@RunWith(SpringRunner.class)
public class ConfigurationServiceTest {
	@InjectMocks
	private ConfigurationServiceImpl configurationService;

	@Mock
	private ConfigurationDao configurationDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEngagementPopupsData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		List<DHEngagementPopup> engagementPopups = new ArrayList<>();
		for(int i=0; i<2; i++) {
			DHEngagementPopup engagementPopup = this.getFakeEngagementPopup(i);
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
		assertTrue(apiResponse.getResult() == null);
	}

	@Test
	public void testEngagementPopupsError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		final String EXCEPTION_MSG = "Exception Test";

		when(configurationDao.getEngagementPopups()).thenThrow(new IOException(EXCEPTION_MSG));

		ApiResponse<List<DHEngagementPopup>> apiResponse = configurationService.engagementPopups(request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());
	}

	private DHEngagementPopup getFakeEngagementPopup(int index) {
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
}
