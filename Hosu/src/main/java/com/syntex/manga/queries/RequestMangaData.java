package com.syntex.manga.queries;

import java.util.List;
import java.util.Map;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;

public class RequestMangaData {
	
	private QueriedManga manga;
	private Map<String, String> attributes;
	private List<Chapter> chapters;

	public RequestMangaData(QueriedManga manga, Map<String, String> attributes, List<Chapter> chapters) {
		this.manga = manga;
		this.attributes = attributes;
		this.chapters = chapters;
	}
	
	public QueriedManga getManga() {
		return manga;
	}
	public void setManga(QueriedManga manga) {
		this.manga = manga;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	public List<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

}
