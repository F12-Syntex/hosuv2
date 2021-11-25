module ASDF {
	requires java.base;
	requires java.desktop;
	requires javafx.graphics;
	requires reflections;
	requires com.jfoenix;
	requires eu.hansolo.medusa;
	requires fontawesomefx;
	requires AnimateFX;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires com.google.gson;
	requires org.json;
	requires jdk.compiler;
	requires javafx.web;
	requires java.management;
	requires javafx.media;
	requires okhttp3;
	
	opens com.hosu1.application to javafx.graphics, javafx.fxml, com.jfoenix, javafx.media;
	opens com.hosu.panes to javafx.graphics, javafx.fxml, com.jfoenix, javafx.media;

	
	
}

//module java.base does not "opens java.lang.reflect" to module com.jfoenix