package com.recipefy.timer;

import com.recipefy.cooking.TimerData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

public class TimerClickHandler implements View.OnClickListener{
	
	
	
	Activity cook;
	Timer timer;
	TextView timerText;
	
	public TimerClickHandler(Activity cook, Timer timer, TextView timerText){
		this.cook = cook;
		this.timer = timer;
		this.timerText = timerText;
	}

	@Override
	public void onClick(View arg0) {
		
		if(timerText.getText().equals("--:--") || timerText.getText().equals("-- : --")){
			// create alert dialog builder
			AlertDialog.Builder alertDialogBuilder = timer.numpickerAlertGenerator(timer);
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
			return;
		}
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cook);

		// set title
		alertDialogBuilder.setTitle("Timer");

		// set dialog message
		alertDialogBuilder
		.setMessage("Would you like to remove or " + (timer.isPaused() ? "resume" : "pause") + " the timer?")
		.setCancelable(false)				
		.setPositiveButton("Remove",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				timerText.setText("--:--");
				timer.setPaused(false);
				timer.setTimerdata(new TimerData(0,0));
				dialog.cancel();
			}
		})
		.setNeutralButton("Nevermind!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		})
		.setNegativeButton((timer.isPaused() ? "Resume" : "Pause"),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				timer.setPaused(!timer.isPaused());
				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
}
