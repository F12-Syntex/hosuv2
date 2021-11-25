package com.hosu.panes;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.datatypes.SearchableData;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanInvalidArgumentException;
import net.sandrohc.jikan.model.anime.AnimeSearchSub;

public class MalContent extends SearchableContent{

	public String text;

	public String lastSearched = "";
	
	public boolean firstSearch = true;
	
	public MalContent() {
		this.pane = new GridPane();
		this.maxColumns = 4.6;
		this.maxRows = 2.5;
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
			
			this.buffer();
			
			Jikan jikan = new Jikan(); 
		
			List<SearchableData> data = new ArrayList<>();
			
			
			try {
				
				
				Collection<AnimeSearchSub> results = jikan
						.query()
						.anime()
						.search()
				        .query(name)
				        .execute()
				        .collectList()
				        .block();
			
				
				System.out.println("Found: " + results.size() + " anime");
				
				results.stream().forEach((a) -> {
					SearchableData searchableData = new SearchableData(a.imageUrl, (e) -> {
						new Thread(() -> {
							
							Platform.runLater(() -> {
								pane.getChildren().clear();
							});
							
							this.buffer();
							
							AniContentPane pane = HosuClient.getInstance().getPaneHandler().getAniContentPane();
							pane.setMangaData(jikan.query().manga().get(a.malId).execute().block());
							
							HosuClient.getInstance().getPaneHandler().setActive(pane);
							
							Platform.runLater(() -> {
								MalContent.this.pane.getChildren().clear();
							});
								

						}, "dataSearchThread").start();
					});
					data.add(searchableData);
				});
				
				
				
			} catch (JikanInvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			this.lastSearched = name;
			
			Platform.runLater(() -> {
				pane.getChildren().clear();
			});

			
			this.load(data);			
			
			
			
	}, "searchThread").start();
		
	}
	
}
