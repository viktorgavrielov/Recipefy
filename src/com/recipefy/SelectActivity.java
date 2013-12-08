package com.recipefy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectActivity extends Activity {
	
	private ImageItem _item;
	List<CharSequence> _toPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		_toPass = new ArrayList<CharSequence>();
		_item = (ImageItem) getIntent().getSerializableExtra("item");
		ImageView view = (ImageView) this.findViewById(R.id.bigPicture);
		view.setImageBitmap(BitmapFactory.decodeByteArray(_item.getImage(), 0, _item.getImage().length));
		TextView title = (TextView) this.findViewById(R.id.recipeTitle);
		String toSet = "<b>"+_item.getTitle()+"</b>";
		title.setText(Html.fromHtml(toSet));
		view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SelectActivity.this.cook();
                }
            });
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_list_item_1,_toPass);
		ListView listView = (ListView) findViewById(R.id.matchlist);
		listView.setAdapter(adapter);
		System.out.println("Size of curr ingred is: "+_item.getCurrIngredients().size());
		for(String item:_item.getIngredients()){
			for(String food: _item.getCurrIngredients()){
				if(food.contains(item)){
					System.out.println("bold item");
					String toAdd = "<b>"+item+"</b>";
					adapter.add(Html.fromHtml(toAdd));
				}
				else{
					adapter.add(item);
				}
			}
		}
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
