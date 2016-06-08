package com.conceptcandy.expomagik.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conceptcandy.expomagik.R;
import com.conceptcandy.expomagik.UserFunctions;
import com.conceptcandy.expomagik.util.ImageLoader;

public class ExhibitorAdapter extends ArrayAdapter<ExhibitorRow> {

	List<ExhibitorRow> data;
	Context context;
	int layoutResID;

	String photoPath, pname;
	Bitmap image, bitmap;
	public ImageLoader imageLoader;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String ExhibitionID, ExhibitorID, brief, proBrief, wishToMeet, company;
	
	static class Holder {
		
		RelativeLayout rlMain;
		LinearLayout llHide;
		
		ImageView dp;
		TextView exbName;
		TextView exbBooth;
		TextView exbCity;
		TextView exbBrief;
		TextView exbProBrief;
		TextView exbWishToMeet;
		TextView exbCompanyName;
	}

	public ExhibitorAdapter(Context context, int layoutResourceId,
			List<ExhibitorRow> data) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
		imageLoader = new ImageLoader(context);
		
		pDialog = new ProgressDialog(context);
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

			holder.rlMain = (RelativeLayout) row.findViewById(R.id.rl_exb);
			holder.llHide = (LinearLayout) row.findViewById(R.id.ll_hide_exb);
			
			holder.dp = (ImageView) row.findViewById(R.id.exl_dp);
			holder.exbName = (TextView) row.findViewById(R.id.exlName);
			holder.exbBooth = (TextView) row.findViewById(R.id.exlBooth);
			holder.exbCity = (TextView) row.findViewById(R.id.exlCity);
			
			holder.exbBrief = (TextView) row.findViewById(R.id.exlBrief);
			holder.exbProBrief = (TextView) row.findViewById(R.id.exlProductBrief);
			holder.exbWishToMeet = (TextView) row.findViewById(R.id.exlWishToMeet);
			holder.exbCompanyName = (TextView) row.findViewById(R.id.exlCompanyName);


			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final ExhibitorRow itemdata = data.get(position);
		
		photoPath = itemdata.getCompLogo();
		photoPath = "http://www.expomagik.com/ImageCompanyLogo/" + photoPath;
		imageLoader.DisplayImage(photoPath, holder.dp);
		
		holder.exbName.setText(itemdata.getExbName());
		holder.exbBooth.setText("Booth Number : " + itemdata.getExbAddress());
		holder.exbCity.setText(itemdata.getExbCity() + ", " + itemdata.getExbCountry());
		
		final Holder holder1 = holder;
		
		holder.rlMain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ExhibitorID = itemdata.getExbid();
				
				fetchDetails();
				
				holder1.llHide.setVisibility(View.VISIBLE);
				holder1.exbBrief.setText(brief);
				holder1.exbProBrief.setText(proBrief);
				holder1.exbWishToMeet.setText(wishToMeet);
				holder1.exbCompanyName.setText(company);
				
			}
		});
		
		return row;

	}
	
	public void fetchDetails() {
		
		pDialog.setMessage("Relax for a while...");
		pDialog.setCancelable(false);
		pDialog.show();
		
		namevalue.clear();
		namevalue.add(new BasicNameValuePair("Exhibition",ExhibitionID));
		namevalue.add(new BasicNameValuePair("ExhibitorID",ExhibitorID));
		
		String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitorDetail.asmx/Exhibitor", namevalue);

		JSONArray arr = null;
		try {
			arr = new JSONArray(json);
			
			for (int i = 0; i < arr.length(); i++) {
				try {
					
	
					brief = arr.getJSONObject(i).getString("brief");
					proBrief = arr.getJSONObject(i).getString("productBrief");
					wishToMeet = arr.getJSONObject(i).getString("wishToMeet");
					company = arr.getJSONObject(i).getString("companyType");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
		}
		
		if (pDialog.isShowing())
			pDialog.dismiss();

	}
}
