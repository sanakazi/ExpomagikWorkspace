package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.conceptcandy.expomagik.model.ExhibitorAdapter;
import com.conceptcandy.expomagik.model.ExhibitorRow;
import com.conceptcandy.expomagik.util.Utils;

public class ExhibitorList extends Activity {

	Context context;
	
	TextView txtExbName , mainTile;
	
	ImageView back;
	
	ListView listExhibitors;
	ExhibitorAdapter adapterExb;
	List<ExhibitorRow> dataExb;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String ExhibitionID, ExhibitionName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibitors);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = ExhibitorList.this;
		
		pDialog = new ProgressDialog(context);
		
		Intent i = getIntent();
		ExhibitionID = i.getStringExtra("ExhibitionID");
		ExhibitionName = i.getStringExtra("ExhibitionName");
		
		txtExbName = (TextView)findViewById(R.id.elist_title);
		txtExbName.setText(ExhibitionName);
		
		mainTile = (TextView) findViewById(R.id.ei_mainTitle);
		mainTile.setText("Exhibitors List");
		
		back = (ImageView) findViewById(R.id.ei_back_arrow);
 		
 		listExhibitors = (ListView)findViewById(R.id.listExhibitors);
		dataExb = new ArrayList<ExhibitorRow>();
		adapterExb = new ExhibitorAdapter(context, R.layout.row_exhibitor, dataExb);
		listExhibitors.setAdapter(adapterExb);
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getExhibitorList();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	public void getExhibitorList() {
		
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
				namevalue.add(new BasicNameValuePair("exhibitionid",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Exhibitors.asmx/ExhibitorList", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							dataExb.add(new ExhibitorRow(
									arr.getJSONObject(i).getString("ExhibitorID"),
									arr.getJSONObject(i).getString("fullName"), 
									arr.getJSONObject(i).getString("boothNumbers"), 
									arr.getJSONObject(i).getString("city"), 
									arr.getJSONObject(i).getString("country"),
									arr.getJSONObject(i).getString("compLogo")
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
				
				adapterExb.notifyDataSetChanged();
				
			}

		}.execute();

	}
}
