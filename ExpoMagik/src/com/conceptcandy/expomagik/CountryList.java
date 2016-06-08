package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.conceptcandy.expomagik.model.IndustryAdapter;
import com.conceptcandy.expomagik.model.IndustryRow;
import com.conceptcandy.expomagik.util.Utils;

public class CountryList extends Fragment {

	ImageView icDrawer;

	EditText searchItem;
	
	ListView list;
	IndustryAdapter adapter;
	List<IndustryRow> data;
	List<IndustryRow> fdata;

	Context context;

	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!Constants.VenueID.equals(""))
		{
			Constants.flag = "venueWise";
			
			getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
		}
		if(!Constants.cityId.equals(""))
		{
			Constants.flag = "cityWise";
			
			getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.country_list, container, false);
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		context = getActivity();

		pDialog = new ProgressDialog(context);
		
		Constants.isCountryList = true;
		
		searchItem = (EditText) view.findViewById(R.id.searchItems);
		
		icDrawer = (ImageView) view.findViewById(R.id.ic_drCountry);

		list = (ListView) view.findViewById(R.id.listCountries);
		data = new ArrayList<IndustryRow>();
		fdata = new ArrayList<IndustryRow>();
		adapter = new IndustryAdapter(context, R.layout.row_category, data, fdata);
		list.setAdapter(adapter);

		icDrawer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.openDrawer();

			}
		});
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Constants.countryId = data.get(position).getId();
				
				Intent cityList = new Intent(context, CityList.class);
				startActivity(cityList);
				
			}
		});
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getCountries();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		
		adapter.filter("");
		
		searchItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String items = searchItem.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(items);
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
		
		return view;
	}
	
	public void getCountries() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				data.clear();
				adapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/CountryList.asmx/Exhibitions", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < arr.length(); i++) {
					try {
						
						data.add(new IndustryRow(
								arr.getJSONObject(i).getString("ID"), 
								arr.getJSONObject(i).getString("identity"),
								arr.getJSONObject(i).getString("Name"),
								arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
								));
						
						fdata.add(new IndustryRow(
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

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();

				adapter.notifyDataSetChanged();
			}

		}.execute();

	}
}
