package com.recipefy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class IngredientInput extends FragmentActivity implements
		ActionBar.OnNavigationListener, TextWatcher {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private ArrayAdapter<String> _adapter;
	private ArrayList<String> _ingredients;
	private ArrayList<String> _pantry;
	private String[] _ingredientsArray;
	protected List<String> _updateList;
	protected IngredientInput input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingredient_input);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
		ListView listView= (ListView) findViewById(R.id.ingredientslist);
		_pantry = new ArrayList<String>();
		input = this;
		try{
	        InputStream instream = getAssets().open("ingredients.txt");

	            InputStreamReader inputreader = new InputStreamReader(instream);
	            BufferedReader buffreader = new BufferedReader(inputreader);


	            ArrayList<String> lines = new ArrayList<String>();
	            boolean hasNextLine =true;
	            while (hasNextLine){
	                String line =  buffreader.readLine();
	                lines.add(line);
	                hasNextLine = line != null;

	            }
	            lines.remove(lines.size()-1);
	            _ingredients = lines;
	            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_pantry);

	            listView.setAdapter(adapter);
	            //listView.setVisibility(View.GONE);
	            _adapter = adapter;

	        instream.close();
	        SwipeDismissListener touchListener =
			          new SwipeDismissListener(
			                  listView,
			                  new SwipeDismissListener.DismissCallbacks() {
			                	  
			                	  public boolean canDismiss(int position){
			                		  return true;
			                	  }
			                      public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			                          for (int position : reverseSortedPositions) {
			                              adapter.remove(adapter.getItem(position));
			                          }
			                          adapter.notifyDataSetChanged();
			                      }
			                  });
			  listView.setOnTouchListener(touchListener);
			  listView.setOnScrollListener(touchListener.makeScrollListener());

	    }
	    catch(java.io.FileNotFoundException e){

	    }catch(java.io.IOException e){

	    }	
		//setTheme(android.R.style.Theme);
		final AutoCompleteTextView autocomplete = (AutoCompleteTextView) findViewById(R.id.ingredientsBar);
		//final String[] testStrings = new String[] {"chocolate","cake","chicken","chowder","apple","banana","paper","test"};
		final String[] autoStrings = _ingredients.toArray(new String[_ingredients.size()]);
		_ingredientsArray = autoStrings;
		ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,autoStrings);
		autocomplete.setThreshold(2);
		autocomplete.addTextChangedListener(this);
		autocomplete.setAdapter(autoAdapter);
		autocomplete.setOnKeyListener(new OnKeyListener() {
			  public boolean onKey(View view, int keyCode, KeyEvent event){
				    if (event.getAction() == KeyEvent.ACTION_DOWN){
				      switch (keyCode)
				      {
				        case KeyEvent.KEYCODE_DPAD_CENTER:
				        case KeyEvent.KEYCODE_ENTER:
				          List<String> toPass = new ArrayList<String>();
				          toPass.add(autocomplete.getText().toString());
				          input.updateAdapater(toPass);
				          return true;
				        default:
				          break;
				      }
				    }
				    return false;
				  }
				});
	
	}
	

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingredient_input, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}
	
	
	public void updateAdapater(List<String> toAdd){
		//_adapter.clear();
		for(String item:toAdd){
			_adapter.insert(item, _adapter.getCount());
		}
		_adapter.notifyDataSetChanged();
		
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_ingredient_input_dummy, container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause(){
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putInt("array_size", _ingredientsArray.length);
		for(int i=0;i<_ingredientsArray.length;i++){
			editor.putString("array_"+i, _ingredientsArray[i]);
		}
		editor.commit();
	}

}
