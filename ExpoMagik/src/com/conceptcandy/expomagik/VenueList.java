package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.conceptcandy.expomagik.model.CityAdapter;
import com.conceptcandy.expomagik.model.IndustryRow;
import com.conceptcandy.expomagik.util.Utils;

public class VenueList extends Activity {

	Context context;
	
	TextView txtHeading;
	
	EditText searchItem;
	
	ListView listVenue;
	CityAdapter adapterVenue;
	List<IndustryRow> dataVenue;
	List<IndustryRow> fdataVenue;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_list);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = VenueList.this;
		
		pDialog = new ProgressDialog(context);
		
		searchItem = (EditText) findViewById(R.id.searchItems);
		
		txtHeading = (TextView)findViewById(R.id.cityListHeading);
		txtHeading.setText("Select Venue");
		
		listVenue = (ListView)findViewById(R.id.listCities);
		dataVenue = new ArrayList<IndustryRow>();
		fdataVenue = new ArrayList<IndustryRow>();
		adapterVenue = new CityAdapter(context, R.layout.row_category, dataVenue, fdataVenue);
		listVenue.setAdapter(adapterVenue);
		
		listVenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Constants.VenueID = dataVenue.get(position).getId();
				
				finish();
			}
		});
		
		adapterVenue.filter("");
		
		searchItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String items = searchItem.getText().toString().toLowerCase(Locale.getDefault());
				adapterVenue.filter(items);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getVenueList();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
	}
	
	
	public void getVenueList() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("cityID",Constants.cityId));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VenueList.asmx/Venues", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							dataVenue.add(new IndustryRow(
									arr.getJSONObject(i).getString("ID"), 
									arr.getJSONObject(i).getString("identity"),
									arr.getJSONObject(i).getString("Name"),
									arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
									));
							
							fdataVenue.add(new IndustryRow(
									arr.getJSONObject(i).getString("ID"), 
									arr.getJSONObject(i).getString("identity"),
									arr.getJSONObject(i).getString("Name"),
									arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
									));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				adapterVenue.notifyDataSetChanged();
				Constants.cityId = "";
			}

		}.execute();

	}
}