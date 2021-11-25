package com.manga.data;

public class Page {

	private String trim;
	private int pageNumber;
	
	public Page(String trim, int pageNumber) {
		this.trim = trim;
		this.pageNumber = pageNumber;
	}
	
	public String getImageURL() {
		return trim;
	}
	public void setName(String trim) {
		this.trim = trim;
	}
	public String getName() {
		return trim;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
}
