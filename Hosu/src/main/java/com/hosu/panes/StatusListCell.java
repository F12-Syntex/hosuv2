package com.hosu.panes;

import java.lang.reflect.Constructor;

import com.hosu.application.HosuClient;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatusListCell extends ListCell<String> {
 protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        if(item!=null){
        	try {
            
	        	ImageView imageView = new ImageView(HosuClient.getInstance().getImageHandler().getFromCashe(com.hosu.css.Image.NSFW2));
	            imageView.setPreserveRatio(true);
	            
	            Domain domain = Domain.valueOf(item.toUpperCase().replace(" ", "_"));
	            
	            Constructor<?> constructor = domain.getSource().getDeclaredConstructor(String.class);
				Source invoke = (Source) constructor.newInstance("");
				
				if(invoke.nsfw()) {
					setGraphic(imageView);
					setContentDisplay(ContentDisplay.RIGHT);
				}
				
				
				
	            setText(item);
	            setFont(Font.font("ARIAL", FontWeight.BOLD, 16));
	            setTextFill(Color.WHITE);
	            setCursor(Cursor.HAND);
	            setAlignment(Pos.CENTER_LEFT);
	            
        	}catch (Throwable e) {
				// TODO: handle exception
			}
        }
    }
 
 
}