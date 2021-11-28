package com.hosu.panes;

import java.util.ArrayList;
import java.util.List;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.helpers.PanesHelper;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomePage extends Pane{

	public GridPane pane = new GridPane();
	public int globalCounter = 0;
	public int maxColumns = 1;
	
	public void download(DownloadQuery manga) {
		List<DownloadQuery> instance = new ArrayList<>();
		instance.add(manga);
		this.download(instance);
	}
		
	
	public void download(List<DownloadQuery> mangas) {
		
		StackPane EMPTY2 = new StackPane();
		EMPTY2.setMinHeight(0);
		EMPTY2.setMaxHeight(0);
		
		for(DownloadQuery i : mangas) {
			
			StackPane EMPTY = new StackPane();
			EMPTY.setMinHeight(0);
			EMPTY.setMaxHeight(0);
			
			pane.add(EMPTY, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns) + 1);
			
			pane.setVgap(5);
			
			StackPane EMPTY3 = new StackPane();
			EMPTY3.setMinHeight(20);
			EMPTY3.setMaxHeight(20);
			
			globalCounter++;
			
			//add anime to the pane
			double prefWidth = pane.getPrefWidth() - pane.getPrefWidth() / 12;
			double prefHeigth = pane.getPrefHeight() / 5.5;
			
			VBox nodeholder = new VBox();
			nodeholder.setId("download");
			nodeholder.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
			nodeholder.setMinSize(prefWidth, prefHeigth);
			nodeholder.setMaxSize(prefWidth, prefHeigth);
			
			StackPane node = new StackPane();
			node.setId("download");
			node.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
			node.setMinSize(prefWidth, prefHeigth);
			node.setMaxSize(prefWidth, prefHeigth);
			
			ProgressBar bar = new ProgressBar(0.1);
			bar.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
			bar.setMinSize(prefWidth / 1.2, prefHeigth/14);
			bar.setMaxSize(prefWidth / 1.2, prefHeigth/14);
			
			new Thread(() -> {
				for(;;) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					Platform.runLater(() -> {
						System.out.println("set: " + bar.getProgress() + 0.0001);
						bar.setProgress(bar.getProgress() + 0.0001);
					});
				}
			}).start();
			
			//A verticle pane to store nodes.
			VBox detailsBox = new VBox();
			detailsBox.setId("content3");
			detailsBox.setAlignment(Pos.TOP_CENTER);
			detailsBox.setSpacing(5);

			//holds the nodes to be displayed, the details box
			ScrollPane details = new ScrollPane(detailsBox);
			details.setFitToHeight(true);
			details.setFitToWidth(true);
			details.setMinSize(prefWidth, prefHeigth*3);
			details.setMaxSize(prefWidth, prefHeigth*3);
			
			
			//details return holds the scroll pane details
			details.setId("content3");
			StackPane detailsReturn = new StackPane(details);
			detailsReturn.setId("content3");
			this.attachCss(detailsReturn, Styling.SCROLL_PANE);

			//an element to be added to the verticle pane, details box.
			//PixelMatrix mat = new PixelMatrix();
			//detailsBox.getChildren().add(mat);
			
			HosuButton minimize = new HosuButton(com.hosu.css.Image.DROP_DOWN, com.hosu.css.Image.DOUBLE_DROP_DOWN, 32, null);
			
			minimize.setListener((e) -> {

				if(minimize.clicked) {
					minimize.setImage(com.hosu.css.Image.ARROW_UP);
					minimize.setOnHover(com.hosu.css.Image.DOUBLE_UP);	
					minimize.setHoverContent(com.hosu.css.Image.ARROW_UP);
					
					//drop down
					nodeholder.setMinSize(prefWidth, prefHeigth*4);
					nodeholder.setMaxSize(prefWidth, prefHeigth*4);
					
					nodeholder.getChildren().add(detailsReturn);

					
					
					
				}else {
					minimize.setImage(com.hosu.css.Image.DROP_DOWN);
					minimize.setOnHover(com.hosu.css.Image.DOUBLE_DROP_DOWN);	
					minimize.setNormalContent(com.hosu.css.Image.DROP_DOWN);
					
					//contract
					nodeholder.setMinSize(prefWidth, prefHeigth);
					nodeholder.setMaxSize(prefWidth, prefHeigth);

					nodeholder.getChildren().remove(detailsReturn);
					
					
				}

				minimize.refreshImages();
			
			});
			
			javafx.scene.layout.Pane cancel = new HosuButton(com.hosu.css.Image.DELETE, com.hosu.css.Image.DELETE, 24, (e) -> {
				
			}).get();
			
			HosuButton pause = new HosuButton(com.hosu.css.Image.PAUSE, com.hosu.css.Image.PLAY, 24, (e) -> {
				
			}).makeToggle(true);
			
			HosuButton stop = new HosuButton(com.hosu.css.Image.STOP, com.hosu.css.Image.STOP, 24, (e) -> {
				
			});
			
			HosuButton restart = new HosuButton(com.hosu.css.Image.RESTART, com.hosu.css.Image.RESTART, 24, (e) -> {
				
			});
			
			HosuButton close = new HosuButton(com.hosu.css.Image.EXIT, com.hosu.css.Image.EXIT, 24, (e) -> {
				pane.getChildren().remove(nodeholder);
			});
			
			HosuButton turbo = new HosuButton(com.hosu.css.Image.TURBO_ON, com.hosu.css.Image.TURBO_OFF, 24, (e) -> {
				 
			}).makeToggle(true);;

			javafx.scene.layout.Pane min = minimize.get();
			StackPane.setAlignment(min, Pos.BOTTOM_RIGHT);
			
			javafx.scene.layout.Pane exit = close.get();
			StackPane.setAlignment(exit, Pos.TOP_RIGHT);
			
			
			
			
			
			VBox container = new VBox();
			container.setMinSize(prefWidth / 1.1, prefHeigth/4);
			container.setMaxSize(prefWidth / 1.1, prefHeigth/4);
			//container.setStyle("-fx-background-color: white");
			container.setSpacing(20);
			
			HBox box = new HBox();
			box.setMinSize(prefWidth / 1.1, prefHeigth/14);
			box.setMaxSize(prefWidth / 1.1, prefHeigth/14);
		//	box.setStyle("-fx-background-color: red");
			
			box.getChildren().add(bar);
			box.getChildren().add(cancel);
			
			box.setAlignment(Pos.CENTER);
			
			container.setAlignment(Pos.CENTER_LEFT);
			
			Image img = HosuClient.getInstance().getImageHandler().getFromCashe(com.hosu.css.Image.PICTURES);
			ImageView view = new ImageView(img);
			
			Label label = new Label(i.getManga().getAlt(), view);
			label.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
			label.setTextFill(Color.web("#717fbc"));
			
			container.getChildren().add(label);
			//container.getChildren().add(EMPTY3);
			container.getChildren().add(box);
			
			HBox options = new HBox();
			options.getChildren().add(pause.get());
			options.getChildren().add(stop.get());
			options.getChildren().add(restart.get());
			options.getChildren().add(turbo.get());
			
			options.setMinWidth(node.getMinWidth() / 2);
			options.setMaxWidth(node.getMinWidth() / 2);
			options.setAlignment(Pos.CENTER_RIGHT);
			
			container.getChildren().add(options);
			
			node.getChildren().add(container);				
			node.getChildren().add(min);
			node.getChildren().add(exit);
			
			nodeholder.getChildren().add(node);
			
			
			//StackPane.setAlignment(container, Pos.TOP_CENTER);
			
			pane.add(nodeholder, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns));
			
			
		}
		
		pane.add(EMPTY2, (globalCounter % (int)maxColumns + 1),  (globalCounter / (int)maxColumns) + 1);
		
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

		PanesHelper.modifyScrollBar(scroallable, 20);
		
		return returnPane;
	}
	
	
}
