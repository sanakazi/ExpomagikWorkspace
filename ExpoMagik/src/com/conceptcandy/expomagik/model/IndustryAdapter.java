package com.conceptcandy.expomagik.model;

import java.util.List;
import java.util.Locale;

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

public class IndustryAdapter extends ArrayAdapter<IndustryRow> {

	List<IndustryRow> data;
	List<IndustryRow> fdata;
	Context context;
	int layoutResID;

	String photoPath, pname;
	
	static class Holder {
		
		ImageView icon, flag;
		TextView catName, noOfExb;
	}

	public IndustryAdapter(Context context, int layoutResourceId,
			List<IndustryRow> data, List<IndustryRow> fdata) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.fdata = fdata;
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

			holder.icon = (ImageView) row.findViewById(R.id.cat_icon);
			holder.flag = (ImageView) row.findViewById(R.id.country_flag);
			holder.catName = (TextView) row.findViewById(R.id.txtCategory);
			holder.noOfExb = (TextView) row.findViewById(R.id.txtIndustryNoOfExb);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final IndustryRow itemdata = data.get(position);
		
		photoPath = itemdata.getIcon();
		if(Constants.isCountryList==true)
		{
			photoPath = "http://www.expomagik.com/ImageCountryFlag/" + photoPath;
			holder.icon.setVisibility(View.GONE);
			holder.flag.setVisibility(View.VISIBLE);
			Picasso.with(context).load(photoPath).into(holder.flag);
		}
		else
		{
			photoPath = "http://www.expomagik.com/ImageIndustryIdentity/" + photoPath;
			Picasso.with(context).load(photoPath).into(holder.icon);
		}
		
		holder.catName.setText(itemdata.getCatName());
		holder.noOfExb.setText(itemdata.getNoOfUpcomingExhibition());
		
		return row;

	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		data.clear();
		if (charText.length() == 0) {
			data.addAll(fdata);
		} else {

			for (int i = 0; i < fdata.size(); i++) {
				IndustryRow temp = fdata.get(i);

				if (temp.getCatName().toString().toLowerCase(Locale.getDefault()).contains(charText)) {
					data.add(fdata.get(i));
				}

			}
		}
		notifyDataSetChanged();
	}
	
}
