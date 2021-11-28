package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestAnimeData;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class FreeComics extends Source{

	public FreeComics(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
		
			String data = this.readURL("https://www.freecomics.xxx/?search=" + this.query.replace(" ", "+"))
					.split("<div class=\"xcontainer\">")[1]
					.split("<div class=\"xpageon\">1</div>")[0];
			
			List<QueriedEntity> mangas = new ArrayList<>();
			
			for(String i : data.split("<a href=\"")) {
				if(!i.contains("<div class=\"bookinfo\">")) continue;
				String section = i.split("<div class=\"bookinfo\">")[0];
				if(!section.contains("href")) continue;
				
				String url = section.split("href=\"")[1].split("\"")[0].split("url=")[1];
				String alt = section.split(url + "\" title=\"")[1].split("\"")[0];
				String img = section.split("data-src=\"")[1].split("\"")[0];
				
				QueriedEntity manga = new QueriedEntity(img, alt, this, url);
				mangas.add(manga);
			}
			
			RequestQueryResults requestQueryResults = new RequestQueryResults(this, this.query, mangas);
			
			return requestQueryResults;
		
		};
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedEntity manga) {
		
		return () -> {
		
			String data = this.readURL(manga.getUrl());
			
			Chapter chapter = new Chapter(manga, manga.getAlt(), manga.getUrl(), () -> {
				List<String> pages = new ArrayList<>();
				for(String line : data.split("data-lightbox=\"roadtrip\"")) {
					if(!line.contains("data-src=\"")) continue;
					String page = line.split("data-src=\"")[1].split("\"")[0];
					pages.add(page);
				}
				return pages;
			});
			
			
			Map<String, String> attributes = new HashMap<>();
			List<Chapter> chapters = new ArrayList<>();
			
			chapters.add(chapter);
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, attributes, chapters);
			
			return requestMangaData;
		};
		
	}

	@Override
	public Callable<RequestAnimeData> requestAnimeData(QueriedEntity manga) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static void main(String[] args) {
		try {
			
			long start = System.currentTimeMillis();
			QueriedEntity manga = new FreeComics("sex").requestQueryResults().call().getMangas().get(0);
			manga.getAsManga();
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
	public SourceType sourceType() {
		// TODO Auto-generated method stub
		return SourceType.MANGA;
	}
}
