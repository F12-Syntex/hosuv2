package com.hosu.mangaplayer;

import java.util.LinkedList;

import com.hosu.settings.Settings;
import com.hosu1.application.HosuClient;
import com.manga.data.ChapterHandler;
import com.manga.sorter.LinkedChapters;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Player {

	private int chapterNumber;
	private ChapterHandler chapters;
	
	private int pageNumber = 1;
	private boolean loading = false;
	
	LinkedList<Cashe> cashe = new LinkedList<>();

	private ImageView view;
	
	private LinkedChapters handler;
	
	private Stage stage;
	
	public Player(ChapterHandler chapter, int chapterNumber) {
		this.chapters = chapter;
		this.chapterNumber = chapterNumber;
	}

	public void launch() {
		
		StackPane root  = new StackPane();
		
		Image image = HosuClient.getInstance().getImageHandler().loading;
		
		this.view = new ImageView(image);
		
		view.setPreserveRatio(true);
		
		root.getChildren().add(view);
        this.stage = new Stage();
        
        if(chapters.getChapter(chapterNumber).getName().contains(">")) {
            stage.setTitle(chapters.getChapter(chapterNumber).getName().split(">")[1]);        	
        }else {
            stage.setTitle(chapters.getChapter(chapterNumber).getName());
        }

       
        double width = image.getWidth();
        double height = image.getHeight();
        
        if(image.getWidth() > Settings.SIZE.getWidth()) {
        	view.setFitWidth(Settings.SIZE.getWidth());
        	width = Settings.SIZE.getWidth();
        }
        
        if(image.getHeight() <= Settings.SIZE.getHeight()*1.5) {
        	view.setFitHeight(Settings.SIZE.getHeight());
        	height = Settings.SIZE.getHeight();
        }
        
               
		//ScrollPane scroallable = new ScrollPane(root);
		//this.modifyScrollBar(scroallable);
		//scroallable.setFitToHeight(true);
		//scroallable.setFitToWidth(true);
        
		ChangeListener<? super Number> listener = (a,b,c) -> {
        	view.setFitWidth(root.getWidth());
        	view.setFitHeight(root.getHeight());
		};
		
        //scroallable.heightProperty().addListener(listener);
        //scroallable.widthProperty().addListener(listener);
        root.heightProperty().addListener(listener);
        root.widthProperty().addListener(listener);
		
        
		root.setOnKeyPressed(i -> {
        	
			if(this.handler == null) return;
			
        	if(i.getCode() == KeyCode.F12) {
        		stage.setFullScreen(!stage.isFullScreen());
        	}
        	
			if(i.getCode() == KeyCode.RIGHT || i.getCode() == KeyCode.UP) {
				this.nextPage();
			}
			
			if(i.getCode() == KeyCode.LEFT || i.getCode() == KeyCode.DOWN) {
				this.prevPage();
			}
        	
        	System.out.println("key: " + i.getCode().name());
        });
        
		root.setOnScroll(i -> {
        	
        	//System.out.println(i.getDeltaY());
        	
        	//this.accessScrollBar(scroallable);
			
			if(this.handler == null) return;
			
			if(i.getDeltaY() < 0) {
				//System.out.println("nextPage");
				//this.nextPage();
				//this.scrollWheelToTop(scroallable);
			}
			if(i.getDeltaY() > 0) {
				//System.out.println("prevPage");
				//this.prevPage();
				//this.scrollWheelToBottom(scroallable);
			}
        	
        });
        
        
        root.setStyle("-fx-background-color: #0d101c");
       
		//scroallable.setMinWidth(Settings.SIZE.getWidth());
		
		//scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));
		
        stage.setScene(new Scene(root, width, height));
        stage.show();
        
        //Chapter firstChap = chapters.getChapter(chapterNumber);
        
        //this.page = firstChap.getPages().getPages().get(0);
        
        //this.loadChapters(firstChap);
		this.showPage();
		root.requestFocus();
	}
	
	public void nextPage() {
		if(loading) return;
		
		this.loading = true;
		this.pageNumber++;
		
		
		new Thread(() -> {
			if(this.handler.nextPageRequireDownload()) {
					this.buffer();
					if(this.handler.getNextPage() != null) {
						handler.getNextPage();
					}
					this.showPage();
					loading = false;
				return;
			}else {		
				if(this.handler.getNextPage() != null) {
					handler.getNextPage();
				}
				this.showPage();
				this.loading = false;
			}
		}).start();
		
	}
	
	public void prevPage() {
		if(loading) return;
		
		this.loading = true;
		
		this.pageNumber--;
		new Thread(() -> {
			if(this.handler.prevPageRequireDownload()) {
				new Thread(() -> {
					this.buffer();
					if(this.handler.getPrevPage() != null) {
						handler.getPrevPage();
					}
					this.showPage();
					loading = false;
				}).start();
				return;
			}else {
				if(this.handler.getPrevPage() != null) {
					handler.getPrevPage();
				}
				this.showPage();
				this.loading = false;
			}
		}).start();
	}
	
	
	public void showPage() {
		new Thread(() -> {
			
			if(this.handler == null) {
				this.view.setImage(HosuClient.getInstance().getImageHandler().loading);
				this.handler = new LinkedChapters(this.chapters.getChapter(chapterNumber), chapters);
				this.view.setImage(this.handler.getCurrentPage().getCashedImage());
				return;
			}
			
			this.view.setImage(HosuClient.getInstance().getImageHandler().loading);
			this.view.setImage(this.handler.getCurrentPage().getCashedImage());
			
			Platform.runLater(() -> {
		        if(chapters.getChapter(chapterNumber).getName().contains(">")) {
		            stage.setTitle(this.handler.getCurrentPage().getChapter().getName().split(">")[1] + " Page " + (this.handler.getCurrentPage().getPage().getPageNumber() - 1));        	
		        }else {
		            stage.setTitle(this.handler.getCurrentPage().getChapter().getName() + " Page " + (this.handler.getCurrentPage().getPage().getPageNumber() - 1));        	
		        }
			});
			
		}).start();
	}
	
	public void buffer() {
		new Thread(() -> {
			this.view.setImage(HosuClient.getInstance().getImageHandler().loading);
		}).start();
	}
	
	public void showPage1() {
		boolean found = false;
		for(Cashe page : this.cashe) {
			if(page.getPage().getPageNumber() == this.pageNumber) {
				found = true;
				Platform.runLater(() -> { 
					this.view.setImage(page.getImage());
					return;
				});
			}
		}
		if(!found) {
			if(this.pageNumber < 1) {
				this.chapterNumber-=1;
			}else {
				this.chapterNumber+=1;
			}
			
			this.cashe.clear();
			
			//this.page = chapters.getChapter(chapterNumber).getPages().getPages().get(0);
			
	        //this.loadChapters(chapters.getChapter(chapterNumber-1));
	        
			this.pageNumber = 1;
		}
	}
	
	
	/*
	public void loadChapters(Chapter chapter) {
		new Thread(() -> {
			/*
			List<LinkedPage> pages = chapter.getPages().getPages();
			for(LinkedPage page : pages) {
				new Thread(() -> {
					
					Image image = new Image(page.getImageURL());
					Cashe cashe = new Cashe(image, page);
					
					this.cashe.add(cashe);
					
					System.out.println("Added " + page.getPageNumber());
					if(page.getPageNumber() == 1) {
						this.view.setImage(image);
					}
					
					
					
					
					
				}).start();
			}
		
			
		Image image = new Image(page.getImageURL());
		this.view.setImage(image);
			
		}).start();
	}
	*/
	
	public int getChapterNumber() {
		return chapterNumber;
	}

	public void setChapterNumber(int chapterNumber) {
		this.chapterNumber = chapterNumber;
	}
	
	
	public static void scrollWheelToTop(ScrollPane scrollPane) {
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	scrollBar.setValue(scrollBar.getMin());
	            }

	        }
	    }
	}
	
	public static void scrollWheelToBottom(ScrollPane scrollPane) {
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	scrollBar.setValue(scrollBar.getMax());
	            }

	        }
	    }
	}
	
	public static void modifyScrollBar(ScrollPane scrollPane) {
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	System.out.println(scrollBar.heightProperty().get());
	            }
	            
	            double defaultUnitIncrement = scrollBar.getUnitIncrement();
	            scrollBar.setUnitIncrement(defaultUnitIncrement * 2);

	        }
	    }
	}
	
}
