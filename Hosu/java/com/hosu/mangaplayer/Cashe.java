package com.hosu.mangaplayer;

import com.manga.data.Page;

import javafx.scene.image.Image;

public class Cashe {

	private Image image;
	private Page page;
	
	public Cashe(Image image, Page page) {
		this.image = image;
		this.page = page;
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

}
