package com.syntex.manga.sources;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.annotations.Expose;
import com.syntex.manga.cashe.CasheHelper;
import com.syntex.manga.cashe.CashingPool;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public abstract class Source{
	
	@Expose
	public String query;
	
	public Source(String query) {
		this.query = query;
	}
	
	public abstract Callable<RequestQueryResults> requestQueryResults();
	public abstract Callable<RequestMangaData> requestMangaData(QueriedManga manga);
	public abstract boolean nsfw();
	
	
	public Callable<RequestQueryResults> requestCashedQueryResults() {
		Callable<RequestQueryResults> cashed = this.getCashedQuery();
		if(cashed != null) {
			CashingPool.pool.execute(() -> {
				try {
					this.requestQueryResults().call();
				} catch (Exception e) {}
			});
			return cashed;
		}
		return this.requestQueryResults();
	}
	
	/*
	 * helper methods for sources.
	 */
	
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
	
	protected Callable<RequestQueryResults> getCashedQuery(){
		
		List<RequestQueryResults> cashed = CasheHelper.deserialiseQuriedCasheFile(this);
		
		if(cashed == null) {
			return null;
		}
		
		if(cashed.size() > 0) {
			return () -> cashed.get(0);
		}
		
		return null;
		
	}
}
