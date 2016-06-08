package com.conceptcandy.expomagik.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conceptcandy.expomagik.Constants;
import com.conceptcandy.expomagik.R;
import com.squareup.picasso.Picasso;

public class EventAdapter extends ArrayAdapter<EventRow> {

	List<EventRow> data;
	List<EventRow> fdata;
	Context context;
	int layoutResID;
	
	String photoPath, pname;

	static class LeaveApproveHolder {
		
		TextView eventDate, eventName, eventPlace, eventCat;
		ImageView icon, flag;
		
	}

	public EventAdapter(Context context, int layoutResourceId,
			List<EventRow> data, List<EventRow> fdata) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.fdata = fdata;
		this.context = context;
		this.layoutResID = layoutResourceId;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LeaveApproveHolder holder = null;
		View row = convertView;
		holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new LeaveApproveHolder();

			holder.icon = (ImageView) row.findViewById(R.id.ex_icon);
			//holder.eventDate = (TextView) row.findViewById(R.id.txtEventDate);
			holder.eventName  = (TextView) row.findViewById(R.id.txtEventName);
			holder.eventPlace = (TextView) row.findViewById(R.id.txtEventPlace);
			holder.eventCat = (TextView) row.findViewById(R.id.txtEventCat);

			row.setTag(holder);
		} else {
			holder = (LeaveApproveHolder) row.getTag();
		}

		final EventRow itemdata = data.get(position);
		
		String fdate = null;
		String edate = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy aa", Locale.getDefault());
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM-dd");
		SimpleDateFormat enewFormat = new SimpleDateFormat("MMM-dd-yyyy");
		try {  
		    Date date = format.parse(itemdata.getDate());  
		    fdate = newFormat.format(date);
		    edate = enewFormat.format(date);
		} catch (ParseException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}

		//String month = fdate.substring(0, 3);
		//String date = fdate.substring(4, 6);
	//	String printDate = month + "\n " + date;
		
		photoPath = itemdata.getIcon();
			photoPath = "http://www.expomagik.com/ImageExhibitionIdentity/" + photoPath;
			//holder.icon.setVisibility(View.GONE);
			holder.icon.setVisibility(View.VISIBLE);
			Picasso.with(context).load(photoPath).into(holder.icon);
		
		/*else
		{
			photoPath = "http://www.expomagik.com/ImageIndustryIdentity/" + photoPath;
			Picasso.with(context).load(photoPath).into(holder.icon);
		}*/
		
				
		//holder.eventDate.setText(itemdata.getName());
		holder.eventName.setText(itemdata.getName());
		holder.eventPlace.setText(itemdata.getDate());
		holder.eventCat.setText(itemdata.getPlace());
		
		return row;

	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		data.clear();
		if (charText.length() == 0) {
			data.addAll(fdata);
		} else {

			for (int i = 0; i < fdata.size(); i++) {
				EventRow temp = fdata.get(i);

				if (temp.getName().toString().toLowerCase(Locale.getDefault()).contains(charText)) {
					data.add(fdata.get(i));
				}

			}
		}
		notifyDataSetChanged();
	}
}
