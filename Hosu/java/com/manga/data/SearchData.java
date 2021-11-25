package com.manga.data;

import com.manga.scrape.data.DataScrape;
import com.manga.scrape.data.MangaSteamDataScrape;
import com.manga.scrape.data.NhentaiDataScrape;
import com.manga.sources.Sources;

public class SearchData {

	private final String name;
	private final String url;
	private final String img;
	private final Sources sources;
	
	public SearchData(String name, String url, String img, Sources sources) {
		this.name = name;
		this.url = url;
		this.img = img;
		this.sources = sources;
	}
	
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public String getImg() {
		return img;
	}
	public Sources getSources() {
		return sources;
	}
	
	public DataScrape getData() {
		switch (sources) {
		case MANGA_STREAM: {
			DataScrape scrape = new MangaSteamDataScrape(url, this);
			return scrape;
		}
		case NHENTAI: {
			DataScrape scrape = new NhentaiDataScrape(url, this);
			return scrape;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + sources);
		}	
	}

	

}
