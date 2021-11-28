package com.hosu.panes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.controlsfx.control.PopOver;

import com.hosu.application.HosuClient;
import com.hosu.css.Image;
import com.hosu.datatypes.SearchableData;
import com.hosu.settings.LinkGrabber;
import com.hosu.settings.onImageClick;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestQueryResults;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.model.manga.Manga;
import net.sandrohc.jikan.model.manga.MangaSearchSub;

public abstract class SearchableContent extends Pane{
	
	protected GridPane pane;
	
	protected int globalCounter = 0;
	
	protected double maxColumns = 3;
	protected double maxRows = 3;

	protected final ExecutorService workerPool = Executors.newFixedThreadPool(1);
	
	public SearchableContent() {
		this.pane = new GridPane();
	}
	
	public abstract void onSearch(String text);
	
	public void buffer() {

		double width = HosuClient.getInstance().getBody().getPrefWidth();
		double height = HosuClient.getInstance().getBody().getPrefHeight();
		
		ImageView view = new ImageView(HosuClient.getInstance().getImageHandler().loading);
		view.setCache(true);
		
		if(width != -1 && height != -1) {
			view.setFitWidth(width);
			view.setFitHeight(height);
			view.setPreserveRatio(false);
		}
		
		StackPane loading = new StackPane(view);
		view.setId("content");
		
		Platform.runLater(() -> {
			this.pane.getChildren().add(loading);	
		});
		
	}
	

