package gov.dot.its.datahub.webapi.model;

public class SearchResponseModel<T> {
	private SearchRequestModel searchRequest;
	private long numHits;
	private float maxScore;
	private T result;

	public SearchRequestModel getSearchRequest() {
		return searchRequest;
	}
	public void setSearchRequest(SearchRequestModel searchRequest) {
		this.searchRequest = searchRequest;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public long getNumHits() {
		return numHits;
	}
	public void setNumHits(long numHits) {
		this.numHits = numHits;
	}
	public float getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(float maxScore) {
		this.maxScore = maxScore;
	}

}
