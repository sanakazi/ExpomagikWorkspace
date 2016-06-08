package com.conceptcandy.expomagik.model;

import java.util.List;

import com.conceptcandy.expomagik.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterStartOrg extends BaseAdapter {
	
	
	List<ModelStartOrg> data;
	Context context;
	int layoutResID;

	String photoPath, pname;
	
	
	
	
	public AdapterStartOrg(Context context, int layoutResourceId, List<ModelStartOrg>list) {
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.data=list;
		this.layoutResID=layoutResourceId;
	}
	
	
	static class Holder{
		
		private ImageView  image_icon=null;
	}
	
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Holder holder = null;
		View row = convertView;
		holder = null;
		
		
		

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new Holder();
			holder.image_icon = (ImageView) row.findViewById(R.id.image_icon);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final ModelStartOrg model = data.get(position);
		
		photoPath = model.getImageurl();
		photoPath = "http://www.expomagik.com/ImageOrganizerLogo/" + photoPath;
		Picasso.with(context).load(photoPath).into(holder.image_icon);
		
		
		/*if (!data.get(position).getImageurl().isEmpty()) {
			Picasso.with(context).load(data.get(position).getImageurl()).into(holder.image_icon);
		}*/
		
		
		
		
		return row;
	}

}
