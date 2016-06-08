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

import com.conceptcandy.expomagik.model.SpeakerAdapter;
import com.conceptcandy.expomagik.model.SpeakerRow;
import com.conceptcandy.expomagik.util.Utils;

public class Speakers extends Activity {

	Context context;
	
	TextView txtExbName , mainTitle;

	ListView listSpeakers;
	SpeakerAdapter adapterSpeakers;
	List<SpeakerRow> dataSpeakers;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	ImageView back;
	
	String ExhibitionID, ExhibitionName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speakers);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = Speakers.this;
		
		pDialog = new ProgressDialog(context);
		
		Intent i = getIntent();
		ExhibitionID = i.getStringExtra("ExhibitionID");
		ExhibitionName = i.getStringExtra("ExhibitionName");
		
		txtExbName = (TextView)findViewById(R.id.spklist_title);
		txtExbName.setText(ExhibitionName);
		
		back = (ImageView) findViewById(R.id.ei_back_arrow);
		
		
		mainTitle = (TextView) findViewById(R.id.ei_mainTitle);
		mainTitle.setText("Speakers List");
 		
		listSpeakers= (ListView)findViewById(R.id.listSpeakers);
		dataSpeakers= new ArrayList<SpeakerRow>();
		adapterSpeakers = new SpeakerAdapter(context, R.layout.row_speakers, dataSpeakers);
		listSpeakers.setAdapter(adapterSpeakers);
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getSpeakersList();
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
	
	
	public void getSpeakersList() {
		
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
				namevalue.add(new BasicNameValuePair("ExhibitionID",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Speakers.asmx/SpeakerList", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {

							dataSpeakers.add(new SpeakerRow(
									arr.getJSONObject(i).getString("speakerID"),
									arr.getJSONObject(i).getString("agenda"),
									arr.getJSONObject(i).getString("name"),
									arr.getJSONObject(i).getString("topic"),
									arr.getJSONObject(i).getString("brief"),
									arr.getJSONObject(i).getString("photo")
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
				
				adapterSpeakers.notifyDataSetChanged();
				
			}

		}.execute();

	}
}
