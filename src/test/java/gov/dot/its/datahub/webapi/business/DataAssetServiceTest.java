package gov.dot.its.datahub.webapi.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.datahub.webapi.MockDataDataAsset;
import gov.dot.its.datahub.webapi.dao.DataAssetDao;
import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;

@RunWith(SpringRunner.class)
public class DataAssetServiceTest {

	private static final String TEST_ID = "Test:1234";
	private static final String TEST_SORT_BY_NAME = "sortBy";
	private static final String TEST_SORT_ORDER_NAME = "sortOrder";
	private static final String TEST_LIMIT_NAME = "limit";

	private MockDataDataAsset mockData;

	@InjectMocks
	private DataAssetServiceImpl dataAssetService;

	@Mock
	private DataAssetDao dataAssetDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	public DataAssetServiceTest() {
		this.mockData = new MockDataDataAsset();
	}

	@Test
	public void testFindAllInvalidOrderBy() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, "invalidField");
		params.put(TEST_SORT_ORDER_NAME, null);
		params.put(TEST_LIMIT_NAME, "10");

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());

	}

	@Test
	public void testFindAllNullOrEmptyOrderBy() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, null);
		params.put(TEST_SORT_ORDER_NAME, null);
		params.put(TEST_LIMIT_NAME, "10");

		when(dataAssetDao.getDataAssets(any(String.class), any(String.class), any(Integer.class))).thenReturn(null);

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getErrors() == null);

	}

	@Test
	public void testFindAllValidOrderBy() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, "lastUpdate");
		params.put(TEST_SORT_ORDER_NAME, "asc");
		params.put(TEST_LIMIT_NAME, "10");

		List<DataAsset> dataAssets = this.mockData.getFakeDataAssets();

		when(dataAssetDao.getDataAssets(any(String.class), any(String.class), anyInt())).thenReturn(dataAssets);

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertTrue(apiResponse.getErrors() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(!apiResponse.getResult().isEmpty());
		assertEquals(dataAssets.get(0).getName(), apiResponse.getResult().get(0).getName());
		assertEquals(dataAssets.get(0).getId(), apiResponse.getResult().get(0).getId());
		assertEquals(dataAssets.get(0).getDescription(), apiResponse.getResult().get(0).getDescription());

	}

	@Test
	public void testFindAllNullData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, null);
		params.put(TEST_SORT_ORDER_NAME, null);
		params.put(TEST_LIMIT_NAME, "10");

		List<DataAsset> dataAssets = null;

		when(dataAssetDao.getDataAssets(any(String.class), any(String.class), any(Integer.class))).thenReturn(dataAssets);

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(apiResponse.getErrors() == null);
		assertTrue(apiResponse.getMessages() == null);
	}

	@Test
	public void testFindAllEmptyData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, null);
		params.put(TEST_SORT_ORDER_NAME, null);
		params.put(TEST_LIMIT_NAME, null);

		List<DataAsset> dataAssets = new ArrayList<>();

		when(dataAssetDao.getDataAssets(any(String.class), any(String.class), any(Integer.class))).thenReturn(dataAssets);

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(apiResponse.getErrors() == null);
		assertTrue(apiResponse.getMessages() == null);
	}

	@Test
	public void testFindAllException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Map<String, String> params = new HashMap<>();
		params.put(TEST_SORT_BY_NAME, "lastUpdate");
		params.put(TEST_SORT_ORDER_NAME, "desc");
		params.put(TEST_LIMIT_NAME, "5");

		when(dataAssetDao.getDataAssets(anyString(), anyString(), anyInt())).thenThrow(new IOException("Test Exception"));

		ApiResponse<List<DataAsset>> apiResponse = dataAssetService.findAll(request, params);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());
		assertTrue(apiResponse.getMessages() == null);
	}

	@Test
	public void testFindByIdData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		DataAsset dataAsset = this.mockData.getFakeDataAsset(TEST_ID);

		when(dataAssetDao.getDataAsset(any(String.class))).thenReturn(dataAsset);

		ApiResponse<DataAsset> apiResponse = dataAssetService.findById(request, TEST_ID);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getErrors() == null);
		assertEquals(dataAsset.getId(), apiResponse.getResult().getId());
		assertEquals(dataAsset.getName(), apiResponse.getResult().getName());
		assertEquals(dataAsset.getDescription(), apiResponse.getResult().getDescription());
	}

	@Test
	public void testFindByIdNullData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		when(dataAssetDao.getDataAsset(any(String.class))).thenReturn(null);

		ApiResponse<DataAsset> apiResponse = dataAssetService.findById(request, TEST_ID);

		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(apiResponse.getErrors() == null);
	}

	@Test
	public void testFindByIdException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		when(dataAssetDao.getDataAsset(any(String.class))).thenThrow(new IOException("Test Exception"));

		ApiResponse<DataAsset> apiResponse = dataAssetService.findById(request, TEST_ID);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(apiResponse.getMessages() == null);
		assertTrue(!apiResponse.getErrors().isEmpty());
	}

}
