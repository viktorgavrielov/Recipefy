package com.recipefy;

import java.util.ArrayList;
import java.util.List;

import com.recipefy.cooking.CookingStep;
import com.recipefy.cooking.CookingStepsGenerator;
import com.recipefy.cooking.TimerData;
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
		
		CookingStepsGenerator csgenerator = new CookingStepsGenerator(getAssets());
		
		// returns an arraylist
		List<CookingStep> cookingsteps = csgenerator.getCookingSteps("a dummy recipe id");
		
		// set the textview text to the first CookingStep instruction in the arraylist
		text.setText(cookingsteps.get(0).getInstruction());
		
		// start a timer with the first TimerData instance of the CookingStep
		Timer timer = new Timer(this, (Button) this.findViewById(R.id.timer1), cookingsteps.get(0).getTimers().get(0));
		timer.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cook, menu);
		return true;
	}

}
