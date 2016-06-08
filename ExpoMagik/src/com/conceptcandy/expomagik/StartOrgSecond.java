package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.conceptcandy.expomagik.model.EventRow;
import com.conceptcandy.expomagik.model.GridExbAdapter;
import com.conceptcandy.expomagik.model.HomePagerRow;
import com.conceptcandy.expomagik.util.Utils;
import com.conceptcandy.expomagik.utils.GridViewScrollable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class StartOrgSecond extends Activity{
	
Context context;
	
	Double latitude = 0.0, longitude;
	
	TextView txtHeading;
	
	EditText txtSearchBox;
	ImageView icDrawer, search, filter;;
	
	GridViewScrollable gridView;
	GridExbAdapter adapterGrid;
	List<EventRow> dataGrid;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String exType = "exhibition";
	String lastIndex = "0";
	
	boolean paging = false;
	
	//private View view;

	private String orgnizationid;
	
	
	private TextView text_comp_name,text_country,text_city,text_phone,text_aboutus;

	protected String fullName;

	protected String Country;

	protected String City;

	protected String phoneNumbers;
	
	ScrollView scroll; 
	
	GPSTracker gps ;
	
	public void backToOrganiser(View v){
		Intent i = new Intent(StartOrgSecond.this, MainActivity.class);
		startActivity(i);
		finish();
	}
	
	
	
	private final void focusOnView(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
            	scroll.scrollTo(0, gridView.getBottom());
            }
        });
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.start_org_details);
		//orgnizationid=MySharedPreferences.getInstance().getString(this, "orgnizationid", "");
		Intent i = getIntent();
		orgnizationid = i.getStringExtra("orgnizationid");
		Log.i("orgnizationid", orgnizationid);
		
		gps = new GPSTracker(context);
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		context = this;
		Constants.homepage = false;
		
		pDialog = new ProgressDialog(context);

		
		
		try
		{
			
			if(gps.canGetLocation())
			{ 
				try
				{
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					Constants.latitude = latitude;
					Constants.longitude = longitude;
					
				}
				catch(Exception e)
				{
					//Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				gps.showSettingsAlert();
			}
		}
		catch(Exception e)
		{
			latitude = 0.0;
			longitude = 0.0;
			Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
		}
		
		
		/*icDrawer = (ImageView) view.findViewById(R.id.ic_drGridExb);
	 
		text_comp_name=(TextView) view.findViewById(R.id.text_comp_name);
		text_country=(TextView) view.findViewById(R.id.text_country);
		text_city= (TextView) view.findViewById(R.id.text_city);
		text_phone= (TextView) view.findViewById(R.id.text_phone);
		text_aboutus=(TextView) view.findViewById(R.id.text_aboutus);
		
		
		gridView = (GridView) view.findViewById(R.id.grid_exb1);*/
		
		
		icDrawer = (ImageView) findViewById(R.id.ic_drGridExb);
		 
		text_comp_name=(TextView) findViewById(R.id.text_comp_name);
		text_country=(TextView) findViewById(R.id.text_country);
		text_city= (TextView) findViewById(R.id.text_city);
		text_phone= (TextView) findViewById(R.id.text_phone);
		text_aboutus=(TextView) findViewById(R.id.text_aboutus);
		
		
		gridView = (GridViewScrollable) findViewById(R.id.grid_exb1);		
	    dataGrid = new ArrayList<EventRow>();
		adapterGrid = new GridExbAdapter(context, R.layout.row_grid_exb, dataGrid);
	    gridView.setAdapter(adapterGrid);
	    gridView.setVerticalScrollBarEnabled(false);
	    
	    scroll = (ScrollView) findViewById(R.id.info_scroll);
	    	    
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String ExhibitionID = dataGrid.get(position).getExhibitionID();
				Intent eventDetails = new Intent(context, EventInfo.class);
				eventDetails.putExtra("ExhibitionID", ExhibitionID);
				startActivity(eventDetails);
				
			}
		});
		/*
		 icDrawer.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (Constants.flag.equalsIgnoreCase("drawerorg")) {
						MainActivity.openDrawer();
					}
					
					//MainActivity.openDrawer();
					
					//getActivity().finish();
				}
			});
		*/
			netCheck = UserFunctions.isConnectionAvailable(this);
			if (netCheck == true) {
				
			/*	
				if (Constants.flag.equalsIgnoreCase("drawerorg")) {
					
					
					id=Constants.POID;
					getexhibitionsDetails();
					getExhibitions();
					
				}else {*/
					getexhibitionsDetails();
					getExhibitions();
								
			} else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", this);
			}
		
	}
	
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			if ((pDialog!=null) && pDialog.isShowing()) {
				
				pDialog.dismiss();
				pDialog=null;
				
			}
			
			gps.stopUsingGPS();
			
		}
	
     private void getexhibitionsDetails() {
		// TODO Auto-generated method stub
		
	
	// Fetch Data
			new AsyncTask<String, String, String>() {

				private String aboutus;

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					//super.onPreExecute();
					//pDialog = new ProgressDialog(context);
					//pDialog.setMessage("Relax for a while...");
					//pDialog.setCancelable(false);
					//pDialog.show();
					
					//dataGrid.clear();
				}

				@Override
				protected String doInBackground(String... params) {

				 
					namevalue.clear();
					namevalue.add(new BasicNameValuePair("organiserid",orgnizationid));
					
					String json = UserFunctions.loadJson("http://webservices.expomagik.com/OrganiserDetail.asmx/organiser", namevalue);

					if (json!=null) {
						
					
					
					JSONArray arr = null;
					try {
						arr = new JSONArray(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < arr.length(); i++) {
						try {
						 
							 							
							fullName = arr.getJSONObject(i).getString("fullName");
							Country = arr.getJSONObject(i).getString("Country");
							City = arr.getJSONObject(i).getString("City");
							phoneNumbers = arr.getJSONObject(i).getString("phoneNumbers");
							aboutus= arr.getJSONObject(i).getString("AboutUs");
							
							 
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					//if (pDialog.isShowing())
						//pDialog.dismiss();

					//adapterGrid.notifyDataSetChanged();
					
					text_comp_name.setText(fullName);
					text_country.setText(Country);
					text_city.setText(City);
					text_phone.setText(phoneNumbers);
					String brief = aboutus.replaceAll("â", "\'");
					text_aboutus.setText(brief);
				}

			}.execute();
	 
	}


 




public void getPagerExhibitions() {

	// Fetch Data
	new AsyncTask<String, String, String>() {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
			}

			//pagerData.clear();
		}

		@Override
		protected String doInBackground(String... params) {

			// **Code**

			namevalue.clear();
			namevalue.add(new BasicNameValuePair("type", exType));

			String json = UserFunctions.loadJson("http://webservices.expomagik.com/MobileHomeList.asmx/Exhibitions",
					namevalue);

			if (json != null) {
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < arr.length(); i++) {

					/*try {
						pagerData.add(new HomePagerRow(arr.getJSONObject(i).getString("ExhibitionID"),
								arr.getJSONObject(i).getString("ExhibitionName"),
								arr.getJSONObject(i).getString("startdate") + " To "
										+ arr.getJSONObject(i).getString("enddate"),
								arr.getJSONObject(i).getString("Country"), arr.getJSONObject(i).getString("City"),
								arr.getJSONObject(i).getString("industry"),
								arr.getJSONObject(i).getString("mobileimage")));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null) {
				if (pDialog.isShowing())
					pDialog.dismiss();
			}

			//pagerExb.setAdapter(pagerAdapter);
			//pagerAdapter.notifyDataSetChanged();
			
			
		}
		
		

	}.execute();

}




public void getExhibitions() {
	
	// Fetch Data
	new AsyncTask<String, String, String>() {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//pDialog.setMessage("Relax for a while...");
			//pDialog.setCancelable(false);
			//pDialog.show();
			
			dataGrid.clear();
		}

		@Override
		protected String doInBackground(String... params) {

			// **Code**

			namevalue.clear();
			namevalue.add(new BasicNameValuePair("type",exType));
			namevalue.add(new BasicNameValuePair("OrganiserID", orgnizationid));
			String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/OrganiserwiseExhibitions", namevalue);

			if (json!=null) {
				
			
			
			JSONArray arr = null;
			try {
				arr = new JSONArray(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (arr!=null) {
				
			

			for (int i = 0; i < arr.length(); i++) {
				try {
					
					dataGrid.add(new EventRow(
							arr.getJSONObject(i).getString("ExhibitionID"),
							arr.getJSONObject(i).getString("startdate") + " To " + arr.getJSONObject(i).getString("enddate"),
							arr.getJSONObject(i).getString("ExhibitionName"), 
							arr.getJSONObject(i).getString("City") + "-" + arr.getJSONObject(i).getString("Country"), 
							arr.getJSONObject(i).getString("exibitionIdentity"), 
											""
											));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*if (pDialog.isShowing())
				pDialog.dismiss();*/

			adapterGrid.notifyDataSetChanged();
		}

	}.execute();

}

 
/*
@Override 
public void onBackPressed() { 
    if (getFragmentManager().getBackStackEntryCount() > 0) {
        getFragmentManager().popBackStack();
    } else { 
        super.onBackPressed(); 
    } 
}*/




}
