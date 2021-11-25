package com.manga.scrape.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manga.data.Chapter;
import com.manga.data.Genre;
import com.manga.data.MangaData;
import com.manga.data.SearchData;
import com.manga.sources.Sources;

public class MangaSteamDataScrape extends DataScrape{

	public MangaSteamDataScrape(String url, SearchData search) {
		super(url, search);
	}

	@Override
	public MangaData get() {
		
		String data = this.getHtml();
		
		String detailsSection = data.split("<p class=\"description-update\">")[1].split("<span id=\"bookmark\">")[0];
		
		String alternative = "N/A";
		String view = "N/A";
		String authors = "N/A";
		List<Genre> genre = new ArrayList<>();
		List<Chapter> chapters = new ArrayList<>();
		String type = "N/A";
		String release = "N/A";
		String status = "N/A";
		
		//details section
		for(String i : detailsSection.split("<span>")) {
			if(i.isEmpty()) continue;
			
			if(i.contains("Alternative:")) alternative = i.split("</span>")[1].split("<br>")[0].trim();
			if(i.contains("View:")) view = i.split("</span>")[1].split("<br>")[0].trim();
			if(i.contains("Author(s):")) authors = i.split("</span>")[1].split("<br>")[0].trim();;
			
			if(i.contains("Genre:")) {
				for(String o : i.split("<a href=\"")) {
					if(!o.startsWith("http")) continue;
					String genreURL = o.split("\">")[0].trim();
					String genreName = o.split("\">")[1].split("</a>")[0].trim();
					Genre currentGenre = new Genre(genreName, genreURL);
					genre.add(currentGenre);
				}
			}
			
			if(i.contains("Type:")) type = i.split("</span>")[1].split("<br>")[0].trim();;
			if(i.contains("Release:")) release = i.split("</span>")[1].split("<br>")[0].trim();;
			if(i.contains("Status:")) status = i.split("</span>")[1].split("<br>")[0].trim();;
		}

		int chapterN = 0;
		
		for(String o : data.split("<div class=\"content mCustomScrollbar\">")[1]
				.split("<div class=\"chapter-list\">")[1]
				.split("<div class=\"total-chapter\">")[0].split("<li class=\"row\">")) {
		
			if(!o.contains("title")) continue;
			
			String pageURL = o.split("href=\"")[1].split("\"")[0];
			String pageName = o.split("title=\"")[1].split("</a>")[0];
		
			Chapter chapter = new Chapter(0, pageName, pageURL, Sources.MANGA_STREAM);
			chapters.add(chapter);
			chapterN++;
		}
		
		for(Chapter o : chapters) {
			o.setChapter(chapterN);
			chapterN--;
		}
		
		Map<String, String> details = new HashMap<String, String>();
		
		details.put("alternative", alternative);
		details.put("alternative", alternative);
		details.put("authors", authors);
		details.put("views", view);
		details.put("type", type);
		details.put("release", release);
		details.put("status", status);
		
		MangaData mangaData = new MangaData(chapters, details);
		
		return mangaData;
	}

	
	@Override
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
	
}
