package com.hosu.panes;

import java.util.ArrayList;
import java.util.List;

import com.hasu1.manga.cashe.MangaCashe;
import com.hosu.css.Styling;
import com.hosu.datatypes.SearchableData;
import com.hosu1.application.HosuClient;
import com.manga.data.SearchData;
import com.manga.scrapper.Manga;
import com.manga.sources.Sources;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MangaContent extends SearchableContent{

	public String text;

	public MangaCashe cashe;
	
	private String lastSearched = "";
	
	public boolean firstSearch = true;
	
	public MangaContent() {
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
			
			List<SearchData> manga = null;
			
			boolean crashed = false;
			
			if(this.cashe.isSearchQueryCashed(name)) {
				manga = this.cashe.getSearch(name).getResults();
			}else {
				
				try {
				
					manga = new Manga(Sources.MANGA_STREAM).query(name).limit(30).execute().getResults();
				
				}catch(Exception o) {
					
					Platform.runLater(() -> {

						ImageView view = new ImageView(new Image("https://logicwebmedia.s3.amazonaws.com/wp-content/uploads/20171219213524/isp-blocked-blogpost.jpg"));
						
						pane.add(view, 0, 0);
						
						return;
						
					});

					crashed = true;
					System.out.println("Loaded");
					
				}
			}
				
				if(crashed) {
					return;
				}
			
			this.lastSearched = name;
			
			Platform.runLater(() -> {
				pane.getChildren().clear();
			});
			
			if(!manga.isEmpty()) {
				List<SearchableData> data = new ArrayList<>();
				for(SearchData i : manga) {
					SearchableData searchableData = new SearchableData(i.getImg(), (e) -> {
						new Thread(() -> {
							
							Platform.runLater(() -> {
								pane.getChildren().clear();
							});
							
							this.buffer();
							
							MangaInfoPane pane = HosuClient.getInstance().getPaneHandler().getMangaInfoPane();
							pane.setMangaData(i.getData().get(), i.getImg());
							
							HosuClient.getInstance().getPaneHandler().setActive(pane);
							
							Platform.runLater(() -> {
								MangaContent.this.pane.getChildren().clear();
							});
								

						}, "dataSearchThread").start();
					});
					data.add(searchableData);
				}
				
				this.load(data);
			}
			
	}, "searchThread").start();
		
	}
	
}
