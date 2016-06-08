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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.conceptcandy.expomagik.model.CityAdapter;
import com.conceptcandy.expomagik.model.IndustryRow;

public class CityList extends Activity {

	Context context;
	
	ImageView back;
	EditText searchItem;
	
	ListView listCity;
	CityAdapter adapterCity;
	List<IndustryRow> dataCity;
	List<IndustryRow> fdataCity;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!Constants.VenueID.equals(""))
		{
			finish();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_list);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		context = CityList.this;
		
		pDialog = new ProgressDialog(context);
		
		back = (ImageView)findViewById(R.id.cl_back_arrow);
		
		searchItem = (EditText) findViewById(R.id.searchItems);
		
 		listCity = (ListView)findViewById(R.id.listCities);
 		dataCity = new ArrayList<IndustryRow>();
 		fdataCity = new ArrayList<IndustryRow>();
		adapterCity = new CityAdapter(context, R.layout.row_category, dataCity, fdataCity);
		listCity.setAdapter(adapterCity);
		
		listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				if(Constants.isCityList==true)
				{
					Constants.isCityList = false;
					Constants.cityId = dataCity.get(position).getId();
					finish();
				}
				else
				{
					Constants.cityId = dataCity.get(position).getId();
					Intent cityList = new Intent(context, VenueList.class);
					startActivity(cityList);
				}
				
			}
		});
		
		adapterCity.filter("");
		
		searchItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String items = searchItem.getText().toString().toLowerCase(Locale.getDefault());
				adapterCity.filter(items);
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
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getCityList();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
	}
	
	
	public void getCityList() {
		
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
				namevalue.add(new BasicNameValuePair("countryID",Constants.countryId));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/cityList.asmx/Exhibitions", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							dataCity.add(new IndustryRow(
									arr.getJSONObject(i).getString("ID"), 
									arr.getJSONObject(i).getString("identity"),
									arr.getJSONObject(i).getString("Name"),
									arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
									));
							
							fdataCity.add(new IndustryRow(
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
				
				adapterCity.notifyDataSetChanged();
				Constants.countryId = "";
			}

		}.execute();

	}
}