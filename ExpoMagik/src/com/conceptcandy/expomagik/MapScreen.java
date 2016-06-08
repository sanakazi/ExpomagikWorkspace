package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.conceptcandy.expomagik.banner.JSONParser;
import com.conceptcandy.expomagik.model.WorkaroundMapFragment;
import com.conceptcandy.expomagik.util.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapScreen extends FragmentActivity {
	
	Context context;
	
	// Google Map
	private GoogleMap googleMap;
 
    // latitude and longitude   
	double latitude, longitude;
	double Dlatitude, Dlongitude;
	
	GPSTracker gps ;
	
	
	//get location from location
	public void getLocation() {
		
		try
		{
			
			if(gps.canGetLocation())
			{ 
				try
				{
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					
					/*Constants.latitude = latitude;
					Constants.longitude = longitude;*/
					showLLocation();
					
				}
				catch(Exception e)
				{
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
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		gps.stopUsingGPS();
	}
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_screen);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		gps = new GPSTracker(context);
		
		context = MapScreen.this;
		
		initilizeMap();
		
		((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
	          @Override
	          public void onTouch() {
	              
	          }
	     });
		
		Intent i = getIntent();
		Dlatitude = Double.parseDouble( i.getStringExtra("latitude") );
		Dlongitude = Double.parseDouble( i.getStringExtra("longitude") );
		
		Log.d("MapScreen", "Destination lan and lat is-"+Dlatitude+"-"+Dlongitude);
		MarkerOptions marker = new MarkerOptions().position(new LatLng(Dlatitude, Dlongitude)).title(Constants.venue);
		googleMap.addMarker(marker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
				new LatLng(latitude, longitude)).zoom(13).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		//googleMap.isMyLocationEnabled();
		googleMap.setMyLocationEnabled(true);
		
		//getLocation();
		
		navigationMap();
		
    }
    
    public void navigationMap(){
    	Uri gmmIntentUri = Uri.parse("google.navigation:q="+Dlatitude+","+Dlongitude);
    	Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    	mapIntent.setPackage("com.google.android.apps.maps");
    	startActivity(mapIntent);
    }
    
    private void initilizeMap() {
        /*if (googleMap == null) {
        	
        	googleMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create map", Toast.LENGTH_SHORT).show();
            }
        }*/
    	Log.d("MapScreen", "latitude and longitude is- "+latitude+"-"+longitude);
    	if (googleMap == null) {
			googleMap = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Start Point"));

			LatLng coordinate = new LatLng(latitude, longitude);
			CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
			googleMap.animateCamera(yourLocation);

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
    }
	
	public void showLLocation()
	{
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(Constants.venue);
		googleMap.addMarker(marker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
				new LatLng(latitude, longitude)).zoom(13).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		//googleMap.isMyLocationEnabled();
		googleMap.setMyLocationEnabled(true);
		
		new connectAsyncTask(makeURL()).execute();
	}
	
	public String makeURL() {
		StringBuilder urlString = new StringBuilder();
		urlString.append("https://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(Dlatitude));
		urlString.append(",");
		urlString.append(Double.toString(Dlongitude));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		urlString.append("&key=AIzaSyBm_UrCU1I9PevpVb0ZHEuT3GYBxG_cQQI");//api key changed
		return urlString.toString();
	}
	
	public class connectAsyncTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		String url;

		connectAsyncTask(String urlPass) {
			url = urlPass;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MapScreen.this);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			Log.d("MapScreen", "URL is-"+url);
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result != null) {
				drawPath(result);
			}
		}
	}
	
	public void drawPath(String result) {

		try {
			// Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);
			
			/*Abhay*/
			//fit the route on screen
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			 for(int i = 0; i < list.size();i++){
			        builder.include(list.get(i));
			    }
			LatLngBounds bounds = builder.build();
			int padding = 20;
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
			
			Polyline line = googleMap
					.addPolyline(new PolylineOptions().addAll(list).width(12).color(Color.parseColor("#05b1fb"))// Google
																												// maps
																												// blue
																												// color
							.geodesic(true));
			googleMap.animateCamera(cu);

		} catch (JSONException e) {

		}
	}

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}
}
