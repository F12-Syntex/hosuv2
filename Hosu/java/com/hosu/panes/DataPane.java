package com.hosu.panes;

import com.hosu.css.Styling;
import com.hosu.settings.LinkGrabber;
import com.hosu.settings.onImageClick;
import com.hosu1.application.HosuClient;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class DataPane extends Pane{

	private LinkGrabber URL;
	
	private double width;
	private double height;
	private onImageClick click;
	private boolean preserveRatio = true;
	private String showText = "";
	
	private static Font font = new Font(16);
	
	public DataPane(LinkGrabber URL, double prefWidth, double prefHeight, onImageClick e) {
		this.URL = URL;
		this.width = prefWidth;
		this.height = prefHeight;
		this.click = e;
	}
	
	public DataPane dontPreserveRatio() {
		this.preserveRatio = false;
		return this;
	}
	
	public DataPane showText(String text) {
		this.showText = text;
		return this;
	}
	
	@Override
	public javafx.scene.layout.Pane get() {
		
		ImageView view = new ImageView(HosuClient.getInstance().getImageHandler().loading);
		
		view.setCache(true);
		
		if(width != -1 && height != -1) {
			view.setFitWidth(width);
			view.setFitHeight(height);
			view.setPreserveRatio(this.preserveRatio);
		}
		
		StackPane shadowPane = new StackPane();
		shadowPane.setId("pane");
		
		StackPane pane = new StackPane(view);
		view.setId("content");
		
		StackPane textPane = new StackPane();
		
		//textPane.setPrefWidth(pane.getWidth());
		textPane.setAlignment(Pos.CENTER);
		textPane.setId("shadow");
		
		pane.setAlignment(Pos.TOP_CENTER);
		StackPane.setMargin(textPane, new Insets(20, 0, 0, 0));
		
		Label label = new Label(this.showText);
		label.setFont(font);
		label.setTextFill(Color.WHITE);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		textPane.getChildren().add(label);
		
		textPane.setMaxSize(width, (32 * (1 + (this.showText.length() / 30))));
		textPane.setMinSize(width, (32 * (1 + (this.showText.length() / 30))));
		
		new Thread(() -> {
		//Platform.runLater(() -> {
		//Queue.service.submit(() -> {
			String data = URL.getURL();
			if(!data.isEmpty()) {
				Image img = new Image(data);

				if(img.isError()) {
					System.out.println("Image failed.");
				}

				view.setImage(img);
				
				System.out.println("loading img");
				
				Platform.runLater(() -> {
				
					pane.setOnMousePressed(e -> {
						click.onClick(img);
					});
					pane.setOnMouseEntered(e -> {
						pane.getChildren().add(shadowPane);
					});
					pane.setOnMouseExited(e -> {
						pane.getChildren().remove(shadowPane);
					});
					
					if(!this.showText.isEmpty()) {
						pane.getChildren().add(textPane);
					}
					
					pane.setCursor(Cursor.HAND);
				});
			}	
		//});
		//});
		}, "imageLoadThread1").start();
		
		
		this.attachCss(pane, Styling.CONTENT_VIEW);
		
		return pane;
	}
	
	public LinkGrabber getURL() {
		return URL;
	}

	public void setURL(LinkGrabber uRL) {
		URL = uRL;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	
}
