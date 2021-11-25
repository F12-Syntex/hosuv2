package com.hosu.panes;

import java.lang.reflect.Constructor;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.mangaplayer.Player;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestMangaData;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;
import com.syntex.manga.utils.NumberUtils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MangaContent extends SearchableContent{

	public String text;

	private String lastSearched = "";
	
	public boolean firstSearch = true;
	
	public MangaContent() {
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
		
		this.firstSearch = false;

		return returnPane;
	}

	public void onSearch(String name) {
		
		//set loading screen.
		pane.getChildren().clear();
		this.buffer();
		
		this.workerPool.execute(() -> {
			
			try {
			
				//get manga information
				Domain domain = Domain.valueOf(HosuClient.getInstance().getPaneHandler().getSearch()
											  .comboBox.getValue().toUpperCase().replace(" ", "_"));
				
				Constructor<?> constructor = domain.getSource().getDeclaredConstructor(String.class);
				Source invoke = (Source) constructor.newInstance(name);
				
				
				if(NumberUtils.isInt(name) && domain == Domain.NHENTAI) {
					
					//374291
					String url = "https://nhentai.to/g/" + name.trim();
					
					RequestMangaData request = invoke.requestMangaData(new QueriedManga("", name, invoke, url)).call();
					
					Platform.runLater(() -> {
						try {
							Player player = new Player(request);
							player.play(request.getChapters().get(0));
						}catch (Exception e) {
							e.printStackTrace();
						}
						return;
					});
				}
					
				
				RequestQueryResults request = invoke.requestCashedQueryResults().call();
				
				System.out.println("Found: " + request.getMangas().size() + " queries.");
				
				this.load(request, (e) -> {
					this.workerPool.execute(() -> {
						
						Platform.runLater(() -> {
							pane.getChildren().clear();
						});
						
						this.buffer();
						
						MangaInfoPane pane = HosuClient.getInstance().getPaneHandler().getMangaInfoPane();
						pane.setMangaData(e);
						
						HosuClient.getInstance().getPaneHandler().setActive(pane);
						
						Platform.runLater(() -> {
							MangaContent.this.pane.getChildren().clear();
						});
							

					});
					
				});
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		});
	}
}
