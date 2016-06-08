package com.conceptcandy.expomagik.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conceptcandy.expomagik.R;
import com.squareup.picasso.Picasso;

public class GridExbAdapter  extends BaseAdapter {
    
	
	List<EventRow> data;
	Context context;
	int layoutResID;

	String photoPath, pname;
 
	// Constructor
    public GridExbAdapter(Context context, int layoutResourceId,
			List<EventRow> data){
        
        
        this.data = data;
        this.context = context;
		this.layoutResID = layoutResourceId;
    }
    
    static class Holder {
		
		LinearLayout bottom_color;
		ImageView exbPic;
		TextView exbName;
		TextView exbTime;
		TextView exbVenue;
	}
 
    @Override
    public int getCount() {
        return data.size();
    }
 
    @Override
    public Object getItem(int position) {
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	Holder holder = null;
		View row = convertView;
		holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);
			holder = new Holder();
			holder.bottom_color = (LinearLayout) row.findViewById(R.id.grd_bottom_color);
			holder.exbPic = (ImageView) row.findViewById(R.id.img_exhb_grid);
			holder.exbName = (TextView) row.findViewById(R.id.txtGridExbName);
			holder.exbTime = (TextView) row.findViewById(R.id.txtGridDate);
			holder.exbVenue = (TextView) row.findViewById(R.id.txtGridVenue);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final EventRow itemdata = data.get(position);
		
		photoPath = itemdata.getCategory();
		photoPath = "http://www.expomagik.com/ImageExhibitionIdentity/" + photoPath;
		Picasso.with(context).load(photoPath).into(holder.exbPic);
		
		
//		holder.exbPic.getLayoutParams().height = LayoutParams.MATCH_PARENT;
//		
//		int width = holder.exbPic.getWidth();
//		width = width/3;
//		int height = width*2;
//		
//		holder.exbPic.setMinimumHeight(height);
//		holder.exbPic.setMaxHeight(height);
//		holder.exbPic.requestLayout();
		
		holder.exbName.setText(itemdata.getName());
		holder.exbTime.setText(itemdata.getDate());
		holder.exbVenue.setText(itemdata.getPlace());
		
		if(position%1==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_orange);
		}
		if(position%2==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_blue);
		}
		if(position%3==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_pink);
		}
		if(position%4==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_purple);
		}
		if(position%5==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_green);
		}
		if(position%6==0)
		{
			holder.bottom_color.setBackgroundResource(R.color.ftr_darkpurple);
		}
		
		return row;
		
    }

}
