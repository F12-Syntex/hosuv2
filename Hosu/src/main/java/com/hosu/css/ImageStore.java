package com.hosu.css;

import javafx.scene.image.Image;

public class ImageStore {

	private Image image;
	private com.hosu.css.Image id;
	
	public ImageStore(Image image, com.hosu.css.Image id) {
		this.image = image;
		this.id = id;
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public com.hosu.css.Image getId() {
		return id;
	}
	public void setId(com.hosu.css.Image id) {
		this.id = id;
	}
	
	
	
}
