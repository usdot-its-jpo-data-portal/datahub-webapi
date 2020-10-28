package gov.dot.its.datahub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RelatedModelTest {
	final String TEST_VALUE = "TEST_VALUE";

	@Test
	public void testInstance() {
		RelatedModel relatedModel = new RelatedModel();
		assertNotNull(relatedModel);
	}

	@Test
	public void testId() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setId(TEST_VALUE);
		assertEquals(TEST_VALUE, relatedModel.getId());
	}

	@Test
	public void testName() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setName(TEST_VALUE);
		assertEquals(TEST_VALUE, relatedModel.getName());
	}

	@Test
	public void testUrls() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setId(TEST_VALUE);
		relatedItemModel.setName(TEST_VALUE);
		relatedItemModel.setUrl(TEST_VALUE);

		List<RelatedItemModel> urls = new ArrayList<>();
		urls.add(relatedItemModel);

		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setUrls(urls);
		assertTrue(relatedModel.getUrls().size()>0);
		assertEquals(TEST_VALUE, relatedModel.getUrls().get(0).getId());
	}
}
