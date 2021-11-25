package com.syntex.manga.exceptions;

@SuppressWarnings("serial")
public class RequestQueryException extends Exception{
	public RequestQueryException() {
		super("This query returned no results.");
	}
}
