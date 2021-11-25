package com.syntex.manga.utils;

public class NumberUtils {

	public static boolean isInt(String text) {
		try {
			Integer.parseInt(text);
			return true;
		}catch (Exception e) {}
		return false;
	}

}
