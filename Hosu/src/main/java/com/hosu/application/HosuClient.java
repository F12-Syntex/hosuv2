package com.hosu.application;
	
import java.awt.Dimension;

import org.controlsfx.control.PopOver;

import com.hosu.css.CssManager;
import com.hosu.css.ImageHandler;
import com.hosu.css.Styling;
import com.hosu.database.Database;
import com.hosu.helpers.Windows;
import com.hosu.panes.HosuButton;
import com.hosu.panes.Loader;
import com.hosu.panes.PaneHandler;
import com.hosu.panes.SettingsPane;
import com.hosu.settings.Settings;
import com.hosu.windows.EmbedViewer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HosuClient extends Application {
	
	private static HosuClient hosuClient;
	
	private CssManager cssManager;
	private ImageHandler imageHandler;
	
	private PaneHandler paneHandler;
	private Stage stage;
	
	private VBox side;
	
	private VBox body;
	
	public final static Database database = new Database();

	private static double xOffset = 0; 
	private static double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		
		database.connect();
		
		this.imageHandler = new ImageHandler();
		new Thread(() -> {
			imageHandler.initialise();
		}).start();
		
		this.stage = primaryStage;
		
		this.initialise();
		
		HosuClient.setInstance(this);
		
        VBox root = new VBox();  
        
        root.getStylesheets().add(this.cssManager.getCss(Styling.HOSU));
        root.setId("root");  
        
        this.side = new VBox();  
        
        side.getStylesheets().add(this.cssManager.getCss(Styling.HOSU));
        side.setMinWidth(40);
        side.setMaxWidth(40);
        side.setId("side");

        //.setHighlightedColour(Color.web("#0d101c"), Color.web("#181c2e"))
        
		HosuButton minimize = new HosuButton(com.hosu.css.Image.SEARCH_OFF, com.hosu.css.Image.SEARCH_ON, 28, (e) -> {
			this.paneHandler.getHosuBar().lastSet = this.paneHandler.getMangaContent();
			this.paneHandler.setActiveWithSearch(this.paneHandler.getMangaContent());
			this.paneHandler.getMangaContent().get();
		});
		HosuButton downloads = new HosuButton(com.hosu.css.Image.DOWNLOADS, com.hosu.css.Image.DOWNLOADS, 28, (e) -> {
			this.paneHandler.getHosuBar().lastSet = this.paneHandler.getMangaContent();
			this.paneHandler.setActive(this.paneHandler.getHome());
			this.paneHandler.getMangaContent().get();
		});
		StackPane icons = (StackPane) new HosuButton(com.hosu.css.Image.ICONS, com.hosu.css.Image.ICONS, 28, (e) -> {
				
		}).get();
		
		StackPane source = (StackPane) new HosuButton(com.hosu.css.Image.SAUCE, com.hosu.css.Image.SAUCE, 28, (e) -> {
			EmbedViewer.show("https://saucenao.com/");
			//EmbedViewer.show("https://embed.animeflix.ws/video.php?id=MTc1MTQ5&title=Scarlet+Nexus&typesub=SUB&sub=&cover=Y292ZXIvc2NhcmxldC1uZXh1cy5wbmc=-#-default");
		}).get();
		
		StackPane settings = (StackPane) new HosuButton(com.hosu.css.Image.BACKEND_SETTINGS, com.hosu.css.Image.BACKEND_SETTINGS, 28, (e) -> {
			EmbedViewer.show("https://saucenao.com/");
			//EmbedViewer.show("https://embed.animeflix.ws/video.php?id=MTc1MTQ5&title=Scarlet+Nexus&typesub=SUB&sub=&cover=Y292ZXIvc2NhcmxldC1uZXh1cy5wbmc=-#-default");
		}).get();
		
		StackPane home = (StackPane) new HosuButton(com.hosu.css.Image.STATS, com.hosu.css.Image.STATS, 28, (e) -> {
			this.paneHandler.getHosuBar().lastSet = this.paneHandler.getStatsPage();
			this.paneHandler.setActive(this.paneHandler.getStatsPage());
		}).get();
        
		//side offYOffset
		StackPane empty = new StackPane();
		//empty.setMinHeight(Settings.SIZE.getHeight() / 10);
		//empty.setMaxHeight(Settings.SIZE.getHeight() / 10);
		
		side.getChildren().add(home);
		side.getChildren().add(empty);
		side.getChildren().add(minimize.get());
		side.getChildren().add(downloads.get());
		side.getChildren().add(icons);
		side.getChildren().add(source);
		side.getChildren().add(settings);
 		
		side.setAlignment(Pos.TOP_CENTER);

        PopOver popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setAutoHide(false);
        popOver.setHideOnEscape(false);
        popOver.setAutoHide(true);
        
        popOver.setAnimated(true);
        popOver.setId("popover");

		icons.setOnMousePressed((e) -> {
			if(!popOver.isShowing()) {
				Pane test = new Loader(popOver).get();
		        popOver.setContentNode(test);
				popOver.show(icons);	
			}else {
				popOver.hide();
			}
		});
		
		icons.setId("popover");
		icons.getStylesheets().add(this.cssManager.getCss(Styling.HOSU));
        
        PopOver settingsPopOver = new PopOver();
        settingsPopOver.setDetachable(false);
        settingsPopOver.setAutoHide(false);
        settingsPopOver.setHideOnEscape(false);
        settingsPopOver.setAutoHide(true);
        
        settingsPopOver.setAnimated(true);
        settingsPopOver.setId("popover");
        
		settings.setOnMousePressed((e) -> {
			if(!settingsPopOver.isShowing()) {
				Pane test = new SettingsPane().get();
				settingsPopOver.setContentNode(test);
				settingsPopOver.show(settings);	
			}else {
				settingsPopOver.hide();
			}
		});
		
		icons.setId("popover");
		icons.getStylesheets().add(this.cssManager.getCss(Styling.HOSU));
		
        Dimension size = Settings.SIZE;
      
        root.setPrefSize(size.getWidth(), size.getHeight());
        
        try {
        	             
            this.body = this.getContentPane();
            body.setId("content");
            body.setPrefHeight(size.getHeight() - size.getHeight()/15);
            body.setPrefWidth(size.getWidth());
            body.setAlignment(Pos.CENTER);
            
        	this.paneHandler.register();
            
            javafx.scene.layout.Pane top = this.paneHandler.getHosuBar().get();
            top.setId("bar");

            //body.getChildren().add(this.paneHandler.getSearch().get());
            //body.getChildren().add(this.paneHandler.getRedditContent().get());
             
            //body.getChildren().add(this.paneHandler.getHome().get());
            
            HBox container = new HBox();
    		container.getChildren().add(side);
    		container.getChildren().add(body);
            
            root.getChildren().add(top);
            root.getChildren().add(container);
            
            StackPane bottom = new StackPane();
            bottom.setPrefHeight(size.getHeight()/30);
            bottom.setId("bar-bottom");
            
            root.getChildren().add(bottom);
            
            Scene scene = new Scene(root);      
            
            HosuClient.applyListeners(primaryStage);
            
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);  
            primaryStage.setTitle(Settings.NAME);  
            primaryStage.show();
            
			this.paneHandler.getHosuBar().lastSet = this.paneHandler.getMangaContent();
			this.paneHandler.getHome().get();
			//this.paneHandler.setActiveWithSearch(this.paneHandler.getMangaContent());
			this.paneHandler.setActive(this.paneHandler.getStatsPage());
			this.paneHandler.getMangaContent().get();

        	//ResizeHelper.addResizeListener(primaryStage, size.getWidth()/2, size.getHeight(), Integer.MAX_VALUE, Integer.MAX_VALUE);            
            
        }catch (Exception e) {
        	e.printStackTrace();
        	System.exit(1);
        }
        
        /*
	try {
			
			List<Chapter> chapters = new ArrayList<>();
			
			List<String> pages = new ArrayList<>();
			
			pages.add("https://live.staticflickr.com/65535/50904014368_6e46b6dba4_o.jpg");
			pages.add("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg");
			
			Chapter chapter = new Chapter(null, "example", "example", () -> pages);
			chapters.add(chapter);
			
			List<String> pages2 = new ArrayList<>();
			
			pages2.add("https://live.staticflickr.com/65535/50904014368_6e46b6dba4_o.jpg");
			pages2.add("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg");
			
			Chapter chapter2 = new Chapter(null, "example1", "example1", () -> pages);
			chapters.add(chapter2);
			
			QueriedManga queried = new QueriedManga("https://live.staticflickr.com/65535/50904838917_5a4401b5e3_o.jpg", "asd", null, "");
			
			RequestMangaData manga = new RequestMangaData(queried, null, chapters);
        	
			Player player = new Player(manga);
			player.play(manga.getChapters().get(manga.getChapters().size()-1));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        
	}
	
	public static HosuClient getHosuClient() {
		return hosuClient;
	}

	public static void setHosuClient(HosuClient hosuClient) {
		HosuClient.hosuClient = hosuClient;
	}

	public VBox getContentPane() {
		VBox pane = new VBox();
		
		pane.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(0), Insets.EMPTY)));
		
		pane.setId("side-bar");
		
		return pane;
	}
	
	private void initialise() {
		this.cssManager = new CssManager();
		this.paneHandler = new PaneHandler();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static HosuClient getInstance() {
		return hosuClient;
	}

	public static void setInstance(HosuClient hosuClient) {
		HosuClient.hosuClient = hosuClient;
	}

	public CssManager getCssManager() {
		return cssManager;
	}

	public void setCssManager(CssManager cssManager) {
		this.cssManager = cssManager;
	}
	
	public static void applyListeners(Stage stage) {
        Bounds allScreenBounds = Windows.computeAllScreenBounds();
        ChangeListener<Number> boundsListener = (obs, oldValue, newValue) -> {
            double x = stage.getX();
            double y = stage.getY();
            double w = stage.getWidth();
            double h = stage.getHeight();
            if (x < allScreenBounds.getMinX()) {
                stage.setX(allScreenBounds.getMinX());
            }
            if (x + w > allScreenBounds.getMaxX()) {
                stage.setX(allScreenBounds.getMaxX() - w);
            }
            if (y < allScreenBounds.getMinY()) {
                stage.setY(allScreenBounds.getMinY());
            }
            if (y + h > allScreenBounds.getMaxY()) {
                stage.setY(allScreenBounds.getMaxY() - h);
            }
        };
        stage.xProperty().addListener(boundsListener);
        stage.yProperty().addListener(boundsListener);
        stage.widthProperty().addListener(boundsListener);
        stage.heightProperty().addListener(boundsListener);
        stage.centerOnScreen();
    }
	
	public static void addMovement(Stage stage, Node root) {
        
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
		
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	stage.setX(event.getScreenX() - xOffset);
            	stage.setY(event.getScreenY() - yOffset);
            }
        });
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public VBox getBody() {
		return body;
	}

	public void setBody(VBox body) {
		this.body = body;
	}

	public ImageHandler getImageHandler() {
		return imageHandler;
	}

	public void setImageHandler(ImageHandler imageHandler) {
		this.imageHandler = imageHandler;
	}

	public PaneHandler getPaneHandler() {
		return paneHandler;
	}

	public void setPaneHandler(PaneHandler paneHandler) {
		this.paneHandler = paneHandler;
	}
	
}
