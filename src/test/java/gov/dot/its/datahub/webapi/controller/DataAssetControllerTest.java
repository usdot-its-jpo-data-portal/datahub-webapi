package gov.dot.its.datahub.webapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.datahub.webapi.MockDataDataAsset;
import gov.dot.its.datahub.webapi.business.DataAssetService;
import gov.dot.its.datahub.webapi.model.ApiError;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;

@RunWith(SpringRunner.class)
@WebMvcTest(DataAssetController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3006, uriScheme="http")
public class DataAssetControllerTest {

	private static final String TEST_ID = "Test:1234";
	private static final String TEST_DATAASSETS_URL = "%s/v1/dataassets";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";

	private MockDataDataAsset mockData;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private DataAssetService dataAssetService;

	public DataAssetControllerTest() {
		this.mockData = new MockDataDataAsset();
	}

	@Test
	public void testDatasetsData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		DataAsset dataAsset = this.mockData.getFakeDataAsset(TEST_ID);
		List<DataAsset> dataAssets = new ArrayList<>();
		dataAssets.add(dataAsset);

		ApiResponse<List<DataAsset>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, dataAssets, null, null, request);

		when(dataAssetService.findAll(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/dataassets/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)

						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DataAsset>>> valueType = new TypeReference<ApiResponse<List<DataAsset>>>(){};
		ApiResponse<List<DataAsset>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
		assertNotNull(responseApi.getResult());
		assertFalse(responseApi.getResult().isEmpty());
		DataAsset respDataAsset = responseApi.getResult().get(0);
		assertEquals(dataAsset.getAccessLevel(), respDataAsset.getAccessLevel());
		assertEquals(dataAsset.getDescription(), respDataAsset.getDescription());
		assertEquals(dataAsset.getDhId(), respDataAsset.getDhId());
		assertEquals(dataAsset.getDhSourceName(), respDataAsset.getDhSourceName());
		assertEquals(dataAsset.getId(), respDataAsset.getId());
		assertEquals(dataAsset.getName(), respDataAsset.getName());
		assertEquals(dataAsset.getSourceUrl(), respDataAsset.getSourceUrl());
		assertTrue(!dataAsset.getTags().isEmpty());
		assertEquals(dataAsset.getTags().get(0), respDataAsset.getTags().get(0));
		assertEquals(dataAsset.getTags().get(1), respDataAsset.getTags().get(1));
		assertEquals(dataAsset.getTags().get(2), respDataAsset.getTags().get(2));

	}

	@Test
	public void testDatasetsNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<DataAsset>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(dataAssetService.findAll(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/dataassets/no-data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)

						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DataAsset>>> valueType = new TypeReference<ApiResponse<List<DataAsset>>>(){};
		ApiResponse<List<DataAsset>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
		assertNull(responseApi.getResult());

	}

	@Test
	public void testDatasetsError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError("Test Error"));

		ApiResponse<List<DataAsset>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(dataAssetService.findAll(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/dataassets/error",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)

						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DataAsset>>> valueType = new TypeReference<ApiResponse<List<DataAsset>>>(){};
		ApiResponse<List<DataAsset>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertNotNull(responseApi.getErrors());
		assertFalse(responseApi.getErrors().isEmpty());
		assertNull(responseApi.getMessages());
		assertNull(responseApi.getResult());

	}

	@Test
	public void testDatasetsDataQueryParams() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		DataAsset dataAsset = this.mockData.getFakeDataAsset(TEST_ID);
		List<DataAsset> dataAssets = new ArrayList<>();
		dataAssets.add(dataAsset);

		ApiResponse<List<DataAsset>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, dataAssets, null, null, request);

		when(dataAssetService.findAll(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL+"?sortby=lastUpdate&sortOrder=desc&limit=10", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/dataassets/queryparams",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)

						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<DataAsset>>> valueType = new TypeReference<ApiResponse<List<DataAsset>>>(){};
		ApiResponse<List<DataAsset>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNull(responseApi.getMessages());
		assertNull(responseApi.getErrors());
		assertFalse(responseApi.getResult().isEmpty());

	}

	@Test
	public void testDataset() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		DataAsset dataAsset = this.mockData.getFakeDataAsset(TEST_ID);

		ApiResponse<DataAsset> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, dataAsset, null, null, request);

		when(dataAssetService.findById(any(HttpServletRequest.class), anyString())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL+"/id", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/dataassets/id",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)

						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<DataAsset>> valueType = new TypeReference<ApiResponse<DataAsset>>(){};
		ApiResponse<DataAsset> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNull(responseApi.getMessages());
		assertNull(responseApi.getErrors());
		assertNotNull(responseApi.getResult());

	}

}
