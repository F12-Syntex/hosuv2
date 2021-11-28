package com.hosu.panes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.windows.EmbedViewer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.sandrohc.jikan.model.anime.Anime;

public class AniContentPane extends Pane{

	private Anime manga = null;
	
	private static Font font = new Font(16);
	
	public void setMangaData(Anime manga) {
		System.out.println("LOADED: " + manga.episodes);
		this.manga = manga;
	}
	
	@Override
	public javafx.scene.layout.Pane get() {
		VBox pane = new VBox();
		
		StackPane content = new StackPane();
		
		content.setPrefWidth(HosuClient.getHosuClient().getBody().getWidth() / 1.2);
		content.setPrefHeight(HosuClient.getHosuClient().getBody().getHeight() / 1.2);
		
		content.setMaxWidth(HosuClient.getHosuClient().getBody().getWidth() / 1.2);
		content.setMaxHeight(HosuClient.getHosuClient().getBody().getHeight() / 1.2);
		
		content.setId("background2");

		HBox contentPane = new HBox(25);
		
		contentPane.setAlignment(Pos.CENTER);
		
		content.getChildren().add(contentPane);

		StackPane.setAlignment(content, Pos.BOTTOM_CENTER);
		
		StackPane left = new StackPane();
		left.setId("testtop");
		
		StackPane bottom = new StackPane();
		bottom.setId("testtop");
		
		ImageView mangaImg;
					
		mangaImg = new ImageView(new Image(manga.imageUrl));

		mangaImg.setId("testtop");
		
		left.setAlignment(Pos.CENTER);
		
		GridPane grids = new GridPane();

		int length = 0;

		grids.setMinWidth((length+1) * font.getSize());
		
		grids.setId("content");
		
		left.getChildren().add(mangaImg);
		
		double half = content.getPrefWidth() / 1.95;
		double widthOffset = 180;

		double topNodeHeight = content.getPrefHeight() - ( 20 + 300 );
		
		double nodeSize = (HosuClient.getInstance().getBody().getPrefWidth()) - ((half - widthOffset) + 20);
		
		
		StackPane top = this.getTextPane(nodeSize);
		
		top.setId("selectionbg");
		top.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
		bottom.getChildren().add(this.getDesc(nodeSize));
		
		//300
		StackPane.setMargin(top, new Insets(20, 20, 20, half - widthOffset));
		StackPane.setMargin(left, new Insets(20, half + widthOffset, 250, 20));
		StackPane.setMargin(bottom, new Insets(topNodeHeight + 40, 20, 20, half - widthOffset));
		
		content.getChildren().add(top);
		content.getChildren().add(left);
		//content.getChildren().add(bottom);
		
		pane.getChildren().add(content);
		
		pane.setMinSize(HosuClient.getHosuClient().getBody().getWidth(), HosuClient.getHosuClient().getBody().getHeight());
		
		pane.setId("background");
		
		pane.setAlignment(Pos.CENTER);
		
		return pane;
	}

	
	public StackPane getDesc(double nodeSize){
		
		Font detailsFont = Font.font(18);
		Text value;
		
		value = new Text(manga.synopsis);
		
		value.setFill(Color.SILVER);
		
		value.setFont(detailsFont);
		value.setWrappingWidth(50);
		
		TextFlow textFlowPane1 = new TextFlow();
		textFlowPane1.getChildren().addAll(value);
		textFlowPane1.setPadding(new Insets(3));
		textFlowPane1.setTextAlignment(TextAlignment.LEFT);
		textFlowPane1.setMinWidth(nodeSize);
		textFlowPane1.setMaxHeight(1000);
	
		textFlowPane1.setId("content");
		
		ScrollPane scroallable = new ScrollPane(textFlowPane1);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		scroallable.setId("selectionbg");
		scroallable.setMaxWidth(nodeSize);
		
		StackPane scroll = new StackPane(scroallable);
		this.attachCss(scroll, Styling.SCROLL_PANE);
		this.attachCss(scroll, Styling.JFXBUTTON);
		
		return scroll;
		
	}
	
