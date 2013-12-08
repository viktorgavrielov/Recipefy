package com.recipefy;

import java.io.Serializable;
import java.util.List;

public class ImageItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] _image;
    private String _title;
    private float _rating;
    private int _time;
    private String _match;
    private List<String> _ingredients;
    private List<String> _currIngredients;
 
  
	public ImageItem(byte[] image, String title) {
        super();
        _image = image;
        _title = title;
    }
 
    public byte[] getImage() {
        return _image;
    }
 
    public void setImage(byte[] image) {
        _image = image;
    }
 
    public String getTitle() {
        return _title;
    }
 
    public void setTitle(String title) {
        _title = title;
    }
    
    public void setRating(float rating){
    	_rating = rating;
    }
    
    public float getRating(){
    	return _rating;
    }
    
    public void setTime(int time){
    	_time = time;
    }
    
    public int getTime(){
    	return _time;
    }
    
    public void setMatch(String match){
    	_match = match;
    }
    
    public String getMatch(){
    	return _match;
    }
    
    public void setIngredients(List<String> ing){
    	_ingredients = ing;
    }
    
    public List<String> getIngredients(){
    	return _ingredients;
    }
    
    public void setCurrIngredients(List<String> ing){
    	_currIngredients = ing;
    }
    
    public List<String> getCurrIngredients(){
    	return _currIngredients;
    }

	
}
