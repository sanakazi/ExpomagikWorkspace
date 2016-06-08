package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.conceptcandy.expomagik.model.CityAdapter;
import com.conceptcandy.expomagik.model.IndustryRow;
import com.conceptcandy.expomagik.util.Utils;

public class PopulerOrganizers extends Fragment {

	Context context;

	ImageView back,searchOrganiser;
	TextView txtHeading;
	EditText searchItem;
	
	String searchText = "";
	
	ListView listPO;
	CityAdapter adapterPO;
	List<IndustryRow> dataPO;
	List<IndustryRow> fdataPO;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.city_list, container, false);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());
		
		context = getActivity();
		
		pDialog = new ProgressDialog(context);
		searchItem = (EditText) v.findViewById(R.id.searchItems);
		back = (ImageView) v.findViewById(R.id.cl_back_arrow);
		searchOrganiser = (ImageView) v.findViewById(R.id.imgSearch);
		
		txtHeading = (TextView) v.findViewById(R.id.cityListHeading);
		txtHeading.setText("Organisers");
		
 		listPO = (ListView) v.findViewById(R.id.listCities);
 		dataPO = new ArrayList<IndustryRow>();
 		fdataPO = new ArrayList<IndustryRow>();
		adapterPO = new CityAdapter(context, R.layout.row_category, dataPO, fdataPO);
		listPO.setAdapter(adapterPO);
		
		listPO.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Constants.POID = dataPO.get(position).getId();
				Constants.flag = "drawerorg";
				//Log.i("PopulerOrg position id", Constants.POID);	
				
				Intent i = new Intent(getActivity(), StartOrgSecond.class);
				i.putExtra("orgnizationid", dataPO.get(position).getId());
				startActivity(i);
				getActivity().finish();
				
				// Bundle bundle= new Bundle();
				// bundle.putString("list", Constants.POID);
				 
				//finish();
			}
		});
		
		searchOrganiser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchText = searchItem.getText().toString().trim();
				getPOList();
			}
		});
		
		adapterPO.filter("");
		
		searchItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				/*String items = searchItem.getText().toString().toLowerCase(Locale.getDefault());
				adapterPO.filter(items);*/
				String items = searchItem.getText().toString();
				
				if(items.isEmpty()){
					searchText = "";
					getPOList();
				}
				
				
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
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
				MainActivity.openDrawer();
			}
		});
		
		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getPOList();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		return v;
	}
	
	
	public void getPOList() {
		
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
				String json;
				
				dataPO.clear();
				fdataPO.clear();
				
				if(searchText == "") {
					json = UserFunctions.loadJson("http://webservices.expomagik.com/OrganisersList.asmx/Ogranisers", namevalue);
				}else{					
					namevalue.add(new BasicNameValuePair("srchtext", searchText));
					json=  UserFunctions.loadJson("http://webservices.expomagik.com/OrganisersList.asmx/SearcOrganizer", namevalue);
					searchText = "";
				}

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							dataPO.add(new IndustryRow(
									arr.getJSONObject(i).getString("ID"), 
									arr.getJSONObject(i).getString("NoOFExhibition"),
									arr.getJSONObject(i).getString("fullName"),
									arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
									));
							
							fdataPO.add(new IndustryRow(
									arr.getJSONObject(i).getString("ID"), 
									arr.getJSONObject(i).getString("NoOFExhibition"),
									arr.getJSONObject(i).getString("fullName"),
									arr.getJSONObject(i).getString("NoOfUpcomingExhibition")
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
				
				adapterPO.notifyDataSetChanged();
			}

		}.execute();

	}
	
	
	private static long back_pressed_time;
	private static long PERIOD = 2000;
	
	/*@Override
	public void onBackPressed() {
		if (Constants.homepage == true) {
			if (back_pressed_time + PERIOD > System.currentTimeMillis())
				super.onBackPressed();
			else
				Toast.makeText(getBaseContext(), "Press back again to exit..", Toast.LENGTH_SHORT).show();
			back_pressed_time = System.currentTimeMillis();
		} else {
			netCheck = UserFunctions.isConnectionAvailable(PopulerOrganizers.this);
			if (netCheck == true) {
				//changeFragment(new Home());
			} else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", PopulerOrganizers.this);
			}
		}
	}
*/
 
    
    
    
    
 
}