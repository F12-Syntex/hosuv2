package com.syntex.manga.queries;

import java.util.List;
import java.util.Map;

import com.syntex.manga.models.Episode;
import com.syntex.manga.models.QueriedEntity;

public class RequestAnimeData {
	
	private QueriedEntity anime;
	private Map<String, String> attributes;
	private List<Episode> episodes;

	public RequestAnimeData(QueriedEntity anime, Map<String, String> attributes, List<Episode> episodes) {
		this.anime = anime;
		this.attributes = attributes;
		this.setEpisodes(episodes);
	}

	public QueriedEntity getAnime() {
		return anime;
	}

	public void setAnime(QueriedEntity anime) {
		this.anime = anime;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	
}
