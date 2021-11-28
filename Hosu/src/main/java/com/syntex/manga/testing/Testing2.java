package com.syntex.manga.testing;

import java.util.List;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.sources.MangaOwl;
import com.syntex.manga.sources.Source;

public class Testing2 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		
		String[] query = {"tokyo revengers"};
		
		System.out.println("Query test...");
		
		Class<?>[] sources = {
				MangaOwl.class
		};
		
		for(String i : query) {
			for(Class<?> src : sources) {
				new Thread(() -> {
					
					final long start = System.currentTimeMillis();
					System.out.println("Quering " + i + " with source " + src.getName() + ".class");
					try {
						
						List<QueriedEntity> data = MangaAPI.search(i, (Class<? extends Source>) src).requestQueryResults().call().getMangas();
						
						
							data.forEach(o -> {				
								new Thread(() -> {
									o.getAsManga().getChapters().forEach(j -> {
										try {
											j.download();
										} catch (Exception e) {
											e.printStackTrace();
										}
									});
								}).start();
							});
							
							int results = data.size();
							
							System.out.println("Found " + results + " in " + 
							Math.abs(start - System.currentTimeMillis()));
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}).start();
			}
		}
		
		
	}

}
	