	public StackPane getTextPane(double nodeSize){
		
		System.out.println("init");
		
		GridPane grids = new GridPane();

		grids.setPadding(new Insets(0));
		
		int length = 0;
		
		Font detailsFont = Font.font(18);
		
		int index = 0;
		
		Map<String, String> mapping = new HashMap<>();
		
		Field[] fields;
		
		fields = this.manga.getClass().getFields();
		
		for(Field o : fields) {
			
			try {

				

			    Object clazz;
			    
			    clazz = this.manga;

			    Field field = clazz.getClass().getField(o.getName());
				
				mapping.put(o.getName().substring(0,1).toUpperCase() + o.getName().substring(1).toLowerCase() + ": ", field.get(clazz).toString());
				
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (NullPointerException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
		
		for(String key : mapping.keySet()) {
			
			String refrence = mapping.get(key);
			
			refrence = refrence.trim();
			refrence = refrence.replace(System.lineSeparator(), "");
			
			if(refrence.isEmpty()) refrence = "N/A";
			
			String displayKey = key;
			
			displayKey = displayKey.replace(",", System.lineSeparator()).trim();
			//displayKey = displayKey.replace(System.lineSeparator(), "");
			
			if(displayKey.equalsIgnoreCase("alternative")) continue;
			
			System.out.println(displayKey + " : " + key);
			
			Text label = new Text(displayKey);
			label.setFill(Color.WHITE);
			
			Text value = new Text(refrence);
			value.setFill(Color.SILVER);
			
			Hyperlink link = null;
			
			if(refrence.startsWith("http")) {
				link = new Hyperlink();
			    link.setText(refrence);
			    final String uri = refrence;
			    link.setOnAction(new EventHandler<ActionEvent>() {
			        @Override
			        public void handle(ActionEvent e) {
			           // try {
			               //java.awt.Desktop.getDesktop().browse(java.net.URI.create(uri));
			                EmbedViewer.show(uri);
						// } catch (IOException e1) {
			            //    e1.printStackTrace();
			            //}
			        }
			    });
			}
			
			label.setFont(detailsFont);
			value.setFont(detailsFont);
			
			label.setWrappingWidth(50);
			value.setWrappingWidth(50);
			
			TextFlow textFlowPane = new TextFlow();
			textFlowPane.getChildren().addAll(label);
			textFlowPane.setPadding(new Insets(3));
			textFlowPane.setTextAlignment(TextAlignment.LEFT);
			textFlowPane.setMinWidth(nodeSize/5);
			textFlowPane.setMaxHeight(1000);
			
			TextFlow textFlowPane1 = new TextFlow();
			if(link != null) {
				link.setFont(detailsFont);
				textFlowPane1.getChildren().addAll(link);
				//textFlowPane1.getChildren().addAll(value);	
			}else {
				textFlowPane1.getChildren().addAll(value);	
			}
			
			textFlowPane1.setPadding(new Insets(3));
			textFlowPane1.setTextAlignment(TextAlignment.LEFT);
			textFlowPane1.setMinWidth(nodeSize/2);
			
			HBox box = new HBox(0, textFlowPane, textFlowPane1);
		
			if(refrence.contains("http")) {

				WebView view = new WebView();

				WebEngine webEngine = view.getEngine();
				
				webEngine.load(refrence);

				ScrollPane scroallable = new ScrollPane(view);
				scroallable.setFitToHeight(true);
				scroallable.setFitToWidth(true);
				scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
				
				scroallable.setStyle("-fx-background-color: #0d101c");
				
				scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));

				scroallable.setMaxSize(400, 400);
				
				final String uri = refrence;
				
				textFlowPane1.setOnMousePressed(i -> {
					EmbedViewer.show(uri);
				});
				
			}
			
			grids.add(box, 0, index);
			
			index++;
			
		}
		
		grids.setMinWidth((length+1) * font.getSize());
		
		grids.setId("content");
		
		ScrollPane scroallable = new ScrollPane(grids);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		scroallable.setId("selectionbg");
		scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
		
		StackPane scroll = new StackPane(scroallable);
		this.attachCss(scroll, Styling.SCROLL_PANE);
		this.attachCss(scroll, Styling.JFXBUTTON);
		
		return scroll;
		
	}
	
	
	public StackPane get1(){
		GridPane grids = new GridPane();

		//List<Chapter> chapters = this.data.getChapters().getChapters();
		
		//Collections.reverse(chapters);
	
		int index = 1;
		
		int length = 0;

			for(String i : javafx.scene.text.Font.getFamilies()) {
				
				Button button = new Button(i);
				button.setFont(Font.font(i));
				button.setCursor(Cursor.HAND);
				
				final int temp = index;
				Platform.runLater(() -> {
					grids.add(button, 0, temp);
				});
				
				button.setOnMousePressed(o -> {
					StringSelection selection = new StringSelection(i);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				});
				
				//button.setId("button");
				
				index++;
			}
		
		grids.setMinWidth((length+1) * font.getSize());
		
		grids.setId("content");
		
		ScrollPane scroallable = new ScrollPane(grids);
		scroallable.setFitToHeight(true);
		scroallable.setFitToWidth(true);
		scroallable.setId("selectionbg");
		scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
		
		StackPane scroll = new StackPane(scroallable);
		this.attachCss(scroll, Styling.SCROLL_PANE);
		this.attachCss(scroll, Styling.JFXBUTTON);
		
		return scroll;
		
	}
	

}
