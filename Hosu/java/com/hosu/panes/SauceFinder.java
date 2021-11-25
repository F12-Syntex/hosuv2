package com.hosu.panes;

import com.hosu.css.Styling;
import com.hosu.settings.Settings;
import com.hosu1.application.HosuClient;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class SauceFinder extends Pane{
	
	@Override
	public javafx.scene.layout.Pane get() {

		WebView view = new WebView();

		WebEngine webEngine = view.getEngine();
		
		webEngine.load("https://saucenao.com/");
	
		ScrollPane scroallable = new ScrollPane(view);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		
		scroallable.setMaxSize(Settings.SIZE.getWidth(), Settings.SIZE.getHeight());
		scroallable.setMinSize(Settings.SIZE.getWidth(), Settings.SIZE.getHeight());
		
		scroallable.setStyle("-fx-background-color: #0d101c");
		
		scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));

		return new StackPane(scroallable);
	}
	
	
}
