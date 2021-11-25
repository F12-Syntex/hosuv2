package com.manga.scrapper;

import com.manga.sources.Sources;

public class Manga {

	private Sources sources;
	
	public Manga(Sources sources) {
		this.sources = sources;
	}
	
	public MangaQuery query(String query) {
		return new MangaQuery(sources, query);
	}
	
	
	
	
}
	