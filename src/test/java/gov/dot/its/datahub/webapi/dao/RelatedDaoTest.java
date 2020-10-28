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

import gov.dot.its.datahub.webapi.MockDataRelated;
import gov.dot.its.datahub.webapi.MockESUtils;
import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.RelatedModel;

@RunWith(SpringRunner.class)
public class RelatedDaoTest {
	static final String TEST_ID = "ID";

	private MockESUtils mockESUtils;
	private MockDataRelated mockData;

	@InjectMocks
	RelatedDaoImpl relatedDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(relatedDao, "relatedIndex", "relatedIndex");
		ReflectionTestUtils.setField(relatedDao, "codeHubEndPoint", "http://its.dot.gov/code");
		ReflectionTestUtils.setField(relatedDao, "codeHubQueryString", "?test=true");
		mockESUtils = new MockESUtils();
		mockData = new MockDataRelated();
	}

	@Test
	public void getRelatedItemsNoData() throws IOException {
		GetResponse fakeResponse = mockESUtils.generateFakeGetResponse(null);

		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(fakeResponse);

		List<RelatedItemModel> relatedItems = relatedDao.getRelatedItems(TEST_ID);
		assertTrue(relatedItems.isEmpty());
	}

	@Test
	public void getRelatedItemsData() throws IOException {
		RelatedModel relatedModel = mockData.getFakeRelatedModel();
		GetResponse fakeResponse = mockESUtils.generateFakeGetResponse(relatedModel);

		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(fakeResponse);

		List<RelatedItemModel> relatedItems = relatedDao.getRelatedItems(TEST_ID);
		assertFalse(relatedItems.isEmpty());
	}
}
