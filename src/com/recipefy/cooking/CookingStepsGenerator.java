package com.recipefy.cooking;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CookingStepsGenerator {

	private static final String RECIPE_FILE = "assets/recipes.txt";

	public List<CookingStep> getCookingSteps(String recipeID){

		List<CookingStep> cookingSteps = new ArrayList<CookingStep>();

		// get strings of cooking instrucitons based on recipeID
		// TODO: right now it only returns a random recipe from a text file
		List<String> rawInstructions = getInstructions(recipeID);
		
		// parse strings for potential timers and generate a list of 
		// instances of CookingStep
		cookingSteps = makeCookingSteps(rawInstructions);

		return cookingSteps;
	}

	/**
	 * USUALLY this would call the api and get the url of the recipe,
	 * then parse the corresponding web site to generate plain Strings 
	 * of recipe steps.
	 * Given that we are not parsing the website to get the recipes, we
	 * are randomly selecting a recipe from assets/recipes.txt
	 * @param recipeID
	 * @return
	 */
	private List<String> getInstructions(String recipeID){

		List<ArrayList<String>> recipes = new ArrayList<ArrayList<String>>();		

		try{

			InputStreamReader inputreader = new InputStreamReader(new FileInputStream(RECIPE_FILE));
			BufferedReader reader = new BufferedReader(inputreader);

			try{

				String line;
				while((line = reader.readLine()) != null){
					ArrayList<String> recipe = new ArrayList<String>();
					do
					{
						recipe.add(line);
						line = reader.readLine();
					}
					while(line != null && !line.equals(""));
					recipes.add(recipe);
				}

			} finally {
				reader.close();
			}


		}catch(FileNotFoundException e){
			System.err.println("FILE NOT FOUND: " + RECIPE_FILE);
		} catch (IOException e) {
			System.err.println("IO Error while reading " + RECIPE_FILE);
		}

		// pseudorandomly returning one of the recipes
		return recipes.size() > 0 ? recipes.get(Math.abs(((int) System.currentTimeMillis())) % (recipes.size())) : null;
	}

	/**
	 * Parses each cooking step and looks for timer times
	 * Makes a list of CookingSteps each containing the recipe
	 * step and a list of TimerData instances if timer times were found
	 * @param rawInstructions
	 * @return 
	 */
	private List<CookingStep> makeCookingSteps(List<String> rawInstructions){		
		List<CookingStep> cookingSteps = new ArrayList<CookingStep>();
		
		for(String instruction : rawInstructions){
			CookingStep step = new CookingStep();
			step.setInstruction(instruction);
			
			String[] words = instruction.split(" ");
			for(int i = 0; i < words.length; i++){
					
				if(words[i].contains("sec") || words[i].contains("min") || words[i].contains("hour")){	
					int unitmult = words[i].contains("min") ? 60 : words[i].contains("hour") ? 3600 : 1;
					try{
						// immediately before sec/min/hour, i.e. "8 minutes"
						int time = Integer.parseInt(words[i-1]);
						
						// 3 before sec/min/hour, i.e. "6 to 8 minutes"
						int snoozetime = Math.max((int) ((double) time *.15), 1);
						try{
							snoozetime = time - Integer.parseInt(words[i-3]);
							time = Integer.parseInt(words[i-3]);
						} catch(Exception e){}
						
						TimerData timerdata = new TimerData();
						timerdata.setInitialTime(time * unitmult);
						timerdata.setSnoozeTime(snoozetime * unitmult);
						step.addTimer(timerdata);
					} catch (Exception e){}
				}
			}
			cookingSteps.add(step);
		}
		
		return cookingSteps;
	}
	
	/**
	 * TESTING PURPOSE ONLY
	 * @param args
	 */
	public static void main(String[] args){
		CookingStepsGenerator gen = new CookingStepsGenerator();

		for(String step : gen.getInstructions("")){
			System.out.println(step);
		}
		
		System.out.println();
		
		for(CookingStep step : gen.getCookingSteps("")){
			System.out.println("STEP: " + step.getInstruction());
			for(TimerData timer : step.getTimers()){
				System.out.println(" Timer: " + timer.getInitialTime());
				System.out.println(" Timer: " + timer.getSnoozeTime());
			}
		}
	}
}
