package com.hosu.windows;

import com.hosu.settings.Settings;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class MediaViewer {
	
	public static void show(Media image) {
		
		 StackPane root  = new StackPane();
		
		 MediaPlayer view = new MediaPlayer(image);
		
		 MediaView mediaView = new MediaView(view);
		
		root.getChildren().add(mediaView);
		
		Stage stage = new Stage();
		stage.setTitle(image.getSource());
		
		double width = image.getWidth();
		double height = image.getHeight();
		
		view.setOnStalled(() -> {
			System.out.println("buffering");
		});
		
		if(image.getWidth() > Settings.SIZE.getWidth()) {
			mediaView.setFitWidth(Settings.SIZE.getWidth());
			width = Settings.SIZE.getWidth();
		}
		if(image.getHeight() > Settings.SIZE.getHeight()) {
			mediaView.setFitHeight(Settings.SIZE.getHeight());
			height = Settings.SIZE.getHeight();
		}
		
		root.widthProperty().addListener((a,b,c) -> {
		    if(image.getWidth() > root.getWidth()) {
		    	mediaView.setFitWidth(root.getWidth());
		    }
		    if(image.getHeight() > root.getHeight()) {
		    	mediaView.setFitHeight(root.getHeight());
		    }
		});
		
		root.heightProperty().addListener((a,b,c) -> {
		    if(image.getWidth() > root.getWidth()) {
		    	mediaView.setFitWidth(root.getWidth());
		    }
		    if(image.getHeight() > root.getHeight()) {
		    	mediaView.setFitHeight(root.getHeight());
		    }
		});
		
		root.setStyle("-fx-background-color: #0d101c");
		
		stage.setScene(new Scene(root, width, height));
		stage.show();
		
	}
	
}

