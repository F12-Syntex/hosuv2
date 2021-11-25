package com.hosu.welcome;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.helpers.ResizeHelper;
import com.hosu.panes.HosuButton;
import com.hosu.settings.Settings;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Startup {

	public static void launch() {
	
		StackPane root  = new StackPane();
		
		Stage stage = new Stage();
		stage.setTitle("Setup");

		root.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));	
		
		root.setId("startup");
		
        Scene scene = new Scene(root, 600.0, 382.0);
		
		stage.setScene(scene);
		
		ResizeHelper.addResizeListener(stage);   
		 
		StackPane pane2 = new StackPane();
		pane2.setId("content");
		
		HosuButton close = new HosuButton(com.hosu.css.Image.RED_CIRCLE, com.hosu.css.Image.RED_CIRCLE, 16, (e) -> {
			HosuClient.getInstance().getStage().setIconified(true);
			Platform.exit();
			System.exit(0);
		});
		
		root.getChildren().add(close.get());
		root.setAlignment(Pos.TOP_RIGHT);
		
		StackPane.setMargin(pane2, new Insets(24));
		
		//root.getChildren().add(pane2);
		
		scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
	
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);  
		stage.setTitle(Settings.NAME);  
		stage.show();
		
	}

}
