package com.hosu.mangaplayer;

import java.util.ArrayList;
import java.util.List;

import com.syntex.manga.models.Chapter;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;

public class Testing {

	public static void main(String[] args) {
		try {

			System.out.println("Starting.");

			
			List<Chapter> chapters = new ArrayList<>();
			
			List<String> pages = new ArrayList<>();
			
			pages.add("https://live.staticflickr.com/65535/50904014368_6e46b6dba4_o.jpg");
			pages.add("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg");
			
			Chapter chapter = new Chapter(null, "example", "example", () -> pages);
			chapters.add(chapter);
			
			List<String> pages2 = new ArrayList<>();
			
			pages2.add("https://live.staticflickr.com/65535/50904014368_6e46b6dba4_o.jpg");
			pages2.add("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg");
			
			Chapter chapter2 = new Chapter(null, "example1", "example1", () -> pages);
			chapters.add(chapter2);
			
			QueriedManga queried = new QueriedManga("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg", "asd", null, "");
			
			RequestMangaData manga = new RequestMangaData(queried, null, chapters);
			
			
			Player player = new Player(manga);
			
			player.play(manga.getChapters().get(0));
			
			/*
			
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();
			player.front();

			player.back();
			player.back();
			player.back();
			player.back();
			player.back();
			player.back();
			player.back();
			
			*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
