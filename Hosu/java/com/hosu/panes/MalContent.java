package com.hosu.panes;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.hasu1.manga.cashe.MangaCashe;
import com.hosu.css.Styling;
import com.hosu.datatypes.SearchableData;
import com.hosu1.application.HosuClient;
import com.manga.data.SearchData;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import okhttp3.OkHttpClient;

public class MalContent extends SearchableContent{

	public String text;

	public MangaCashe cashe;
	
	private String lastSearched = "";
	
	public boolean firstSearch = true;
	
	public MalContent() {
		this.pane = new GridPane();
		this.maxColumns = 4.6;
		this.maxRows = 2.5;
		this.cashe = new MangaCashe();
	}
	
	@Override
	public javafx.scene.layout.Pane get() {
		
		pane.setPrefHeight(HosuClient.getInstance().getBody().getPrefHeight() - (HosuClient.getInstance().getBody().getPrefHeight() / 15));
		pane.setPrefWidth(HosuClient.getInstance().getBody().getPrefWidth());
		pane.setAlignment(Pos.CENTER);

		ScrollPane scroallable = new ScrollPane(pane);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
		
		pane.setId("contentPane");
		scroallable.setId("contentPane");
		
		StackPane returnPane = new StackPane(scroallable);
		
		this.attachCss(returnPane, Styling.SCROLL_PANE);

		
		if(!firstSearch) {
			this.onSearch(lastSearched);
		}

		
		return returnPane;
	}

	public void onSearch(String name) {
		new Thread(() -> {
			Platform.runLater(() -> {
				pane.getChildren().clear();
			});
			
			this.firstSearch = false;
			
			List<SearchData> dat = new ArrayList<SearchData>();
			dat.add(new SearchData("", "", "", null));
			
			this.buffer();
			
			/*
			
			Jaikan4.setConfiguration(builder -> builder
			        .setOkHTTPClient(new OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(5)).build())
			        .setUserAgent("Jaikan 4 (by Mihou)")
			        .setRatelimit(Duration.ofSeconds(2))
			        .setRequestCache(caffeine -> caffeine.expireAfterWrite(Duration.ofHours(6)))
			        .build());
			
			Stream<AnimeResult> search = Jaikan.search(Endpoints.SEARCH, AnimeResult.class, "anime", name).stream();
			
			List<SearchableData> data = new ArrayList<>();
			
			this.lastSearched = name;
			
			Platform.runLater(() -> {
				pane.getChildren().clear();
			});

			
			this.load(data);			
			
			*/
			
	}, "searchThread").start();
		
	}
	
}
