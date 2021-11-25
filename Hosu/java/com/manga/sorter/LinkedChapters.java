package com.manga.sorter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manga.data.Chapter;
import com.manga.data.ChapterHandler;
import com.manga.data.LinkedPage;

public class LinkedChapters {

	private int chapter;
	
	private ChapterHandler chapters;
	private LinkedPage page;
	
	private Map<Integer, LinkedPage> cashe = new HashMap<Integer, LinkedPage>();
	
	public LinkedChapters(Chapter current, ChapterHandler chapters) {
		this.chapters = chapters;
		this.chapter = current.getChapter();
		
		List<LinkedPage> cashed = current.getPagesIfAvailable();
		
		if(cashed != null && !cashed.isEmpty()) {
			this.page = cashed.get(0);
		}else {
			this.page = current.getPages().getPages().get(0);	
		}
		
		cashe.put(chapter, page);
		
	}

	public LinkedPage getCurrentPage() {
		return this.page;
	}
	
	public boolean nextPageRequireDownload() {	
		if(page.getNextPage() == null) {
			Chapter chap = chapters.getChapter((chapter+1));
			if(chap == null) {
				System.out.println("Reason for false: last chapter");
				return false;
			}
			//first check if the next chapter is cashed
			if(cashe.containsKey((chapter+1))) {
				System.out.println("Reason for false: (cashed)");
				return false;
			}
			
			//check if the pages are already downloaded
			if(chap.getPagesIfAvailable() != null && !chap.getPagesIfAvailable().isEmpty()) {
				System.out.println("Reason for false: (pages are already available)");
				return false;
			}
			
			return true;
		}
		
		System.out.println("Reason for false: (pages are already available)");
		return false;
	}
	public boolean prevPageRequireDownload() {	
		if(page.getPrevPage() == null) {
			Chapter chap = chapters.getChapter((chapter-1));
			if(chap == null) {
				return false;
			}
			//first check if the next chapter is cashed
			if(cashe.containsKey((chapter-1))) {
				return false;
			}
			
			//check if the pages are already downloaded
			if(chap.getPagesIfAvailable() != null && !chap.getPagesIfAvailable().isEmpty()) {
				return false;
			}
			
			return true;
		}
		return false;
	}

	public LinkedPage getNextPage() {	
		if(page.getNextPage() == null) {
			Chapter chap = chapters.getChapter((chapter+1));
			if(chap == null) {
				//last chapter
				return null;
			}
			
			
			//load next chapter
			chapter++;
			
			//first check if the next chapter is cashed
			if(cashe.containsKey(chapter)) {
				return cashe.get(chapter);
			}
			
			//check if the pages are already downloaded
			if(chap.getPagesIfAvailable() != null && !chap.getPagesIfAvailable().isEmpty()) {
				return chap.getPagesIfAvailable().get(0);
			}
			
			//chapter is not cashed, manually load
			List<LinkedPage> nextPages = chap.getPages().getPages();
			LinkedPage node = nextPages.get(0);
			
			page.setNextPage(node);
			node.setPrevPage(this.getCurrentPage());
			
			
			nextPages.get(0).setPrevPage(page);
			
			cashe.put(chapter, nextPages.get(0));
			System.out.println("Saved chapter: " + chapter);
			System.out.println("Cashed " + nextPages.size() + " pages for chapter " + nextPages.get(0).getChapter().getChapter());
			this.updateCashe();
			
		}
		//send the last chapter
		this.page = this.page.getNextPage();
		return this.page;
	}
	
	
	public LinkedPage getPrevPage() {
		if(page.getPrevPage() == null) {
			Chapter chap = chapters.getChapter((chapter-1));
			if(chap == null) {
				//first chapter
				return null;
			}
			
			//load prev chapter
			chapter--;
			
			//first check if the next chapter is cashed
			if(cashe.containsKey(chapter)) {
				return cashe.get(chapter);
			}
			
			//check if the pages are already downloaded
			if(chap.getPagesIfAvailable() != null && !chap.getPagesIfAvailable().isEmpty()) {
				return chap.getPagesIfAvailable().get(chap.getPagesIfAvailable().size()-1);
			}
			
			//chapter is not cashed, manually load
			List<LinkedPage> prevPages = chap.getPages().getPages();
			LinkedPage node = prevPages.get(prevPages.size()-1);
			
			page.setPrevPage(node);
			node.setNextPage(this.getCurrentPage());
			
			
			cashe.put(chapter, prevPages.get(prevPages.size()-1));
			
			System.out.println("Saved chapter: " + chapter);
			System.out.println("Cashed " + prevPages.size() + " pages for chapter " + prevPages.get(0).getChapter().getChapter());
			this.updateCashe();
				
			//send the last chapter
			this.page = this.page.getPrevPage();
			return this.page;
		}
		//send the last chapter
		this.page = this.page.getPrevPage();
		return this.page;
	}
	
	public void updateCashe() {
		int currentChapter = this.getCurrentPage().getChapter().getChapter();
		
		final Integer[] keys = this.cashe.keySet().toArray(new Integer[this.cashe.size()]);
		
		for(int key : keys) {
			if(key != currentChapter && key != (currentChapter-1) && key != (currentChapter+1)) {
				this.cashe.remove(key);
				this.getCurrentPage().destroyNode(key);
				System.out.println("Destroyed node: " + key + " c: " + currentChapter);
			}
		}
	
	
		
	}
	
	


}
