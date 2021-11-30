package com.hosu.panes;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.settings.Settings;

import javafx.scene.layout.StackPane;

public class SettingsPane extends Pane {
	

	@Override
	public javafx.scene.layout.Pane get() {

		int size = 2;
		
		StackPane test = new StackPane();
		test.setMinSize(Settings.SIZE.getWidth() / 2, Settings.SIZE.getHeight() / 2);
		test.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        test.setId("side");
        
		return test;
	}

	
}
