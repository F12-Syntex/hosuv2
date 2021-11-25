package com.syntex.manga.api;

import java.lang.reflect.Constructor;

import com.syntex.manga.download.Downloader;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;


/**
 * This is the api class that allows you to access manga sources.
 *
 * @author syntex
 */
public class MangaAPI {
	
	private final static Downloader downloadHandler = new Downloader();
	
	public static Source search(String query, Class<? extends Source> source) {
		for(Domain domain : Domain.values()) {
			if(domain.getSource().equals(source)) {
				return search(query, domain);
			}
		}
		return null;
	}
	
	public static Source search(String query, Domain source) {
		
		try {
		
			Constructor<?> constructor = source.getSource().getDeclaredConstructor(String.class);
			Source invoke = (Source) constructor.newInstance(query);
			
			return invoke;
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Downloader getDownloader() {
		return MangaAPI.downloadHandler;
	}
	
	public static void shutdown() {
		MangaAPI.downloadHandler.threadpool.shutdownNow();
	}
	
	//MangaAPI api = new MangaAPI();
	//
	

}
