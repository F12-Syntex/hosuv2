package com.hosu.css;

public enum Styling {
	
	HOSU("Hosu.css"), SCROLL_PANE("ScrollPane.css"), CONTENT_VIEW("ContentView.css"), JFXBUTTON("Button.css"), ALERTS("Alerts.css");
	
	private final String resource;
	
	Styling(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
	
}
