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

public class MangaBuddy extends Source{

	public MangaBuddy(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
		
			String data = this.readURL("https://mangabuddy.com/search?q=" + this.query.replace(" ", "+"));
			
			List<QueriedManga> mangas = new ArrayList<>();
			
			for(String section : data.split("<div class=\"section-body\">")[1].split("</div></div><script>")[0].split("<a title=")) {
				if(!section.contains("data-src")) continue;
				String alt = section.split("\"")[1].split("\"")[0];
				String image = "https:" + section.split("data-src=\"")[1].split("\"")[0];
				String url = "https://mangabuddy.com" + section.split("href=\"")[1].split("\"")[0];
				
				QueriedManga manga = new QueriedManga(image, alt, this, url);
				mangas.add(manga);
			}
			
			RequestQueryResults requestQueryResults = new RequestQueryResults(this, this.query, mangas);
			
			return requestQueryResults;
		
		};
		
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		
		return () -> {
		
			String data = this.readURL(manga.getUrl());
			
			List<Chapter> chapters = new ArrayList<>();
			
			for(String section : data.split("id=\"chapter-list\"")[1].split("<div class=\"readmore\" id=\"show-more-chapters\">")[0].split("<a href=\"")) {
				if(!section.contains("title")) continue;
				String alt = section.split("</div>")[0].split("title=\"")[1].split("\"")[0];
				String url = "https://mangabuddy.com" + section.split("</div>")[0].split("\"")[0];
				Chapter chapter = new Chapter(manga, alt, url, () -> {
					
					List<String> pages = new ArrayList<>();
					
					String read = readURL(url).split("var chapImages = '")[1].split("</script>")[0].split("'")[0].trim();
					
					for(String i : read.split(",")) {
						String page = i.trim();
						pages.add("https://static.youmadcdn.xyz/manga/" + page);
					}
					
					return pages;
				});
				
				chapters.add(chapter);
			} 
			
			String attributeSection = data.split("<div class=\"meta box mt-1 p-10\">")[1].split("<div class=\"d-flex\">")[0];
			
			Map<String, String> attributes = new HashMap<>();
			
			for(String i : attributeSection.split("<p><strong>")) {
				if(!i.contains("title")) continue;
				String key = i.trim().split("</strong>")[0];
				String value = i.trim().split("</strong>")[1].split("title=\"")[1].split("\"")[0];
				attributes.put(key, value);
			}
			
			String desc = data.split("<p class=\"content\" style=\"margin-bottom: unset;line-height: 1.8; padding: 10px;\">")[1].split("</p>")[0].trim();
			attributes.put("Description", desc);
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, attributes, chapters);
			
			return requestMangaData;
		
		};
		
	}

	public static void main(String[] args) {
		try {
			
			long start = System.currentTimeMillis();
			QueriedManga manga = new MangaBuddy("sex").requestQueryResults().call().getMangas().get(0);
			RequestMangaData request = manga.getSource().requestMangaData(manga).call();
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
		return false;
	}
	
}
