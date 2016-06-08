package com.conceptcandy.expomagik;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.conceptcandy.expomagik.model.EventRow;
import com.conceptcandy.expomagik.model.GridExbAdapter;
import com.conceptcandy.expomagik.util.Utils;

public class ExhibitonsGrid  extends Fragment {

	Context context;
	
	Double latitude = 0.00, longitude;
	
	TextView txtHeading;
	LinearLayout linear_show_hide;
	EditText txtSearchBox;
	ImageView icDrawer, search, filter;
	
	GridView gridView;
	GridExbAdapter adapterGrid;
	List<EventRow> dataGrid;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String lastIndex = "0";
	
	boolean paging = false;

	private ImageView linePink;

	private ImageView lineBlue;

	private Button inCity;

	private Button topTrending;
	 
	public String drawerFlag = "";
	
	private GPSTracker gps; 
	
	public ExhibitonsGrid(String msg){
		this.drawerFlag = msg;		
	}
	
	public ExhibitonsGrid() {
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Constants.homepage = false;
		
		if(Constants.IndustryID.equals("Filter"))
		{
			netCheck = UserFunctions.isConnectionAvailable(getActivity());
			if (netCheck == true) {
				Constants.flag = "filter";
				
				getFilterizedData();
				txtHeading.setText("Filter");
			} else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", getActivity());
			}
		}
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.exb_grid, container, false);
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		context = getActivity();
		Constants.homepage = false;
		
		pDialog = new ProgressDialog(context);
		dataGrid = new ArrayList<EventRow>();
		
		gps = new GPSTracker(context);
		
		if(Constants.flag.equals("nearMe"))
		{
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
						Log.d("Exhibition Grid","Latitude is-"+longitude);						
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
						Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
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
			if(latitude != 0.00){
				getEventsNearMe();
			}
		}
		
		
		
		//for tabs 
		
		linePink = (ImageView) view.findViewById(R.id.linePinkSelcted);
		lineBlue = (ImageView) view.findViewById(R.id.lineBlueSelcted);
		inCity = (Button) view.findViewById(R.id.tab_InCity);
		topTrending = (Button) view.findViewById(R.id.tab_TopTrending);
		
		linear_show_hide=(LinearLayout) view.findViewById(R.id.linear_show_hide);
		
		txtHeading = (TextView) view.findViewById(R.id.lbl_top_grid);
		if(!Constants.lbl_top_heading.equals(""))
		{
			txtHeading.setText(Constants.lbl_top_heading);
			Constants.lbl_top_heading = "";
		}
		
		if( drawerFlag.equals("exhibition")) {
			txtHeading.setText("Exhibitions");
		}
		
		txtSearchBox = (EditText) view.findViewById(R.id.searchGridExb);
		icDrawer = (ImageView) view.findViewById(R.id.ic_drGridExb);
		search = (ImageView) view.findViewById(R.id.imgSearchGridExb);
		filter = (ImageView) view.findViewById(R.id.ic_grdFilter);
		
		gridView = (GridView) view.findViewById(R.id.grid_exb);		
		adapterGrid = new GridExbAdapter(context, R.layout.row_grid_exb, dataGrid);
		gridView.setAdapter(adapterGrid);
		
		icDrawer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.openDrawer();
				
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Constants.searchText = txtSearchBox.getText().toString();
				seachExhibitions();
				
			}
		});
		
		filter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent filter = new Intent(context, Filter.class);
				startActivity(filter);
			}
		});

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

		gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(paging==true)
				{	
					getExhibitionsPaging();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});

		netCheck = UserFunctions.isConnectionAvailable(context);
		if(netCheck == true)
		{
			if(Constants.flag.equalsIgnoreCase("searchByText"))
			{
				txtSearchBox.setText(Constants.searchText);
				seachExhibitions();
				txtHeading.setText("Search");
			}
			else if(Constants.flag.equalsIgnoreCase("nearMe"))
			{
				//getEventsNearMe();
				txtHeading.setText("Near Me");
			}
			else if(Constants.flag.equalsIgnoreCase("upcoming"))
			{
				getExhibitions();
				txtHeading.setText("What's Next!");
			}
			else if(Constants.flag.equalsIgnoreCase("trending"))
			{
				getEventsTopTrending();
				txtHeading.setText("Trending Now!");
			}
			else if(Constants.flag.equalsIgnoreCase("populer"))
			{
				getPopuler();
				txtHeading.setText("Starred Organisers!");
			}
			else if(Constants.flag.equalsIgnoreCase("filter"))
			{
				getFilterizedData();
				txtHeading.setText("Filter");
			}
			else if(Constants.flag.equalsIgnoreCase("conferences"))
			{
				Constants.exType = "conference";
				getExhibitions();
				txtHeading.setText("Conferences");
				filter.setVisibility(View.VISIBLE);
			}
			else if(Constants.flag.equalsIgnoreCase("venueWise"))
			{
				linear_show_hide.setVisibility(View.VISIBLE);
				getVenueWiseExb();
			}
			else if(Constants.flag.equalsIgnoreCase("cityWise"))
			{
				linear_show_hide.setVisibility(View.VISIBLE);
				getCityWisePaging();
			}
			else
			{
				if(Constants.IndustryID.equals(""))
				{
					Constants.exType = "exhibition";
					getExhibitionsPaging();
					paging = true;
					filter.setVisibility(View.VISIBLE);
				}
				else
				{
					linear_show_hide.setVisibility(View.VISIBLE);
					getCategorizedData();
				}
			}
			Constants.flag = "";
		}
		else
		{
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		
		
		inCity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linePink.setVisibility(View.VISIBLE);
				lineBlue.setVisibility(View.INVISIBLE);

				Constants.exType = "exhibition";
				
				if(Constants.flag.equalsIgnoreCase("venueWise"))
				{
					getVenueWiseExb();
				}
				else if(Constants.flag.equalsIgnoreCase("cityWise"))
				{
					getCityWisePaging();
				}
				else
				{
					getCategorizedData();
				}
				
			}
		});

		topTrending.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineBlue.setVisibility(View.VISIBLE);
				linePink.setVisibility(View.INVISIBLE);

				Constants.exType = "conference";
				
				if(Constants.flag.equalsIgnoreCase("venueWise"))
				{
					getVenueWiseExb();
				}
				else if(Constants.flag.equalsIgnoreCase("cityWise"))
				{
					getCityWisePaging();
				}
				else
				{
					getCategorizedData();
				}
			}
		});		
		
		return view;
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

	public void getExhibitionsPaging() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
