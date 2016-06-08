package com.conceptcandy.expomagik.model;

import java.util.List;

import com.conceptcandy.expomagik.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ExhInfoAdapter extends ArrayAdapter<ExhibitorRow> {

	List<ExhibitorRow> data;
	Context context;
	int layoutResID;

	String photoPath, pname;
	Bitmap image, bitmap;
	
	String ExhibitionID, ExhibitorID, brief, proBrief, wishToMeet, company;
	
	static class Holder {
		
		TextView exbName;
		TextView exbBooth;
		TextView exbCity;
	}

	public ExhInfoAdapter(Context context, int layoutResourceId,
			List<ExhibitorRow> data) {
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

			holder.exbName = (TextView) row.findViewById(R.id.exlNameInfo);
			holder.exbBooth = (TextView) row.findViewById(R.id.exlBoothInfo);
			holder.exbCity = (TextView) row.findViewById(R.id.exlCityInfo);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final ExhibitorRow itemdata = data.get(position);
		
		holder.exbName.setText(itemdata.getExbName());
		holder.exbBooth.setText("Booth Number : " + itemdata.getExbAddress());
		holder.exbCity.setText(itemdata.getExbCity() + ", " + itemdata.getExbCountry());
		
		return row;

	}
}
