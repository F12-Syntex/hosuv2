package com.manga.scrape.pages;

import java.util.ArrayList;
import java.util.List;

import com.manga.data.Chapter;
import com.manga.data.LinkedPage;
import com.manga.data.Page;

public class MangaStreamPageScrape extends PageScrape{

	private Chapter chapter;
	
	public MangaStreamPageScrape(String url, Chapter chapter) {
		super(url);
		this.chapter = chapter;
	}

	@Override
	public List<LinkedPage> getPages() {
		String dat = this.getHtml();

		String filtered = dat.split("div class=\"each-page")[1].split("</div>")[2];
		
		List<LinkedPage> pages = new ArrayList<LinkedPage>();
		
		for(String i : filtered.split("\\r?\\n")) {
			if(i.contains("https")) {
				
				i = i.replace("<p id=arraydata style=display:none>", "").replace("</p>", "");
				
				String[] filter = i.split(",");
				
				for(int o = 0; o < filter.length; o++) {
				
					int pageNumber = (o+1);
					
					Page page = new Page(filter[o].trim(), pageNumber);
					
					LinkedPage prev = null;
					LinkedPage next = null;
					
					LinkedPage linkedPage = new LinkedPage(chapter, page, prev, next);
					
					if(o != 0) {
						//Page prevPage = new Page(filter[(o-1)].trim(), (pageNumber-1));
						prev = pages.get(o-1);
						prev.setNextPage(linkedPage);
						linkedPage.setPrevPage(prev);
					}
					
					pages.add(linkedPage);
				}
				
			}
		}
		
		return pages;
	}
}
