package gov.dot.its.datahub.webapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import gov.dot.its.datahub.webapi.MockDataDataAsset;
import gov.dot.its.datahub.webapi.MockESUtils;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

@RunWith(SpringRunner.class)
public class DataAssetDaoTest {

	static final private String TEST_ID = "ID";

	private MockESUtils mockESUtils;
	private MockDataDataAsset mockData;

	@InjectMocks
	DataAssetDaoImpl dataAssetDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(dataAssetDao, "index", "testIndex");
		mockESUtils = new MockESUtils();
		mockData = new MockDataDataAsset();
	}

	@Test
	public void testGetDataAssetsSortByDescending() throws IOException {
		DataAsset dataAsset = mockData.getFakeDataAsset(TEST_ID);
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(dataAsset);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<DataAsset> dataAssets = dataAssetDao.getDataAssets(TEST_ID, "desc", 100);
		assertFalse(dataAssets.isEmpty());
	}

	@Test
	public void testGetDataAssetsSortByAscending() throws IOException {
		DataAsset dataAsset = mockData.getFakeDataAsset(TEST_ID);
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(dataAsset);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<DataAsset> dataAssets = dataAssetDao.getDataAssets(null, "asc", 100);
		assertFalse(dataAssets.isEmpty());
	}

	@Test
	public void testGetDataAssetsNoSortBy() throws IOException {
		DataAsset dataAsset = mockData.getFakeDataAsset(TEST_ID);
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(dataAsset);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<DataAsset> dataAssets = dataAssetDao.getDataAssets(null, "desc", 100);
		assertFalse(dataAssets.isEmpty());
	}

	@Test
	public void testGetDataAsset() throws IOException {
		DataAsset fakeDataAsset = mockData.getFakeDataAsset(TEST_ID);
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(fakeDataAsset);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		DataAsset dataAsset = dataAssetDao.getDataAsset(TEST_ID);
		assertNotNull(dataAsset);
		assertEquals(TEST_ID, dataAsset.getId());
	}

	@Test
	public void testGetDataAssetNoData() throws IOException {
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(null);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		DataAsset dataAsset = dataAssetDao.getDataAsset(TEST_ID);
		assertNull(dataAsset);
	}

	@Test
	public void testSearchDataAssetsByWordsData() throws IOException {
		DataAsset fakeDataAsset = mockData.getFakeDataAsset(TEST_ID);
		SearchResponse searchResponse = mockESUtils.generateFakeSearchResponse(fakeDataAsset);

		SearchRequestModel searchRequestModel = mockData.getSearchRequestModel(false, TEST_ID);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		SearchResponseModel<List<DataAsset>> response = dataAssetDao.searchDataAssetsByWords(searchRequestModel);
		assertNotNull(response);
		assertNotNull(response.getResult());
		assertFalse(response.getResult().size()==0);
	}
}
