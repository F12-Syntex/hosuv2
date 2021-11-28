package com.hosu.css;

public enum Image {
	
	HOSU("Hosu.css"), SETTINGS("icons8_settings_64px.png"), SETTINGS_SELECTED("icons8_settings_64px_1.png"), RED_CIRCLE("icons8_red_circle_100px.png"),
	BLUE_CIRCLE("icons8_blue_circle_100px.png"), PULL_DOWN_LIGHT("icons8_pull_down_100px.png"), PULL_DOWN_DARK("icons8_pull_down_100px_1.png"),
	LIST_LIGHT("icons8_list_100px_1.png"), LIST_DARK("icons8_list_100px.png"), TV_LIGHT("icons8_tv_100px.png"), TV_DARK("icons8_tv_100px_1.png"),
	LIGHT_BOOK("icons8_book_100px.png"), DARK_BOOK("icons8_book_100px_1.png"), NHENTAI("nhnetai.jpg"), LOADING("loading.gif"), REDDIT("reddit.png"),
	IMAGE_LOAD("loading3.gif"), HOME("icons8-homepage-100.png"), SEARCH("icons8_search_folder_64px.png"), DOWNLOAD("icons8_downloads_folder_64px.png"),
	NSFW("nsfw.png"), NSFW2("nsfw2.png"), SERVER("server.png"), DOWNLOAD2("downloadArrow.png"), DOWNLOAD_COMPLETE("downloadComplete.png"),
	OPEN_BROWSER("icons8_add_tab_48px.png"), NEW_WINDOW("icons8_add_tab_48px.png"), HEART_OFF("icons8_add_to_favorites_48px_1.png"), HEART_ON("icons8_add_to_favorites_48px.png"),
	EDIT("icons8_edit_file_48px.png"), DROP_DOWN("expand.png"), DOUBLE_DROP_DOWN("icons8_double_down_48px.png"), DOUBLE_UP("icons8_double_up_48px.png"),
	ARROW_UP("icons8_collapse_arrow_48px.png"), DELETE("icons8_unavailable_48px.png"), PICTURES("icons8_pictures_folder_24px.png"),
	PAUSE("icons8_pause_button_48px.png"), PLAY("icons8_circled_play_48px.png"), STOP("icons8_multiply_48px.png"), RESTART("icons8_restart_48px_1.png"),
	TURBO_ON("icons8_quick_mode_on_48px.png"), TURBO_OFF("icons8_quick_mode_off_48px.png"), EXIT("icons8_delete_24px.png"), SEARCH_OFF("icons8_search_48px.png"),
	SEARCH_ON("icons8_search_more_48px.png"), SEARCH2("icons8_search_bar_48px.png"), DOWNLOADS("icons8_downloads_48px.png"), ICONS("icons8_small_icons_48px.png"),
	LOADER2("Cube-0.6s-111px.gif"), LOADER3("Blocks-1s-410px.gif"), SEARCH_BROWSER("icons8_new_window_48px.png"), SAUCE("soy-sauce.png");
	
	
	private final String resource;
	
	Image(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
	
}
