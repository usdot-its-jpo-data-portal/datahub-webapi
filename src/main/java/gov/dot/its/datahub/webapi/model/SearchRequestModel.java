package gov.dot.its.datahub.webapi.model;

public class SearchRequestModel {
	private String term;
	private boolean phrase;
	private int limit;

	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public boolean isPhrase() {
		return phrase;
	}
	public void setPhrase(boolean phrase) {
		this.phrase = phrase;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
