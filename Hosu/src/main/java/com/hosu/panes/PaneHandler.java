package com.hosu.panes;

import com.hosu.application.HosuClient;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PaneHandler {
	
	private SearchableContent currentSearchablePane;
	
	private Search search;
	private HosuBar hosuBar;
	private MangaContent mangaContent;
	private MangaInfoPane mangaInfoPane;
	private HomePage home;
	
	private AniContentPane aniContentPane;
	private AniContentPaneManga mangaContentPane;
	private MalContent malContent;
	
	private StatsPage statsPage;
	
	public AniContentPane getAniContentPane() {
		return aniContentPane;
	}

	public void setAniContentPane(AniContentPane aniContentPane) {
		this.aniContentPane = aniContentPane;
	}

	public MalContent getMalContent() {
		return malContent;
	}

	public void setMalContent(MalContent malContent) {
		this.malContent = malContent;
	}

	public ObservableList<Node> getLast() {
		return last;
	}

	public void setLast(ObservableList<Node> last) {
		this.last = last;
	}

	private ObservableList<Node> last = null;

	public PaneHandler() {}

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

		this.search = new Search();
		this.hosuBar = new HosuBar();
		this.mangaContent = new MangaContent();
		this.mangaInfoPane = new MangaInfoPane();
		this.home = new HomePage();
		
		this.malContent = new MalContent();
		this.aniContentPane = new AniContentPane();
		this.mangaContentPane = new AniContentPaneManga();
		
		this.statsPage = new StatsPage();
		
		this.setDefaultActivePane();
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

	public AniContentPaneManga getMangaContentPane() {
		return mangaContentPane;
	}

	public void setMangaContentPane(AniContentPaneManga mangaContentPane) {
		this.mangaContentPane = mangaContentPane;
	}

	public StatsPage getStatsPage() {
		return statsPage;
	}

	public void setStatsPage(StatsPage statsPage) {
		this.statsPage = statsPage;
	}
	
}
