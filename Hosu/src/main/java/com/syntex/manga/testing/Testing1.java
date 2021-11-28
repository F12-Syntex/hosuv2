package com.syntex.manga.testing;

import java.util.List;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.MangaOwl;

public class Testing1 {

	public static void main(String[] args) {
		
		/*
		try {
			QueriedManga queried = MangaAPI.search("attack on titan", MangaOwl.class).requestQueryResults().get().getMangas().get(0);
			System.out.println(queried.getAlt());
			System.out.println(queried.getUrl());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		
		try {
		
			String query = "attack on titan";
			
			System.out.println("Quering " + query + "...");
			
			RequestQueryResults results = MangaAPI.search(query, MangaOwl.class).requestQueryResults().call();
			
			for(QueriedEntity i : results.getMangas()) {
				
				System.out.println("Found manga -> " + i.getAlt() + ", getting chapters.");
				
				List<Chapter> data = i.getAsManga().getChapters();
				
				MangaAPI.getDownloader().queue(i.getAlt(), data);
				
				/*
				
				for(Chapter chapter : data) {
					
					System.out.println("Found chapter " + chapter.getName() + " downloading");
					
					List<String> pages = chapter.getPages().get();

					for(String page : pages) {
						
						System.out.println(page);
						
					}
					
					
				}
			
			*/
			
				
			}
			
			
			
			
		}catch (Throwable e) {
		
		}
		
		
	}
	
}
