package com.hosu.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Reddit {


	/*
	 * 
	 * 
	 * 
	 */
	
	public static List<String> getGifs() {
		
		//https://scrolller.com/hentai-gifs
		
		List<String> urls = new ArrayList<String>();
		
		try {
		
			HttpGet request = new HttpGet("https://thehentaigif.com/");
			CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36").setConnectionTimeToLive(5, TimeUnit.SECONDS).build();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			//JSONObject result = new JSONObject(EntityUtils.toString(entity));
			String dat = EntityUtils.toString(entity);
			//Akari.getLogger(Reddit.class).info("Response recieved.\n" + dat);
			
			//<img src=
			
			for(String i : dat.split("\\r?\\n")) {
				if(i.contains("<img src=") && i.contains("data-gif")) {
					urls.add(i.split("data-gif=\"")[1].split("\"")[0]);
				}
			}
			
			Collections.shuffle(urls);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return urls;
	}
	
	
	public static List<String> getRandomImages(String search, String cato){
		
		if(search.equalsIgnoreCase("random")) {
			return getGifs1();
		}
		
		if(cato.equalsIgnoreCase("all")) {
			List<String> New = getRandomImages(search, "new", 9999);
			List<String> Hot = getRandomImages(search, "top", 9999);
			List<String> Top = getRandomImages(search, "hot", 9999);
			
			List<String> all = new ArrayList<String>();
			all.addAll(New);
			all.addAll(Hot);
			all.addAll(Top);
			
			Collections.shuffle(all);
			return all;
			
		}
		
		
		
		return getRandomImages(search, cato, 999999);
	}
	
	public static List<String> getRandomImagesAll(String search, int limit){
		
		if(search.equalsIgnoreCase("random")) {
			return getGifs1();
		}
		
			List<String> New = getRandomImages(search, "new", 9999);
			List<String> Hot = getRandomImages(search, "top", 9999);
			List<String> Top = getRandomImages(search, "hot", 9999);
			
			List<String> all = new ArrayList<String>();
			all.addAll(New);
			all.addAll(Hot);
			all.addAll(Top);
			
			Collections.shuffle(all);
			
			if(all.size() > limit) {
				return all.subList(0, limit);
			}
			
			return all;

	}
	
	public static List<String> getGifs1() {
		List<String> gifs = new ArrayList<>();
		for(int i = 0; i < 20; i++) {
			gifs.add("https://cdn.porngifs.com/img/" + ThreadLocalRandom.current().nextInt(100000));
		}
		return gifs;
	}
	
	public static List<String> getRandomImages(String search, String cato, int limit){
		try {
			
			
			String uri = "https://www.reddit.com/r/" + search + "/" + cato + ".json?&show=all";

			HttpGet request = new HttpGet(uri);
			CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36").setConnectionTimeToLive(5, TimeUnit.SECONDS).build();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			JSONObject result = new JSONObject(EntityUtils.toString(entity));
			
			JSONArray obj = result.getJSONObject("data").getJSONArray("children");
			
			List<String> images = new ArrayList<String>();
			
			if(obj.length() == 0) {
				return null;
			}

			for(int i = 0; i < obj.length(); i++) {
				JSONObject data = obj.getJSONObject(i).getJSONObject("data");
				if(data.has("url")) {
					String imageURI = data.getString("url");
					if(!imageURI.equalsIgnoreCase("self") && (imageURI.contains(".png") || imageURI.contains(".jpg") || imageURI.contains(".jpeg") || imageURI.contains(".gif"))) {
						images.add(imageURI);
					}
				}
				if(data.has("preview")) {
					if(data.getJSONObject("preview").has("reddit_video_preview")) {
						if(data.getJSONObject("preview").getJSONObject("reddit_video_preview").has("fallback_url")) {
							//String url = data.getJSONObject("preview").getJSONObject("reddit_video_preview").getString("fallback_url");
							//images.add(url);
						}
					}
				}
			}
		
			Collections.shuffle(images);
			
			if(limit > 0 && images.size() > limit) {
				images = images.subList(0, limit);
			}

			return images;
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}

	
}
