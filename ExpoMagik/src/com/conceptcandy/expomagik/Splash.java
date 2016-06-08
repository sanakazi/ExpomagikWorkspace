package com.conceptcandy.expomagik;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class Splash extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		getCoutryName();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}

		}, SPLASH_TIME_OUT);
	}
	
	
	public void getCoutryName(){
		try {
			GPSTracker gps = new GPSTracker(this);
			if (gps.canGetLocation()) {
				try {
					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					Constants.latitude = latitude;
					Constants.longitude = longitude;

					Geocoder gcd = new Geocoder(this, Locale.getDefault());
					List<Address> addresses;
					addresses = gcd.getFromLocation(latitude, longitude, 1);
					if (addresses.size() > 0) {
						String currentCity = addresses.get(0).getLocality();
						Constants.currentCity = currentCity;
						Constants.COUNTRY_NAME = addresses.get(0).getCountryName();
					}
				} catch (Exception e) {
					//Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
				}
			} else {
				gps.showSettingsAlert();
			}
		} catch (Exception e) {
			double latitude = 0.0;
			double longitude = 0.0;
			//Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_LONG).show();
		}
	}
}
