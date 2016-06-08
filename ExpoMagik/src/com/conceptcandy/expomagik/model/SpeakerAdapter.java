package com.conceptcandy.expomagik.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conceptcandy.expomagik.R;
import com.conceptcandy.expomagik.util.ImageLoader;

public class SpeakerAdapter extends ArrayAdapter<SpeakerRow> {

	List<SpeakerRow> data;
	Context context;
	int layoutResID;

	String photoPath, pname;
	Bitmap image, bitmap;
	public ImageLoader imageLoader;
	
	static class Holder {
		
		ImageView dp;
		TextView Name;
		TextView Topic;
		TextView Brief;
	}

	public SpeakerAdapter(Context context, int layoutResourceId,
			List<SpeakerRow> data) {
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

			holder.dp = (ImageView) row.findViewById(R.id.speakerDp);
			holder.Name = (TextView) row.findViewById(R.id.speakerName);
			holder.Topic = (TextView) row.findViewById(R.id.speakerTopic);
			holder.Brief = (TextView) row.findViewById(R.id.speakerBrief);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final SpeakerRow itemdata = data.get(position);
		
		photoPath = itemdata.getPhoto();
		photoPath = "http://www.expomagik.com/ImageSpeakers/" + photoPath;
		
		imageLoader.DisplayImage(photoPath, holder.dp);
		
		
		holder.Name.setText(itemdata.getName());
		holder.Topic.setText(itemdata.getTopic());
		holder.Brief.setText(itemdata.getBrief());
		
		return row;

	}	
}
