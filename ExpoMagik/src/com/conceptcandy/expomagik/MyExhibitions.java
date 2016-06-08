package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.conceptcandy.expomagik.model.EventAdapter;
import com.conceptcandy.expomagik.model.EventRow;
import com.conceptcandy.expomagik.util.Utils;

public class MyExhibitions extends Fragment {
	
	//String exhibId;

	ImageView icDrawer, linePink, lineBlue;
	Button btnMyExbs, btnFavs;

	EditText searchItem;
	
	ListView list;

	EventAdapter adapter;
	List<EventRow> data;
	List<EventRow> fdata;

	Context context;

	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.my_exbs, container, false);
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		context = getActivity();

		pDialog = new ProgressDialog(context);
		
//		exhibId = getActivity().getIntent().getExtras().getString("exhibitionid");
		
		icDrawer = (ImageView) view.findViewById(R.id.ic_drMyExbs);
		searchItem = (EditText) view.findViewById(R.id.searchItems);

		btnMyExbs = (Button) view.findViewById(R.id.tab_myExbs);
		btnFavs = (Button) view.findViewById(R.id.tab_myFavs);
		
		linePink = (ImageView) view.findViewById(R.id.linePinkSelcted);
		lineBlue = (ImageView) view.findViewById(R.id.lineBlueSelcted);
		
		list = (ListView)view.findViewById(R.id.listMyExbs);
		data = new ArrayList<EventRow>();
		fdata = new ArrayList<EventRow>();
		adapter = new EventAdapter(context, R.layout.row_event_list, data, fdata);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				if( !data.isEmpty() ){
					EventRow itemdata = data.get(position);
					
					Intent detailEvent = new Intent(getActivity(), EventInfo.class);
					detailEvent.putExtra("ExhibitionID", itemdata.getExhibitionID());
					startActivity(detailEvent);
				} else{
					EventRow itemdata = fdata.get(position);
					
					Intent detailEvent = new Intent(getActivity(), EventInfo.class);
					detailEvent.putExtra("ExhibitionID", itemdata.getExhibitionID());
					startActivity(detailEvent);
				}
				
			}
		});
		
		
		icDrawer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.openDrawer();

			}
		});
		
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
		
		btnMyExbs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linePink.setVisibility(View.VISIBLE);
				lineBlue.setVisibility(View.INVISIBLE);
				data.clear();
				getMyEvents();
			}
		});

		btnFavs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineBlue.setVisibility(View.VISIBLE);
				linePink.setVisibility(View.INVISIBLE);
				fdata.clear();
				getMyFavEvents();
			}
		});
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getMyEvents();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		
		return view;
	}

	public void getMyEvents() {
		
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
				namevalue.add(new BasicNameValuePair("visitorid",Constants.visitorId));
				
				Log.d("MyExhibitions", "visitors id is-"+Constants.visitorId);
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/MyEvents.asmx/VisitorwiseExhibitions", namevalue);
	
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
	
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							data.add(new EventRow(
									arr.getJSONObject(i).getString("ExhibitionID"),
									arr.getJSONObject(i).getString("exibitionIdentity"),
									arr.getJSONObject(i).getString("startdate") + " To " + arr.getJSONObject(i).getString("enddate"),
									arr.getJSONObject(i).getString("ExhibitionName"), 
									arr.getJSONObject(i).getString("City") + "-" + arr.getJSONObject(i).getString("Country"), 
									arr.getJSONObject(i).getString("industry"), 
													""
													));
							
						fdata.add(new EventRow(
									arr.getJSONObject(i).getString("ExhibitionID"),
									arr.getJSONObject(i).getString("exibitionIdentity"),
									arr.getJSONObject(i).getString("startdate") + " To " + arr.getJSONObject(i).getString("enddate"),
									arr.getJSONObject(i).getString("ExhibitionName"), 
									arr.getJSONObject(i).getString("City") + "-" + arr.getJSONObject(i).getString("Country"), 
									arr.getJSONObject(i).getString("industry"), 
													""
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
	
				adapter.notifyDataSetChanged();
				
				if(data.size()==0)
				{
					UserFunctions.dialogboxshow("Message", "No data available.", context);
				}
			}
	
		}.execute();
	
	}

	
public void getMyFavEvents() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				fdata.clear();
				adapter.clear();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
				// **Code**
	
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("visitorid",Constants.visitorId));
			//	namevalue.add(new BasicNameValuePair("exhibitionid",Constants.exhibitionId));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/MyEvents.asmx/VisitorwiseSavedList", namevalue);
	
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
	
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							data.add(new EventRow(
									arr.getJSONObject(i).getString("ExhibitionID"),
									arr.getJSONObject(i).getString("exibitionIdentity"),
									arr.getJSONObject(i).getString("startdate") + " To " + arr.getJSONObject(i).getString("enddate"),
									arr.getJSONObject(i).getString("ExhibitionName"), 
									arr.getJSONObject(i).getString("City") + "-" + arr.getJSONObject(i).getString("Country"), 
									arr.getJSONObject(i).getString("industry"), 
													""
													));
							
							fdata.add(new EventRow(
									arr.getJSONObject(i).getString("ExhibitionID"),
									arr.getJSONObject(i).getString("exibitionIdentity"),
									arr.getJSONObject(i).getString("startdate") + " To " + arr.getJSONObject(i).getString("enddate"),
									arr.getJSONObject(i).getString("ExhibitionName"), 
									arr.getJSONObject(i).getString("City") + "-" + arr.getJSONObject(i).getString("Country"), 
									arr.getJSONObject(i).getString("industry"), 
													""
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
	
				adapter.notifyDataSetChanged();
				
				if(fdata.size()==0)
				{
					UserFunctions.dialogboxshow("Message", "No data available.", context);
				}
			}
	
		}.execute();
	
	}

	
}
