package com.manga.data;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MangaData {

	private Map<String, String> details;
	
	private List<Chapter> chapters;
	
	public MangaData(List<Chapter> chapters, Map<String, String> details) {
		this.chapters = chapters;
		Collections.reverse(chapters);
		this.details = details;
	}
	
	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public ChapterHandler getChapters() {
		ChapterHandler chapterhandler = new ChapterHandler(chapters);
		return chapterhandler;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
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
