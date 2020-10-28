package gov.dot.its.datahub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.datahub.webapi.MockDataRelated;

@RunWith(SpringRunner.class)
public class DataAssetTest {

	static final String TEST_VALUE = "test";

	private MockDataRelated mockDataRelated;

	public DataAssetTest() {
		mockDataRelated = new MockDataRelated();
	}

	@Test
	public void testInstance() {
		DataAsset dataAsset = new DataAsset();
		assertNotNull(dataAsset);
		assertTrue(dataAsset.getTags().isEmpty());
		assertTrue(dataAsset.getHighlights().isEmpty());
		assertTrue(dataAsset.getRelated().isEmpty());
	}

	@Test
	public void testId() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setId(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getId());
	}

	@Test
	public void testName() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setName(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getName());
	}

	@Test
	public void testDescription() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setDescription(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getDescription());
	}

	@Test
	public void testAccessLevel() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setAccessLevel(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getAccessLevel());
	}

	@Test
	public void testLastUpdate() {
		DataAsset dataAsset = new DataAsset();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		dataAsset.setLastUpdate(timestamp);
		assertEquals(timestamp, dataAsset.getLastUpdate());
	}

	@Test
	public void testTags() {
		List<String> tags = new ArrayList<>();
		tags.add(TEST_VALUE);

		DataAsset dataAsset = new DataAsset();
		dataAsset.setTags(tags);
		assertFalse(dataAsset.getTags().isEmpty());
		assertEquals(TEST_VALUE, dataAsset.getTags().get(0));
	}

	@Test
	public void testSourceUrl() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setSourceUrl(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getSourceUrl());
	}

	@Test
	public void testDhId() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setDhId(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getDhId());
	}

	@Test
	public void testDhLastUpdate() {
		DataAsset dataAsset = new DataAsset();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		dataAsset.setDhLastUpdate(timestamp);
		assertEquals(timestamp, dataAsset.getDhLastUpdate());
	}

	@Test
	public void testDhSourceName() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setDhSourceName(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getDhSourceName());
	}

	@Test
	public void testHighlights() {
		Map<String, List<String>> highLights = new HashMap<>();
		highLights.put(TEST_VALUE, new ArrayList<>(Arrays.asList(TEST_VALUE)));

		DataAsset dataAsset = new DataAsset();
		dataAsset.setHighlights(highLights);
		assertFalse(dataAsset.getHighlights().isEmpty());
		assertTrue(dataAsset.getHighlights().containsKey(TEST_VALUE));
		assertFalse(dataAsset.getHighlights().get(TEST_VALUE).isEmpty());
		assertEquals(TEST_VALUE, dataAsset.getHighlights().get(TEST_VALUE).get(0));
	}

	@Test
	public void testEsScore() {
		float f = 1.1F;
		DataAsset dataAsset = new DataAsset();
		dataAsset.setEsScore(f);
		assertEquals(f, dataAsset.getEsScore());
	}

	@Test
	public void testRelated() {
		RelatedItemModel relatedItemModel = mockDataRelated.getFakeRelatedItemModel(TEST_VALUE);
		List<RelatedItemModel> itemModels = new ArrayList<>(Arrays.asList(relatedItemModel));

		DataAsset dataAsset = new DataAsset();
		dataAsset.setRelated(itemModels);
		assertFalse(dataAsset.getRelated().isEmpty());
		assertNotNull(dataAsset.getRelated().get(0));
		assertEquals(TEST_VALUE, dataAsset.getRelated().get(0).getId());
	}

	@Test
	public void testMetrics() {
		long l = 2L;
		Metrics metrics = new Metrics();
		metrics.setDownloadsTotal(l);
		metrics.setPageViewsLastMonth(l);
		metrics.setPageViewsTotal(l);

		DataAsset dataAsset = new DataAsset();
		dataAsset.setMetrics(metrics);
		assertNotNull(dataAsset.getMetrics());
		assertEquals(l, dataAsset.getMetrics().getDownloadsTotal());
		assertEquals(l, dataAsset.getMetrics().getPageViewsLastMonth());
		assertEquals(l, dataAsset.getMetrics().getPageViewsTotal());
	}

	@Test
	public void testDhType() {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setDhType(TEST_VALUE);
		assertEquals(TEST_VALUE, dataAsset.getDhType());
	}
}
