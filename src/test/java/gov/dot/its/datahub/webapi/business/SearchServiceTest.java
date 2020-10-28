package gov.dot.its.datahub.webapi.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

import gov.dot.its.datahub.webapi.MockDataRelated;
import gov.dot.its.datahub.webapi.MockDataSearch;
import gov.dot.its.datahub.webapi.dao.DataAssetDao;
import gov.dot.its.datahub.webapi.dao.RelatedDao;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;


@RunWith(SpringRunner.class)
public class SearchServiceTest {

	private static final int TEST_LIMIT = 10;
	private static final String TEST_TERM = "Data";

	private MockDataSearch mockData;
	private MockDataRelated mockDataRelated;

	@InjectMocks
	private SearchServiceImpl searchService;

	@Mock
	private DataAssetDao dataAssetDao;

	@Mock
	private RelatedDao relatedDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	public SearchServiceTest() {
		this.mockData = new MockDataSearch();
		this.mockDataRelated = new MockDataRelated();
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelNull() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, null);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertFalse(apiResponse.getErrors().isEmpty());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelTermNull() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(null, false, TEST_LIMIT);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestModelTermEmpty() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel("", false, TEST_LIMIT);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertFalse(apiResponse.getErrors().isEmpty());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.mockData.getFakeSearchResponseModelNull(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertNull(apiResponse.getErrors());
		assertNull(apiResponse.getMessages());
		assertNull(apiResponse.getResult().getResult());
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueEmpy() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.mockData.getFakeSearchResponseModelEmpty(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getErrors());
		assertNull(apiResponse.getMessages());
		assertNotNull(apiResponse.getResult());
		assertTrue(apiResponse.getResult().getResult().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestPhraseTrueHasData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, true, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.mockData.getFakeSearchResponseModel(searchRequestModel);

		when(dataAssetDao.searchDataAssetsByPhrase(any(SearchRequestModel.class))).thenReturn(searchResponseModel);
		when(relatedDao.getRelatedItems(any(String.class))).thenReturn(new ArrayList<>());

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertNotNull(apiResponse.getResult().getResult());
		assertFalse(apiResponse.getResult().getResult().isEmpty());
		assertNull(apiResponse.getMessages());
		assertNull(apiResponse.getErrors());
	}


	@Test
	public void testSearchDataAssetsSearchRequestNoPhrase() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.mockData.getFakeSearchResponseModel(searchRequestModel);
		List<RelatedItemModel> relatedItemModels = new ArrayList<>();
		for(int i=1; i<2; i++) {
			RelatedItemModel relatedItemModel = mockDataRelated.getFakeRelatedItemModel(String.valueOf(i));
			relatedItemModels.add(relatedItemModel);
		}

		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenReturn(searchResponseModel);
		when(relatedDao.getRelatedItems(any(String.class))).thenReturn(relatedItemModels);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult().getResult());
		assertFalse(apiResponse.getResult().getResult().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestNoPhraseNoRelated() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		SearchResponseModel<List<DataAsset>> searchResponseModel = this.mockData.getFakeSearchResponseModel(searchRequestModel);
		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenReturn(searchResponseModel);
		when(relatedDao.getRelatedItems(any(String.class))).thenReturn(new ArrayList<>());

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult().getResult());
		assertFalse(apiResponse.getResult().getResult().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestIOException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenThrow(new IOException("Test IOException"));

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testSearchDataAssetsSearchRequestESException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		SearchRequestModel searchRequestModel = this.mockData.getFakeRequestModel(TEST_TERM, false, TEST_LIMIT);

		ElasticsearchStatusException esException = new ElasticsearchStatusException("Test ESException", RestStatus.FORBIDDEN, "");
		when(dataAssetDao.searchDataAssetsByWords(any(SearchRequestModel.class))).thenThrow(esException);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = searchService.searchDataAssets(request, searchRequestModel);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertFalse(apiResponse.getErrors().isEmpty());
		assertNull(apiResponse.getResult());
	}

}
