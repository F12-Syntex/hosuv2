package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.utils.TagUtils;

public class MangaStream extends Source{

	public MangaStream(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
			
			String name = this.query;
			List<QueriedManga> mangas = new ArrayList<>();
			
			String data = this.readURL("http://mangastream.mobi/search?q=" + this.query.replace(" ", "+"));
			
			String[] lines = data.split(System.lineSeparator());
			
			for(int i = 0; i < lines.length; i++) {
				if(lines[i].contains("img src")) {
					//System.out.println(lines[i]);
					//System.out.println(lines[i+1]);
				}
			}
			
			List<String[]> images = TagUtils.getImageTags(data, 2, 1);
			
			for(String[] j : images) {
				
			//	for(int i = 0; i < j.length; i++) {
			//		System.out.println(i + ": " + j[i]);
			//	}
				
				String img = j[2].split("\"")[1].split("\"")[0];
				String alt = j[3].split("\"")[1].split("\"")[0];
				String url = j[1].split("\"")[1].split("\"")[0];
				
				QueriedManga manga = new QueriedManga(img, alt, this, url);
				mangas.add(manga);
			}
			
			
			return new RequestQueryResults(this, name, mangas);
		};
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		return () -> {
			
			String data = this.readURL(manga.getUrl());
			
			Map<String, String> values = new HashMap<>();
			
			String[] lines = data.split("<div class=\"media-left cover-detail\">")[1]
								 .split("<div class=\"media-body\">")[1]
								 .split("</div>")[0]
								 .split("<span>");

			for(String i : lines) {
				//System.out.println(i);
				if(i.contains("</span>")) {
					if(i.contains("Status")) continue;
					if(i.contains("Genre")) continue;

					String field = i.replace("<span>", "").replace("</span>", "").split("<br>")[0].trim();
					String value = i.split("</span>")[1].split("<br>")[0].trim();
					
					if(value.isEmpty()) {
						continue;
					}
					
					values.put(field, value);
				}
			}
			
			String[] chapterLines = data.split("<div class=\"col-xs-12 chapter\">");
			
			
			List<Chapter> chapters = new ArrayList<Chapter>();
			
			for(String chapterData : chapterLines) {
				String name = chapterData.split("title")[1].split(">")[1].split("<")[0].trim();
				String url = chapterData.split("<a  href=\"")[1].split("\"")[0].trim();
				String span = chapterData.split("<span>")[1].split("</span>")[0].trim();
				if(!span.isEmpty()) {
					name = span;
				}
				
				Callable<List<String>> pages = () -> {
					String pageData = this.readURL(url);
					
					String[] images = pageData.split("<p id=arraydata style=display:none>")[1].split("<")[0].split(",");
					
					List<String> chapterPages = new LinkedList<>();
					
					for(String page : images) {
						String pageURL = page.trim();
						chapterPages.add(pageURL);
					}
					
					return chapterPages;	
				};
				
				
				Chapter chapter = new Chapter(manga, name, url, pages);
				chapters.add(chapter);
			}
			
			RequestMangaData requestedData = new RequestMangaData(manga, values, chapters);
			
			return requestedData;
		};
	}

	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
