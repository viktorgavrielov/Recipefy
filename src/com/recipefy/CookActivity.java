package com.recipefy;

import com.recipefy.timer.Timer;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class CookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		TextView text = (TextView) this.findViewById(R.id.cooking_step);
		text.setText(Html.fromHtml("<b>Hello all</b> world!"));
		
		Timer timer = new Timer(this, (Button) this.findViewById(R.id.timer1), 15);
		timer.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cook, menu);
		return true;
	}

}
