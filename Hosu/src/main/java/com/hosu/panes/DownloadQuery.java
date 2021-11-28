package com.hosu.panes;

import com.syntex.manga.models.QueriedEntity;

public class DownloadQuery {

	private QueriedEntity manga;

	public DownloadQuery(QueriedEntity manga) {
		super();
		this.manga = manga;
	}

	public QueriedEntity getManga() {
		return manga;
	}

	public void setManga(QueriedEntity manga) {
		this.manga = manga;
	}
	
}
