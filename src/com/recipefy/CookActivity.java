package com.recipefy;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.recipefy.cooking.CookingStep;
import com.recipefy.cooking.CookingStepsGenerator;
import com.recipefy.cooking.TimerData;
import com.recipefy.timer.Timer;
import com.recipefy.timer.TimerClickHandler;

public class CookActivity extends Activity {

	private List<CookingStep> _steps;
	private int _currStep;
	private Timer _timer1;
	private Timer _timer2;
	private Timer _timer3;
	private TimerData _timerdata1;
	private TimerData _timerdata2;
	private TimerData _timerdata3;
	
	// getting rid of the three dots
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		
		// set background
		findViewById(R.id.cookactivity).setBackgroundResource(R.drawable.background1);
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
				Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out);
		in.setDuration(1500);
		out.setDuration(500);
		final TextSwitcher text = (TextSwitcher) this.findViewById(R.id.stepsView);
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
		text.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
			TextView t = new TextView(CookActivity.this);
			t.setTextSize(16);
			t.setOnTouchListener(new TouchListener(text));
			t.setGravity(Gravity.CENTER);
			return t;
			}
			});
		text.setInAnimation(in);
		text.setOutAnimation(out);
		findViewById(R.id.cookactivity).setOnTouchListener(new TouchListener(text));
		text.setText(cookingsteps.get(0).getInstruction());
		//text.setOnTouchListener(new TouchListener(text));
		
		_timerdata1 = new TimerData(0,0);
		_timerdata2 = new TimerData(0,0);
		_timerdata3 = new TimerData(0,0);
		
		TextView leftTimer = (TextView) this.findViewById(R.id.timer1);
		TextView middleTimer = (TextView) this.findViewById(R.id.timer2);
		TextView rightTimer = (TextView) this.findViewById(R.id.timer3);
		
		_timer1 = new Timer(this, leftTimer, _timerdata1);
		_timer2 = new Timer(this, middleTimer, _timerdata2);
		_timer3 = new Timer(this, rightTimer, _timerdata3);
		
		_timer1.start();
		_timer2.start();
		_timer3.start();
		
		leftTimer.setOnClickListener(new TimerClickHandler(this, _timer1, leftTimer));
		middleTimer.setOnClickListener(new TimerClickHandler(this, _timer2, middleTimer));
		rightTimer.setOnClickListener(new TimerClickHandler(this, _timer3, rightTimer));

		changeStartButtons(0);


	}	
	
	public void startTimer1(View view){
		startTimer(_timerdata1);
	}
	
	public void startTimer2(View view){
		startTimer(_timerdata2);
	}
	
	public void startTimer3(View view){
		startTimer(_timerdata3);
	}

	public void startTimer(TimerData timerdata){
		TextView leftTimer = (TextView) this.findViewById(R.id.timer1);
		TextView middleTimer = (TextView) this.findViewById(R.id.timer2);
		TextView rightTimer = (TextView) this.findViewById(R.id.timer3);
		if(leftTimer.getText().equals(getResources().getString(R.string.defTimerString1))) {
			_timer1.setTimerdata(timerdata);
		} else if(middleTimer.getText().equals(getResources().getString(R.string.defTimerString2))) {
			_timer2.setTimerdata(timerdata);
		} else if(rightTimer.getText().equals(getResources().getString(R.string.defTimerString3))) {
			_timer3.setTimerdata(timerdata);
		} else {
			Toast toast = Toast.makeText(this, "Sorry, all timers are being used.", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private class TouchListener implements OnTouchListener {

		private TextSwitcher _view;

		public TouchListener(TextSwitcher text){
			_view = text;
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
				if(_currStep < _steps.size()-1){
					_currStep++;
				}
				_view.setText(_steps.get(_currStep).getInstruction());

			}

			changeStartButtons(_currStep);

			return false;
		}

	}
	
	private void changeStartButtons(int index){
		TextView start1 = (TextView) this.findViewById(R.id.startView1);
		TextView start2 = (TextView) this.findViewById(R.id.startView2);
		TextView start3 = (TextView) this.findViewById(R.id.startView3);
		start1.setVisibility(View.INVISIBLE);
		start2.setVisibility(View.INVISIBLE);
		start3.setVisibility(View.INVISIBLE);
		_timerdata1 = _timerdata2 = _timerdata3 = null;
		
		List<TimerData> timerList = _steps.get(_currStep).getTimers();
		for(TimerData timer:timerList){
			if(start1.getVisibility() == View.INVISIBLE){
				_timerdata1 = timer;
				start1.setText("Start"+" "+_timer1.getTimeString(timer.getInitialTime()));
				start1.setVisibility(View.VISIBLE);
			}
			else if(start2.getVisibility() == View.INVISIBLE){
				_timerdata2 = timer;
				start2.setText("Start"+" "+_timer2.getTimeString(timer.getInitialTime()));
				start2.setVisibility(View.VISIBLE);
			}
			else if(start3.getVisibility() == View.INVISIBLE){
				_timerdata3 = timer;
				start3.setText("Start"+" "+_timer3.getTimeString(timer.getInitialTime()));
				start3.setVisibility(View.VISIBLE);
			}
		}
	}

}
