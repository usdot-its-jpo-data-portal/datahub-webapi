package gov.dot.its.datahub.webapi.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.rest.RestStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.datahub.webapi.dao.DataAssetDao;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;


@RunWith(SpringRunner.class)
public class SearchServiceTest {

	private static final int TEST_LIMIT = 10;
	private static final String TEST_TERM = "Data";

	@InjectMocks
	private SearchServiceImpl searchService;

	@Mock
	private DataAssetDao dataAssetDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelNull() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, null);

		assertTrue(apiResponse != null);
		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getErrors().isEmpty());
		assertTrue(apiResponse.getResult() == null);
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelTermNull() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(null, false, TEST_LIMIT);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelTermEmpty() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel("", false, TEST_LIMIT);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getErrors().isEmpty());
		assertTrue(apiResponse.getResult() == null);
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModelNull(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertTrue(apiResponse.getErrors() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getResult().getResult() == null);
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueEmpy() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModelEmpty(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getErrors() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getResult() != null);
		assertTrue(apiResponse.getResult().getResult().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueHasData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertTrue(apiResponse.getResult().getResult() != null);
		assertTrue(!apiResponse.getResult().getResult().isEmpty());
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getErrors() == null);
	}


	@Test
	public void testSearchDataAssetsSearchRequestNoPhrase() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult().getResult() != null);
		assertTrue(!apiResponse.getResult().getResult().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestIOException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenThrow(new IOException("Test IOException"));

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestESException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		ElasticsearchStatusException esException = new ElasticsearchStatusException("Test ESException", RestStatus.FORBIDDEN, "");
		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenThrow(esException);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getErrors().isEmpty());
		assertTrue(apiResponse.getResult() == null);
	}

	private SearchResponseModel<List<DataAsset>> getFakeSearchResponseModel(SearchRequestModel searchRequestModel) {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setName("Test");
		dataAsset.setDescription("This is the description");
		dataAsset.setAccessLevel("Public");
		dataAsset.setDhLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setDhSourceName("dh");
		dataAsset.setDhId("dh-Test:1234");
		dataAsset.setEsScore(1.3F);
		dataAsset.setId("Test:1234");

		List<DataAsset> dataAssets = new ArrayList<>();
		dataAssets.add(dataAsset);

		SearchResponseModel<List<DataAsset>> searchResponseModel = new SearchResponseModel<>();
		searchResponseModel.setMaxScore(3.5F);
		searchResponseModel.setNumHits(1);
		searchResponseModel.setResult(dataAssets);
		searchResponseModel.setSearchRequest(searchRequestModel);

		return searchResponseModel;
	}

	private SearchRequestModel getFakeRequestModel(String term, boolean phrase, int limit) {
		SearchRequestModel searchRequestModel = new SearchRequestModel();
		searchRequestModel.setLimit(limit);
		searchRequestModel.setPhrase(phrase);
		searchRequestModel.setTerm(term);

		return searchRequestModel;
	}


	private SearchResponseModel<List<DataAsset>> getFakeSearchResponseModelNull(SearchRequestModel searchRequestModel) {
		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);
		searchResponseModel.setResult(null);
		return searchResponseModel;
	}

	private SearchResponseModel<List<DataAsset>> getFakeSearchResponseModelEmpty(SearchRequestModel searchRequestModel) {
		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);
		searchResponseModel.setResult(new ArrayList<>());
		return searchResponseModel;
	}
}
