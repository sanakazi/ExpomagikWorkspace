package com.conceptcandy.expomagik.model;

import java.util.List;

import com.conceptcandy.expomagik.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VisitorAdapter extends ArrayAdapter<VisitorRow> {

	List<VisitorRow> data;
	Context context;
	int layoutResID;
	
	static class Holder {
		
		TextView name, designation, city;
	}

	public VisitorAdapter(Context context, int layoutResourceId,
			List<VisitorRow> data) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
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

			holder.name = (TextView) row.findViewById(R.id.visitorName);
			holder.designation = (TextView) row.findViewById(R.id.visitorDesignation);
			holder.city = (TextView) row.findViewById(R.id.visitorCity);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final VisitorRow itemdata = data.get(position);
		
		holder.name.setText(itemdata.getVisitorName());
		holder.designation.setText(itemdata.getDesignation() + " - " + itemdata.getCompanyName());
		holder.city.setText(itemdata.getAddress());
		
		return row;

	}
	
}
