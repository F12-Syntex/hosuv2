package com.hosu.panes;

import javafx.scene.layout.StackPane;

public class DropDownBar extends Pane{

	@Override
	public javafx.scene.layout.Pane get() {

		StackPane pane = new StackPane();
		
		pane.setStyle("-fx-background-color: #181c2e");
		
		
		return pane;
	}
	
}
