package com.hosu.panes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.hosu.css.Styling;
import com.hosu.datatypes.SearchableData;
import com.hosu.helpers.DownloadHelper;
import com.hosu.helpers.Reddit;
import com.hosu.settings.onImageClick;
import com.hosu.windows.ImageViewer;
import com.hosu1.application.HosuClient;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class RedditContent extends SearchableContent{

	public String text;

	public RedditContent() {
		this.pane = new GridPane();
		this.maxRows = 3;
		this.maxColumns = 3;
	}

	//String url = "https://www.freepnglogos.com/uploads/anime-face-png/politically-incorrect-u00bb-thread-60766375-33.png";

	
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

		//this.applySizeToNode(pane, width, height);
		//this.applySizeToNode(returnPane, width, height);
		
		return returnPane;
	}

	public void onSearch(String name) {
		new Thread(() -> {
			Platform.runLater(() -> {
				this.save(name);
				pane.getChildren().clear();
			});
			
			//this.loadingScreen();
			this.buffer();
			
			try {
			
				List<String> urls = Reddit.getRandomImagesAll(name, (int) (this.maxRows * this.maxColumns) * 2);
				
				Platform.runLater(() -> {
					pane.getChildren().clear();
				});
				this.globalCounter = 0;
				
				
				if(!urls.isEmpty()) {
					
					List<SearchableData> data = new ArrayList<>();
					
					for(String i : urls) {
						data.add(new SearchableData(i, new onImageClick() {	
							@Override
							public void onClick(Image image) {
								ImageViewer.show(image);
							}
						}));
					}
					
					this.load(data);
					
				}
				
			}catch (Exception e) {
				Platform.runLater(() -> {
					pane.getChildren().clear();
				});
			}
			
			
		}).start();
	}
	
	public void save(String name) {
		//save to file
		new Thread(() -> {
			
			List<String> temp = Reddit.getRandomImagesAll(name, 99999);
			for(String o : temp) {
				try {
					//ThreadLocalRandom.current().nextInt(100000) + ".png"
					String filter = o.replace(":", "").replace("/", "");
					if(filter.contains("?")) {
						filter = ThreadLocalRandom.current().nextInt(100000) + ".gif";
					}
					
					System.out.println(filter);		
					DownloadHelper.DownloadImage(o, "C:/Users/synte/Desktop/Nagare Testing/imgs/" + name  + "/" + filter);
				} catch (IOException e) {
					continue;
				}
			}
			
			
		}).start();
	}
	
	
	
}
