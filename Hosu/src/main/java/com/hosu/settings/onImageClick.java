package com.hosu.settings;

import com.syntex.manga.models.QueriedEntity;

@FunctionalInterface
public interface onImageClick {
	public void onClick(QueriedEntity img);
}
