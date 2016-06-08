package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.conceptcandy.expomagik.model.VisitorAdapter;
import com.conceptcandy.expomagik.model.VisitorRow;
import com.conceptcandy.expomagik.util.Utils;

public class Visitors extends Activity {

	Context context;
	
	TextView txtExbName , mainTitle;
	
	ImageView back;

	ListView listVisitors;
	VisitorAdapter adapterVisitors;
	List<VisitorRow> dataVisitors;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String ExhibitionID, ExhibitionName;
	
	String vtcID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitors);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = Visitors.this;
		
		pDialog = new ProgressDialog(context);
		
		Intent i = getIntent();
		ExhibitionID = i.getStringExtra("ExhibitionID");
		ExhibitionName = i.getStringExtra("ExhibitionName");
		
		txtExbName = (TextView)findViewById(R.id.vislist_title);
		txtExbName.setText(ExhibitionName);
		
		mainTitle = (TextView) findViewById(R.id.ei_mainTitle);
		mainTitle.setText("Visitors List");
		
		back = (ImageView) findViewById(R.id.ei_back_arrow);
 		
		listVisitors = (ListView)findViewById(R.id.listVisitors);
		dataVisitors= new ArrayList<VisitorRow>();
		adapterVisitors = new VisitorAdapter(context, R.layout.row_visitors, dataVisitors);
		listVisitors.setAdapter(adapterVisitors);
		
		listVisitors.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				vtcID = dataVisitors.get(arg2).getVisitorID();
				
				dialogboxshow("Message", "Connect to this Visitor.", context);
			}
		});
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getVisitorList();
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
	
	
	public void getVisitorList() {
		
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
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorList.asmx/VisitorsList", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {

							dataVisitors.add(new VisitorRow(
									arr.getJSONObject(i).getString("visitorID"), 
									arr.getJSONObject(i).getString("visitorName"),
									arr.getJSONObject(i).getString("Email"),
									arr.getJSONObject(i).getString("mobileNo"), 
									arr.getJSONObject(i).getString("address"),
									arr.getJSONObject(i).getString("companyName"),
									arr.getJSONObject(i).getString("designation")
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
				
				adapterVisitors.notifyDataSetChanged();
				
			}

		}.execute();

	}
	
	public void connectVisitor() {
		
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
				namevalue.add(new BasicNameValuePair("VisitorToConnectID",vtcID));
				namevalue.add(new BasicNameValuePair("visitorid",Constants.visitorId));
				namevalue.add(new BasicNameValuePair("exhibitionid",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Connect.asmx/InsertRecordConnectToVisitor", namevalue);
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				Toast.makeText(context, "Connected Successfully.", Toast.LENGTH_LONG).show();
				
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
		okbutton.setText("Connect");

		messagetitleis.setText(title);
		messageis.setText(msg);

		okbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				if(!Constants.visitorId.equalsIgnoreCase(""))
				{
					connectVisitor();
				}
				else
				{
					Constants.redirectToHome = false;
					Intent login = new Intent(context, Login.class);
					startActivity(login);
				}				
			}
		});

		try {
			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}

	}
}
