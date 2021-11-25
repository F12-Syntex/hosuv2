package com.manga.scrapper;

import java.util.List;

import com.manga.data.SearchData;
import com.manga.scrape.tools.MangaStreamSearchScrape;
import com.manga.scrape.tools.NhentaiSearchScrape;
import com.manga.scrape.tools.QueryScrape;
import com.manga.sources.Sources;

public class Query {

	private QueryScrape scrape;
	private MangaQuery query;
	
	public Query(MangaQuery mangaQuery) {
		this.query = mangaQuery;
		switch (mangaQuery.sources) {
		case MANGA_STREAM: {
			String url = "http://mangastream.mobi/search?q=" + mangaQuery.query.replace(" ", "+");
			QueryScrape scrape = new MangaStreamSearchScrape(url, mangaQuery.sources);
			this.scrape = scrape;
			break;
		}
		case NHENTAI: {
			String url = "https://hentairead.com/?s=" + mangaQuery.query.replace(" ", "%20") + "&post_type=wp-manga";
			QueryScrape scrape = new NhentaiSearchScrape(url, mangaQuery.sources);
			this.scrape = scrape;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + mangaQuery.sources);
		}	
	}
	
	public List<SearchData> getResults() {
		List<SearchData> data = this.scrape.get();
		
		if(data.size() < this.query.limit || this.query.limit == -1) return data;
		
		return data.subList(0, this.query.limit);
	}
	
	public static String url(Sources source, String query) {
			switch (source) {
			case MANGA_STREAM: {
				String url = "http://mangastream.mobi/search?q=" + query.replace(" ", "+");
				return url;
			}
			case NHENTAI: {
				String url = "https://hentairead.com/?s=" + query.replace(" ", "%20") + "&post_type=wp-manga";
				return url;
			}
		}
			return null;
	}

}
