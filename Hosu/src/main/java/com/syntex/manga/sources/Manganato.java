package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class Manganato extends Source{

	public Manganato(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
		
			String data = this.readURL("https://manganato.com/search/story/" + this.query.replace(" ", "_")).split("Page 1")[1];
			
			List<QueriedManga> mangas = new ArrayList<>();
			
			for(String i : data.split("</h3>")) {
				if(!i.contains("<h3>")) continue;
				if(!i.contains("href")) continue;
				if(!i.contains("title")) continue;
				if(!i.contains("src")) continue;
				
				String section = i.split("<h3>")[0].split("search-story-item")[1];
				
				String url = section.split("href=\"")[1].split("\"")[0];
				String alt = section.split("title=\"")[1].split("\"")[0];
				String image = section.split("src=\"")[1].split("\"")[0];
				
				QueriedManga manga = new QueriedManga(image, alt, this, url);
				mangas.add(manga);
			}
			
			RequestQueryResults results = new RequestQueryResults(this, this.query, mangas);
			
			return results;
		
		};
		
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		return () -> {
			String data = this.readURL(manga.getUrl());
			
			Map<String, String> attributes = new HashMap<>();
			
			for(String i : data.split("<tr>")) {
				String section = i.split("</tr>")[0];
				if(!section.contains("<i class=\"info-")) continue;
				
				String key = section.split("</i>")[1].split("</td>")[0].replace(" :", "");
				String value = section.split("<td class=\"table-value\">")[1].split("<h2>")[0].replace(" :", "").trim();
				
				if(value.contains("href")) value = value.split(">")[1].split("<")[0];
				if(value.contains("<")) value = value.split("<")[0];
				
				attributes.put(key, value);
			}
			
			List<Chapter> chapters = new ArrayList<>();
			
			for(String i : data.split("<ul class=\"row-content-chapter\">")[1].split("<li class=\"a-h\">")) {
				String section = i.split("</li>")[0];
				if(!section.contains("href")) continue;
				
				String url = section.split("href=\"")[1].split("\"")[0].split("</a>")[0];
				String alt = section.split("title=\"")[1].split("\"")[0].split("</a>")[0];
				
				Chapter chapter = new Chapter(manga, alt, url, () -> {
					String pageData = this.readURL(url);
					String pageContent = pageData.split("<div class=\"container-chapter-reader\">")[1].split("<div style=")[0];
					List<String> pages = new ArrayList<String>();
					
					for(String o : pageContent.split("<img src=\"")) {
						String pageURL = o.split("\"")[0];
						pages.add(pageURL);
					}
					
					return pages;
				});
				
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
