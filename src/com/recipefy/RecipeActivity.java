package com.recipefy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

import com.recipefy.recipegetter.RecipeData;
import com.recipefy.recipegetter.RecipeGenerator;
import com.recipefy.recipegetter.RecipeGenerator.FilterValue;

public class RecipeActivity extends Activity {

	private GridView _gridView;
	private GridViewAdapter _customGridAdapter;
	private ArrayList<String> _ingredients;
	protected RecipeActivity _this;
	private ProgressDialog _dialog;


	// getting rid of the three dots
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return false;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);

		//set background
		findViewById(R.id.recipeactivity).setBackgroundResource(R.drawable.background1);

		_gridView = (GridView) findViewById(R.id.gridView);
		_dialog = new ProgressDialog(this);
		new getRecipesTask().execute(1);
		//_customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, this.getData());
		//_gridView.setAdapter(_customGridAdapter);
		//_ingredients = getIntent().getStringArrayListExtra("ingredients");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe, menu);
		return true;
	}
	
	public void asyncCall(ArrayList<ImageItem> items){
		_customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, items, _gridView);
		_gridView.setAdapter(_customGridAdapter);
		_customGridAdapter.sort(new Comparator<ImageItem>(){

			@Override
			public int compare(ImageItem item1, ImageItem item2) {
				//return new Float(item1.getRating()).compareTo(item2.getRating());
				if(item1.getRating()<item2.getRating()){
					return 1;
				}
				else if(item1.getRating()>item2.getRating()){
					return -1;
				}
				else{
					return 0;
				}
			}
			
		});
		_customGridAdapter.notifyDataSetChanged();
		//_customGridAdapter.fixOrder();
	}

	

	private class getRecipesTask extends AsyncTask<Integer, String, ArrayList<ImageItem>> {
		
		protected void onPreExecute(){
			_dialog.setMessage("Your Suggestions Are Loading");
			_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			_dialog.setCancelable(false);
			_dialog.show();
		}
		
	    protected ArrayList<ImageItem> doInBackground(Integer... urls) {
	     	final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
			_ingredients = getIntent().getStringArrayListExtra("ingredients");
			RecipeGenerator generator = new RecipeGenerator();
			HashMap<FilterValue,List<String>> map = new HashMap<FilterValue,List<String>>();
			map.put(FilterValue.ALLOWED_INGREDIENT, _ingredients);
			try {
				List<RecipeData> data =generator.getRecipes(map);
				System.out.println("DATA SIZE: " + data.size());
				for(RecipeData recipe:data){
					if(recipe.getPrepTimeSecs()!=0&&recipe.getRating()!=0.0f){
						Bitmap image = recipe.getImageSmall();
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						image.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();
						ImageItem item = new ImageItem(byteArray,recipe.getRecipeName());
						item.setRating(recipe.getRating());
						item.setTime(recipe.getPrepTimeSecs()/60);
						float length = (float)_ingredients.size()/(float)recipe.getIngredientsNeeded().size();
						int adjLength = (int) Math.ceil(length*100);
						item.setMatch(adjLength+"%");
						item.setIngredients(recipe.getIngredientsNeeded());
						item.setCurrIngredients(_ingredients);
						imageItems.add(item);
					}
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	         return imageItems;
	     }

	   
	     protected void onPostExecute(ArrayList<ImageItem> result) {
	        RecipeActivity.this.asyncCall(result);
	        _dialog.dismiss();
	     }
	 }

}
