package com.hosu.css;

public class CssManager {

	public String getCss(Styling styling) {
		return getClass().getResource(styling.getResource()).toExternalForm();
	}
	
}
