package com.hosu.mangaplayer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.helpers.PanesHelper;
import com.hosu.settings.Settings;
import com.syntex.manga.models.Chapter;
import com.syntex.manga.queries.RequestMangaData;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Player {

	private final RequestMangaData manga;
	
	private DoublyLinkedList<Chapter> chapters = new DoublyLinkedList<>();
	
	private DoublyLinkedList<Chapter>.Node node;
	
	private int index = 0;
	
	private final ExecutorService forwardMiner = Executors.newFixedThreadPool(2);
	private final ExecutorService backwardsMiner = Executors.newFixedThreadPool(2);
	private final ExecutorService workerPool = Executors.newFixedThreadPool(2);
	
	private EventHandler<? super KeyEvent> keyListener;

	private boolean refreshing = false;
	
	private StackPane root;
	private ScrollPane scroallable;
	
	
	/* JAVAFX COMPONANTS */
	private Stage stage;
	private ImageView view;
	
	
	
	public Player(RequestMangaData manga) {
		this.manga = manga;
		
		for(Chapter i : manga.getChapters()) {
			chapters.add(i);
		}
		
	}
	
	/*
	 * Assume the first chapter
	 */
	public void play() {
		this.play(this.chapters.head().getData());
	}
	
	public void play(Chapter chapter) {
		
		//create UI
		this.launch();
	
		workerPool.execute(() -> {
			this.node = chapters.getNodes(chapter).get(0);
			
			//cash node
			this.node.data.casheImage();
			
			DoublyLinkedList<Chapter>.Node front = this.node;
			DoublyLinkedList<Chapter>.Node back = this.node;
			
			while(front.next != null) {
				//for each next node
				final DoublyLinkedList<Chapter>.Node value = front;
				this.forwardMiner.execute(() -> {
					value.data.cashe();
				});
				front = front.next;
			}
			
			while(back.prev != null) {
				//for each prev node
				final DoublyLinkedList<Chapter>.Node value = back;
				this.backwardsMiner.execute(() -> {
					value.data.cashe();
				});
				back = back.prev;
			}
		
			this.keyListener = i -> {
		    	
		    	if(i.getCode() == KeyCode.F12) {
		    		stage.setFullScreen(!stage.isFullScreen());
		    	}
		    	
				if(i.getCode() == KeyCode.RIGHT || i.getCode() == KeyCode.UP) {
					this.front();
				}
				
				if(i.getCode() == KeyCode.LEFT || i.getCode() == KeyCode.DOWN) {
					this.back();
				}
		    
			};
			
	        this.root.setOnKeyPressed(keyListener);
	        
	        this.root.setOnScroll(i -> {
				if(i.getDeltaY() < 0) {
					this.front();
				}
				if(i.getDeltaY() > 0) {
					this.back();
				}
			});
			
			this.refresh();
			
		});
		
		
	}
	
	private void launch() {
		
		this.root  = new StackPane();
		
		Image image = HosuClient.getInstance().getImageHandler().loading;
		
		this.view = new ImageView(image);	

		view.setPreserveRatio(true);
		
		//root.getChildren().add(view);
        this.stage = new Stage();
       
        double width = image.getWidth();
        double height = image.getHeight();
        
        if(image.getWidth() > Settings.SIZE.getWidth()) {
        	view.setFitWidth(Settings.SIZE.getWidth());
        	width = Settings.SIZE.getWidth();
        }
        
        /*
        if(image.getHeight() <= Settings.SIZE.getHeight()*1.5) {
        	view.setFitHeight(Settings.SIZE.getHeight());
        	height = Settings.SIZE.getHeight();
        }
        */
        
        ChangeListener<? super Number> listener = (a,b,c) -> {
        	this.setReadingView();
		};
		
        root.heightProperty().addListener(listener);
        root.widthProperty().addListener(listener);

		//this.root.getChildren().add(scroallable);
		this.root.getChildren().add(view);
		
		this.root.setId("contentPane");
		this.root.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
		this.root.applyCss();
		
		
		stage.setScene(new Scene(root, width, height));
        stage.setTitle(this.manga.getManga().getAlt());
        stage.show();

        this.root.requestFocus();
	}
	
	public void front() {
		
		//notify
		System.out.println("Going to the next page.");
		
		//check if node is still initialising
		if(this.node == null) return;
		
		//get pages
		List<String> pages = this.node.data.getPages();
		
		//increment the global counter
		this.index++;
		
		//check the current chapters pages has another page
		if(this.index >= pages.size()) {
			//if it does not have enough pages, either inc the chapter, or decrement the counter ( as there this is the last page of the series ).
			if(this.node.prev != null) {
				this.node = this.node.prev;
				//set the counter to 0, in order to continue to the next page, of the next chapter
				this.index = 0;
			}else {
				this.index--;
				//this.buffer();
				return;
			}
		}
		
		this.refresh();
		PanesHelper.scrollWheelToTop(this.scroallable);
	}
	
	public void back() {

		//notify
		System.out.println("Going to the prevouis page.");
				
		
		//check if node is still initialising
		if(this.node == null) return;
		
		//increment the global counter
		this.index--;
		
		//check if this is the first page.
		if(this.index <= 0) {
			//if it is the first page, then go back 1 chapter, otherwise stay at index 0.
			if(this.node.next != null) {			
				//set the counter to the last page of the prev chap, in order to continue to the next page, of the next chapter
				//chapter will already be cashed, or is in the process of cashing.
				this.index = this.node.next.data.getPages().size() - 1;
				this.node = this.node.next;
			}else {
				this.index = 0;
				//this.buffer();
				return;
			}
		}
		
		this.refresh();
		PanesHelper.scrollWheelToBottom(this.scroallable);
	}
	
	private void refresh() {
		
		if(!refreshing) {
			this.refreshing = true;
			System.out.println("loading page(" + this.index + ") chapter " + this.node.data.getName());
			
			this.buffer();
			this.node.data.casheImage();
		
			Platform.runLater(() -> {
				this.view.setImage(this.node.getData().getCashedImages().get(index));
				this.setReadingView();
				this.stage.setTitle(this.manga.getManga().getAlt() + " - " + this.node.getData().getName());
				System.out.println("page loaded.");
			});
			
			//cashe next and prev pages. and remove all other pages
			if(this.node.next != null) {
				workerPool.execute(() -> {			
					this.node.next.data.casheImage();
					this.node.next.data.freeMemory();
				});
			}
			if(this.node.prev != null) {
				workerPool.execute(() -> {			
					this.node.prev.data.casheImage();
					this.node.prev.data.freeMemory();
				});
			}
			this.refreshing = false;
		}
		
	}
	
	public void setReadingView() {
		
		
		if((this.view.getImage().getHeight()/this.view.getImage().getWidth()) > 2) {
			//manhua view.
			if(this.root.getChildren().contains(this.view)) {
				this.root.getChildren().remove(this.view);
				
				this.scroallable = new ScrollPane(this.view);
				scroallable.setFitToHeight(true);
				scroallable.setFitToWidth(true);
				scroallable.setMaxWidth(HosuClient.getInstance().getBody().getPrefWidth());
				scroallable.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.SCROLL_PANE));
				scroallable.applyCss();
				scroallable.setId("img");
				
				this.scroallable.setOnKeyPressed(keyListener);
				
				view.setFitWidth(view.getImage().getWidth());
				view.setFitHeight(view.getImage().getHeight());
				
				view.setPreserveRatio(true);
				
				this.root.getChildren().add(scroallable);
				
				PanesHelper.modifyScrollBar(scroallable, 12);
				this.scroallable.requestFocus();
			}
		}else {
			//manga view
	    	view.setFitHeight(root.getHeight());
			if(this.root.getChildren().contains(this.scroallable)){
				view.setPreserveRatio(true);
				this.root.getChildren().remove(this.scroallable);
				this.root.getChildren().add(view);
				this.root.requestFocus();
			}
		}
		System.out.println(this.view.getImage().getWidth() + "x" + this.view.getImage().getHeight());
		System.out.println(this.view.getImage().getWidth() + "x" + this.view.getImage().getHeight());
    	//view.setFitHeight(root.getHeight());
	}
	
	public void buffer() {
		this.view.setImage(HosuClient.getInstance().getImageHandler().loading);
	}
	
	public RequestMangaData getManga() {
		return manga;
	}
	
	
}
