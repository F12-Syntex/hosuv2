package com.syntex.manga.testing;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.sources.Domain;

public class QueryTest {
	
	public static void main(String[] args) {
		
		Domain source = Domain.MANGA_NATO;
		String query = "death";
		
		try {
			MangaAPI.search(query, source).requestQueryResults().call().getMangas().forEach(i -> {
				System.out.println("Found " + i.getAlt());
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
