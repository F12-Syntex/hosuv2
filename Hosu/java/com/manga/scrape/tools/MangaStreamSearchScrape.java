package com.manga.scrape.tools;

import java.util.ArrayList;
import java.util.List;

import com.manga.data.SearchData;
import com.manga.sources.Sources;

public class MangaStreamSearchScrape extends QueryScrape{

	public MangaStreamSearchScrape(String url, Sources sources) {
		super(url, sources);
	}

	@Override
	public List<SearchData> get(){
		String data = this.getHtml();

		List<SearchData> results = new ArrayList<>();
		
		for(String i : data
				.split("<div class=\"col-md-8\">")[1]
				.split("<div class=\"col-md-12\">")[0]
				.split("<div class=\"col-md-6\">")) {
			
			if(i.contains("<div class=\"media mainpage-manga\">")) {
				
				i = i.split("<div class=\"media-left cover-manga\">")[1].split("<p class=\"description descripfix\">")[0];
				
				
				String URL = "";
				String TITLE = "";
				String IMG = "";
				
				for(String line : i.split("\\r?\\n")) {
					if(line.contains("class=\"tooltips")) continue;
					
					if(line.contains("<a href=")) {
						URL = line.split("<a href=\"")[1].split("\"")[0].trim();
					}
					if(line.contains("title=")) {
						TITLE = line.split("title=\"")[1].split("\">")[0].trim();
					}
					if(line.contains("<img src=\"")) {
						IMG = line.split("<img src=\"")[1].split("\"")[0].trim();
					}			
				}
				
				SearchData searchdata = new SearchData(TITLE, URL, IMG, sources);
				results.add(searchdata);
				
			}
		}
		
		if(this.limit == -1) {
			return results;
		}
		
		return results.subList(0, limit);
	}


}
