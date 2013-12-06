package com.recipefy.cooking;

/**
 * time unit is seconds
 * @author viktorgavrielov
 *
 */
public class TimerData {
	private int initialTime;
	private int snoozeTime;
	
	public TimerData(){}
	
	public TimerData(int initialTime, int snoozeTime){
		this.initialTime = initialTime;
		this.snoozeTime = snoozeTime;
	}
	
	public int getInitialTime() {
		return initialTime;
	}
	public void setInitialTime(int initialTime) {
		this.initialTime = initialTime;
	}
	public int getSnoozeTime() {
		return snoozeTime;
	}
	public void setSnoozeTime(int snoozeTime) {
		this.snoozeTime = snoozeTime;
	}
}
