package com.recipefy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

public class RecipeActivity extends Activity {
	
	 private GridView _gridView;
	 private GridViewAdapter _customGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		_gridView = (GridView) findViewById(R.id.gridView);
        _customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, this.getData());
        _gridView.setAdapter(_customGridAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe, menu);
		return true;
	}
	
	private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        // retrieve String drawable array
       // TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
     //   for (int i = 0; i < imgs.length(); i++) {
            //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
           //         imgs.getResourceId(i, -1));
         //   imageItems.add(new ImageItem(bitmap, "Image#" + i));
       // }
        Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chocolate);
        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fish);
        Bitmap bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.crab);
        Bitmap bitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.muffins);
        imageItems.add(new ImageItem(bitmap1, "Chocolate Tart"));
        imageItems.add(new ImageItem(bitmap2, "Fish Sticks"));
        imageItems.add(new ImageItem(bitmap3, "Crab"));
        imageItems.add(new ImageItem(bitmap4, "Muffins"));
        return imageItems;
 
    }
	
	

}
