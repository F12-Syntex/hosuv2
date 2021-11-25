package com.manga.scrapper;

import com.manga.sources.Sources;

public class MangaQuery {

	public Sources sources;
	public String query;
	public int limit;
	
	public MangaQuery(Sources sources, String query) {
		this.sources = sources;
		this.query = query;
		this.limit = -1;
	}
	
	public MangaQuery limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public Query execute() {
		return new Query(this);
	}
	
}
