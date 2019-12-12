package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.datahub.webapi.model.RelatedItemModel;

public interface RelatedDao {
	public List<RelatedItemModel> getRelatedItems(String id) throws IOException;
}
