package com.syntex.anime;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Animeflix {
	
	private final String domain = "https://animeflix.ws/";
	
	public static void main(String[] args) {
		
		Animeflix read = new Animeflix();
		read.query("scarlet");
		
	}
	
	public void query(String query) {
		String data = this.readURL("https://animeflix.ws/search.html?keyword=" + query.replace(" ", "-"));
		System.out.println("grabbing data for " + "https://animeflix.ws/search.html?keyword=" + query.replace(" ", "-"));
		
		for(String i : data.split("<article class=\"bs relative animate-me\">")) {
			if(i.contains("<!DOCTYPE")) continue;
			String image = i.split("<img class=\"coveri coveris\" src=\"")[1].split("\"")[0];
			String alt = i.split("<img class=\"coveri coveris\" src=\"")[1].split("\"")[4];
			String url = domain + i.split("href=\"")[1].split("\"")[0];

			System.out.println("grabbing data for " + "https://animeflix.ws/search.html?keyword=" + query.replace(" ", "-") + " >> " + url);
			String dat2 = this.readURL(url);

			for(String o : dat2.split("<li class=\"inline\">Episode List</li>")[1].split("<li class=\"epi-me\">")) {
				if(!o.contains("a href=\"")) continue;
				String episodeURL = domain + o.split("a href=\"")[1].split("\"")[0];
				String episodeTitle =  o.split("<div title=\"")[1].split("\"")[0];
				
				System.out.println("grabbing data for " + "https://animeflix.ws/search.html?keyword=" + query.replace(" ", "-") + " >> " + url + " >> " + episodeURL);
				String dat3 = this.readURL(episodeURL);
				
				String videoURL = "https://embed.animeflix.ws/video.php" + dat3.split("value=\"https://embed.animeflix.ws/video.php?")[1].split("\"")[0];
				
				System.out.println(videoURL);
				break;
			}
			
			break;
		}
		
	}
	
	private String readURL(String page) {
		try {
			HttpGet request = new HttpGet(page);
			CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36").setConnectionTimeToLive(5, TimeUnit.SECONDS).build();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String dat = EntityUtils.toString(entity);
			return dat;
		} catch (Exception e) {}
		
		return "";
	}

}
