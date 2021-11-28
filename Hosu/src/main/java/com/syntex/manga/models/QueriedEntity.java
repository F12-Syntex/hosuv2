package com.syntex.manga.models;

import com.syntex.manga.queries.RequestAnimeData;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

public class QueriedEntity {

	private String image;
	private String alt;
	
	public Domain source;
	
	private String url;
	
	public String query;
	
	public QueriedEntity(String image, String alt, Source source, String url) {
		this.image = image;
		this.alt = alt;
		this.source = Domain.fromClass(source.getClass());
		this.url = url;
		this.query = source.query;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public Source getSource() {
		return Domain.fromDomain(this.source.getSource(), this.query);
	}

	public Domain getDomain() {
		return this.source;
	}
	public RequestMangaData getAsManga() {
		try {
			return getSource().requestMangaData(this).call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public RequestAnimeData getAsAnime() {
		try {
			return getSource().requestAnimeData(this).call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
