package com.recipefy.timer;

import com.recipefy.CookActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;

public class Timer extends Thread{

	private int timeleft;
	private Button timerbutton;
	Activity cook;

	public Timer(Activity cook, Button timerbutton, int timeleft){
		this.cook = cook;
		this.timerbutton = timerbutton;
		this.timeleft = timeleft;
	}

	@Override
	public void run() {
		
		cook.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(cook, "Timer started!", Toast.LENGTH_SHORT);
				toast.show();
			}
		});

		while(timeleft > 0){
			int seconds = timeleft;
			int hours = seconds / 3600;
			seconds -= hours * 3600;
			int minutes = seconds / 60;
			seconds -= minutes * 60;

			String timeString = "";
			if(hours > 0){
				timeString += hours + ":";
			}
			if(minutes < 10){
				timeString += "0";
			}
			timeString += minutes + ":";
			if(seconds < 10){
				timeString += "0";
			}
			timeString += seconds;

			final String updateString = timeString;
			cook.runOnUiThread(new Runnable() {
				public void run() {
					timerbutton.setText(updateString);
				}
			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Timer interrupted!");
			}
			timeleft--;
		}
		
		

		// DIALOG AFTER TIME RAN OUT
		cook.runOnUiThread(new Runnable() {
			public void run() {
				timerbutton.setText("00:00");
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cook);

				// set title
				alertDialogBuilder.setTitle("Timer Alert");

				// set dialog message
				alertDialogBuilder
				.setMessage("A timer is up!")
				.setCancelable(false)				
				.setPositiveButton("Snooze 01:00",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						(new Timer(cook, timerbutton, 60)).start();
						dialog.cancel();
					}
				})
				.setNeutralButton("Snooze custom", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				})
				.setNegativeButton("Close",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// TODO: this should remove the button
						timerbutton.setEnabled(false);
						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		});

	}

}
