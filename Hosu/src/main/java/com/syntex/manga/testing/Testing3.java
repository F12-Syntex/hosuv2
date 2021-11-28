package com.syntex.manga.testing;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.sources.HentaiRead;
import com.syntex.manga.sources.Source;

public class Testing3 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		
		String[] query = {"attack", "tokyo", "redo", "one piece", "bleach", "naruto", "sex", "hentai", "highscool sex"};
		
		System.out.println("Query test...");
		
		Class<?>[] sources = {
				HentaiRead.class
		};
		
		for(String i : query) {
			new Thread(() -> {
				
				final long start = System.currentTimeMillis();
				Class<?> src = sources[ThreadLocalRandom.current().nextInt(sources.length)];
				System.out.println("Quering " + i + " with source " + src.getName() + ".class");
				try {
					List<QueriedEntity> data = MangaAPI.search(i, (Class<? extends Source>) src).requestQueryResults().call().getMangas();
					
					data.forEach(o -> {
						o.getAsManga().getChapters().forEach(j -> {
							try {
								j.download();
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
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
	