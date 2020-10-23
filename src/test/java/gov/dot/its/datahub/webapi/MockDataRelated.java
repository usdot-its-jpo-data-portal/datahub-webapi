package gov.dot.its.datahub.webapi;

import java.util.ArrayList;
import java.util.List;

import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.RelatedModel;

public class MockDataRelated {

	public RelatedItemModel getFakeRelatedItemModel(String id) {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setId(id);
		relatedItemModel.setName("Name-"+id);
		relatedItemModel.setUrl("http://related/"+id);
		return relatedItemModel;
	}

	public RelatedModel getFakeRelatedModel() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setId("id");
		relatedModel.setName("name");
		List<RelatedItemModel> relatedItemModels = new ArrayList<>();
		for(int i=1; i<2; i++) {
			RelatedItemModel relatedItemModel = this.getFakeRelatedItemModel(String.valueOf(i));
			relatedItemModels.add(relatedItemModel);
		}
		relatedModel.setUrls(relatedItemModels);
		return relatedModel;
	}

}
