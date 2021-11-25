package com.manga.data;

import javafx.scene.image.Image;

public class LinkedPage {

	private Chapter chapter;
	private Page page;
	
	private LinkedPage nextPage;
	private LinkedPage prevPage;
	
	private Image cashedImage;
	
	public LinkedPage(Chapter chapter, Page page, LinkedPage nextPage, LinkedPage prevPage) {
		this.chapter = chapter;
		this.page = page;
		this.nextPage = nextPage;
		this.prevPage = prevPage;
		this.cashedImage = new Image(page.getImageURL());
	}
	
	public void destroyNode(int chapter) {
		if(chapter < this.chapter.getChapter()) {
			LinkedPage chapterCheck = this.getPrevPage();
			while(chapterCheck.getChapter().getChapter() > chapter) {
				chapterCheck = chapterCheck.getPrevPage();
			}
			chapterCheck.getNextPage().setPrevPage(null);
		}
		if(chapter > this.chapter.getChapter()) {
			LinkedPage chapterCheck = this.getNextPage();
			while(chapterCheck.getChapter().getChapter() < chapter) {
				chapterCheck = chapterCheck.getNextPage();
			}
			chapterCheck.getPrevPage().setNextPage(null);
		}
	}
	
	public Image getCashedImage() {
		return cashedImage;
	}
	public void setCashedImage(Image cashedImage) {
		this.cashedImage = cashedImage;
	}
	public Chapter getChapter() {
		return chapter;
	}
	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public LinkedPage getNextPage() {
		return nextPage;
	}
	public void setNextPage(LinkedPage nextPage) {
		this.nextPage = nextPage;
	}
	public LinkedPage getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(LinkedPage prevPage) {
		this.prevPage = prevPage;
	}
	

}
