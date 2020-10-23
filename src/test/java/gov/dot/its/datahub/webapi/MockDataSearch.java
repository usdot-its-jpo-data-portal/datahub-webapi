package gov.dot.its.datahub.webapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import gov.dot.its.datahub.webapi.model.ApiResponse;
import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.Metrics;
import gov.dot.its.datahub.webapi.model.RelatedItemModel;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

public class MockDataSearch {

	public ApiResponse<SearchResponseModel<List<DataAsset>>> getFakeApiResponse(HttpServletRequest request, SearchRequestModel searchRequestModel) {
		DataAsset dataAsset = getFakeDataAsset("1234");

		List<DataAsset> dataAssets = new ArrayList<>();
		dataAssets.add(dataAsset);

		SearchResponseModel<List<DataAsset>> searchResponseModel = new SearchResponseModel<>();
		searchResponseModel.setMaxScore(1.0F);
		searchResponseModel.setNumHits(1);
		searchResponseModel.setResult(dataAssets);
		searchResponseModel.setSearchRequest(searchRequestModel);

		ApiResponse<SearchResponseModel<List<DataAsset>>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, searchResponseModel, null, null, request);

		return apiResponse;
	}


	public SearchRequestModel getFakeSearchRequestModel() {
		SearchRequestModel searchRequestModel = new SearchRequestModel();
		searchRequestModel.setLimit(10);
		searchRequestModel.setPhrase(false);
		searchRequestModel.setTerm("Test");

		return searchRequestModel;
	}

	public SearchResponseModel<List<DataAsset>> getFakeSearchResponseModel(SearchRequestModel searchRequestModel) {
		DataAsset dataAsset = getFakeDataAsset("1234");

		List<DataAsset> dataAssets = new ArrayList<>();
		dataAssets.add(dataAsset);

		SearchResponseModel<List<DataAsset>> searchResponseModel = new SearchResponseModel<>();
		searchResponseModel.setMaxScore(3.5F);
		searchResponseModel.setNumHits(1);
		searchResponseModel.setResult(dataAssets);
		searchResponseModel.setSearchRequest(searchRequestModel);

		return searchResponseModel;
	}

	public SearchRequestModel getFakeRequestModel(String term, boolean phrase, int limit) {
		SearchRequestModel searchRequestModel = new SearchRequestModel();
		searchRequestModel.setLimit(limit);
		searchRequestModel.setPhrase(phrase);
		searchRequestModel.setTerm(term);

		return searchRequestModel;
	}


	public SearchResponseModel<List<DataAsset>> getFakeSearchResponseModelNull(SearchRequestModel searchRequestModel) {
		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);
		searchResponseModel.setResult(null);
		return searchResponseModel;
	}

	public SearchResponseModel<List<DataAsset>> getFakeSearchResponseModelEmpty(SearchRequestModel searchRequestModel) {
		SearchResponseModel<List<DataAsset>> searchResponseModel = this.getFakeSearchResponseModel(searchRequestModel);
		searchResponseModel.setResult(new ArrayList<>());
		return searchResponseModel;
	}


	private DataAsset getFakeDataAsset(String id) {
		DataAsset dataAsset = new DataAsset();
		dataAsset.setAccessLevel("Public");
		dataAsset.setDescription("Description of the data asset");
		dataAsset.setDhId("intId");
		dataAsset.setDhLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setDhSourceName("source");
		dataAsset.setEsScore(1.0F);
		dataAsset.setId("id:"+id);
		dataAsset.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		dataAsset.setName("SampleDataAsset");
		dataAsset.setSourceUrl("http://testing.com/id:"+id);
		List<String> tags = new ArrayList<>();
		tags.add("Sample tag number one");
		tags.add("Sample tag number two");
		tags.add("Sample tag number three");
		dataAsset.setTags(tags);

		List<RelatedItemModel> relateds = new ArrayList<>();
		RelatedItemModel relItem = new RelatedItemModel();
		relItem.setId("585e203c4bf7b9ff12966fd9697b87cb");
		relItem.setName("related1-name");
		relItem.setUrl("http://related.item.com/id=585e203c4bf7b9ff12966fd9697b87cb");
		relateds.add(relItem);
		relItem = new RelatedItemModel();
		relItem.setId("7f3bac27fc81d39ffa8ede58b39c8fb6");
		relItem.setName("related2-name");
		relItem.setUrl("http://related.item.com/id=7f3bac27fc81d39ffa8ede58b39c8fb6");
		relateds.add(relItem);

		dataAsset.setRelated(relateds);

		Metrics metrics = new Metrics();
		metrics.setDownloadsTotal(5L);
		metrics.setPageViewsLastMonth(15L);
		metrics.setPageViewsTotal(25L);

		dataAsset.setMetrics(metrics);

		return dataAsset;
	}
}
