

package com.conceptcandy.expomagik.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conceptcandy.expomagik.R;
import com.conceptcandy.expomagik.util.ImageLoader;

public class RelatedAdapter extends ArrayAdapter<RelatedRow> {

	List<RelatedRow> data;
	Context context;
	int layoutResID;
	String photoPath, pname;
	public ImageLoader imageLoader;
	
	static class Holder {
		
		TextView relExbName;
		TextView relExbCity;
		ImageView image;
	}

	public RelatedAdapter(Context context, int layoutResourceId,
			List<RelatedRow> data) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
		imageLoader = new ImageLoader(context);
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

			holder.relExbName = (TextView) row.findViewById(R.id.relExbName);
			holder.relExbCity = (TextView) row.findViewById(R.id.relCity);
			holder.image=(ImageView) row.findViewById(R.id.image);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}
		RelatedRow itemdata = data.get(position);
		
		photoPath = itemdata.getImg();
		Log.d("Related Adapter", "photo path is-"+photoPath);		
		if( photoPath.isEmpty() ){
			holder.image.setImageResource(R.drawable.event_alike);
		}else {
			photoPath = "http://www.expomagik.com/ImageExhibitionIdentity/"+photoPath;
			imageLoader.DisplayImage(photoPath, holder.image);
		}
				
		holder.relExbName.setText(itemdata.getExhibitionName());
		holder.relExbCity.setText(itemdata.getCity() + ", " + itemdata.getCountry());
		
		return row;

	}	
}
