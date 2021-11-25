package com.manga.scrape.data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.manga.data.MangaData;
import com.manga.data.SearchData;

public abstract class DataScrape {	
	
	protected String url;
	protected int limit = -1;
	protected SearchData search;

	public DataScrape(String url, SearchData search2) {
		this.url = url;
		this.search = search2;
	}

	
	public DataScrape limit(int limit, SearchData searchData) {
		this.limit = limit;
		this.search = searchData;
		return this;
	}

	protected String getHtml() {
		try {
			
			HttpGet request = new HttpGet(this.url);
			CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36").setConnectionTimeToLive(5, TimeUnit.SECONDS).build();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String dat = EntityUtils.toString(entity);
			
			return dat;
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public abstract MangaData get();
	
}
