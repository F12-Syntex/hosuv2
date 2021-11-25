package com.hentai1.library;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.hosu.helpers.Reddit;

public class ImageSucker {

	public ImageSucker() {}
	
	public void search() {
		
		try {

			HttpGet request = new HttpGet("https://scrolller.com/r/hentai");
			CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36").setConnectionTimeToLive(5, TimeUnit.SECONDS).build();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String dat = EntityUtils.toString(entity);
			
			for(String i : dat.split(">")) {
				if(i.contains("https://femto.scrolller.com/")) {
					System.out.println(i);
				}
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
	public static void main(String[] args) {
		Reddit.getRandomImages("hentai", "hot", 999).forEach(i -> {
			System.out.println(i);
		});
	}
	
}
