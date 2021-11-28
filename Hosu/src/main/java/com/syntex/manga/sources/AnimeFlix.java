package com.syntex.manga.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.syntex.manga.models.Episode;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestAnimeData;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class AnimeFlix extends Source{

	private final String DOMAIN = "https://animeflix.ws/";
	
	public AnimeFlix(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		
		return () -> {
			String data = this.readURL("https://animeflix.ws/search.html?keyword=" + query.replace(" ", "-"));
			
			List<QueriedEntity> entities = new ArrayList<>();
			
			for(String i : data.split("<article class=\"bs relative animate-me\">")) {
				if(i.contains("<!DOCTYPE")) continue;
				String image = i.split("<img class=\"coveri coveris\" src=\"")[1].split("\"")[0];
				String alt = i.split("<img class=\"coveri coveris\" src=\"")[1].split("\"")[4];
				String url = DOMAIN + i.split("href=\"")[1].split("\"")[0];
				QueriedEntity entity = new QueriedEntity(image, alt, this, url);
				entities.add(entity);
			}
			
			RequestQueryResults results = new RequestQueryResults(this, this.query, entities);
			
			return results;
		};
	
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedEntity manga) {
		return null;
	}

	@Override
	public Callable<RequestAnimeData> requestAnimeData(QueriedEntity manga) {
		
		return () -> {
			String data = this.readURL(manga.getUrl());

			List<Episode> episodes = new ArrayList<>();
			
			for(String o : data.split("<li class=\"inline\">Episode List</li>")[1].split("<li class=\"epi-me\">")) {
				if(!o.contains("a href=\"")) continue;
				String episodeURL = DOMAIN + o.split("a href=\"")[1].split("\"")[0];
				String episodeTitle =  o.split("<div title=\"")[1].split("\"")[0];
				
				Episode episode = new Episode(episodeTitle, episodeURL, manga, () -> {
					String dat3 = this.readURL(episodeURL);
					String videoURL = "https://embed.animeflix.ws/video.php" + dat3.split("value=\"https://embed.animeflix.ws/video.php?")[1].split("\"")[0];
					return videoURL;
				});

				episodes.add(episode);
			}
			
			RequestAnimeData animeData = new RequestAnimeData(manga, new HashMap<>(), episodes);
			
			return animeData;	
		};
		
	}

	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SourceType sourceType() {
		// TODO Auto-generated method stub
		return SourceType.ANIME;
	}
	
}
