package com.recipefy.timer;



import com.recipefy.cooking.TimerData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Timer extends Thread{

	private TimerData timerdata;
	private TextView timerbutton;
	Activity cook;
	private boolean isPaused;

	public Timer(Activity cook, TextView timerbutton, TimerData timerdata){
		this.timerdata = timerdata;
		this.timerbutton = timerbutton;
		this.cook = cook;	
		this.isPaused = false;
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
	public void run(){

		while(true){

			int timeleft;

			while((timeleft = timerdata.getInitialTime()) == 0){
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {}
			}
			// show toaster
			cook.runOnUiThread(new Runnable() {
				public void run() {
					Toast toast = Toast.makeText(cook, "Timer started!", Toast.LENGTH_SHORT);
					toast.show();
				}
			});



			while(timeleft > 0 && timerdata.getInitialTime() != 0){

				if(!isPaused){
					String timeString = getTimeString(timeleft);

					final String updateString = timeString;
					cook.runOnUiThread(new Runnable() {
						public void run() {
							timerbutton.setText(updateString);
						}
					});
					timeleft--;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}			
			}


			// DIALOG AFTER TIME RAN OUT
			if(timerdata.getInitialTime() != 0){
				timerdata.setInitialTime(0);
				showTimeUpDialog();
			}
		}

	}

	private void showTimeUpDialog(){
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
						int oldSnoozeTime = timerdata.getSnoozeTime();
						timerdata.setSnoozeTime((int) ((double) oldSnoozeTime/10));
						timerdata.setInitialTime(oldSnoozeTime);
						//timerbutton.setOnClickListener(new TimerClickHandler(cook, timer, timerbutton));
						dialog.cancel();
					}
				})
				.setNeutralButton("Snooze custom", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						AlertDialog.Builder timepickerDialogBuilder = numpickerAlertGenerator(Timer.this);
						AlertDialog timepickerdialog = timepickerDialogBuilder.create();
						timepickerdialog.show();				

					}
				})
				.setNegativeButton("Close",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						timerbutton.setText("--:--");
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

	public AlertDialog.Builder numpickerAlertGenerator (final Timer timer){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cook);

		final TimePicker timepicker = new TimePicker(cook);
		timepicker.setIs24HourView(true);
		timepicker.setCurrentHour(timerdata.getSnoozeTime() / 3600);
		timepicker.setCurrentMinute((timerdata.getSnoozeTime() - timepicker.getCurrentHour() * 3600) / 60);

		// set title
		alertDialogBuilder.setTitle("Pick hours and/or minutes!");

		// set dialog message
		alertDialogBuilder
		.setView(timepicker)
		.setCancelable(false)				
		.setPositiveButton("Set Timer",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				int seconds = timepicker.getCurrentHour() * 3600 + timepicker.getCurrentMinute() * 60;
				dialog.cancel();
				if(seconds > 0){
					timerdata.setSnoozeTime((int)((double) seconds/10));
					timerdata.setInitialTime(seconds);
					//Timer timer = new Timer(cook, timerbutton, new TimerData(seconds,(int) ((double) seconds/10)));
					//timerbutton.setOnClickListener(new TimerClickHandler(cook, timer, timerbutton));
					//timer.start();
				}				
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				if(timerbutton.getText().equals("--:--") || timerbutton.getText().equals("-- : --")){
					return;
				}
				timer.showTimeUpDialog();
			}
		});



		return alertDialogBuilder;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public TimerData getTimerdata() {
		return timerdata;
	}

	public void setTimerdata(TimerData timerdata) {
		this.timerdata = timerdata;
	}
}
