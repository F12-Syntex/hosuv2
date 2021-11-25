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

public class HentaiRead extends Source{

	public HentaiRead(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
	
		return () -> {
	
			List<QueriedManga> results = new ArrayList<>();
			
			try {
			
			String data = this.readURL("https://hentairead.com/?s=" + this.query + "&post_type=wp-manga");
					
			String[] check = data.split("<div class=\"tab-content-wrap\">")[1].split("<div class=\"tab-thumb c-image-hover\">");
			
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
					
					QueriedManga result = new QueriedManga(img, name, this, url);
					results.add(result);
				}
			}
			
			RequestQueryResults requestQueryResults = new RequestQueryResults(this, this.query, results);
			
			return requestQueryResults;
			
			}catch (Throwable e) {
				RequestQueryResults queryResults = new RequestQueryResults(this, query, results);
				System.err.println("The query " + this.query + " returned no results.");
				return queryResults;
			}
		
		};
		
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
	
		return () -> {
			
			System.out.println("asd");
			
			String data = this.readURL(manga.getUrl());
	
			String[] check = data.split("<div class=\"chapter-preview-content\">")[1].split("</ul>")[0].split("</li>");
			
			Chapter chapter = new Chapter(manga, " Chapter 1", manga.getUrl(), () -> {
	
				List<String> pages = new ArrayList<String>();
				
				for(int i = 0; i < check.length; i++) {
					
					String img = "";
					
					for(String line : check[i].split("\\r?\\n")) {	
						if(line.contains("<img src=\"")) img = line.split("<img src=\"")[1].split("\"")[0].split(".jpeg")[0] + ".jpeg";
					}
					
					if(img.isEmpty()) continue;
					pages.add(img);
				}
	
				return pages;
			});
	
			//manga info
			String[] summary = data.split("<div class=\"loader-inner ball-pulse\">")[1]
												.split("<div class=\"content-area\">")[0]
												.split("<div class=\"summary-heading\">");
			
			Map<String, String> details = new HashMap<String, String>();
			
			for(String i : summary) {
				if(!i.contains("<h5>")) continue;
				i = i.split("<h5>")[1];
				//System.out.println("\n\n\n\n\n" + i);
				String key = i.split("</h5>")[0].trim();
				
				if(key.equalsIgnoreCase("Rating")) details.put(key, i.split("<span id=\"averagerate\">")[1].split("</span>")[0] + "/5");
				if(key.equalsIgnoreCase("Rank")) details.put(key, i.split("<div class=\"summary-content\">")[1].split("</div>")[0]);
				if(key.equalsIgnoreCase("Release")) details.put(key, i.split("rel=\"tag\">")[1].split("</a>")[0]);
				if(key.equalsIgnoreCase("Status")) details.put(key, i.split("<div class=\"summary-content\">")[1].split("</div>")[0]);		
			}
			
			List<Chapter> chapters = new ArrayList<>();
			chapters.add(chapter);
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, details, chapters);
			
			return requestMangaData;
			
		};
		
	}
	
	public static void main(String[] args) {
		try {
			
			long start = System.currentTimeMillis();
			
			QueriedManga manga = new HentaiRead("loli").requestQueryResults().call().getMangas().get(0);
			
			//System.out.println("Found " + manga.getUrl());
			
			RequestMangaData request = manga.getSource().requestMangaData(manga).call();
			
			//System.out.println("data " + request.getChapters().size());
			
			List<String> pages = request.getChapters().get(0).getPages();
			
			System.out.println("took " + (System.currentTimeMillis()-start) + "(ms) to find " + pages.size() + " pages. printing.");
			pages.forEach(System.out::println);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return true;
	}

}
