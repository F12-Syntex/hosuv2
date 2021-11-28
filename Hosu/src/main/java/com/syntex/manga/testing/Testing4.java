package com.syntex.manga.testing;

import java.util.List;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.models.Chapter;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.sources.MangaBuddy;
import com.syntex.manga.sources.Source;

public class Testing4 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		
		String[] query = {"loli"
				+ ""};
		
		System.out.println("Query test...");
		
		Class<?>[] sources = {
				MangaBuddy.class
		};
		
		for(String i : query) {
			for(Class<?> src : sources) {
				new Thread(() -> {
					System.out.println("Quering " + i + " with source " + src.getName() + ".class");
					try {
						
						RequestMangaData data = MangaAPI.search(i, (Class<? extends Source>) src).requestQueryResults().call().getMangas().get(0).getAsManga();

						List<Chapter> chapters = data.getChapters().subList(0, 1);
						
						MangaAPI.getDownloader().queue(i, chapters);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}).start();
			}
		}
		
		
	}

}
	