package com.syntex.manga.sources;

import java.util.concurrent.Callable;

import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;

public class NineComics extends Source{

	public NineComics(String query) {
		super(query);
	}

	@Override
	public Callable<RequestQueryResults> requestQueryResults() {
		

		
		return null;
	}

	@Override
	public Callable<RequestMangaData> requestMangaData(QueriedManga manga) {
		// TODO Auto-generated method stub 
		return null;
	}

	@Override
	public boolean nsfw() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		try {
			
			new NineComics("attack on").requestQueryResults();
			
			//long start = System.currentTimeMillis();
			//QueriedManga manga = new FreeComics("sex").requestQueryResults().call().getMangas().get(0);
			//manga.getAsManga();
			//RequestMangaData request = manga.getSource().requestMangaData(manga).call();
			//List<String> pages = request.getChapters().get(0).getPages();
			
			//System.out.println("took " + (System.currentTimeMillis()-start) + "(ms) to find " + pages.size() + " pages. printing.");
			//pages.forEach(System.out::println);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
