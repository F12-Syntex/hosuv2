package com.hosu.panes;

import org.controlsfx.control.PopOver;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.settings.Settings;

import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Loader extends Pane {
	
	private PopOver popOver;
	
	public Loader(PopOver popOver) {
		this.popOver = popOver;
	}

	@Override
	public javafx.scene.layout.Pane get() {

		int size = 2;
		
		FlowGridPane test = new FlowGridPane(size, size);
		test.setMinSize(Settings.SIZE.getWidth() / 3, Settings.SIZE.getHeight() / 3);
		test.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        test.setId("side");

    	double width = ((Settings.SIZE.getWidth() / 1.8) / size);
    	double height = ((Settings.SIZE.getHeight() / 2) / size);
        
        
    	/*
        for(com.hosu.css.Image i : com.hosu.css.Image.values()) {
        	
        	if(i.getResource().contains("css")) continue;
        	
        	try {
            	test.getChildren().add(getImage(width, height, HosuClient.getInstance().getImageHandler().getFromCashe(i)));	
        	}catch (Exception e) {
        		
			}
        }
        */
        
    	//https://c.tenor.com/bVm05NUoyF0AAAAM/bokuno-hero-academia-izuku.gif
    	
    	test.getChildren().add(getImage(width, height, HosuClient.getInstance().getImageHandler().imageLoading));
        test.getChildren().add(getImage(width, height, HosuClient.getInstance().getImageHandler().loading1));
        test.getChildren().add(getImage(width, height, HosuClient.getInstance().getImageHandler().loading2));
        test.getChildren().add(getImage(width, height, HosuClient.getInstance().getImageHandler().loading3));
        
        /*
        String[] urls = new String[] {
        	"https://c.tenor.com/94IrZAPQl9sAAAAC/anime-girl.gif",
        	"https://c.tenor.com/mKTS5nbF1zcAAAAM/cute-anime-dancing.gif",
        	"https://c.tenor.com/6MsukwHKJ58AAAAM/ara-anime.gif",
        	"https://c.tenor.com/rnhV3fu39f8AAAAM/eating-anime.gif",
        	"https://c.tenor.com/bFtrVz3Ahg4AAAAM/anime-girls.gif"
        };
        
        for(String i : urls) {
        	test.getChildren().add(getImage(width, height, new Image(i)));
        }
        */
        
		return test;
	}

	public StackPane getImage(double d, double e, Image loading) {
		
		StackPane dark = new StackPane();
		dark.setStyle("-fx-background-color: rgba(0,0,0,0)");
		
		StackPane pane = new StackPane();
		
		ImageView view = new ImageView(loading);
		view.setFitWidth(d);
		view.setFitHeight(e);
		view.setPreserveRatio(true);
		
		pane.getChildren().add(view);
		
		dark.setOnMouseEntered((e2) -> {
			dark.setStyle("-fx-background-color: rgba(0,0,0,0.2)");
		});
		dark.setOnMouseExited((e2) -> {
			dark.setStyle("-fx-background-color: rgba(0,0,0,0)");
		});
		dark.setOnMousePressed(i -> {
			HosuClient.getInstance().getImageHandler().loading = loading;
			popOver.hide();
		});
		
		pane.getChildren().add(dark);
		pane.setCursor(Cursor.HAND);
		
		return pane;
	}
	
}
