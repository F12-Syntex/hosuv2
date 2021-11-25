package com.syntex.manga.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.utils.Encoder;

public class Downloader {

		public final ExecutorService threadpool = Executors.newFixedThreadPool(15);
		public final File home;
		public final File download;
		
		public Downloader() {
			String path = System.getProperty("user.home");

			File programFiles = new File(path);
			File mangaFiles = new File(programFiles, "Manga");
			
			if(!mangaFiles.exists()) {
				boolean success = mangaFiles.mkdirs();
				System.out.println(success);
			}

			this.home = mangaFiles;
			
			File downloads = new File(this.home, "Downloads");
			if(!downloads.exists()) {
				downloads.mkdirs();
			}
			this.download = downloads;
		}
		
		public void queue(String name, List<Chapter> data) {
			
			this.threadpool.execute(() -> {
			
				QueriedManga manga = data.get(0).getManga();
				
				File downloads = new File(this.home, "Downloads");
				File file = new File(downloads, Encoder.encode(manga.getAlt()).replace("+", " "));
				
				if(!file.exists()) {
					file.mkdir();
				}
				
				JSONObject json = new JSONObject();
				
				for(Chapter i : data) {
					try {
						threadpool.execute(() -> {
							try {
								i.download();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
						System.out.println(i.getName() + " has finished downloading");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//write json serialised data to folder.
				File serialise = new File(file, "json");
				
				if(!serialise.exists()) {
					serialise.mkdir();
				}
				
				File serialisedFile = new File(serialise, "data.json");
				
				if(!serialisedFile.exists()) {
					try {
						serialise.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				serialise.mkdirs();
				
				JSONObject queriedData = new JSONObject();
				
				queriedData.put("alt", manga.getAlt());
				queriedData.put("url", manga.getUrl());
				queriedData.put("image", manga.getImage());
				
				json.put("data", queriedData);
				
				String text = json.toString();
				
				try {
					write(serialisedFile, text);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			});
			
		}
		
		/*
		 * @pram path being the location of the file do decode
		 * returns a RequestMangaData instance.
		 */
		public void deserialise() {
			
			
			
		}

		
		@SuppressWarnings("unused")
		private void write(File file, List<String> content) throws FileNotFoundException, UnsupportedEncodingException {
			PrintWriter writer = new PrintWriter(file);
			content.forEach(writer::println) ;
			writer.close();
		}
		
		private void write(File file, String content) throws FileNotFoundException, UnsupportedEncodingException {
			PrintWriter writer = new PrintWriter(file);
			writer.print(content);
			writer.close();
		}
		
	}
	