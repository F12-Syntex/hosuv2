package com.hosu.panes;

import java.awt.Dimension;

import com.hosu.settings.Settings;
import com.hosu.windows.EmbedViewer;
import com.hosu.windows.MediaViewer;
import com.hosu1.application.HosuClient;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
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
		
		HosuButton mangaDropDown = new HosuButton(com.hosu.css.Image.LIGHT_BOOK, com.hosu.css.Image.DARK_BOOK, iconSize, (e) -> {
			panehandler.setActiveWithSearch(panehandler.getMangaContent());
			this.lastSet = panehandler.getMangaContent();
		});
		
		HosuButton reddit = new HosuButton(com.hosu.css.Image.REDDIT, com.hosu.css.Image.REDDIT, iconSize, (e) -> {
			panehandler.setActiveWithSearch(panehandler.getRedditContent());
			this.lastSet = panehandler.getRedditContent();
		});
		
		HosuButton home = new HosuButton(com.hosu.css.Image.HOME, com.hosu.css.Image.HOME, iconSize, (e) -> {
			panehandler.setActive(panehandler.getHome());
			this.lastSet = panehandler.getHome();
		});
		
		HosuButton hentai = new HosuButton(com.hosu.css.Image.NHENTAI, com.hosu.css.Image.NHENTAI, iconSize, (e) -> {
			panehandler.setActiveWithSearch(panehandler.getNmangaContent());
			this.lastSet = panehandler.getNmangaContent();
		});
		
		HosuButton search = new HosuButton(com.hosu.css.Image.MAL, com.hosu.css.Image.MAL, iconSize, (e) -> {
			panehandler.setActiveWithSearch(panehandler.getMalContent());
			this.lastSet = panehandler.getMalContent();
		});
		
		HosuButton getSauce = new HosuButton(com.hosu.css.Image.SAUCE, com.hosu.css.Image.SAUCE, iconSize, (e) -> {
			//SauceFinder.show();
			//EmbedViewer.show("https://vidstream.pro/e/7ZPNMX8ZLY02?domain=animebee.to");
			//EmbedViewer.show("https://jzscuqezoqkcpvy.win/p/player.html#aHR0cHM6Ly92LnZydi5jby9ldnMxLzVhOTJjMmM5Y2I2MWVlODg4M2M3YjY2YmFmYTdiYTljL2Fzc2V0cy9zbnFvcjBqbjE1eWloczhfLDE3NTc0MzkubXA0LDE3NTc0NDMubXA0LDE3NTc0MzUubXA0LDE3NTc0MzEubXA0LDE3NTc0MjcubXA0LC51cmxzZXQvbWFzdGVyLm0zdTg/UG9saWN5PWV5SlRkR0YwWlcxbGJuUWlPbHQ3SWxKbGMyOTFjbU5sSWpvaWFIUjBjQ282THk5MkxuWnlkaTVqYnk5bGRuTXhMelZoT1RKak1tTTVZMkkyTVdWbE9EZzRNMk0zWWpZMlltRm1ZVGRpWVRsakwyRnpjMlYwY3k5emJuRnZjakJxYmpFMWVXbG9jemhmTERFM05UYzBNemt1YlhBMExERTNOVGMwTkRNdWJYQTBMREUzTlRjME16VXViWEEwTERFM05UYzBNekV1YlhBMExERTNOVGMwTWpjdWJYQTBMQzUxY214elpYUXZiV0Z6ZEdWeUxtMHpkVGdpTENKRGIyNWthWFJwYjI0aU9uc2lSR0YwWlV4bGMzTlVhR0Z1SWpwN0lrRlhVenBGY0c5amFGUnBiV1VpT2pFMk16TXpNamMyTkRSOWZYMWRmUV9fJlNpZ25hdHVyZT1RMFJXMFl4clpBZ3YtRWxxcUxLOVBpT2VTTER5UkV6QzRRMkhUV1U2a25Ofm1ETUpjYlU4Z1lWSnpXcGNqT0tNY0pJWjZuT1dPQXU1dUI3WkdldHpGcmJMT3BWVnhqSjRQSGdKN3c2REF2MHJTcDZJeXp3Z2FRVjJKRHFyODJsR25UOH5uUlRPLUtiUXpOelN3UHd2UVMwamdna2VqLXFSM0ZqOU9MeTdjNHZybGlMRTV3VXU1clBma3pHY09GTVozVzVyVkUxQ09LNXpoMFZOaWsyRkZpU3pSNGtwejlUUEpPWDVsM0ZTaGFwNTJZdW1ETmlqRjVxTTVYbm1TZHlndDYyRUpWbTY3dzlPYVZCUTdxbklMNFlEajVzMFRQcXVvY1NSNGQzWjRwd3ZUTE5iVkFsdC13NUFZN3lMbmhhcWFPY3ZPY05paVZhcE5EbkhTcUNXS2dfXyZLZXktUGFpci1JZD1BUEtBSk1XU1E1UzdaQjNNRjVWQQ==#uid=MjYzMw==");
			//EmbedViewer.show("https://dood.la/d/61p24dcab060");
			//EmbedViewer.show("https://ddl.animeout.com/public.php?url=ains.animeout.com/series/Completed/Shingeki%20no%20Kyojin%20S2/[AnimeOut]%20Shingeki%20no%20Kyojin%20(Season%202)%20-%2037%20(720p)[DameDesuYo][Henz].mkv");
			//EmbedViewer.show("https://third-party.animekisa.tv/player-vidcdn.php?url=aHR0cHM6Ly92aWRzdHJlYW1pbmcuaW8vbG9hZC5waHA/aWQ9TWpZek13PT0mdGl0bGU9U2hpbmdla2krbm8rS3lvamluJnR5cGVzdWI9U1VCJnN1Yj1leUpsYmlJNmJuVnNiQ3dpWlhNaU9tNTFiR3g5JmNvdmVyPWFXMWhaMlZ6TDJGdWFXMWxMMU5vYVc1blpXdHBMVzV2TFV0NWIycHBiaTVxY0djPQ==&isb=false&adb=false");
			
			//EmbedViewer.show("https://s-delivery35.mxdcontent.net/v/b956ebd84f3cc5fcc3deafdf46880fcb.mp4?s=cbo9EDQXfYi84dKKgG4hYw&e=1633346593&_t=1633330477");
			//EmbedViewer.show("https://www68.sbcdnvideo.com/tysxes777s66j6cdaa3bxxage6uba5ppootm3t6au6eilwscyqg3mjdunzdq/shingeki-no-kyojin-episode-1.mp4");
			
			//MediaViewer.show(new Media("https://streamtape.com/v/6QlK6dqYz8i9z3O/shingeki-no-kyojin-episode-1.mp4"));
			
			//EmbedViewer.show("https://www68.sbcdnvideo.com/tysxes777s66j6cdaa3bxxage6uba5ppootm3t6augeilwscyqg7riakzg4q/shingeki-no-kyojin-episode-1.mp4");
			
			MediaViewer.show(new Media("https://www1876.ff-04.com/token=qrP-fUZ664TBFbWIbE0NcA/1633421128/159.242.0.0/162/4/85/f229361bc64f2188a36b016a3150e854-1080p.mp4"));
			
			//panehandler.setActive(panehandler.getSauceFinder());
			//this.lastSet = panehandler.getSauceFinder();
		});
			
		HBox leftPane = new HBox();
		//leftPane.getChildren().add(animeDropDown.get());
		Color highlight = Color.rgb(133, 133, 133, 0.3);
		
		leftPane.getChildren().add(home.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(mangaDropDown.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(reddit.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(hentai.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(getSauce.setHighlightedColour(highlight).get());
		leftPane.getChildren().add(search.setHighlightedColour(highlight).get());
		
		
	    //pane.getChildren().add(dropDown.get(root));
		
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
