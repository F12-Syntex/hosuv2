package com.manga.data;

import java.util.ArrayList;
import java.util.List;

public class ChapterHandler {

	List<Chapter> chapters = new ArrayList<>();
	
	public ChapterHandler(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	public Chapter getChapter(int number) {
		for(Chapter chapter : this.chapters) {
			if(chapter.getChapter() == number) {
				return chapter;
			}
		}
		return null;
	}

}
