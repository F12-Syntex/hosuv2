package com.hosu.panes;

import java.util.List;

import com.hosu.datatypes.SearchableData;
import com.hosu.settings.LinkGrabber;
import com.hosu1.application.HosuClient;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public abstract class SearchableContent extends Pane{
	
	protected GridPane pane;
	
	protected int globalCounter = 0;
	
	protected double maxColumns = 3;
	protected double maxRows = 3;
	
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
	
	
	
	public void load(List<SearchableData> content) {
		
		Platform.runLater(() -> {
			this.globalCounter = 0;
			this.pane.getChildren().clear();
		});
		
		double prefWidth = HosuClient.getInstance().getBody().getPrefWidth() / maxColumns;
		double prefHeight = HosuClient.getInstance().getBody().getPrefHeight() / maxRows;
		
		for(int i = 0; i < content.size(); i++) {
			
			final int copy = i;
			final SearchableData searchableContent = content.get(copy);
		
			DataPane dat = new DataPane(new LinkGrabber() {
				@Override
				public String getURL() {
					return searchableContent.getLoadedURL();
				}
			}, prefWidth, prefHeight, searchableContent.getOnClick());
			
			javafx.scene.layout.Pane toShow = dat.dontPreserveRatio().get();
			
			Platform.runLater(() -> {
				toShow.setMaxSize(prefWidth, prefHeight);
				toShow.setMinSize(prefWidth, prefHeight);
				pane.add(toShow, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns));
				globalCounter++;
			});
				
			
		}
		
	}
	
}
