package com.syntex.manga.models;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.utils.Encoder;

import javafx.scene.image.Image;

public class Chapter implements IDownloadable{

	private String name;
	private String url;
	private QueriedEntity manga;
	public Callable<List<String>> pages;

	private List<String> cashedPages = new ArrayList<>();
	private List<Image> cashedImage = new ArrayList<>();
	
	private boolean imagesCashed = false;
	
	public Chapter(QueriedEntity manga, String name, String url, Callable<List<String>> pages) {
		this.name = name;
		this.url = url;
		this.manga = manga;
		this.pages = pages;
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

	public QueriedEntity getManga() {
		return manga;
	}

	public void setManga(QueriedEntity manga) {
		this.manga = manga;
	}

	public void cashe() {
		try {
			this.cashedPages = this.pages.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Image> getCashedImages() {
		
		if(this.cashedImage.isEmpty()) {
			this.casheImage();
		}
		
		return this.cashedImage;
	}
	
	public void casheImage() {
		
		if(this.imagesCashed) return;
		
		this.imagesCashed = true;
		//System.out.println("Cashing chapter: " + this.name);
		
		if(this.cashedPages.isEmpty()) {
			this.cashe();
		}
		
		for(String img : this.cashedPages) {
			URL url;
			try {
				
				url = new URL(img);
				InputStream input = Encoder.openInputStream(url);
				Image image = new Image(input);
				if(image.getWidth() == 0) {
					url = new URL(img.replace("http", "https"));
					input = Encoder.openInputStream(url, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36.	");
					image = new Image(input);
				}
				this.cashedImage.add(image);
				//System.out.println("added image: " + img + " : " + this.cashedImage.size());
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void freeMemory() {
		//this.cashedImage.clear();
		//this.imagesCashed = true;
	}
	
	public List<String> getPages() {
		
		if(!this.cashedPages.isEmpty()){
			return this.cashedPages;
		}
		
		try {
			this.cashedPages = this.pages.call();
			return this.cashedPages;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public void download() throws Exception {
		
		List<String> pages = this.getPages();
		
		int page = 1;
		
		File parent = new File(MangaAPI.getDownloader().download, Encoder.encode(this.manga.getAlt()).replace("+", " "));
		File chapterPath = new File(parent, Encoder.encode(this.name).replace("+", " "));
		
		
		if(!chapterPath.exists()) {
			chapterPath.mkdirs();
		}
		
		for(String i : pages){
			
			String dir = chapterPath.getAbsolutePath() + "\\page"+page+".jpg";

			System.out.println("downloading from " + i + " to " + dir);
			
			File file = new File(dir);
			file.createNewFile();

			FileUtils.copyURLToFile(new URL(i), file, 10000, 10000);
			
			page++;
			
		}
	}

		
	}