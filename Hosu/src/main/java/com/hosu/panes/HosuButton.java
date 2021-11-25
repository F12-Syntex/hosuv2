package com.hosu.panes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.hosu.application.HosuClient;
import com.hosu.css.Image;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HosuButton extends Pane {

	private Image image;
	private Image onHover;
	private int size;
	private javafx.scene.layout.Pane root;
	
	public ImageView content = new ImageView();
	
	private BufferedImage normal;
	private BufferedImage hover;
	
	public Color highlightedColour = null;
	
	private boolean toggle = false;
	
	private EventHandler<? super MouseEvent> onClick;
	
	boolean clicked = false;
	
	public HosuButton(Image image, Image onHover, int size, EventHandler<? super MouseEvent> onClick) {
		this.image = image;
		this.onHover = onHover;
		this.size = size;
		
		this.normal = resize(HosuClient.getInstance().getImageHandler().getImage(image), size, size);
		this.hover = resize(HosuClient.getInstance().getImageHandler().getImage(onHover), size, size);
		
		this.onClick = onClick;
	}
	
	
	
	public void swap() {
		BufferedImage temp = this.normal;
		this.normal = this.hover;
		this.hover = temp;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	public boolean getClicked() {
		return this.clicked;
	}
	
	public HosuButton setListener(EventHandler<? super MouseEvent> onClick) {
		this.onClick = onClick;
		return this;
	}
	
	public HosuButton setHighlightedColour(Color color) {
		this.highlightedColour = color;
		return this;
	}
	
	public HosuButton makeToggle(boolean toggle) {
		this.toggle = toggle;
		return this;
	}
	
	public void setHoverContent(Image image) {
		this.content.setImage(this.convertToFxImage(resize(HosuClient.getInstance().getImageHandler().getImage(image), this.size, this.size)));
	}
	public void setNormalContent(Image image) {
		this.content.setImage(this.convertToFxImage(resize(HosuClient.getInstance().getImageHandler().getImage(image), this.size, this.size)));
	}

	@Override
	public javafx.scene.layout.Pane get() {
		
		this.setRoot(root);
		
		this.content.setImage(this.convertToFxImage(normal));
		
		StackPane pane = new StackPane();
		
		pane.setOnMouseEntered((e) -> {
			this.content.setImage(this.convertToFxImage(hover));
		});
		
		pane.setOnMouseExited((e) -> {
			this.content.setImage(this.convertToFxImage(normal));
		});
		
		pane.setOnMousePressed(onClick);
		
		pane.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			this.clicked = !this.clicked;
			if(this.toggle) {
				this.swap();
			}
		});
		
		pane.setCursor(Cursor.HAND);
		
		pane.getChildren().add(this.content);

		pane.setMinSize(size + size/2, size + size/2);
		pane.setMaxSize(size + size/2, size + size/2);
		
		return pane;
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Image getOnHover() {
		return onHover;
	}
	public void setOnHover(Image onHover) {
		this.onHover = onHover;
	}	
	
	public void refreshImages() {
		this.normal = resize(HosuClient.getInstance().getImageHandler().getImage(image), size, size);
		this.hover = resize(HosuClient.getInstance().getImageHandler().getImage(onHover), size, size);
	}
	
	private javafx.scene.image.Image convertToFxImage(BufferedImage image) {
	    WritableImage wr = null;
	    if (image != null) {
	        wr = new WritableImage(image.getWidth(), image.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < image.getWidth(); x++) {
	            for (int y = 0; y < image.getHeight(); y++) {
	                pw.setArgb(x, y, image.getRGB(x, y));
	            }
	        }
	    }

	    return new ImageView(wr).getImage();
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
	    int w = img.getWidth();  
	    int h = img.getHeight();  
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;  
	}


	public javafx.scene.layout.Pane getRoot() {
		return root;
	}


	public void setRoot(javafx.scene.layout.Pane root) {
		this.root = root;
	} 
	
}
