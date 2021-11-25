package com.manga.scrape.tools;

import java.util.ArrayList;
import java.util.List;

import com.manga.data.SearchData;
import com.manga.sources.Sources;

public class NhentaiSearchScrape extends QueryScrape{

	public NhentaiSearchScrape(String url, Sources sources) {
		super(url, sources);
	}

	@Override
	public List<SearchData> get(){

		String data = this.getHtml();

		String[] check = data.split("<div class=\"tab-content-wrap\">")[1].split("<div class=\"tab-thumb c-image-hover\">");
		
		List<SearchData> results = new ArrayList<>();
		
		for(String i : check) {
			if(i.contains("<div class=\"summary-heading\">")) {
				String required = i.split("<div class=\"summary-heading\">")[0];
				//System.out.println("\n\n\n\n\n " + required);
				
				String img = "";
				String url = "";
				String name = "";
				
				for(String line : required.split("\\r?\\n")) {		
					if(line.contains("data-src")) img = line.split("data-src=\"")[1].split("\"")[0];
					if(line.contains("<h3 class=\"h4\">")) name = line.split(">")[2].split("<")[0];
					if(line.contains("<h3 class=\"h4\">")) url = line.split("href=\"")[1].split("\">")[0];
				}
				
				img = img.trim();
				url = url.trim();
				name = name.trim();
				
				SearchData result = new SearchData(name, url, img, this.sources);
				results.add(result);
			}
		}
		
		return results;
	}


}
