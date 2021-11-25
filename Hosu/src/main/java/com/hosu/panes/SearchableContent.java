package com.hosu.panes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hosu.application.HosuClient;
import com.hosu.css.Image;
import com.hosu.datatypes.SearchableData;
import com.hosu.settings.LinkGrabber;
import com.hosu.settings.onImageClick;
import com.syntex.manga.cashe.CasheHelper;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.queries.RequestQueryResults;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
		
		List<QueriedManga> content = data.getMangas();
		
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
	
	private void update(SearchableData searchableContent, QueriedManga i) {
		
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
		
		options.getChildren().add(new HosuButton(Image.DOWNLOAD2, Image.DOWNLOAD_COMPLETE, 32, (e) -> {
			DownloadQuery download = new DownloadQuery(i);
			HosuClient.getInstance().getPaneHandler().getHome().download(download);
		}).makeToggle(true).get());
		
		options.getChildren().add(new HosuButton(Image.NEW_WINDOW, Image.OPEN_BROWSER, 32, (e) -> {
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
		
		options.getChildren().add(new HosuButton(Image.HEART_OFF, Image.HEART_ON, 32, (e) -> {
			
		}).makeToggle(true).get());
		
		options.getChildren().add(new HosuButton(Image.EDIT, Image.EDIT, 32, (e) -> {
			File file = CasheHelper.getLatestQueriedFile(i.getSource());
			try {
				Desktop desktop = Desktop.getDesktop();
				desktop.open(file);
				//Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}).get());
		
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
