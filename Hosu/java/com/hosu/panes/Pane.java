package com.hosu.panes;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.hosu.css.Styling;
import com.hosu.settings.Settings;
import com.hosu1.application.HosuClient;

import animatefx.animation.FadeOutLeft;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class Pane {

	private boolean showing = false;
	
	private javafx.scene.layout.Pane node;
	
	List<Styling> attachedCss = new ArrayList<Styling>();
	
	public abstract javafx.scene.layout.Pane get();
	
	/*
	public void show(javafx.scene.layout.Pane root) {
		this.node = this.get();
		HosuClient.getInstance().getBody().getChildren().add(node);
	}
	
	public void showInvisible() {
		this.node = this.get();
		this.node.setVisible(false);
		HosuClient.getInstance().getBody().getChildren().add(node);
	}

	public void showAndSlideDown() {
		
		if(this.node != null) {
			HosuClient.getInstance().getBody().getChildren().remove(node);
			this.node = null;	
		}
		
		this.showing = true;
		this.node = this.get();
		HosuClient.getInstance().getBody().getChildren().add(node);
		new FadeInLeft(node).setSpeed(3).play();	
		
	}
	*/
	
	public void attachCss(javafx.scene.layout.Pane node, Styling css) {
		node.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(css));
	}
	
	public void remove() {
		if(node == null) return;
		HosuClient.getInstance().getBody().getChildren().remove(node);
		this.node = null;
	}
	
	public void removeSlideOut() {
		if(node == null) return;
		
		this.showing = false;
		new FadeOutLeft(node).setSpeed(3).play();
	}
	
	public boolean isShowing() {
		return this.showing;
	}
	
	public void applySizeToNode1(javafx.scene.layout.Pane pane, Dimension size) {
		this.applySizeToNode1(pane, size.getWidth(), size.getHeight());
	}
	
	public void applySizeToNode1(javafx.scene.layout.Pane pane, double width, double height) {
		
		Stage contentPane = HosuClient.getInstance().getStage();
		
		pane.setPrefSize(width, height);
		pane.setMinSize(width, height);
		pane.setMaxSize(width, height);
		
		contentPane.widthProperty().addListener((a,b,c) -> {
			double newWidth = ( contentPane.getWidth() * width ) / Settings.SIZE.getWidth();
			double newHeight = ( contentPane.getHeight() * height ) / Settings.SIZE.getHeight();
			
			pane.setPrefSize(newWidth, newHeight);
			pane.setMinSize(newWidth, newHeight);
			pane.setMaxSize(newWidth, newHeight);
			
		});
		
		contentPane.heightProperty().addListener((a,b,c) -> {
			double newWidth = ( contentPane.getWidth() * width ) / Settings.SIZE.getWidth();
			double newHeight = ( contentPane.getHeight() * height ) / Settings.SIZE.getHeight();
			
			pane.setPrefSize(newWidth, newHeight);
			pane.setMinSize(newWidth, newHeight);
			pane.setMaxSize(newWidth, newHeight);
			
		});
		
	}
	
	public void applySizeToNodeExact1(javafx.scene.layout.Pane pane, Dimension size) {
		this.applySizeToNode1(pane, size.getWidth(), size.getHeight());
	}
	
	public void applySizeToNodeExact1(javafx.scene.layout.Pane pane, double newWidth, double newHeight) {
		
		VBox contentPane = HosuClient.getInstance().getContentPane();
		
		pane.setPrefSize(newWidth, newHeight);
		pane.setMinSize(newWidth, newHeight);
		pane.setMaxSize(newWidth, newHeight);
		
		contentPane.widthProperty().addListener((a,b,c) -> {
			
			pane.setPrefSize(newWidth, newHeight);
			pane.setMinSize(newWidth, newHeight);
			pane.setMaxSize(newWidth, newHeight);
			
		});
		
		contentPane.heightProperty().addListener((a,b,c) -> {
			
			pane.setPrefSize(newWidth, newHeight);
			pane.setMinSize(newWidth, newHeight);
			pane.setMaxSize(newWidth, newHeight);
		});
		
	}
	
}
