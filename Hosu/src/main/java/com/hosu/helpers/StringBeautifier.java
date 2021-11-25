package com.hosu.helpers;

import org.apache.commons.lang3.StringUtils;

public class StringBeautifier {

	public static String beautifier(String text) {
		return StringUtils.capitalize(text.toLowerCase()).trim().replace("_", " ");
	}
	
}
