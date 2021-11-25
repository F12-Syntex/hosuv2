package com.hosu.panes;

import com.syntex.manga.models.QueriedManga;

public class DownloadQuery {

	private QueriedManga manga;

	public DownloadQuery(QueriedManga manga) {
		super();
		this.manga = manga;
	}

	public QueriedManga getManga() {
		return manga;
	}

	public void setManga(QueriedManga manga) {
		this.manga = manga;
	}
	
}
