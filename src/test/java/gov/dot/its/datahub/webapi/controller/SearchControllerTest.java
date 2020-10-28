package gov.dot.its.datahub.webapi.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.datahub.webapi.MockDataSearch;
import gov.dot.its.datahub.webapi.business.SearchService;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
@AutoConfigureRestDocs(outputDir="target/generated-snippets", uriHost="example.com", uriPort=3006, uriScheme="http")
public class SearchControllerTest {

	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String TEST_DATAASSETS_URL = "%s/v1/search";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

	private MockDataSearch mockData;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private SearchService searchService;

	public SearchControllerTest() {
		this.mockData = new MockDataSearch();
	}

	@Test
	public void testSearchDataAssetsData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeSearchRequestModel();

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = this.mockData.getFakeApiResponse(request, searchRequestModel);

		when(searchService.searchDataAssets(any(HttpServletRequest.class), any(SearchRequestModel.class))).thenReturn(apiResponse);

		String searchRequestModelStr = objectMapper.writeValueAsString(searchRequestModel);

		ResultActions resultActions = this.mockMvc.perform(
				post(String.format(TEST_DATAASSETS_URL,env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contentType(MediaType.APPLICATION_JSON)
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.content(searchRequestModelStr)
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/search/data",
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

		TypeReference<ApiResponse<SearchResponseModel<List<DataAsset>>>> valueType = new  TypeReference<ApiResponse<SearchResponseModel<List<DataAsset>>>>() {};
		ApiResponse<SearchResponseModel<List<DataAsset>>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());

	}
}
