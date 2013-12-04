package com.recipefy.cooking;

import java.util.ArrayList;
import java.util.List;

public class CookingStep {
	
	private String instruction;
	private List<TimerData> timers;
	
	public CookingStep(){
		timers = new ArrayList<TimerData>();
	}
	
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public List<TimerData> getTimers() {
		return timers;
	}

	public void addTimer(TimerData timer) {
		this.timers.add(timer);
	}

	
	
	

}
