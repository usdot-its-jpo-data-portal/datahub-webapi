package gov.dot.its.datahub.webapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;

public class MockDataDataAsset {

	public DataAsset getFakeDataAsset(String id) {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setAccessLevel("Public");
		dataAsset.setDescription("Data Asset Description");
		dataAsset.setDhId("DHID-"+id);
		dataAsset.setDhLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setDhSourceName("Test");
		dataAsset.setHighlights(null);
		dataAsset.setId(id);
		dataAsset.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setName("Test");
		dataAsset.setSourceUrl("http://test.com/"+id);

		List<String> tags = new ArrayList<>();
		tags.add("Test tag 1");
		tags.add("Test tag 2");
		tags.add("Test tag 3");

		dataAsset.setTags(tags);

		return dataAsset;
	}

	public List<DataAsset> getFakeDataAssets() {
		List<DataAsset> dataAssets = new ArrayList<>();

		for(int i=1; i<2; i++) {
			DataAsset dataAsset = this.getFakeDataAsset(String.valueOf(i));
			dataAssets.add(dataAsset);
		}
		return dataAssets;
	}

	public SearchRequestModel getSearchRequestModel(boolean byPhrase, String term) {
		SearchRequestModel searchRequestModel = new SearchRequestModel();
		searchRequestModel.setLimit(1000);
		searchRequestModel.setPhrase(byPhrase);
		searchRequestModel.setTerm(term);

		return searchRequestModel;
	}
}
