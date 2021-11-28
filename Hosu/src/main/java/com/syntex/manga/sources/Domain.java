package com.syntex.manga.sources;

import java.lang.reflect.Constructor;

public enum Domain {

	DOUJIN(Doujins.class), FREE_COMICS(FreeComics.class),
	HENTAI_READ(HentaiRead.class), MANGA_BUDDY(MangaBuddy.class),
	MANGA_DEX(MangaDex.class), MANGA_NATO(Manganato.class), MANGA_OWL(MangaOwl.class),
	MANGA_STREAM(MangaStream.class), NHENTAI(Nhentai.class), ANIME_FLIX(AnimeFlix.class);

	public final String CLASS_PATH;
	
	Domain(Class<? extends Source> clazz) {
		this.CLASS_PATH = clazz.getName();
	}
	
	public Class<?> getSource() {
		try {
			return Class.forName(CLASS_PATH);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public static Domain fromClass(Class<? extends Source> clazz) {
		for(Domain i : Domain.values()) {
			if(i.CLASS_PATH.equals(clazz.getName())) return i;
		}
		return null;
	}
	public static Source fromDomain(String clazz, String query) {
		try {
			return fromDomain(Class.forName(clazz), query);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Source fromDomain(Class<?> clazz, String query) {
		Constructor<?> constructor;
		try {
			constructor = clazz.getDeclaredConstructor(String.class);
			Source invoke = (Source) constructor.newInstance(query);
			return invoke;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