//				pDialog.setMessage("Relax for a while...");
//				pDialog.setCancelable(false);
//				pDialog.show();
				
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				namevalue.add(new BasicNameValuePair("startindex",lastIndex));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/ExhibitionsWithPaging", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					 
				}

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
						 
					}
				}
				
				lastIndex = dataGrid.size() + "";

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
//				if (pDialog.isShowing())
//					pDialog.dismiss();

				adapterGrid.notifyDataSetChanged();
			}

		}.execute();

	}
	
	public void getCityWisePaging() {
		
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
				namevalue.add(new BasicNameValuePair("type", Constants.exType));
				namevalue.add(new BasicNameValuePair("cityId", Constants.cityId));
//				namevalue.add(new BasicNameValuePair("startindex", lastIndex));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/CityIDwiseExhibitions", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
				}

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
						 
					}
				}
				
				lastIndex = dataGrid.size() + "";

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();

				Constants.flag = "cityWise";
				adapterGrid.notifyDataSetChanged();
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
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				namevalue.add(new BasicNameValuePair("country", Constants.COUNTRY_NAME));
				
				
				/*String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/Exhibitions", namevalue);*/

					String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/countrywiseupcoming", namevalue);
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					 
				}

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

				adapterGrid.notifyDataSetChanged();
			}

		}.execute();

	}

	public void getCategorizedData() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("categoryID",Constants.IndustryID));
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionListCategorywise.asmx/CategorywiseExhibitions", namevalue);

				if(!json.equalsIgnoreCase("No Data Found") && json != null)
				{
					JSONArray arr = null;
					try {
						arr = new JSONArray(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
	
					if(arr != null)
					{
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
				if (pDialog.isShowing())
					pDialog.dismiss();

				adapterGrid.notifyDataSetChanged();
			}

		}.execute();
	}
	
	public void seachExhibitions() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("searchtext", Constants.searchText));
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/SearchExhibitions", namevalue);

				JSONArray arr = null;
				try {

					arr = new JSONArray(json);
					
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
				
				adapterGrid.notifyDataSetChanged();
			}

		}.execute();
	}
	
	public void getEventsTopTrending() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
				// **Code**
	
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				namevalue.add(new BasicNameValuePair("country",Constants.COUNTRY_NAME));
							
				/*String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/TopTrendingExhibitions", namevalue);*/
					
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/countrywisetranding", namevalue);
	
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					 
				}
	
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
	
				adapterGrid.notifyDataSetChanged();
			}
	
		}.execute();
	
	}
	
	public void getPopuler() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
				// **Code**
	
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				namevalue.add(new BasicNameValuePair("OrganiserID",Constants.POID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/OrganiserwiseExhibitions", namevalue);
	
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					 
				}
	
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
	
				Constants.POID = "";
				adapterGrid.notifyDataSetChanged();
			}
	
		}.execute();
	
	}

	public void getEventsNearMe() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**
				
				
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("Lng", Double.toString(longitude)));
				namevalue.add(new BasicNameValuePair("Lat",Double.toString(latitude)));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/NearBy.asmx/Exhibitions", namevalue);

				if(json.length()<18)
				{
					Log.d("", "");
				}
				else
				{
					JSONArray arr = null;
					try {
						arr = new JSONArray(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						
					}

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
							
						}
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

				adapterGrid.notifyDataSetChanged();
			}

		}.execute();

	}
	
	public void getVenueWiseExb() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("VenueID", Constants.VenueID));
				namevalue.add(new BasicNameValuePair("type",Constants.exType));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/VenuewiseExhibitions", namevalue);

				try
				{
				if(!json.equalsIgnoreCase("No Data Found") && json != null)
				{
					JSONArray arr = null;
					try {
						arr = new JSONArray(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}

					if(arr!=null)
					{
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
							}
						}
					}
				}

				
				}
				catch(Exception e)
				{
					
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();

				adapterGrid.notifyDataSetChanged();
				Constants.flag = "venueWise";
			}

		}.execute();

	}
	
	
	
	public void getVenueWiseExbConferences() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("VenueID", Constants.VenueID));
				namevalue.add(new BasicNameValuePair("type","conference"));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionList.asmx/VenuewiseExhibitions", namevalue);

				if(!json.equalsIgnoreCase("No Data Found") & json != null)
				{
					JSONArray arr = null;
					try {
						arr = new JSONArray(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
					
					if (arr!=null)
					{
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
				if (pDialog.isShowing())
					pDialog.dismiss();

				adapterGrid.notifyDataSetChanged();
				Constants.VenueID = "";
			}

		}.execute();

	}

	public void getFilterizedData() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				dataGrid.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("fromdate",Constants.FilterFromDate));
				namevalue.add(new BasicNameValuePair("todate",Constants.FilterTODate));
				namevalue.add(new BasicNameValuePair("categoryid",Constants.FilterCategoryID));
				namevalue.add(new BasicNameValuePair("countryid",Constants.FilterCountryID));
				namevalue.add(new BasicNameValuePair("cityid",Constants.FilterCityID));
				namevalue.add(new BasicNameValuePair("type",Constants.FilterType));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Filters.asmx/Exhibitions", namevalue);

				JSONArray arr = null;
				try {

					arr = new JSONArray(json);
					
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

				Constants.IndustryID = "";
				Constants.FilterFromDate = "";
				Constants.FilterTODate = "";
				Constants.FilterCategoryID = "";
				Constants.FilterCountryID = "";
				Constants.FilterCityID = "";
				Constants.FilterType = "";
				
				adapterGrid.notifyDataSetChanged();
				
				if(dataGrid.size()==0)
				{
					dialogboxshow("Message", "No data available.", context);
				}
			}

		}.execute();
	}

	public void dialogboxshow(String title, String msg, final Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialogbox);

		TextView messagetitleis = (TextView) dialog
				.findViewById(R.id.message_title_is);
		TextView messageis = (TextView) dialog.findViewById(R.id.message_is);
		Button okbutton = (Button) dialog.findViewById(R.id.okbutton);

		messagetitleis.setText(title);
		messageis.setText(msg);

		okbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent filter = new Intent(context, Filter.class);
				context.startActivity(filter);
			}
		});

		try {
			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}

	}
}
