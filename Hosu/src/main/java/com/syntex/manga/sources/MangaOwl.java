package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.utils.Encoder;

public class MangaOwl extends Source{

	public MangaOwl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {	
			String data = this.readURL("https://mangaowl.com/search/1?search=" + Encoder.encode(query));
			
			String[] section = data.split("data-title=");
			
			List<QueriedManga> mangas = new ArrayList<>();
			
			for(String o : section) {
				if(!o.contains("data-background-image=")) continue;
				String img = o.split("data-background-image=\"")[1].split("\"")[0].trim();
				String alt = o.split(">")[0].trim().replace("\"", "");
				String url = "https://mangaowl.com" + o.split("<h6 class=\"comic_title\">")[1].split("<a href=\"")[1].split("\"")[0].trim();
				QueriedManga queriedManga = new QueriedManga(img, alt, this, url);
				mangas.add(queriedManga);
			}
			
			RequestQueryResults results = new RequestQueryResults(this, query, mangas);
			
			return results;
		};
		
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		
		return() -> {
			String data = this.readURL(manga.getUrl());
	
			Map<String, String> attributes = new HashMap<>();
			
			for(String i : data.split("@media screen")[1].split("</div>")[0].split("<span>")) {
				if(!i.contains("</p>")) continue;
				for(String line : i.split(System.lineSeparator())) {
					if(!line.contains("label")) continue;
					
					String key = line.split("<label>")[0].trim();
					String value = "N/A";
					
					if(key.contains("<")) continue;
					
					try {
						value = line.split("</span>")[1];	
					}catch (Exception e) {}
					
					
					attributes.put(key, value);
				}
			}
			
			List<Chapter> chapters = new LinkedList<>();
			
			for(String i : data.split("<li class=\"list-group-item chapter_list \">")) {
				if(i.contains("Read online")) continue;
				String alt = i.split("</li>")[0].split("title")[1].split(">")[1].trim().split(System.lineSeparator())[0].split("<")[0].trim();
				String url = i.split("</li>")[0].split("href=\"")[1].split("\"")[0];
			
				Callable<List<String>> pagedInfo = () -> {
					String pagedData = this.readURL(url);
					List<String> pages = new LinkedList<>();
					for(String o : pagedData.split("data-src=\"")) {
						if(!o.contains("blogspot")) continue;
						pages.add(o.split("\"")[0]);
					}
					return pages;
				};
				
				Chapter chapter = new Chapter(manga, alt, url, pagedInfo);
				chapters.add(chapter);
			}
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, attributes, chapters);
			
			return requestMangaData;
		};
		
		
	}

	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
