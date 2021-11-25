package com.manga.scrape.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manga.data.Chapter;
import com.manga.data.LinkedPage;
import com.manga.data.MangaData;
import com.manga.data.Page;
import com.manga.data.SearchData;

public class NhentaiDataScrape extends DataScrape{

	public NhentaiDataScrape(String url, SearchData searchData) {
		super(url, searchData);
	}


	@Override
	public MangaData get() {
		
		String data = this.getHtml();

		String[] check = data.split("<div class=\"chapter-preview-content\">")[1].split("</ul>")[0].split("</li>");
		
		//pages
		int pageNumber = 1;
		
		List<LinkedPage> pages = new ArrayList<LinkedPage>();
		
		Chapter chapter = new Chapter(1, this.search.getName() + " Chapter 1", data, this.search.getSources());
		
		for(int i = 0; i < check.length; i++) {
			
			String img = "";
			
			for(String line : check[i].split("\\r?\\n")) {	
				if(line.contains("<img src=\"")) img = line.split("<img src=\"")[1].split("\"")[0].split(".jpeg")[0] + ".jpeg";
			}
			
			if(img.isEmpty()) continue;
			
			Page page = new Page(img, pageNumber);
			
			LinkedPage prev = null;
			LinkedPage next = null;
			
			LinkedPage linkedPage = new LinkedPage(chapter, page, prev, next);
			
			if(i != 0) {
				//Page prevPage = new Page(filter[(o-1)].trim(), (pageNumber-1));
				prev = pages.get(i-1);
				prev.setNextPage(linkedPage);
				linkedPage.setPrevPage(prev);
			}
			
			pages.add(linkedPage);
			
			pageNumber++;
		}

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
		
		for(String key : details.keySet()) {
			System.out.println(key + ": " + details.get(key));
		}
		
		chapter.setPages(pages);
		
		List<Chapter> chapters = new ArrayList<>();
		chapters.add(chapter);
		
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
