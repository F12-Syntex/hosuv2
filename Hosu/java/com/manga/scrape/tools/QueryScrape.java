package com.manga.scrape.tools;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.manga.data.SearchData;
import com.manga.sources.Sources;

public abstract class QueryScrape {	
	
	protected String url;
	protected int limit = -1;
	protected Sources sources;
	
	public QueryScrape limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public QueryScrape(String url, Sources sources) {
		this.url = url;
		this.sources = sources;
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

	public abstract List<SearchData> get();
	
}
