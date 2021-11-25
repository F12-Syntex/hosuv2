package com.manga.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.manga.scrape.pages.MangaStreamPageScrape;
import com.manga.scrape.pages.PageScrape;
import com.manga.sources.Sources;

public class Chapter {

	private int chapter;
	private String name;
	private String url;
	private Sources sources;
	
	private List<LinkedPage> pages;
	
	public Chapter(int chapter, String name, String url, Sources sources) {
		this.name = name;
		this.url = url;
		this.sources = sources;
		this.chapter = chapter;
		
		this.pages = new ArrayList<LinkedPage>();
	}
	
	public int getChapter() {
		return chapter;
	}

	public void setPages(List<LinkedPage> pages) {
		this.pages = pages;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public Sources getSources() {
		return sources;
	}

	public void setSources(Sources sources) {
		this.sources = sources;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public PageScrape getPages() {
		switch (sources) {
		case MANGA_STREAM: {
			PageScrape scrape = new MangaStreamPageScrape(url, this);
			return scrape;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + sources);
		}
	}
	
	public List<LinkedPage> getPagesIfAvailable(){
		return this.pages;
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
