package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestAnimeData;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class Nhentai extends Source{

	public Nhentai(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		List<QueriedEntity> mangas = new ArrayList<>();
		
		return () -> {
		
			try {
			
			String data = this.readURL("https://nhentai.to/search?q=" + this.query.replace(" ", "+"));

			String section = data.split("<div class=\"container index-container\">")[1].split("<section class=\"pagination\">")[0];
			
			for(String i : section.split("<div class=\"gallery\"")) {
				if(!i.contains("href")) continue;
				
				String url = "https://nhentai.to" + i.split("<a href=\"")[1].split("\"")[0];
				String image = i.split("img src=\"")[1].split("\"")[0];
				String alt = i.split("div class=\"caption\">")[1].split("</div>")[0];
				
				QueriedEntity manga = new QueriedEntity(image, alt, this, url);
				mangas.add(manga);
			}
			
			RequestQueryResults results = new RequestQueryResults(this, this.query, mangas);
			
			return results;
			
			}catch (Exception e) {
				//System.err.println("No results found for source " + this.getClass().getName() + " had a " + e.getLocalizedMessage() + " exception");
				RequestQueryResults results = new RequestQueryResults(this, this.query, mangas);
				return results;
			}

		};
		
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedEntity manga) {
		
		return () -> {
			
			//get max page
			List<String> list = getImages(manga.getUrl());
			int size = list.get(list.size()-1).split("/").length;
			int maxPage = Integer.parseInt(list.get(list.size()-1).split("/")[size-1].split("t")[0].trim());
			
			//cashe data
			Map<Integer, Callable<String>> cashed = new TreeMap<>();
			
			//retrieve dataO
			for(int i = 1; i <= maxPage; i++) {
				//read page
				final int index = i;
				cashed.put(index, () -> {
					return readPage(manga.getUrl(), index);
				});
			}
			
				Chapter chapter = new Chapter(manga, "Chapter 1", manga.getUrl(), () -> {
				List<String> chapters = new ArrayList<>();
				
				//organise data
				for(int key : cashed.keySet()) {
					try {
						String page = cashed.get(key).call();
						chapters.add(page);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return chapters;
			});
			
			List<Chapter> chapters = new ArrayList<>();
			chapters.add(chapter);
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, new HashMap<String, String>(), chapters);
			
			return requestMangaData;
			
		};
	}
	
	private String readPage(String code, int page) {
		String url = code + "/" + page;
		
		//System.out.println("retrieving " + url);
		
		String dat = readURL(url);
		
		for(String i : dat.split("\\r?\\n")) {
			if(i.contains("<img src=\"") && i.contains("galleries")) {
				return i.split("\"")[1].split("\"")[0];
			}
		}
		
		return "";
	}
	
	private List<String> getImages(String url) {
		String dat = readURL(url);
		
		List<String> o = new ArrayList<>();
		
		for(String i : dat.split("\\r?\\n")) {
			if(i.contains("<img src=\"") && i.contains("galleries") && !i.contains("cover")) {
				 o.add(i.split("\"")[1].split("\"")[0]);
			}
		}
		
		return o;
	}
	
	@Override
	public Callable<RequestAnimeData> requestAnimeData(QueriedEntity manga) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		try {
			
			long start = System.currentTimeMillis();
			QueriedEntity manga = new Nhentai("loli").requestQueryResults().call().getMangas().get(0);
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
		return true;
	}
	
	@Override
	public SourceType sourceType() {
		// TODO Auto-generated method stub
		return SourceType.MANGA;
	}
}
