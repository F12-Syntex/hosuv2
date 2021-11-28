package com.hosu.windows;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class EmbedViewer {

	public static void show(String url) {
		WebView view = new WebView();

		WebEngine webEngine = view.getEngine();
		webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
		webEngine.setJavaScriptEnabled(true);

		webEngine.load(url);

		ScrollPane scroallable = new ScrollPane(view);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
		
		Stage stage = new Stage();
		
		scroallable.setStyle("-fx-background-color: #0d101c");
		
		scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));
		
		stage.setScene(new Scene(scroallable, 800, 800));
		stage.show();
			
	}

}
