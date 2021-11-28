package com.hosu.css;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageHandler {
	
	private List<ImageStore> images;
	
	public Image loading;
	
	public Image loading1;
	public Image loading2;
	public Image loading3;
	public Image imageLoading;
	
	public void initialise() {
		
		this.loading = new Image(getImageURL(com.hosu.css.Image.LOADING));
		
		this.loading1 = new Image(getImageURL(com.hosu.css.Image.LOADING));
		this.imageLoading = new Image(getImageURL(com.hosu.css.Image.IMAGE_LOAD));
		this.loading2 = new Image(getImageURL(com.hosu.css.Image.LOADER2));
		this.loading3 = new Image(getImageURL(com.hosu.css.Image.LOADER3));
		
		images = new ArrayList<>();
		
		for(com.hosu.css.Image image : com.hosu.css.Image.values()) {
			new Thread(() -> {
				BufferedImage data = getImage(image);
				images.add(new ImageStore(convertToFxImage(data), image));
			}).start();
		}
	}
	
	public BufferedImage getImage(com.hosu.css.Image styling) {		
		try {
			return ImageIO.read(ImageHandler.class.getResource(styling.getResource()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Image getFXImage(com.hosu.css.Image styling) {		
		return new Image(ImageHandler.class.getResource(styling.getResource()).getPath());
	}
	
	public Image getFromCashe(com.hosu.css.Image styling) {
		Image data = images.stream().filter(i -> i.getId() == styling).findFirst().get().getImage();
		if(data == null) {
			return convertToFxImage(getImage(styling));
		}
		return data;
	}
	
	public String getImageURL(com.hosu.css.Image styling) {	
		return ImageHandler.class.getResource(styling.getResource()).toExternalForm();
	}
	
	private Image convertToFxImage(BufferedImage image) {
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
	
}
