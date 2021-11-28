package com.hosu.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Scrapper {
	
	public final ExecutorService threadpool = Executors.newFixedThreadPool(15);
	
	//final int PAGES = 1937;
	final int PAGES = 1132;

	private final AtomicInteger COUNTER = new AtomicInteger(0);
	private final AtomicInteger MANGA_COUNTER = new AtomicInteger(0);
	
	public void run(Database database) {
	
		final long start = System.currentTimeMillis();
		
		//get feed back on progress.
		new Thread(() -> {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {	
				@Override
				public void run() {
					
					double est = (double)COUNTER.get() / 1132.0;
					
					String time = "forever";
					long taken = (System.currentTimeMillis() - start)/1000;
					
					if(est != 0) {
						time = getDurationString((int)((double)((100.0*taken)/(est*100.0))));
					}
					
					System.out.println("Tasks completed: " + COUNTER.get() + "/" + PAGES + " mangas retrieved: " + MANGA_COUNTER.get() + " EST: " + time);
					if(COUNTER.get() == PAGES) {
						System.exit(0);
					}
				}
			}, 0L, 1000L);
		}).start();
		
		for(int i = 0; i < PAGES; i++) {
			final int page = i;
			this.threadpool.execute(() -> {
				this.find(page, database);
			});
		}
		
		
	}
	
	public boolean redo(int page, Database database) {
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.find(page, database);
	}
	
	private String getDurationString(int seconds) {

	    int hours = seconds / 3600;
	    int minutes = (seconds % 3600) / 60;
	    seconds = seconds % 60;

	    return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
	}

	private String twoDigitString(int number) {

	    if (number == 0) {
	        return "00";
	    }

	    if (number / 10 == 0) {
	        return "0" + number;
	    }

	    return String.valueOf(number);
	}
	
	private boolean find(int page, Database database) {
		
		//String url = "https://www.anime-planet.com/manga/all?page=" + page;
		
		String url = "https://myanimelist.net/topmanga.php?limit=" + ((page-1)*50);
		
		String data = this.readURL(url);
		
		List<String> contents = new ArrayList<>();
		
		for(String i : data.split("<!DOCTYPE")[1].split("alt=\"Manga: ")) {
			if(i.startsWith("html")) continue;
			if(i.contains("html PUBLIC")) continue;
			if(i.equalsIgnoreCase("html PUBLIC")) continue;
			String manga = i.split("\"")[0].trim();
			contents.add(manga);
			MANGA_COUNTER.incrementAndGet();
			//System.out.println(manga);
		}
		
		/*
		for(String i : data.split("<img alt=\"")) {
			String manga = i.split("\"")[0].trim();
			if(manga.startsWith("<")) continue;
			contents.add(manga);
		}
		 */
		//write contents to the database
		database.writeManga(contents);
		
		if(contents.size() < 20) {
			return redo(page, database);
		}
		
		COUNTER.incrementAndGet();
		
		return true;
	}
	
	public String readURL(String page) {
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
