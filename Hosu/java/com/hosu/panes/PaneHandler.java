package com.hosu.panes;

import com.hosu1.application.HosuClient;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PaneHandler {
	
	private SearchableContent currentSearchablePane;
	
	private RedditContent redditContent;
	private Search search;
	private HosuBar hosuBar;
	private MangaContent mangaContent;
	private MangaInfoPane mangaInfoPane;
	private HomePage home;
	private SauceFinder sauceFinder;
	private MalContent malContent;
	
	private NMangaContent NmangaContent;
	private NMangaInfoPane NmangaInfoPane;
	
	private ObservableList<Node> last = null;

	public PaneHandler() {}
	
	public RedditContent getRedditContent() {
		return redditContent;
	}

	public MalContent getMalContent() {
		return malContent;
	}

	public void setMalContent(MalContent malContent) {
		this.malContent = malContent;
	}

	public void setRedditContent(RedditContent redditContent) {
		this.redditContent = redditContent;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public HosuBar getHosuBar() {
		return hosuBar;
	}
	
	public void setActive(Pane pane) {
		
		Platform.runLater(() -> {
		
			this.last = HosuClient.getInstance().getBody().getChildren();
			
			HosuClient.getInstance().getBody().getChildren().clear();
			HosuClient.getInstance().getBody().getChildren().add(pane.get());
			
			if(pane instanceof SearchableContent) {
				this.setCurrentSearchablePane((SearchableContent) pane);
			}
			
		});
		
	}
	
	public void setActive(javafx.scene.layout.Pane pane) {
		
		Platform.runLater(() -> {
		
			this.last = HosuClient.getInstance().getBody().getChildren();
			
			HosuClient.getInstance().getBody().getChildren().clear();
			HosuClient.getInstance().getBody().getChildren().add(pane);

		});
		
	}
	
	public void revert() {
		Platform.runLater(() -> {
			HosuClient.getInstance().getBody().getChildren().clear();
			HosuClient.getInstance().getBody().getChildren().addAll(last);	
		});
	}
	
	public void setActiveWithSearch(Pane pane) {
		Platform.runLater(() -> {
			
			HosuClient.getInstance().getBody().getChildren().clear();
			HosuClient.getInstance().getBody().getChildren().add(this.search.get());
			HosuClient.getInstance().getBody().getChildren().add(pane.get());
			
			if(pane instanceof SearchableContent) {
				this.setCurrentSearchablePane((SearchableContent) pane);
			}
			
			this.deselect(this.search.field);
			
		});
		
	}

	public void setHosuBar(HosuBar hosuBar) {
		this.hosuBar = hosuBar;
	}

	public void register() {

		this.redditContent = new RedditContent();
		this.search = new Search();
		this.hosuBar = new HosuBar();
		this.mangaContent = new MangaContent();
		this.mangaInfoPane = new MangaInfoPane();
		this.home = new HomePage();
		this.sauceFinder = new SauceFinder();
		
		this.NmangaContent = new NMangaContent();
		this.NmangaInfoPane = new NMangaInfoPane();
		
		this.malContent = new MalContent();
		
		this.setDefaultActivePane();
	}
	
	public SauceFinder getSauceFinder() {
		return sauceFinder;
	}

	public void setSauceFinder(SauceFinder sauceFinder) {
		this.sauceFinder = sauceFinder;
	}

	private void deselect(TextField textField) {
	    Platform.runLater(() -> {
	        if (textField.getText().length() > 0 &&
	                textField.selectionProperty().get().getEnd() == 0) {
	            deselect(textField);
	        }else{
	            textField.selectEnd();
	            textField.deselect();
	        }
	    });
	}
	
	public void setDefaultActivePane() {
		this.setCurrentSearchablePane(null);
	}

	public HomePage getHome() {
		return home;
	}

	public void setHome(HomePage home) {
		this.home = home;
	}

	public SearchableContent getCurrentSearchablePane() {
		return currentSearchablePane;
	}

	public void setCurrentSearchablePane(SearchableContent currentSearchablePane) {
		this.currentSearchablePane = currentSearchablePane;
	}
	
	public MangaContent getMangaContent() {
		return mangaContent;
	}

	public void setMangaContent(MangaContent mangaContent) {
		this.mangaContent = mangaContent;
	}

	public MangaInfoPane getMangaInfoPane() {
		return mangaInfoPane;
	}

	public void setMangaInfoPane(MangaInfoPane mangaInfoPane) {
		this.mangaInfoPane = mangaInfoPane;
	}
		
	public NMangaContent getNmangaContent() {
		return NmangaContent;
	}

	public void setNmangaContent(NMangaContent nmangaContent) {
		NmangaContent = nmangaContent;
	}

	public NMangaInfoPane getNmangaInfoPane() {
		return NmangaInfoPane;
	}

	public void setNmangaInfoPane(NMangaInfoPane nmangaInfoPane) {
		NmangaInfoPane = nmangaInfoPane;
	}

	
}
