package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class Doujins extends Source{

	public Doujins(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
		
			List<QueriedManga> mangas = new ArrayList<>();
			
			try {
			
				String data = this.readURL("https://doujins.com/searches?words=" + this.query.replace(" ", "+"));
				
				for(String i : data.split("<!-- Series, search results -->")[1].split("previewfolder")[0].split("<div class=\"thumbnail-doujin\">")) {
					String section = i.split("<strong>")[0];
					
					if(!section.contains("<a href=\"")) continue;
					
					String url = "https://doujins.com" + section.split("<a href=\"")[1].split("\"")[0];
					String thub = section.split("srcset=\"")[1].split("\"")[0].split(" ")[0];
					String alt = section.split("<div class=\"text\">")[1].split("\"")[0].split("</div>")[0];
					
					
					QueriedManga manga = new QueriedManga(thub, alt, this, url);
					mangas.add(manga);
				}
				
				
				RequestQueryResults queryResults = new RequestQueryResults(this, query, mangas);
				
				return queryResults;
				
			}catch (Throwable e) {
				RequestQueryResults queryResults = new RequestQueryResults(this, query, mangas);
				System.err.println("The query " + this.query + " returned no results.");
				return queryResults;
			}
		
		};
	
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		
		return () -> {
			
			String data = this.readURL(manga.getUrl());
			
			String tags = data.split("</i> Tag</div>")[1].split("</li>")[0];
			
			Map<String, String> attributes = new HashMap<>();
			
			String tag = "";
			
			for(String i : tags.split("class=\"\">")) {
				if(i.contains("<hr")) continue;
				tag += i.split("</a>")[0].trim() + ", ";
			}
			
			tag = Optional.ofNullable(tag)
						   .filter(sStr -> sStr.length() != 0)
						   .map(sStr -> sStr.substring(0, sStr.length() - 1))
						   .orElse(tag);
			
			attributes.put("Tag(s)", tag);
			
			String pagesSection = data.split("<!-- Center Image -->")[1].split("<!-- Mobile audio controllers on fullscreen -->")[0];
			
			Callable<List<String>> pagesData = () ->{
			
				List<String> pages = new LinkedList<>();
				
				for(String i : pagesSection.split("<img class=\"doujin active\"")) {
					
					if(!i.contains("data")) continue;
					String thumb = i.split("data-thumb2=\"")[1].split("\"")[0].split(" ")[0].trim();
					pages.add(thumb);
				
				}
				
				return pages;
				
			};
			
			
			Chapter chapter = new Chapter(manga, this.query, manga.getUrl(), pagesData);
			
			List<Chapter> chapters = new ArrayList<>();
			chapters.add(chapter);
			 
			RequestMangaData request = new RequestMangaData(manga, attributes, chapters);
			
			return request;
			
		};
	}
	
	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return true;
	}

}
