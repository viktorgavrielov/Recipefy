package com.recipefy.recipegetter;

import java.util.List;

import android.graphics.Bitmap;

public class RecipeData {
	
	private Bitmap imageLarge;
	private Bitmap imageSmall;
	private List<String> ingredientsNeeded;
	private float rating;
	private int prepTimeSecs;
	private String recipeName;
	private String recipeID;
	
	
	public RecipeData(Bitmap imageLarge, Bitmap imageSmall, List<String> ingredientsNeeded,
			float rating, int prepTimeSecs, String recipeName, String recipeID) {
		this.imageLarge = imageLarge;
		this.imageSmall = imageSmall;
		this.ingredientsNeeded = ingredientsNeeded;
		this.rating = rating;
		this.prepTimeSecs = prepTimeSecs;
		this.recipeName = recipeName;
		this.recipeID = recipeID;
	}
	
	
	public RecipeData() {
	}


	public Bitmap getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(Bitmap imageLarge) {
		this.imageLarge = imageLarge;
	}

	public Bitmap getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(Bitmap imageSmall) {
		this.imageSmall = imageSmall;
	}

	public List<String> getIngredientsNeeded() {
		return ingredientsNeeded;
	}
	public void setIngredientsNeeded(List<String> ingredientsNeeded) {
		this.ingredientsNeeded = ingredientsNeeded;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public int getPrepTimeSecs() {
		return prepTimeSecs;
	}
	public void setPrepTimeSecs(int prepTimeSecs) {
		this.prepTimeSecs = prepTimeSecs;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String name) {
		this.recipeName = name;
	}

	public String getRecipeID() {
		return recipeID;
	}

	public void setRecipeID(String recipeID) {
		this.recipeID = recipeID;
	}
	

}
