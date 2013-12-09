package com.recipefy;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter {
	private GridView _gridview;
	private Context _context;
    private int _layoutResourceId;
    private ArrayList<ImageItem> _data = new ArrayList<ImageItem>();
    private HashMap<String,ViewHolder> _map;
  
 
    public GridViewAdapter(Context context, int layoutResourceId,
            ArrayList<ImageItem> data, GridView gridview) {
        super(context, layoutResourceId, data);
        _layoutResourceId = layoutResourceId;
        _context = context;
        _data = data;
        _map = new HashMap<String,ViewHolder>();
        _gridview = gridview;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            RecipeListener listener = new RecipeListener(position);
            holder.image.setOnClickListener(listener);
            holder.info1 = (TextView) row.findViewById(R.id.text2);
            holder.info2 = (TextView) row.findViewById(R.id.text3);
            holder.info3 = (TextView) row.findViewById(R.id.text4);
            //holder.info1.setOnClickListener(listener);
            //holder.info2.setOnClickListener(listener);
            //holder.info3.setOnClickListener(listener);
            //holder.imageTitle.setOnClickListener(listener);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
 
        ImageItem item = _data.get(position);
        int balsamiclength = "Balsamic Glazed Pearl Onions".length();
        if(item.getTitle().length() < balsamiclength){
        	holder.imageTitle.setText(item.getTitle());
        } else {
        	holder.imageTitle.setText(item.getTitle().substring(0, balsamiclength - 6) + "...");
        }
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        holder.info1.setText(" " + item.getMatch()+" Match ");
        holder.info2.setText(" " + item.getTime()+" Min ");
        holder.info3.setText(" " + item.getRating()+" Rating ");
        holder.name = item.getTitle();
        System.err.println("HOLDER NAME " + holder.name);
        _map.put(holder.name, holder);		
        return row;
    }
 
    static class ViewHolder {
        TextView imageTitle;
        TextView info1;
        TextView info2;
        TextView info3;
        ImageView image;
        String name;
    }
    
	public void selectRecipe(ImageItem item){
		Intent intent = new Intent(_context,SelectActivity.class);
		intent.putExtra("item",item);
		_context.startActivity(intent);
	}
	
	public void fixOrder(){
		ArrayList<ImageItem> items = new ArrayList<ImageItem>();
		for(int i=0;i<this.getCount();i++){
			items.add((ImageItem)this.getItem(i));
		}
		for(ImageItem item : items){
			System.err.println("ITEM NAME: " + item.getTitle());
		}
		for(int i=0;i<items.size();i++){
			ViewHolder holder = _map.get(items.get(i).getTitle());
			holder.image.setOnClickListener(new RecipeListener(i));
		}
		
	}
	
	private class RecipeListener implements View.OnClickListener{
		private int _position;
		private ImageItem _imageitem;
		public RecipeListener(int pos){
			_position = pos;
			_imageitem = _data.get(_position);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//GridViewAdapter.this.fixOrder();		
			_gridview.setAdapter(GridViewAdapter.this);
			GridViewAdapter.this.selectRecipe(_imageitem);
			
		}
		

		
	}
	
	
}
