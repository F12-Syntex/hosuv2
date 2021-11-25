package com.hosu.datatypes;

import com.hosu.settings.onImageClick;

public class SearchableData {

	private String loadedURL;
	private onImageClick onClick;

	public SearchableData(String loadedURL, onImageClick onClick) {
		super();
		this.loadedURL = loadedURL;
		this.onClick = onClick;
	}
	
	public String getLoadedURL() {
		return loadedURL;
	}
	public void setLoadedURL(String loadedURL) {
		this.loadedURL = loadedURL;
	}
	public onImageClick getOnClick() {
		return onClick;
	}
	public void setOnClick(onImageClick onClick) {
		this.onClick = onClick;
	}

}
