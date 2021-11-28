package com.syntex.manga.testing;

import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.download.Downloader;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.sources.MangaStream;

public class Test {

	public static void main(String[] args) {

		Downloader download = MangaAPI.getDownloader();
		
		try {
			
			RequestMangaData o = MangaAPI.search("the last human", MangaStream.class).requestQueryResults().call().getMangas().get(0).getAsManga();
			
			download.queue("The last human", o.getChapters());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}

}
