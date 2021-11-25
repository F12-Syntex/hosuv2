package com.hasu1.manga.cashe;

import java.util.List;

import com.manga.data.SearchData;

public class SearchCashe {

	private String term;
	private List<SearchData> results;

	public SearchCashe(String term, List<SearchData> results) {
		super();
		this.term = term;
		this.results = results;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public List<SearchData> getResults() {
		return results;
	}
	public void setResults(List<SearchData> results) {
		this.results = results;
	}

}
