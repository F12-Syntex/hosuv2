package com.hosu.panes;

import java.awt.Dimension;

import com.hosu.application.HosuClient;
import com.hosu.settings.Settings;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class HosuBar extends Pane{

	private javafx.scene.layout.Pane root;
	
	public Pane lastSet;
	
	private final int iconSize = 20;
	
	@Override
	public javafx.scene.layout.Pane get() {
		
		HBox pane = new HBox();
		pane.setId("bar");
		pane.setAlignment(Pos.CENTER_RIGHT);

		PaneHandler panehandler = HosuClient.getInstance().getPaneHandler();
		
		HosuButton close = new HosuButton(com.hosu.css.Image.RED_CIRCLE, com.hosu.css.Image.RED_CIRCLE, iconSize, (e) -> {
			HosuClient.getInstance().getStage().setIconified(true);
			Platform.exit();
			System.exit(0);
		});
		
		HosuButton minimize = new HosuButton(com.hosu.css.Image.BLUE_CIRCLE, com.hosu.css.Image.BLUE_CIRCLE, iconSize, (e) -> {
			HosuClient.getInstance().getStage().setIconified(true);
		});
		
		HosuButton mangaDropDown = new HosuButton(com.hosu.css.Image.SEARCH, com.hosu.css.Image.SEARCH, iconSize, (e) -> {
			panehandler.setActiveWithSearch(panehandler.getMangaContent());
			this.lastSet = panehandler.getMangaContent();
		});

		HosuButton home = new HosuButton(com.hosu.css.Image.DOWNLOAD, com.hosu.css.Image.DOWNLOAD, iconSize, (e) -> {
			panehandler.setActive(panehandler.getHome());
			this.lastSet = panehandler.getHome();
		});
		
			
		HBox leftPane = new HBox();
		Color highlight = Color.rgb(133, 133, 133, 0.3);
		
		leftPane.getChildren().add(mangaDropDown.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(home.setHighlightedColour(highlight).get());
		
	    pane.getChildren().add(minimize.get());
	    pane.getChildren().add(close.get());
	    
	    pane.widthProperty().addListener((a, b, c) -> {
	    		pane.getChildren().forEach(i -> {
	    			
	    			if(i instanceof ImageView) {
	    				ImageView o = (ImageView)i;
	    				Dimension size = this.getIconDimentions();
	    				o.setFitWidth(size.getWidth());
	    				o.setFitHeight(size.getHeight());
	    			}
	    			
	    		});
	    });
	    
	    pane.heightProperty().addListener((a, b, c) -> {
    		pane.getChildren().forEach(i -> {
    			
    			if(i instanceof ImageView) {
    				ImageView o = (ImageView)i;
    				Dimension size = this.getIconDimentions();
    				o.setFitWidth(size.getWidth());
    				o.setFitHeight(size.getHeight());
    			}
    			
    		});
    });
		
	    AnchorPane anchor = new AnchorPane();
	    
	    anchor.getChildren().add(pane);
	    anchor.getChildren().add(leftPane);
	    
	    AnchorPane.setRightAnchor(pane, 2.0);
	    AnchorPane.setLeftAnchor(leftPane, 2.0);
	    
        HosuClient.addMovement(HosuClient.getInstance().getStage(), anchor);
	    
		return anchor;
	}
	
	private Dimension getIconDimentions() {
		int sizeH = (int) ((root.getHeight() * iconSize) / Settings.SIZE.getHeight());
		int sizeW = (int) ((root.getWidth() * iconSize) / Settings.SIZE.getWidth());
		
		return new Dimension(sizeW, sizeH);
	}

}
