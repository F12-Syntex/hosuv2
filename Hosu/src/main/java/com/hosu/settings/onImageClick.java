package com.hosu.settings;

import com.syntex.manga.models.QueriedManga;

@FunctionalInterface
public interface onImageClick {
	public void onClick(QueriedManga img);
}
