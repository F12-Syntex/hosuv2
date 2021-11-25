package com.hosu1.application;
	
import java.awt.Dimension;

import com.hosu.css.CssManager;
import com.hosu.css.ImageHandler;
import com.hosu.css.Styling;
import com.hosu.helpers.Windows;
import com.hosu.panes.PaneHandler;
import com.hosu.settings.Settings;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
	
	private VBox body;

	private static double xOffset = 0; 
	private static double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		
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
        
        Dimension size = Settings.SIZE;
      
        root.setPrefSize(size.getWidth(), size.getHeight());
        
        try {
        	             
            this.body = this.getContentPane();
            body.setId("content");
            body.setPrefHeight(size.getHeight() - size.getHeight()/15);
            body.setPrefWidth(size.getWidth());

        	this.paneHandler.register();
            
            javafx.scene.layout.Pane top = this.paneHandler.getHosuBar().get();
            top.setId("bar");

            //body.getChildren().add(this.paneHandler.getSearch().get());
            //body.getChildren().add(this.paneHandler.getRedditContent().get());
             
            //body.getChildren().add(this.paneHandler.getHome().get());
            
            root.getChildren().add(top);
            root.getChildren().add(body);
            
            StackPane bottom = new StackPane();
            bottom.setPrefHeight(size.getHeight()/30);
            bottom.setId("bar-bottom");
            
            root.getChildren().add(bottom);
            
            Scene scene = new Scene(root);      
            
            HosuClient.applyListeners(primaryStage);
            
            //root.setMinHeight(size.getHeight());
            
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);  
            primaryStage.setTitle(Settings.NAME);  
            primaryStage.show();
            
        	//ResizeHelper.addResizeListener(primaryStage, size.getWidth()/2, size.getHeight(), Integer.MAX_VALUE, Integer.MAX_VALUE);            
            
        }catch (Exception e) {
        	e.printStackTrace();
        	System.exit(1);
        }

		
        
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
