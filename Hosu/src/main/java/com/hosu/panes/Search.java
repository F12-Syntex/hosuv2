package com.hosu.panes;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.hosu.application.HosuClient;
import com.hosu.helpers.StringBeautifier;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.Domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class Search extends Pane{

	public AutoCompleteTextField field;
	public Font font;
	public ComboBox<String> comboBox;
	public Domain selection = Domain.MANGA_DEX;
	//public String lastSelection = "";
	
	public Search() {
		this.font = new Font("SERIF", 16);
	}
	
	@Override
	public javafx.scene.layout.Pane get() {
	
		StackPane container = new StackPane();
		
		StackPane pane = new StackPane();
		
		double width = HosuClient.getInstance().getBody().getPrefWidth() / 2;
		double height = HosuClient.getInstance().getBody().getPrefHeight() / 15;

		//this.applySizeToNodeExact1(pane, width, height);
		
		pane.setPrefSize(width, height);
		pane.setMinSize(width, height);
		pane.setMaxSize(width, height);
		
		SortedSet<String> mangaQueries = new TreeSet<>();
		
		for(RequestQueryResults i : HosuClient.database.getCashedQueryResults()) {
			for(QueriedEntity o : i.getMangas()) {
				mangaQueries.add(o.getAlt());
			}
		}
		
		this.field = new AutoCompleteTextField(mangaQueries);
		
		field.setPromptText("Search...");
		field.setFont(font);
		
		pane.setId("search");
		
		field.setOnKeyPressed((e) -> {
			if(e.getCode() == KeyCode.ENTER) {
				SearchableContent searchNode = HosuClient.getInstance().getPaneHandler().getCurrentSearchablePane();
				//this.lastSelection = field.getText();
				searchNode.onSearch(StringUtils.capitalize(field.getText()));
				System.out.println("Searching for " + field.getText() + " in for node " + searchNode.getClass().getName());
			}
		});
		
		Label label = new Label();
		label.setText("Search");
		label.setFont(font);
		label.setPrefWidth(width/8);
		
		List<String> values = new ArrayList<>();
		
		for(Domain domain : Domain.values()) {
			values.add(StringBeautifier.beautifier(domain.name()));
		}
		
		ObservableList<String> options = FXCollections.observableArrayList();
		options.addAll(values);
		
		if(this.comboBox != null) {
			this.selection = Domain.valueOf(this.comboBox.getValue().toUpperCase().replace(" ", "_"));
		}
		
		this.comboBox = new ComboBox<>(options);
		comboBox.setCellFactory(c -> new StatusListCell());
		
		comboBox.setId("combo");
		
		comboBox.setMinSize(width/3, height/2 + height/3);
		comboBox.setMaxSize(width/3, height/2 + height/3);
		comboBox.setValue(StringBeautifier.beautifier(selection.name()));
		comboBox.setCursor(Cursor.HAND);
		
		pane.getChildren().add(field);
		
		HBox box = new HBox();
		//box.getChildren().add(label);
		
		box.getChildren().add(comboBox);
		box.getChildren().add(pane);
		
		box.setAlignment(Pos.CENTER_LEFT);
		
		box.setAlignment(Pos.CENTER);
		
		container.getChildren().add(box);
		
		StackPane.setAlignment(pane, Pos.CENTER);
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		
		//System.out.println("searching for " + this.field.getText());
		//HosuClient.getInstance().getPaneHandler().getMangaContent().lastSearched = this.field.getText();
		
		return container;
	}
	
}
