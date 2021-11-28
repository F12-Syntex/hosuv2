package com.hosu.helpers;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

public class PanesHelper {

	public static void modifyScrollBar(ScrollPane scrollPane, int multiplier) {
		if(scrollPane == null) return;
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	System.out.println(scrollBar.heightProperty().get());
	            }
	            
	            scrollBar.setUnitIncrement(multiplier);

	        }
	    }
	}
	
	public static void scrollWheelToTop(ScrollPane scrollPane) {
		if(scrollPane == null) return;
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	scrollBar.setValue(scrollBar.getMin());
	            }

	        }
	    }
	}
	
	public static void scrollWheelToMiddle(ScrollPane scrollPane) {
		if(scrollPane == null) return;
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.HORIZONTAL) {
	            	scrollBar.setValue(scrollBar.getMax());
	            }

	        }
	    }
	}
	
	public static void scrollWheelToBottom(ScrollPane scrollPane) {
		if(scrollPane == null) return;
	    for (Node node : scrollPane.lookupAll(".scroll-bar")) {
	        if (node instanceof ScrollBar) {
	            ScrollBar scrollBar = (ScrollBar) node;
	            if (scrollBar.getOrientation() == Orientation.VERTICAL) {
	            	scrollBar.setValue(scrollBar.getMax());
	            }

	        }
	    }
	}

}
