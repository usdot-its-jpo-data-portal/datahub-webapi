package gov.dot.its.datahub.webapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import gov.dot.its.datahub.webapi.MockDataConfiguration;
import gov.dot.its.datahub.webapi.business.ConfigurationService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DHEngagementPopup;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3006, uriScheme = "http")
public class ConfigurationControllerTest {
	private static final String TEST_ENGAGEMENTPOPUPS_URL = "%s/v1/configurations/engagementpopups";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

	private MockDataConfiguration mockData;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private ConfigurationService configurationService;

	public ConfigurationControllerTest() {
		this.mockData = new MockDataConfiguration();
	}

	@Test
	public void testEngagementPopupsData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<DHEngagementPopup> engagementPopups = new ArrayList<>();

		for (int i=0 ; i<2; i++) {
			DHEngagementPopup engagementPopup = this.mockData.getFakeEngagementPopup(i);
			engagementPopups.add(engagementPopup);
		}

		ApiResponse<List<DHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, engagementPopups, null, null, request);

		when(configurationService.engagementPopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
			get(String.format(TEST_ENGAGEMENTPOPUPS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			)
			.andExpect(status().isOk())
			.andDo(document("api/v1/configurations/engagementpopups/get/data",
				 Preprocessors.preprocessRequest(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)),
				 Preprocessors.preprocessResponse(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH))
			 )
		);

		MvcResult result = resultActions.andReturn();
		String objStr = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<DHEngagementPopup>>>() {};
		ApiResponse<List<DHEngagementPopup>> responseApi = objectMapper.readValue(objStr, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertTrue(!responseApi.getResult().isEmpty());
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);

	}

	@Test
	public void testEngagementPopupsNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<DHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, new ArrayList<>(), null, null, request);

		when(configurationService.engagementPopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
			get(String.format(TEST_ENGAGEMENTPOPUPS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			)
			.andExpect(status().isOk())
			.andDo(document("api/v1/configurations/engagementpopups/get/nodata",
				 Preprocessors.preprocessRequest(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)),
				 Preprocessors.preprocessResponse(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH))
			 )
		);

		MvcResult result = resultActions.andReturn();
		String objStr = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<DHEngagementPopup>>>() {};
		ApiResponse<List<DHEngagementPopup>> responseApi = objectMapper.readValue(objStr, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertTrue(responseApi.getResult().isEmpty());
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);

	}

	@Test
	public void testEngagementPopupsError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<DHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, request);

		when(configurationService.engagementPopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
			get(String.format(TEST_ENGAGEMENTPOPUPS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
			)
			.andExpect(status().isOk())
			.andDo(document("api/v1/configurations/engagementpopups/get/error",
				 Preprocessors.preprocessRequest(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)),
				 Preprocessors.preprocessResponse(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH))
			 )
		);

		MvcResult result = resultActions.andReturn();
		String objStr = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<DHEngagementPopup>>>() {};
		ApiResponse<List<DHEngagementPopup>> responseApi = objectMapper.readValue(objStr, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);

	}

}
