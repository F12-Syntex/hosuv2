package com.syntex.manga.models;

import java.util.concurrent.Callable;

public class Episode implements IDownloadable{

	public Episode(String name, String url, QueriedEntity manga, Callable<String> videoURL) {
		super();
		this.name = name;
		this.url = url;
		this.manga = manga;
		this.videoURL = videoURL;
	}

	private String name;
	private String url;
	private QueriedEntity manga;
	public Callable<String> videoURL;
	
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
	public QueriedEntity getManga() {
		return manga;
	}
	public void setManga(QueriedEntity manga) {
		this.manga = manga;
	}
	public Callable<String> getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(Callable<String> videoURL) {
		this.videoURL = videoURL;
	}
	
	@Override
	public void download() throws Exception {
		// TODO Auto-generated method stub
		
	}

		
}