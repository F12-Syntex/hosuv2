package com.hosu.helpers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Conectivity {

	public static boolean isConnected() {
	      try {
	          URL url = new URL("http://www.google.com");
	          URLConnection connection = url.openConnection();
	          connection.connect();
	          return true;
	       } catch (IOException e) {
	    	   return false;
	       }
	}
	
}
