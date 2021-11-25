package com.hosu.helpers;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.stage.Screen;

public class Windows {

	private static final Dimension relative = new Dimension(1920, 1080);
	
	public static Dimension getScreenDimentions() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static Dimension getScaledDimentions(int scaleW, int scaleH) {
		
		Dimension dimentions = Windows.getScreenDimentions();
		
		double width = dimentions.getWidth();
		double height = dimentions.getHeight(); 
		
		double diffW = (scaleW / 1920.0) * width;
		double diffH = (scaleH / 1080.0) * height;
		
		System.out.println(scaleW + "x" + scaleH);
		System.out.println(diffW + "x" + diffH + " " + dimentions.getWidth() + "x" + dimentions.getHeight());
		
		return new Dimension((int)diffW, (int)diffH);
		
	}
	
	public static Dimension getRelate(double d, double e) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		System.out.println(screenSize.getWidth());
		
		double newWidth = Math.round((screenSize.getWidth()/relative.getWidth())*d);
		double newHeight = Math.round((screenSize.getHeight()/relative.getHeight())*e);
		
		Dimension result = new Dimension((int)newWidth, (int)newHeight);
		
		return result;
		
	}
	
	 public static Bounds computeAllScreenBounds() {
	        double minX = Double.POSITIVE_INFINITY;
	        double minY = Double.POSITIVE_INFINITY;
	        double maxX = Double.NEGATIVE_INFINITY;
	        double maxY = Double.NEGATIVE_INFINITY;
	        for (Screen screen : Screen.getScreens()) {
	            javafx.geometry.Rectangle2D screenBounds = screen.getBounds();
	            if (screenBounds.getMinX() < minX) {
	                minX = screenBounds.getMinX();
	            }
	            if (screenBounds.getMinY() < minY) {
	                minY = screenBounds.getMinY();
	            }
	            if (screenBounds.getMaxX() > maxX) {
	                maxX = screenBounds.getMaxX();
	            }
	            if (screenBounds.getMaxY() > maxY) {
	                maxY = screenBounds.getMaxY();
	            }
	        }
	        return new BoundingBox(minX, minY, maxX-minX, maxY-minY);
	    }
	
}
