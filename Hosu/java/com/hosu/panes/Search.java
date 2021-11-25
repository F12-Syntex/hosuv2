package com.hosu.panes;

import com.hosu1.application.HosuClient;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class Search extends Pane{

	public Search() {
		this.font = new Font("SERIF", 16);
	}
	
	public TextField field;
	public Font font;
	
	@Override
	public javafx.scene.layout.Pane get() {
	
		StackPane container = new StackPane();
		
		StackPane pane = new StackPane();
		
		double width = HosuClient.getInstance().getBody().getPrefWidth() / 2;
		double height = HosuClient.getInstance().getBody().getPrefHeight() / 15;

		//this.applySizeToNodeExact1(pane, width, height);
		
		pane.setPrefSize(width, height);
		pane.setMinSize(width, height);
		pane.setMaxSize(width, height);
		
		this.field = new TextField();
		
		field.setPromptText("Search...");
		field.setFont(font);
		
		pane.setId("search");
		
		field.setOnKeyPressed((e) -> {
			if(e.getCode() == KeyCode.ENTER) {
				SearchableContent searchNode = HosuClient.getInstance().getPaneHandler().getCurrentSearchablePane();
				searchNode.onSearch(field.getText());
				System.out.println("Searching for " + field.getText() + " in for node " + searchNode.getClass().getName());
			}
		});
		
		Label label = new Label();
		label.setText("Search");
		label.setFont(font);
		label.setPrefWidth(width/8);
		
		pane.getChildren().add(field);
		
		HBox box = new HBox();
		//box.getChildren().add(label);
		box.getChildren().add(pane);
		
		box.setAlignment(Pos.CENTER);
		
		container.getChildren().add(box);
		
		StackPane.setAlignment(pane, Pos.CENTER);
		
		return container;
	}
	
}
