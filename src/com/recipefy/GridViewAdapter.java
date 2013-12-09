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
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter {
	private Context _context;
    private int _layoutResourceId;
    private ArrayList<ImageItem> _data = new ArrayList<ImageItem>();
    private HashMap<String,ViewHolder> _map;
  
 
    public GridViewAdapter(Context context, int layoutResourceId,
            ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        _layoutResourceId = layoutResourceId;
        _context = context;
        _data = data;
        _map = new HashMap<String,ViewHolder>();
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
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        holder.info1.setText(" " + item.getMatch()+" Match ");
        holder.info2.setText(" " + item.getTime()+" Min ");
        holder.info3.setText(" " + item.getRating()+" Rating ");
        holder.name = item.getTitle();
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
		for(int i=0;i<items.size();i++){
			ViewHolder holder = _map.get(items.get(i).getTitle());
			holder.image.setOnClickListener(new RecipeListener(i));
		}
		
	}
	
	private class RecipeListener implements View.OnClickListener{
		private int _position;
		
		public RecipeListener(int pos){
			_position = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			GridViewAdapter.this.selectRecipe(_data.get(_position));
			
		}
		

		
	}
}
