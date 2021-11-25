package com.hosu.panes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hosu.css.Styling;
import com.hosu.mangaplayer.Player;
import com.hosu1.application.HosuClient;
import com.jfoenix.controls.JFXButton;
import com.manga.data.Chapter;
import com.manga.data.ChapterHandler;
import com.manga.data.MangaData;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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

public class MangaInfoPane extends Pane{

	private MangaData data = null;
	private static Font font = new Font(16);
	private String image;
	
	public void setMangaData(MangaData data, String image) {
		this.data = data;
		this.image = image;
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
		
		HosuButton minimize = new HosuButton(com.hosu.css.Image.RED_CIRCLE, com.hosu.css.Image.RED_CIRCLE, 16, (e) -> {
			HosuClient.getInstance().getPaneHandler().setActiveWithSearch(HosuClient.getInstance().getPaneHandler().getHosuBar().lastSet);
			
			/*
			new Thread(() -> {
				
				Platform.runLater(() -> {
					pane.getChildren().clear();
				});
				
				
				MangaInfoPane pane1 = HosuClient.getInstance().getPaneHandler().getMangaInfoPane();
				pane1.setMangaData(this.data, this.image, this.name);
				
				HosuClient.getInstance().getPaneHandler().setActive(this);

			}, "dataSearchThread").start();
			*/
		});
		
		javafx.scene.layout.Pane exit = minimize.get();
		

		HBox contentPane = new HBox(25);
		
		contentPane.setMaxHeight(content.getMaxHeight() - exit.getMaxHeight());
		contentPane.setAlignment(Pos.CENTER);
		
		content.getChildren().add(exit);
		content.getChildren().add(contentPane);

		StackPane.setAlignment(exit, Pos.TOP_RIGHT);
		StackPane.setAlignment(content, Pos.BOTTOM_CENTER);
		
		StackPane top = new StackPane();
		top.setId("testtop");
		
		StackPane left = new StackPane();
		left.setId("testtop");
		
		StackPane bottom = new StackPane();
		bottom.setId("testtop");
		
		ImageView mangaImg = new ImageView(new Image(image));

		mangaImg.fitWidthProperty().bind(left.widthProperty());
		mangaImg.fitHeightProperty().bind(left.heightProperty());
		//mangaImg.setPreserveRatio(true);
		mangaImg.setId("testtop");
		
		left.setAlignment(Pos.CENTER);
		
		GridPane grids = new GridPane();

		ChapterHandler chapters = this.data.getChapters();
	
		int index = 1;
		
		int length = 0;
		
		for(Chapter i : chapters.getChapters()) {
			
			String name = i.getName().split(">")[1];
			
			if(length < name.length()) {
				length = name.length();
			}
			
			JFXButton button = new JFXButton(name);
			button.setFont(font);
			button.setCursor(Cursor.HAND);
			
			final int temp = index;
			Platform.runLater(() -> {
				grids.add(button, 0, temp);
			});
			
			button.setId("button");
			
			button.setOnMousePressed(o -> {
				new Thread(() -> {
					System.out.println("initialising");
					Player player = new Player(chapters, i.getChapter());
					Platform.runLater(() -> {
						System.out.println("loading");
						player.launch();
					});
				}).start();
			});
			
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
		
		bottom.getChildren().add(scroll);
		
		System.out.println(top.getWidth());
		
		left.getChildren().add(mangaImg);
		
		double half = content.getPrefWidth() / 1.95;
		double widthOffset = 180;
		
		//content.getPrefHeight() - (20+300+20), 20, 20, 20

		double topNodeHeight = content.getPrefHeight() - ( 20 + 300 );
		
		//(20, half + widthOffset, 20, 20)
		
		StackPane.setMargin(top, new Insets(20, 20, 300, half - widthOffset));
		StackPane.setMargin(left, new Insets(20, half + widthOffset, 250, 20));
		StackPane.setMargin(bottom, new Insets(topNodeHeight + 40, 20, 20, half - widthOffset));
		
		pane.getChildren().add(content);
		
		//top.getChildren().add(details);
		top.getChildren().add(this.getTextPane());
		
		content.getChildren().add(top);
		content.getChildren().add(left);
		content.getChildren().add(bottom);
		
		pane.setMinSize(HosuClient.getHosuClient().getBody().getWidth(), HosuClient.getHosuClient().getBody().getHeight());
		
		pane.setId("background");
		
		pane.setAlignment(Pos.CENTER);
		
		return pane;
	}
	
	public StackPane getTextPane(){
		
		GridPane grids = new GridPane();

		int length = 0;
		
		Font detailsFont = Font.font(16);
		
		int index = 0;
		
		Map<String, String> mapping = data.getDetails();
		
		for(String key : mapping.keySet()) {
			
			String refrence = mapping.get(key);
			
			refrence = refrence.trim();
			refrence = refrence.replace(System.lineSeparator(), "");
			
			if(refrence.isEmpty()) refrence = "N/A";
			
			String displayKey = key;
			
			displayKey = displayKey.trim();
			displayKey = displayKey.replace(System.lineSeparator(), "");
			
			if(displayKey.equalsIgnoreCase("alternative")) continue;
			
			System.out.println(displayKey + " : " + key);
			
			Text label = new Text(displayKey);
			label.setFill(Color.WHITE);
			
			Text value = new Text(refrence);
			value.setFill(Color.SILVER);
			
			label.setFont(detailsFont);
			value.setFont(detailsFont);
			
			label.setWrappingWidth(100);
			value.setWrappingWidth(100);
			
			TextFlow textFlowPane = new TextFlow();
			textFlowPane.getChildren().addAll(label);
			textFlowPane.setPadding(new Insets(3));
			textFlowPane.setTextAlignment(TextAlignment.LEFT);
			
			TextFlow textFlowPane1 = new TextFlow();
			textFlowPane1.getChildren().addAll(value);
			textFlowPane1.setPadding(new Insets(3));
			textFlowPane1.setTextAlignment(TextAlignment.LEFT);
			
			HBox box = new HBox(150, textFlowPane, textFlowPane1);
			
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

		List<Chapter> chapters = this.data.getChapters().getChapters();
		
		Collections.reverse(chapters);
	
		int index = 1;
		
		int length = 0;

			for(String i : javafx.scene.text.Font.getFamilies()) {
				
				JFXButton button = new JFXButton(i);
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
	
	
	
	/*
	@Override
	public javafx.scene.layout.Pane get() {
		
		VBox pane = new VBox();
		
		StackPane content = new StackPane();
		
		content.setPrefWidth(HosuClient.getHosuClient().getBody().getWidth() / 1.2);
		content.setPrefHeight(HosuClient.getHosuClient().getBody().getHeight() / 1.2);
		
		content.setMaxWidth(HosuClient.getHosuClient().getBody().getWidth() / 1.2);
		content.setMaxHeight(HosuClient.getHosuClient().getBody().getHeight() / 1.2);
		
		content.setId("background2");
		
		HosuButton minimize = new HosuButton(com.hosu.css.Image.RED_CIRCLE, com.hosu.css.Image.RED_CIRCLE, 16, (e) -> {
			HosuClient.getInstance().getPaneHandler().setActiveWithSearch(HosuClient.getInstance().getPaneHandler().getHosuBar().lastSet);
		});
		
		javafx.scene.layout.Pane exit = minimize.get();
		
		HBox contentPane = new HBox(25);
		
		ImageView view = new ImageView(image);
		
		
		javafx.scene.control.Label label = new javafx.scene.control.Label("Chapters: " + this.data.getChapters().size() + "\n" +
																		  "Authors: " + this.data.getAuthors() + "\n" +
																		  "Status: " + this.data.getStatus() + "\n" +
																		  "Views: " + this.data.getView());
		label.setFont(font);
		label.setTextFill(Color.WHITE);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.LEFT);
		
		
		GridPane grids = new GridPane();

		List<Chapter> chapters = this.data.getChapters();
		
		Collections.reverse(chapters);
	
		int index = 1;
		
		int length = 0;
		
		for(Chapter i : chapters) {
			
			String name = i.getName().split(">")[1];
			
			if(length < name.length()) {
				length = name.length();
			}
			
			JFXButton button = new JFXButton(name);
			button.setFont(font);
			button.setCursor(Cursor.HAND);
			
			final int temp = index;
			Platform.runLater(() -> {
				grids.add(button, 0, temp);
			});
			
			button.setId("button");
			
			button.setOnMousePressed(o -> {
				new Player(chapters, i.getChapter()).launch();
			});
			
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
		
		contentPane.getChildren().add(view);
		//contentPane.getChildren().add(label);
		contentPane.getChildren().add(scroll);
		
		contentPane.setMaxHeight(content.getMaxHeight() - exit.getMaxHeight());
		
		
		contentPane.setAlignment(Pos.CENTER);
		
		content.getChildren().add(exit);
		content.getChildren().add(contentPane);

		StackPane.setAlignment(exit, Pos.TOP_RIGHT);
		StackPane.setAlignment(content, Pos.BOTTOM_CENTER);
		
		
		pane.getChildren().add(content);
		
		pane.setMinSize(HosuClient.getHosuClient().getBody().getWidth(), HosuClient.getHosuClient().getBody().getHeight());
		
		pane.setId("background");
		
		pane.setAlignment(Pos.CENTER);
		
		return pane;
	}
	*/
	
	
	

}
