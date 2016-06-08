package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.conceptcandy.expomagik.model.AdapterStartOrg;
import com.conceptcandy.expomagik.model.ModelStartOrg;
import com.conceptcandy.expomagik.util.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartOrgNew extends Fragment {

	Context context;
	Double latitude, longitude;
	TextView txtHeading;
	EditText txtSearchBox;
	ImageView icDrawer, search, filter;
	GridView gridView;
	AdapterStartOrg adapterGrid;
	List<ModelStartOrg> dataGrid;
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	String exType = "exhibition";
	String lastIndex = "0";
	boolean paging = false;
	protected String OrganizationId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.grid_view, container, false);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// getExhibitions();
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());

		context = getActivity();
		Constants.homepage = false;

		pDialog = new ProgressDialog(context);

		/*try {
			GPSTracker gps = new GPSTracker(context);
			if (gps.canGetLocation()) {
				try {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();

					Constants.latitude = latitude;
					Constants.longitude = longitude;

				} catch (Exception e) {
					Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
				}
			} else {
				gps.showSettingsAlert();
			}
		} catch (Exception e) {
			latitude = 0.0;
			longitude = 0.0;
			Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
		}*/

		txtHeading = (TextView) view.findViewById(R.id.lbl_top_grid);
		icDrawer = (ImageView) view.findViewById(R.id.ic_drGridExb);
		gridView = (GridView) view.findViewById(R.id.grid_exb1);
		dataGrid = new ArrayList<ModelStartOrg>();

		adapterGrid = new AdapterStartOrg(context, R.layout.row_start_org, dataGrid);
		gridView.setAdapter(adapterGrid);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				  OrganizationId = dataGrid.get(position).getOrganiserid();
				
				 MySharedPreferences.getInstance().putStringKeyValue(getActivity(), "orgnizationid", OrganizationId);
				 
				 Constants.orgID=OrganizationId;
				 
				 if (!Constants.orgID.equals("")) {
					 
					//Constants.flag = "populer";

						
						/*getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new StartOrgSecond(), "fragment").addToBackStack(null)
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();*/
					 
					 Intent openOrganiserDetail = new Intent (getActivity(), StartOrgSecond.class);
					 openOrganiserDetail.putExtra("orgnizationid", OrganizationId);
					 startActivity(openOrganiserDetail);
					 getActivity().finish();
					 
					 
					}
				 
				 
				 
				 
				 
			//getActivity().finish();

				// changeFragment(new StartOrgSecond());
				 
				 
				/*Intent eventDetails = new Intent(context, EventInfo.class);
				eventDetails.putExtra("OrganizationId", OrganizationId);
				startActivity(eventDetails);*/

			}
		});
		
     icDrawer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.openDrawer();
				
			}
		});

		gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) { // TODO
																					// Auto-generated
																					// method
																					// stub
				if (paging == true) {
					// getExhibitionsPaging();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { // TODO

			}
		});

		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {

			if (Constants.flag.equalsIgnoreCase("starred_organisers")) {
				getExhibitions();
			}

		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}

		return view;
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
				// namevalue.add(new BasicNameValuePair("type", exType));
				namevalue.add(new BasicNameValuePair("country", Constants.COUNTRY_NAME));			

				/*String json = UserFunctions
						.loadJson("http://webservices.expomagik.com/StarredOrganisers.asmx/Ogranisers", namevalue);*/

				String json = UserFunctions
					.loadJson("http://webservices.expomagik.com/StarredOrganisers.asmx/OgranisersCountryWise", namevalue);
					
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < arr.length(); i++) {
					try {

						dataGrid.add(new ModelStartOrg(arr.getJSONObject(i).getString("ID"),
								arr.getJSONObject(i).getString("Title"), arr.getJSONObject(i).getString("Imageurl"),
								arr.getJSONObject(i).getString("link"), arr.getJSONObject(i).getString("organiserid")));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Log.i("Griddata", dataGrid.toString());

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
	 public void changeFragment(Fragment targetFragment){
	        //resideMenu.clearIgnoredViewList();
	        getActivity().getSupportFragmentManager()
	                .beginTransaction()
	                .replace(R.id.main_fragment, targetFragment, "fragment")
	                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
	                .commit();
	    }
	 
	 
}
