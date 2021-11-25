package com.hosu.css;

public enum Image {
	
	HOSU("Hosu.css"), SETTINGS("icons8_settings_64px.png"), SETTINGS_SELECTED("icons8_settings_64px_1.png"), RED_CIRCLE("icons8_red_circle_100px.png"),
	BLUE_CIRCLE("icons8_blue_circle_100px.png"), PULL_DOWN_LIGHT("icons8_pull_down_100px.png"), PULL_DOWN_DARK("icons8_pull_down_100px_1.png"),
	LIST_LIGHT("icons8_list_100px_1.png"), LIST_DARK("icons8_list_100px.png"), TV_LIGHT("icons8_tv_100px.png"), TV_DARK("icons8_tv_100px_1.png"),
	LIGHT_BOOK("icons8_book_100px.png"), DARK_BOOK("icons8_book_100px_1.png"), NHENTAI("nhnetai.jpg"), LOADING("loading.gif"), REDDIT("reddit.png"),
	IMAGE_LOAD("loading3.gif"), HOME("icons8-homepage-100.png"), SAUCE("soy-sauce.png"), MAL("mal.jpg");
	
	private final String resource;
	
	Image(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
	
}