	protected void load(List<SearchableData> content) {
		
		Platform.runLater(() -> {
			this.globalCounter = 0;
			this.pane.getChildren().clear();
		});
		
		for(int i = 0; i < content.size(); i++) {
		
			final int copy = i;
			final SearchableData searchableContent = content.get(copy);
			
			
		double prefWidth = HosuClient.getInstance().getBody().getPrefWidth() / maxColumns;
		double prefHeight = HosuClient.getInstance().getBody().getPrefHeight() / maxRows;
		
		
		DataPane dat = new DataPane(new LinkGrabber() {
			@Override
			public String getURL() {
				return searchableContent.getLoadedURL();
			}
		}, prefWidth, prefHeight, searchableContent.getOnClick(), null);
		
		javafx.scene.layout.Pane toShow = dat.dontPreserveRatio().get();
		
		Platform.runLater(() -> {
			toShow.setMaxSize(prefWidth, prefHeight);
			toShow.setMinSize(prefWidth, prefHeight);
			pane.add(toShow, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns));
			globalCounter++;
		});
			
		}
	}
	
	public void load(RequestQueryResults data, onImageClick onClick) {
		
		List<QueriedEntity> content = data.getMangas();
		
		Platform.runLater(() -> {
			this.globalCounter = 0;
			this.pane.getChildren().clear();
		});
		
		content.stream().forEach(i -> {
			this.workerPool.execute(() -> {
				SearchableData search = new SearchableData(i.getImage(), onClick);
				this.update(search, i);
			});
		});
		
		this.workerPool.execute(() -> {
		
			StackPane EMPTY = new StackPane();
			EMPTY.setMinHeight(0);
			EMPTY.setMaxHeight(0);
			
			Platform.runLater(() -> pane.add(EMPTY, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns) + 1));
		
		});

	}
	
	private void update(SearchableData searchableContent, QueriedEntity i) {
		
		double prefWidth = HosuClient.getInstance().getBody().getPrefWidth() / maxColumns;
		double prefHeight = HosuClient.getInstance().getBody().getPrefHeight() / maxRows;
		
		DataPane dat = new DataPane(new LinkGrabber() {
			@Override
			public String getURL() {
				return searchableContent.getLoadedURL();
			}
		}, prefWidth, prefHeight, searchableContent.getOnClick(), i);
		
		javafx.scene.layout.Pane toShow = dat.dontPreserveRatio().showText(i.getAlt()).get();
		
		HBox options = new HBox();
		options.setAlignment(Pos.CENTER);
		options.setSpacing(0);
		options.setId("options");
		
		options.setMinSize(prefWidth, prefHeight/8);
		options.setMaxSize(prefWidth, prefHeight/8);
		
		toShow.setId("img");
	
		
		options.getChildren().add(new HosuButton(Image.DOWNLOAD2, Image.DOWNLOAD_COMPLETE, 24, (e) -> {
			DownloadQuery download = new DownloadQuery(i);
			HosuClient.getInstance().getPaneHandler().getHome().download(download);
		}).makeToggle(true).get());
		
		options.getChildren().add(new HosuButton(Image.SEARCH_BROWSER, Image.SEARCH_BROWSER, 24, (e) -> {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(i.getUrl()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}).get());
		
		if(HosuClient.database.liked.contains(i.getAlt())) {
			options.getChildren().add(new HosuButton(Image.HEART_ON, Image.HEART_OFF, 24, (e) -> {	
				if(HosuClient.database.liked.contains(i.getAlt())) {
					HosuClient.database.removeLiked(i.getAlt());
				}else {
					HosuClient.database.addLiked(i.getAlt());
				}
			}).makeToggle(true).get());	
		}else {
			options.getChildren().add(new HosuButton(Image.HEART_OFF, Image.HEART_ON, 24, (e) -> {	
				if(HosuClient.database.liked.contains(i.getAlt())) {
					HosuClient.database.removeLiked(i.getAlt());
				}else {
					HosuClient.database.addLiked(i.getAlt());
				}
			}).makeToggle(true).get());
		}
		
		
		javafx.scene.layout.Pane icons = new HosuButton(Image.SEARCH_OFF, Image.SEARCH_ON, 24, (e) -> {

		}).makeToggle(true).get();
		
        PopOver popOver = new PopOver();
        popOver.setAutoHide(false);
        popOver.setHideOnEscape(false);
        popOver.setAutoHide(true);
        popOver.setDetachable(true);
        
        popOver.setAnimated(true);
        popOver.setId("popover");

		icons.setOnMousePressed((e) -> {
			if(!popOver.isShowing()) {
				
				StackPane pane = new StackPane();
				ImageView view = new ImageView(HosuClient.getInstance().getImageHandler().loading);
				view.setFitWidth(200);
				view.setFitHeight(200);
				pane.getChildren().add(view);
				
				pane.setStyle("-fx-background-color: #181c2e;");
				view.setStyle("-fx-background-color: #181c2e;");
				
				
				popOver.setContentNode(pane);
				popOver.show(icons);
				
				new Thread(() -> {

					Jikan jikan = new Jikan(); 
					
					try {
						
						List<MangaSearchSub> results = (List<MangaSearchSub>) jikan
								.query()
								.manga()
								.search()
						        .query(i.getAlt())
						        .execute()
						        .collectList()
						        .timeout(Duration.ofSeconds(10))
						        .block();
						
						int malID = results.get(0).malId;
						
						Manga data = jikan.query().manga().get(malID).execute().block();
						
						String url = data.url;
						
						System.out.println(url);
						
						Platform.runLater(() -> {
							WebView viewer = new WebView();
							WebEngine webEngine = viewer.getEngine();
							webEngine.load(url);
							
							/*
							ScrollPane scroallable = new ScrollPane(viewer);
							scroallable.setFitToHeight(true);
							scroallable.setFitToWidth(true);
							scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
							scroallable.setStyle("-fx-background-color: #0d101c");
							scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));
							*/
							
							popOver.setContentNode(viewer);
							
						});

						
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
				
				}).start();
			}else {
				popOver.hide();
			}
		});
		
		icons.setId("popover");
		
		options.getChildren().add(icons);
		
		VBox selection = new VBox();
		selection.getChildren().add(toShow);
		selection.getChildren().add(options);
		
		selection.setAlignment(Pos.CENTER);
		
		
		Platform.runLater(() -> {
			selection.setMaxSize(prefWidth, prefHeight);
			selection.setMinSize(prefWidth, prefHeight);
			
			pane.setHgap(20); //horizontal gap in pixels => that's what you are asking for
			pane.setVgap(options.getMinHeight() + 20); //vertical gap in pixels
			
			pane.add(selection, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns));
			globalCounter++;
		});
	}
	
}
