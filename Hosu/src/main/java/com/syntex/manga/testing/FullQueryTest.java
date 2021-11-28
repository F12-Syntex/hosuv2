package com.syntex.manga.testing;

import java.util.List;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.MangaOwl;

public class FullQueryTest {

	public static void main(String[] args) {
		try {
			
			RequestQueryResults page = new MangaOwl("solo").requestQueryResults().call();
			
			System.out.println("Found " + page.getMangas().size() + " mangas, getting details for " + page.getMangas().get(0).getAlt());
			
			RequestMangaData manga = page.getMangas().get(0).getAsManga();
			
			System.out.println("Found " + manga.getChapters().size() + " chapters, getting pages for " + manga.getChapters().get(0).getName());
			
			List<String> pages = manga.getChapters().get(0).getPages();
			
			System.out.println("Found " + pages.size() + " pages.");
			
			for(String i : pages) {
				System.out.println(i);
			}
			
			try {
				MangaAPI.getDownloader().queue("solo leveling", manga.getChapters());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MangaAPI.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
