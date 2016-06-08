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


public class AgendaAdapter extends ArrayAdapter<AgendaRow> {

	List<AgendaRow> data;
	Context context;
	int layoutResID;
	
	static class Holder {
		
		TextView title, desc;
	}

	public AgendaAdapter(Context context, int layoutResourceId,
			List<AgendaRow> data) {
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

			holder.title = (TextView) row.findViewById(R.id.agendaTitle);
			holder.desc = (TextView) row.findViewById(R.id.agendaDescription);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final AgendaRow itemdata = data.get(position);
		
		holder.title.setText(itemdata.getAgendaTitle());
		holder.desc.setText(itemdata.getAgendaDesc());
		
		return row;

	}
	
}
