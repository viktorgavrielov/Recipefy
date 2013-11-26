package com.recipefy.recipegetter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeGenerator {
	
	public static enum FilterValue {ALLOWED_INGREDIENT, EXCLUDED_INGREDIENT, ALLOWED_ALLERGY, ALLOWED_DIET,
		ALLOWED_CUISINE, EXCLUDED_CUISINE, ALLOWED_COURSE, EXCLUDED_COURSE, ALLOWED_HOLIDAY, EXCLUDED_HOLIDAY};
	
	private final static String APP_ID = "19a2011d";
	private final static String APP_KEY = "4e39e4ef08c4f42716d2c4abc0a85683";
	private final static String MAX_RESULTS = "50";
	
	/**
	 * 
	 * @param searchParams
	 * @return a list of RecipeData
	 * @throws UnknownHostException in case there is no Internet connection
	 * @throws IOException for all other issues with HTTP IO or JSON parsing
	 */
	public List<RecipeData> getRecipes(Map<FilterValue, List<String>> searchParams) throws UnknownHostException, IOException{

		List<RecipeData> recipes;
	
		// generate the URI for the API call
		URI uri = uriBuilder(searchParams);
		
		// obtain response
		String searchResponseJson = obtainResponseJson(uri);

		//parse the JSON and build Recipe Data:		
		recipes = makeRecipeData(searchResponseJson);

		return recipes;
	}
	
	/**
	 * Makes a HTTPS connection, opens the uri, and generates a JSON string from the
	 * response body which is returned.
	 * @param uri
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private String obtainResponseJson(URI uri) throws IllegalStateException, IOException {
		
		// HTTP connection generation
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpget);
		InputStream responseStream = response.getEntity().getContent();

		// reading the response Stream
		int ch;
		String searchResponseJson = "";
		while((ch = responseStream.read()) != -1){
			searchResponseJson += Character.valueOf((char) ch);
		}
		
		response.close();
		
		return searchResponseJson;
	}

	/**
	 * Parse the JSON and make RecipeData objects. Return these in a list.
	 * @param searchResponseJson
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private List<RecipeData> makeRecipeData(String searchResponseJson) throws JsonParseException, IOException {
		List<RecipeData> recipeDataList = new ArrayList<RecipeData>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode rootNode = mapper.readValue(searchResponseJson, JsonNode.class);
		
		JsonNode matches = rootNode.get("matches");
		
		if(matches == null) { return recipeDataList; }
		
		for(JsonNode recipe : rootNode.path("matches")){
			RecipeData recipeData = new RecipeData();
			
			// fetching the small bitmap
			for(JsonNode smallImageUrl : recipe.path("smallImageUrls")){
				if(!smallImageUrl.isMissingNode()){
					recipeData.setImageSmall(getBitmapFromURL(smallImageUrl.toString().replace("\"", "")));
					break;
				}
			}
			
			// Recipe Name
			JsonNode recipeName = recipe.get("recipeName");
			if(recipeName != null){
				recipeData.setRecipeName(recipeName.toString().replace("\"", ""));
			}
			
			// Recipe ID
			JsonNode recipeID = recipe.get("id");
			if(recipeID != null){
				recipeData.setRecipeID(recipeID.toString().replace("\"", ""));
			}
			
			// Recipe Ingredients
			List<String> ingredients = new ArrayList<String>();
			for(JsonNode ingredient : recipe.path("ingredients")){				
				if(!ingredient.isMissingNode()){
					ingredients.add(ingredient.toString().replace("\"", ""));
				}
			}
			recipeData.setIngredientsNeeded(ingredients);
			
			// Recipe Rating
			JsonNode recipeRating = recipe.get("rating");
			if(recipeRating != null){
				try{
					recipeData.setRating(Float.parseFloat(recipeRating.toString()));
				} catch (NumberFormatException e){
					System.err.println("Recipe Rating is not a number: " + recipeRating.toString());
				}
			}
			
			// Recipe preparation time
			JsonNode recipePrepTime = recipe.get("totalTimeInSeconds");
			if(recipePrepTime != null && !recipePrepTime.toString().equals("null")){
				try{
					recipeData.setPrepTimeSecs(Integer.parseInt(recipePrepTime.toString().split("\\.")[0]));
				} catch (NumberFormatException e){
					System.err.println("Recipe Preparation Time is not a number: " + recipePrepTime.toString());
				}
			}			
			
			recipeDataList.add(recipeData);
		}
		

		return recipeDataList;
	}
	
	/**
	 * opens a url to an image and makes a bitmap out of it.
	 * @param src
	 * @return
	 */
	private Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        return null;
	    } catch (NoClassDefFoundError e){
	    	return null;
	    }
	}

	/**
	 * takes the searchParams Map and builds a URI for the API call
	 * @param searchParams
	 * @return the URI for the API call
	 */
	private URI uriBuilder(Map<FilterValue, List<String>> searchParams){
		try {
			URIBuilder uriBuilder = new URIBuilder();
			
			uriBuilder.setScheme("https")
	        .setHost("api.yummly.com")
	        .setPath("/v1/api/recipes")
	        .setParameter("_app_id", APP_ID)
	        .setParameter("_app_key", APP_KEY)
	        .setParameter("maxResult", MAX_RESULTS)
	        .setParameter("requirePictures", "true");
			
			List<String> paramVals;
			
			for(FilterValue filterval : searchParams.keySet()){
				paramVals = searchParams.get(filterval);
				
				switch(filterval) {				
				
				case ALLOWED_ALLERGY:
					for(String allergy : paramVals){
						uriBuilder.addParameter("allowedAllergy[]", allergy);
					}
					break;
				case ALLOWED_COURSE:
					for(String course : paramVals){
						uriBuilder.addParameter("allowedCourse[]", course);
					}
					break;
				case ALLOWED_CUISINE:
					for(String cuisine : paramVals){
						uriBuilder.addParameter("allowedCuisine[]", cuisine);
					}
					break;
				case ALLOWED_DIET:
					for(String diet : paramVals){
						uriBuilder.addParameter("allowedDiet[]", diet);
					}
					break;
				case ALLOWED_HOLIDAY:
					for(String holiday : paramVals){
						uriBuilder.addParameter("allowedHoliday[]", holiday);
					}
					break;
				case ALLOWED_INGREDIENT:
					for(String ingredient : paramVals){
						uriBuilder.addParameter("allowedIngredient[]", ingredient);
					}					
					break;
					
					
				case EXCLUDED_COURSE:
					for(String course : paramVals){
						uriBuilder.addParameter("excludedCourse[]", course);
					}
					break;
				case EXCLUDED_CUISINE:
					for(String cuisine : paramVals){
						uriBuilder.addParameter("excludedCuisine[]", cuisine);
					}
					break;
				case EXCLUDED_HOLIDAY:
					for(String holiday : paramVals){
						uriBuilder.addParameter("excludedHoliday[]", holiday);
					}
					break;
				case EXCLUDED_INGREDIENT:
					for(String ingredient : paramVals){
						uriBuilder.addParameter("excludedIngredient[]", ingredient);
					}
					break;
				default:
					System.err.println("Unknown Parameter " + filterval + " in search query.");
					break;
				}
			}
	        
	        
			return uriBuilder.build();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Only For Testing Purposes!!
	 * @param args
	 */
	public static void main(String[] args){
		RecipeGenerator generator = new RecipeGenerator();
		Map<FilterValue, List<String>> searchParams = new HashMap<FilterValue, List<String>>();
		
		ArrayList<String> allowedIngredients = new ArrayList<String>();
		ArrayList<String> excludeIngredients = new ArrayList<String>();
		ArrayList<String> allowedAllergy = new ArrayList<String>();
		ArrayList<String> allowedDiet = new ArrayList<String>();
		ArrayList<String> allowedCuisine = new ArrayList<String>();
		ArrayList<String> allowedHoliday = new ArrayList<String>();
		
//		allowedIngredients.add("noodles");
//		allowedIngredients.add("cheese");
//		allowedIngredients.add("tomatoes");
//		
//		excludeIngredients.add("chicken");
//		
//		allowedAllergy.add("394^Peanut-Free");
//		
//		allowedDiet.add("390^Pescetarian");
//		
//		allowedCuisine.add("cuisine^cuisine-italian");
		
		allowedHoliday.add("holiday^holiday-new-year");
		
		
		
		searchParams.put(FilterValue.ALLOWED_INGREDIENT, allowedIngredients);
		searchParams.put(FilterValue.EXCLUDED_INGREDIENT, excludeIngredients);
		searchParams.put(FilterValue.ALLOWED_ALLERGY, allowedAllergy);
		searchParams.put(FilterValue.ALLOWED_DIET, allowedDiet);
		searchParams.put(FilterValue.ALLOWED_CUISINE, allowedCuisine);
		searchParams.put(FilterValue.ALLOWED_HOLIDAY, allowedHoliday);
		
		Integer.parseInt("450050".split("\\.")[0]);
		
		List<RecipeData> recipes;
		try {
			recipes = generator.getRecipes(searchParams);
			
			for(RecipeData recipe : recipes){
				System.out.println(recipe.getRecipeName());
			}
		} catch (UnknownHostException e) {
			System.err.println("No internet connection!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
				
	}
}
