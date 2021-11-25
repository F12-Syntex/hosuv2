package com.syntex.manga.models;

@FunctionalInterface
public interface IDownloadable {
	public void download() throws Exception;
}
