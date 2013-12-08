package com.recipefy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.recipefy.recipegetter.RecipeData;
import com.recipefy.recipegetter.RecipeGenerator;
import com.recipefy.recipegetter.RecipeGenerator.FilterValue;

public class RecipeActivity extends Activity {
	
	 private GridView _gridView;
	 private GridViewAdapter _customGridAdapter;
	 private ArrayList<String> _ingredients;
	 protected RecipeActivity _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		_gridView = (GridView) findViewById(R.id.gridView);
        _customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, this.getData());
        _gridView.setAdapter(_customGridAdapter);
        _gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                int position, long id) {
            	System.out.println("Clicking");
                Toast.makeText(RecipeActivity.this, position + "#Selected",
                        Toast.LENGTH_SHORT).show();
            }
     
    });
        _ingredients = getIntent().getStringArrayListExtra("ingredients");
        System.out.println("List is: "+_ingredients);
        for(String item:_ingredients){
        	System.out.println("Ingredient is: "+item);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe, menu);
		return true;
	}
	
	private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        _ingredients = getIntent().getStringArrayListExtra("ingredients");
        RecipeGenerator generator = new RecipeGenerator();
        HashMap<FilterValue,List<String>> map = new HashMap<FilterValue,List<String>>();
        //for(String thing:_ingredients){
        	//System.out.println("Ingredients: "+thing);
        //}
        map.put(FilterValue.ALLOWED_INGREDIENT, _ingredients);
        List<String> testVals = map.get(FilterValue.ALLOWED_INGREDIENT);
        System.out.println("Testvals: "+testVals);
        for(String item:testVals){
        	System.out.println("My item is: "+item);
        }
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
	
		
        // retrieve String drawable array
       // TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
     //   for (int i = 0; i < imgs.length(); i++) {
            //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
           //         imgs.getResourceId(i, -1));
         //   imageItems.add(new ImageItem(bitmap, "Image#" + i));
       // }
        //Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chocolate);
        //Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fish);
        //Bitmap bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.crab);
        //Bitmap bitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.muffins);
        //imageItems.add(new ImageItem(bitmap1, "Chocolate Tart"));
        //imageItems.add(new ImageItem(bitmap2, "Fish Sticks"));
        //imageItems.add(new ImageItem(bitmap3, "Crab"));
        //imageItems.add(new ImageItem(bitmap4, "Muffins"));
        return imageItems;
 
    }
	

	
	

}
