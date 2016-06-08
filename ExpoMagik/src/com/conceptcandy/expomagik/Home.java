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
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.conceptcandy.expomagik.model.EventAdapter;
import com.conceptcandy.expomagik.model.EventRow;
import com.conceptcandy.expomagik.model.HomePagerRow;
import com.conceptcandy.expomagik.model.PagerExbAdapter;
import com.conceptcandy.expomagik.util.Utils;
import com.google.android.gms.analytics.Tracker;
import com.nineoldandroids.view.ViewHelper;

public class Home extends Fragment{

	Button inCity, topTrending;
	ImageView icDrawer, filter, search, linePink, lineBlue;
	LinearLayout ll_nearMe, ll_upcoming, ll_trending, ll_populer;

	EditText txtSearchBox;

	ViewPager pagerExb;
	PagerExbAdapter pagerAdapter;
	List<HomePagerRow> pagerData;

	EventAdapter adapter;
	List<EventRow> data;

	Context context;

	Double latitude, longitude;
	LocationManager lm;

	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;

	String searchText = "";
	String currentCity = "";
	String exType = "exhibition";
	
	private Tracker mTracker;
	
	public final static int PAGES = 10;
	// You can choose a bigger number for LOOPS, but you know, nobody will fling
	// more than 1000 times just in order to test your "infinite" ViewPager :D 
	public final static int LOOPS = 10; 
	public final static int FIRST_PAGE = 9;
	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.8f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Constants.homepage = true;

