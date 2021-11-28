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

public class MangaDex extends Source{

	private final String domain = "https://mangadex.tv/";
	
	public MangaDex(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
		
			String data = this.readURL("https://mangadex.tv/search?type=titles&title=" + this.query.replace(" ", "+") + "&submit=").split("<div class=\"container\" role=\"main\" id=\"content\">")[1];
			
			List<QueriedEntity> mangas = new ArrayList<>();
			
			for(String section : data.split("manga-entry col-lg-6 border-bottom pl-0 my-1")) {
				String[] splitter = section.split("<div style=\"height: 210px; overflow: hidden;\">");
				if(splitter.length <= 1) continue;
				String content = splitter[0];
				//String description = splitter[1].split("</div>")[0].trim();
				
				String alt = content.split("<a class=\"ml-1 manga_title text-truncate\"")[1].split("title=\"")[1].split("\"")[0];
				String url = domain + content.split("<a class=\"ml-1 manga_title text-truncate\"")[1].split("href=\"")[1].split("\"")[0];
				String img = domain + content.split("data-src=\"")[1].split("\"")[0];
				
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
			
			String description = data.split("<div class=\"col-lg-3 col-xl-2 strong\">Description:</div>")[1]
									 .split("<div class=\"col-lg-9 col-xl-10\">")[1]
									 .split("</div>")[0].trim();
			
			String chapters = data.split("<span class=\"far fa-file fa-fw \" aria-hidden=\"true\" title=\"Total chapters\"></span>")[1]
					 			  .split("</li>")[0].trim();
			
			String views = data.split("<span class=\"fas fa-eye fa-fw \" aria-hidden=\"true\" title=\"Views\"></span>")[1]
		 			  .split("</li>")[0].trim();
			
			String rating = data.split("<span class=\"fas fa-star fa-fw \" aria-hidden=\"true\" title=\"Bayesian rating\"></span>")[1]
		 			  .split("</span>")[0].trim();
			
			String[] genres = data.split("<div class=\"col-lg-3 col-xl-2 strong\">Genre:</div>")[1]
							   .split("<div class=\"col-lg-9 col-xl-10\">")[1]
							   .replace("<a class=\"badge badge-secondary\" href=\"/search?genre=", "")
							   .split("</div>")[0].trim()
							   .split("</a>");
			
			String genre = "";
			for(int i = 0; i < genres.length; i++) {
				if((i+1) < genres.length) {
					genre += genres[i].split("\"")[0].trim() + ", ";	
				}else {
					genre += genres[i].split("\"")[0].trim();	
				}
			}
			
			Map<String, String> attributes = new HashMap<>();
			
			attributes.put("description", description);
			attributes.put("chapters", chapters);
			attributes.put("views", views);
			attributes.put("rating", rating);
			attributes.put("genre", genre);
			
			List<Chapter> chapterList = new ArrayList<>();
			
			for(String i : data.split("<div class=\"chapter-row d-flex row no-gutters p-2 align-items-center border-bottom odd-row\"")) {
				if(!i.contains("href")) continue;
				
				String content = i.split("<a href=\"")[1].split("</a>")[0];
				
				if(!content.contains("text-truncate")) continue;
				
				String url = domain + content.split("\"")[0];
				String name = content.split("class=\"text-truncate\">")[1].trim();
				
				Chapter chapter = new Chapter(manga, name, url, () -> {		
					List<String> pages = new ArrayList<>();
					
					String pageData = this.readURL(url).split("<div class=\"reader-images col-auto row no-gutters flex-nowrap m-auto text-center cursor-pointer directional\">")[1];
					
					for(String page : pageData.split("data-src=\"")) {
						if(!page.contains("http")) continue;
						String pageURL = page.split("\"")[0].trim();
						pages.add(pageURL);
					}
					
					return pages;
				});
				
				chapterList.add(chapter);
				
			}
			
			RequestMangaData requestMangaData = new RequestMangaData(manga, attributes, chapterList);
			
			return requestMangaData;
			
		};
	}

	@Override
	public Callable<RequestAnimeData> requestAnimeData(QueriedEntity manga) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		try {
			
			long start = System.currentTimeMillis();
			
			QueriedEntity manga = new MangaDex("loli").requestQueryResults().call().getMangas().get(0);
			
			System.out.println("Found " + manga.getUrl());
			
			manga.getSource().requestMangaData(manga);
			
			RequestMangaData request = manga.getSource().requestMangaData(manga).call();
			
			System.out.println("chapters: " + request.getChapters().size());
			
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
	
	@Override
	public SourceType sourceType() {
		// TODO Auto-generated method stub
		return SourceType.MANGA;
	}
}
