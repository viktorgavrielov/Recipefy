package com.recipefy.timer;

import com.recipefy.CookActivity;
import com.recipefy.cooking.TimerData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends Thread{

	private TimerData timerdata;
	private TextView timerbutton;
	Activity cook;

	public Timer(Activity cook, TextView textView, TimerData timerdata){
		this.timerdata = timerdata;
		this.timerbutton = textView;
		this.cook = cook;		
	}
	
	
	/**
	 * Returns a time string that is built up from the seconds passed in
	 * i.e. 75 seconds ==> 01:15
	 * @param seconds
	 * @return
	 */
	public String getTimeString(int seconds){
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
		return timeString;
	}
	
	
	@Override
	public void run() {
		
		cook.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(cook, "Timer started!", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		int timeleft = timerdata.getInitialTime();

		while(timeleft > 0){
			
			String timeString = getTimeString(timeleft);

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
				.setPositiveButton("Snooze " + getTimeString(timerdata.getSnoozeTime()),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						(new Timer(cook, timerbutton, new TimerData(timerdata.getSnoozeTime(),(int) ((double) timerdata.getSnoozeTime()*.15)))).start();
						dialog.cancel();
					}
				})
				.setNeutralButton("Snooze custom", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// TODO: this should open the custom timer dialog
						// TODO: and also remove the current button
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