		if (!Constants.POID.equals("")) {
			
			/*Constants.flag = "drawerorg";
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment, new StartOrgSecond(), "fragment")
					.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();*/
			
			//getActivity().finish();
		}
		if (Constants.IndustryID.equals("Filter")) {
			netCheck = UserFunctions.isConnectionAvailable(getActivity());
			if (netCheck == true) {
				Constants.flag = "filter";

				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
			}else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", getActivity());
			}
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.home, container, false);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Constants.homepage = true;

		context = getActivity();

		txtSearchBox = (EditText) view.findViewById(R.id.searchBox);

		Utils.trackActivity(getActivity(), mTracker, getClass().getSimpleName());

		icDrawer = (ImageView) view.findViewById(R.id.ic_drH);
		filter = (ImageView) view.findViewById(R.id.ic_hFilter);
		search = (ImageView) view.findViewById(R.id.imgSearch);

		linePink = (ImageView) view.findViewById(R.id.linePinkSelcted);
		lineBlue = (ImageView) view.findViewById(R.id.lineBlueSelcted);
		inCity = (Button) view.findViewById(R.id.tab_InCity);
		topTrending = (Button) view.findViewById(R.id.tab_TopTrending);
			
		
		ll_nearMe = (LinearLayout) view.findViewById(R.id.home_ll_nearme);
		ll_upcoming = (LinearLayout) view.findViewById(R.id.home_ll_upcoming);
		ll_trending = (LinearLayout) view.findViewById(R.id.home_ll_trending);
		ll_populer = (LinearLayout) view.findViewById(R.id.home_ll_populer);

		data = new ArrayList<EventRow>();
		adapter = new EventAdapter(context, R.layout.row_event_list, data, data);

		pagerData = new ArrayList<HomePagerRow>();
		pagerExb = (ViewPager) view.findViewById(R.id.pagerEvents);
		pagerAdapter = new PagerExbAdapter(context, pagerData);
		pagerExb.setAdapter(pagerAdapter);
		pagerExb.setOffscreenPageLimit(2);
		pagerExb.setPageTransformer(true, new DepthPageTransformer());
		
		 //pagerExb.setClipToPadding(false);
		 //pagerExb.setPageMargin(20);

		pagerExb.setClipToPadding(false);
		pagerExb.setPageMargin(24);
		pagerExb.setPadding(88, 3, 88, 3);
		pagerExb.setOffscreenPageLimit(3);

		MainActivity.resideMenu.addIgnoredView(pagerExb);

		pDialog = new ProgressDialog(getActivity());
		
		/*try {
			GPSTracker gps = new GPSTracker(context);
			if (gps.canGetLocation()) {
				try {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();

					Constants.latitude = latitude;
					Constants.longitude = longitude;

					Geocoder gcd = new Geocoder(context, Locale.getDefault());
					List<Address> addresses;
					addresses = gcd.getFromLocation(latitude, longitude, 1);
					if (addresses.size() > 0) {
						currentCity = addresses.get(0).getLocality();
						Constants.currentCity = currentCity;
					}
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

		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constants.flag = "searchByText";
				Constants.searchText = txtSearchBox.getText().toString();
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
			}
		});

		ll_nearMe.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constants.flag = "nearMe";
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
			}
		});

		ll_upcoming.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constants.flag = "upcoming";
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
			}
		});

		ll_trending.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constants.flag = "trending";
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new ExhibitonsGrid(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
			}
		});

		ll_populer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/*
				 * Intent po = new Intent(context, PopulerOrganizers.class);
				 * startActivity(po);
				 */

				Constants.flag = "starred_organisers";
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new StartOrgNew(), "fragment")
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

			}
		});

		inCity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linePink.setVisibility(View.VISIBLE);
				lineBlue.setVisibility(View.INVISIBLE);

				exType = "exhibition";

				getPagerExhibitions();
			}
		});

		topTrending.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineBlue.setVisibility(View.VISIBLE);
				linePink.setVisibility(View.INVISIBLE);

				exType = "conference";

				getPagerExhibitions();
			}
		});

		icDrawer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.openDrawer();

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

		netCheck = UserFunctions.isConnectionAvailable(getActivity());
		if (netCheck == true) {
			getPagerExhibitions();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", getActivity());
		}

		return view;
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

				pagerData.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("type", exType));
				namevalue.add(new BasicNameValuePair("country", Constants.COUNTRY_NAME));
				
				/*String json = UserFunctions.loadJson("http://webservices.expomagik.com/MobileHomeList.asmx/Exhibitions",
						namevalue);*/
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/MobileHomeList.asmx/ExhibitionsCountryWise",
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

						try {
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
						}
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

				pagerExb.setAdapter(pagerAdapter);
				pagerAdapter.notifyDataSetChanged();
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

				data.clear();
				adapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// **Code**

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("categoryID", Constants.IndustryID));

				String json = UserFunctions.loadJson(
						"http://webservices.expomagik.com/ExhibitionListCategorywise.asmx/CategorywiseExhibitions",
						namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < arr.length(); i++) {
					try {

						data.add(new EventRow(arr.getJSONObject(i).getString("ExhibitionID"),
								arr.getJSONObject(i).getString("startdate"),
								arr.getJSONObject(i).getString("ExhibitionName"),
								arr.getJSONObject(i).getString("City") + "-"
										+ arr.getJSONObject(i).getString("Country"),
								arr.getJSONObject(i).getString("industry"), ""));

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

				Constants.IndustryID = "";
				adapter.notifyDataSetChanged();

				if (data.size() == 0) {
					UserFunctions.dialogboxshow("Message", "No data available.", context);
				}
			}

		}.execute();
	}
	
	public class DepthPageTransformer implements PageTransformer {
	    private static final float MIN_SCALE = 0.95f;
	    
	    public void transformPage(View view, float position) {
	    int pageWidth = view.getWidth();
	    
	    if ( position < 0 ){
	    	ViewHelper.setAlpha(view,0.5f);
	    	//ViewHelper.setTranslationX(view,pageWidth * -position);

	        //Scale the page down (between MIN_SCALE and 1)
	        float scaleFactor = MIN_SCALE
	                + (1 - MIN_SCALE) * (1 - Math.abs(position));
	        ViewHelper.setScaleX(view,scaleFactor);
	        ViewHelper.setScaleY(view,scaleFactor);
	    }
	    
	    if( position >= 0 ){
	    	ViewHelper.setAlpha(view,1f);
	    	ViewHelper.setScaleX(view,1);
	        ViewHelper.setScaleY(view,1);
	    } 
	    
	    if( position >= 1 ){
	    	ViewHelper.setAlpha(view,0.5f);
	    	float scaleFactor = MIN_SCALE
		                + (1 - MIN_SCALE) * (1 - Math.abs(position));
	        ViewHelper.setScaleX(view,scaleFactor);
	        ViewHelper.setScaleY(view,scaleFactor);
	    } 
	    
	    /*
	    if (position < -1) { // [-Infinity,-1)
	        // This page is way off-screen to the left.
	        ViewHelper.setAlpha(view,0);

	    } else if (position <= 0) { // [-1,0]
	        // Use the default slide transition when moving to the left page
	        //ViewHelper.setAlpha(view,1);
	        //ViewHelper.setTranslationX(view,0);
	        //ViewHelper.setScaleX(view,1);
	        //ViewHelper.setScaleY(view,1);

	    } else if (position <= 1) { // (0,1]
	        // Fade the page out.
	        ViewHelper.setAlpha(view,1 - position);

	        // Counteract the default slide transition
	        ViewHelper.setTranslationX(view,pageWidth * -position);

	        //Scale the page down (between MIN_SCALE and 1)
	        float scaleFactor = MIN_SCALE
	                + (1 - MIN_SCALE) * (1 - Math.abs(position));
	        ViewHelper.setScaleX(view,scaleFactor);
	        ViewHelper.setScaleY(view,scaleFactor);

	    } else { // (1,+Infinity]
	        // This page is way off-screen to the right.
	        ViewHelper.setAlpha(view,0);
	    }*/
	    
	    }
	}

	
}