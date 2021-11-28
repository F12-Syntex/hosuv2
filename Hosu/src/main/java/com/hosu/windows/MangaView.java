package com.hosu.windows;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.panes.AniContentPaneManga;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.sandrohc.jikan.model.manga.Manga;

public class MangaView {
	
	public static void show(Manga manga) {

		AniContentPaneManga pane = HosuClient.getInstance().getPaneHandler().getMangaContentPane();
		pane.setMangaData(manga);
		
		VBox data = (VBox) pane.get();
		
		Stage stage = new Stage();
		Scene scene = new Scene(data);
		
		scene.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
		scene.setFill(Color.web("#181c2e"));
		
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		
	}
	
}

