package com.recipefy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
 
    public GridViewAdapter(Context context, int layoutResourceId,
            ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        _layoutResourceId = layoutResourceId;
        _context = context;
        _data = data;
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
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
 
        ImageItem item = _data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        return row;
    }
 
    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
