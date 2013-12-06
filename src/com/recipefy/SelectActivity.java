package com.recipefy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class SelectActivity extends Activity {
	
	private ImageItem _item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		_item = (ImageItem) getIntent().getSerializableExtra("item");
		ImageView view = (ImageView) this.findViewById(R.id.bigPicture);
		view.setImageBitmap(BitmapFactory.decodeByteArray(_item.getImage(), 0, _item.getImage().length));
		view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SelectActivity.this.cook();
                }
            });
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_item.getIngredients());
		ListView listView = (ListView) findViewById(R.id.matchlist);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}
	
	public void cook(){
		Intent intent = new Intent(this,CookActivity.class);
		this.startActivity(intent);
	}

}
