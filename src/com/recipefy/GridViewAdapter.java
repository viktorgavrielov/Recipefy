package com.recipefy;

import java.util.ArrayList;

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
    protected GridViewAdapter _this;
    protected int _pos;
 
    public GridViewAdapter(Context context, int layoutResourceId,
            ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        _layoutResourceId = layoutResourceId;
        _context = context;
        _data = data;
        _this = this;
        
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        _pos = position;
        if (row == null) {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    System.out.println("Testing");
                    _this.selectRecipe(_data.get(_pos));
                }
            });

            holder.info1 = (TextView) row.findViewById(R.id.text2);
            holder.info2 = (TextView) row.findViewById(R.id.text3);
            holder.info3 = (TextView) row.findViewById(R.id.text4);
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
        return row;
    }
 
    static class ViewHolder {
        TextView imageTitle;
        TextView info1;
        TextView info2;
        TextView info3;
        ImageView image;
    }
    
	public void selectRecipe(ImageItem item){
		Intent intent = new Intent(_context,SelectActivity.class);
		intent.putExtra("item",item);
		_context.startActivity(intent);
	}
}
