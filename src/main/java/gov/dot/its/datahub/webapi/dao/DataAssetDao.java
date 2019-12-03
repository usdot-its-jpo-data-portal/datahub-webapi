package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.datahub.webapi.model.DataAsset;
import gov.dot.its.datahub.webapi.model.SearchRequestModel;
import gov.dot.its.datahub.webapi.model.SearchResponseModel;

public interface DataAssetDao {
	public List<DataAsset> getDataAssets(String sortBy, String sortDirection, Integer limit) throws IOException;
	public DataAsset getDataAsset(String id) throws IOException;
	public SearchResponseModel<List<DataAsset>> searchDataAssetsByWords(SearchRequestModel searchRequestModel) throws IOException;
	public SearchResponseModel<List<DataAsset>> searchDataAssetsByPhrase(SearchRequestModel searchRequestModel) throws IOException;
}
