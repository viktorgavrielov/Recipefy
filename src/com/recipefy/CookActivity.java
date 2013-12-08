package com.recipefy;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.recipefy.cooking.CookingStep;
import com.recipefy.cooking.CookingStepsGenerator;
import com.recipefy.cooking.TimerData;
import com.recipefy.timer.Timer;

public class CookActivity extends Activity {
	
	private List<CookingStep> _steps;
	private int _currStep;
	private Timer _timer1;
	private Timer _timer2;
	private Timer _timer3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		TextView text = (TextView) this.findViewById(R.id.stepsView);
		//text.setText(Html.fromHtml("<b>Hello all</b> world!"));
		CookingStepsGenerator csgenerator = new CookingStepsGenerator(getAssets());
		// returns an arraylist
		List<CookingStep> cookingsteps = csgenerator.getCookingSteps("a dummy recipe id");
		while(cookingsteps.get(0).getTimers().size()==0){
			cookingsteps = csgenerator.getCookingSteps("a dummy recipe id");
		}
		_steps = cookingsteps;
		_currStep = 0;
		// set the textview text to the first CookingStep instruction in the arraylist
		text.setText(cookingsteps.get(0).getInstruction());
		text.setOnTouchListener(new TouchListener(text));
		// start a timer with the first TimerData instance of the CookingStep
		_timer1 = new Timer(this, (TextView) this.findViewById(R.id.timer2), cookingsteps.get(0).getTimers().get(0));
		if(cookingsteps.get(0).getTimers().size()>1){
			_timer2 = new Timer(this, (TextView) this.findViewById(R.id.timer1), cookingsteps.get(0).getTimers().get(1));
			TextView start2 = (TextView) findViewById(R.id.startView1);
			start2.setText("Start"+" "+_timer2.getTimeString(cookingsteps.get(0).getTimers().get(1).getInitialTime()));
			start2.setVisibility(View.VISIBLE);
			if(cookingsteps.get(0).getTimers().size()>2){
				_timer3 = new Timer(this, (TextView) this.findViewById(R.id.timer3), cookingsteps.get(0).getTimers().get(2));
				TextView start3 = (TextView) findViewById(R.id.startView3);
				start3.setText("Start"+" "+_timer3.getTimeString(cookingsteps.get(0).getTimers().get(2).getInitialTime()));
				start3.setVisibility(View.VISIBLE);
			}
		}
		System.out.println("Timer 1 is: "+_timer1);
		TextView start1 = (TextView) findViewById(R.id.startView2);
		start1.setText("Start"+" "+_timer1.getTimeString(cookingsteps.get(0).getTimers().get(0).getInitialTime()));
		//_timer1.start();
		
	}
	
	
	private class TouchListener implements OnTouchListener {
		
		private TextView _view;
		
		public TouchListener(TextView view){
			 _view = view;
		}


		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			Display display = getWindowManager().getDefaultDisplay();

			DisplayMetrics metrics = new DisplayMetrics();

			display.getMetrics(metrics);

			int width = metrics.widthPixels;

			if(arg1.getX() < width/2){

				if(_currStep>0){
					_currStep--;
				}
				_view.setText(_steps.get(_currStep).getInstruction());
				

			} else {
			if(_currStep<=_steps.size()-1){
				_currStep++;
			}
			_view.setText(_steps.get(_currStep).getInstruction());

			}
			
			List<TimerData> timerList = _steps.get(_currStep).getTimers();
			for(TimerData timer:timerList){
				if(_timer1==null){
					_timer1 =  new Timer(CookActivity.this, (TextView) CookActivity.this.findViewById(R.id.timer2), timer);
					TextView start1 = (TextView) findViewById(R.id.startView2);
					start1.setText("Start"+" "+_timer1.getTimeString(timer.getInitialTime()));
				}
				else if(_timer2==null){
					_timer2 = new Timer(CookActivity.this, (TextView) CookActivity.this.findViewById(R.id.timer1), timer);
					TextView start2 = (TextView) findViewById(R.id.startView1);
					start2.setText("Start"+" "+_timer2.getTimeString(timer.getInitialTime()));
					start2.setVisibility(View.VISIBLE);
					
				}
				else if(_timer3==null){
					_timer3 = new Timer(CookActivity.this, (TextView) CookActivity.this.findViewById(R.id.timer3), timer);
					TextView start3 = (TextView) findViewById(R.id.startView3);
					start3.setText("Start"+" "+_timer3.getTimeString(timer.getInitialTime()));
					start3.setVisibility(View.VISIBLE);
				}
			}

			return false;
		}

	}
	
	public void startTimer1(View view){
		if(_timer1!=null){
			_timer1.start();
		}
	}
	
	public void startTimer2(View view){
		if(_timer2!=null){
			_timer2.start();
		}
	}
	
	public void startTimer3(View view){
		if(_timer3!=null){
			_timer3.start();
		}
	}


}
