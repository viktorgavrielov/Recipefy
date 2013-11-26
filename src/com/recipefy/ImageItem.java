package com.recipefy;

import android.graphics.Bitmap;

public class ImageItem {
	private Bitmap _image;
    private String _title;
 
    public ImageItem(Bitmap image, String title) {
        super();
        _image = image;
        _title = title;
    }
 
    public Bitmap getImage() {
        return _image;
    }
 
    public void setImage(Bitmap image) {
        _image = image;
    }
 
    public String getTitle() {
        return _title;
    }
 
    public void setTitle(String title) {
        _title = title;
    }
}
