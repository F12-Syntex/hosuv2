package com.hosu.mangaplayer;

public class PageTracker {

	private int chapter;
	private int page;
	
	public PageTracker(int chapter, int page) {
		this.chapter = chapter;
		this.page = page;
	}

	
	public int getChapter() {
		return chapter;
	}
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	

}
