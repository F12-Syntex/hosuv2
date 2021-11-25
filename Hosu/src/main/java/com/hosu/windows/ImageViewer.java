package com.hosu.windows;

import com.hosu.settings.Settings;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageViewer {
	
	public static void show(Image image) {
		
		StackPane root  = new StackPane();
		
		ImageView view = new ImageView(image);
		
		view.setPreserveRatio(true);
		
		root.getChildren().add(view);
		Stage stage = new Stage();
		
		double width = image.getWidth();
		double height = image.getHeight();
		
		if(image.getWidth() > Settings.SIZE.getWidth()) {
			view.setFitWidth(Settings.SIZE.getWidth());
			width = Settings.SIZE.getWidth();
		}
		if(image.getHeight() > Settings.SIZE.getHeight()) {
			view.setFitHeight(Settings.SIZE.getHeight());
			height = Settings.SIZE.getHeight();
		}
		
		root.widthProperty().addListener((a,b,c) -> {
		    if(image.getWidth() > root.getWidth()) {
		    	view.setFitWidth(root.getWidth());
		    }
		    if(image.getHeight() > root.getHeight()) {
		    	view.setFitHeight(root.getHeight());
		    }
		});
		
		root.heightProperty().addListener((a,b,c) -> {
		    if(image.getWidth() > root.getWidth()) {
		    	view.setFitWidth(root.getWidth());
		    }
		    if(image.getHeight() > root.getHeight()) {
		    	view.setFitHeight(root.getHeight());
		    }
		});
		
		root.setStyle("-fx-background-color: #0d101c");
		
		stage.setScene(new Scene(root, width, height));
		stage.show();
		
	}
	
}

