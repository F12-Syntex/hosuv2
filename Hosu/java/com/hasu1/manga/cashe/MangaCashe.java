package com.hasu1.manga.cashe;

import java.util.ArrayList;
import java.util.List;

public class MangaCashe {

	private List<SearchCashe> searchedCasheData;
	
	public MangaCashe() {
		this.searchedCasheData = new ArrayList<>();
	}
	
	public void addSearch(SearchCashe data) {
		this.searchedCasheData.add(data);
	}
	
	public boolean isSearchQueryCashed(String query) {
		for(SearchCashe i : this.searchedCasheData) {
			if(i.getTerm().equalsIgnoreCase(query)) {
				return true;
			}
		}
		return false;
	}
	
	public SearchCashe getSearch(String query) {
		for(SearchCashe i : this.searchedCasheData) {
			if(i.getTerm().equalsIgnoreCase(query)) {
				return i;
			}
		}
		return null;
	}

}
