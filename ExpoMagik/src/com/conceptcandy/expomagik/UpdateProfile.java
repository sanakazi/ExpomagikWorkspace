package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.conceptcandy.expomagik.util.Utils;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class UpdateProfile extends Activity {
	
	String visitorNm , designation , companyName;

	Context context;
	
	EditText txtUsername, txtDesgation, txtCompany, txtMobile, txtPhone, txtCity;
	
	RadioButton rbIndividual;
    RadioButton rbCompany;
    
    Button btnRegister;
    
    ImageView ic_drMyExbs;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	int status = 5;
	
	String individual;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editsignup);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = UpdateProfile.this;
		
		pDialog = new ProgressDialog(context);
		
		txtUsername = (EditText) findViewById(R.id.reg_username);
		txtDesgation = (EditText) findViewById(R.id.reg_designation);
		txtCompany = (EditText) findViewById(R.id.reg_comapny);
		txtMobile = (EditText) findViewById(R.id.reg_mobile);
		txtPhone = (EditText) findViewById(R.id.reg_phone);
		txtCity = (EditText) findViewById(R.id.reg_city);
		
		
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UpdateProfile.this);
		  //Editor edt = sp.edit();
		  
		  
		  String vName=sp.getString("visitorName", Constants.visitorName);
		  String vEmail = sp.getString("Email", Constants.visitorEmail);
		  String vMobile = sp.getString("mobileNo", Constants.visitorMobile);
		  String vPhone = sp.getString("phone", Constants.visitorPhone);
		  String vIndividual = sp.getString("individual", Constants.visitorIndividual);
		  String vCmpny = sp.getString("companyName", Constants.visitorCompanyname);
		  String vDesign = sp.getString("designation", Constants.visitorDeignation);
		  String vCity = sp.getString("City", Constants.visitorCity);
		  
		  txtUsername.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.name, ""));
		  txtDesgation.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.designation, ""));
		  txtCompany.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.company, ""));
		  txtMobile.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.mobile, ""));
		  txtPhone.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.phone, ""));
		  txtCity.setText(MySharedPreferences.getInstance().getString(UpdateProfile.this, Constants.city, ""));
		  
		  if(txtUsername.getText().toString().isEmpty()){
			  getProfile();
		  }
		
		
		ic_drMyExbs = (ImageView) findViewById(R.id.ic_drMyExbs);
		
		
		ic_drMyExbs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//MainActivity.openDrawer();
				finish();

			}
		});
		
		rbIndividual = (RadioButton) findViewById(R.id.rdo_indiviual);
		rbCompany = (RadioButton) findViewById(R.id.rdo_company);
		
		btnRegister = (Button) findViewById(R.id.btn_Registration);
		
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			private String visitorMobileNo;
			private String visitorPhoneNo;
			private String visitorCity;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if (txtUsername.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Name.", context);
					return;
				}
				
				else if (rbCompany.isChecked() && txtDesgation.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Designation.", context);
					return;
				}
				else if (rbCompany.isChecked() && txtCompany.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Company.", context);
					return;
				}
				else if (txtMobile.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Mobile number.", context);
					return;
				}
				
				else if (txtCity.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter City.", context);
					return;
				}
				else {
					netCheck = UserFunctions.isConnectionAvailable(context);
					if (netCheck == true) {
						/*visitorNm = txtUsername.getText().toString();
						designation=txtDesgation.getText().toString();
						companyName=txtCompany.getText().toString();
						Constants.visitorName = visitorNm;
						Constants.visitorDeignation = designation;
						Constants.visitorCompanyname = companyName;
*/
						
						
						
						visitorNm = txtUsername.getText().toString();
					    designation=txtDesgation.getText().toString();
					    companyName=txtCompany.getText().toString();
					    visitorMobileNo = txtMobile.getText().toString();
					    visitorPhoneNo=txtPhone.getText().toString();
					    visitorCity=txtCity.getText().toString();
					    Constants.visitorName = visitorNm;
					    Constants.visitorDeignation = designation;
					    Constants.visitorCompanyname = companyName;
					    Constants.visitorMobile = visitorMobileNo;
					    Constants.visitorPhone = visitorPhoneNo;
					    Constants.visitorCity = visitorCity;
					     
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, Constants.name, visitorNm);
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, Constants.designation, designation);
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, Constants.company, companyName);
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, Constants.mobile, visitorMobileNo);
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, Constants.phone, visitorPhoneNo);
					     MySharedPreferences.getInstance().putStringKeyValue(UpdateProfile.this, "vCity", visitorCity);
					      
						
						register();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
					}
				}

			}
		});
	

		
	}
	
	public void register() {
		
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
				//android.os.Debug.waitForDebugger();
				// **Code**
				String phoneNo = txtPhone.getText().toString();
				if(phoneNo.equalsIgnoreCase(""))
				{
					phoneNo = "0";
				}

				if(rbIndividual.isChecked())
				{
					individual = "1";
				}
				else
				{
					individual = "0";
				}
				
				String visitorId = Constants.visitorId;
				
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UpdateProfile.this);
				Editor edt = sp.edit();
				edt.putString("vID", visitorId);
				edt.putString("vName", visitorNm);
				edt.putString("vMail", txtDesgation.getText().toString());
				edt.putString("vCountry", Constants.visitorCountry);
				edt.putString("vCity", txtCity.getText().toString());
				edt.putString("vIndividual", individual);
				edt.putString("vCompanyName", txtCompany.getText().toString());
				edt.putString("vDesignation", txtDesgation.getText().toString());
				edt.commit();
				
				
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("visitorid",visitorId));
				namevalue.add(new BasicNameValuePair("visitorName",visitorNm));
				namevalue.add(new BasicNameValuePair("designation",txtDesgation.getText().toString()));
				namevalue.add(new BasicNameValuePair("companyName",txtCompany.getText().toString()));
				namevalue.add(new BasicNameValuePair("individual",individual));
				namevalue.add(new BasicNameValuePair("mobileNo",txtMobile.getText().toString()));
				namevalue.add(new BasicNameValuePair("phone",phoneNo));
				namevalue.add(new BasicNameValuePair("address",txtCity.getText().toString()));
				namevalue.add(new BasicNameValuePair("cityid","14535" ));
				
				Log.d("Update Profile", "before making load json call***************************"+visitorId);
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorRegistration.asmx/UpdateRecord", namevalue);

				JSONObject arr = null;
				try {
					arr = new JSONObject(json);
					
					String regStatus = arr.getString("status");
				
					
					if(regStatus.equalsIgnoreCase("1"))
					{
						status = 1;
					}
					if(regStatus.equalsIgnoreCase("0"))
					{
						status = 0;
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
				}

				return null;
			}

			@SuppressLint("CommitPrefEdits")
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				

				if(status==1)
				{
					status = 5;
					Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
					Constants.updateProfile = true;
					finish();
				}
			
				
				else
				{
					Toast.makeText(context, "Problem while Updation.", Toast.LENGTH_LONG).show();
				}
			}

		}.execute();

	}	
	
	
public void checkLogin() {
		
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

				String cl_uname = Login.cl_uname;
				String cl_pwd = Login.cl_pwd;
				
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("name",cl_uname));
				namevalue.add(new BasicNameValuePair("pass",cl_pwd));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorLoginNew.asmx/VisitorLogin", namevalue);

				JSONArray arr = null;
				try {
					
					arr = new JSONArray(json);
															
					Constants.visitorId = arr.getJSONObject(0).getString("visitorid");
					Constants.visitorName = arr.getJSONObject(0).getString("Name");
					Constants.visitorCountry = arr.getJSONObject(0).getString("Country");
					Constants.visitorCity = arr.getJSONObject(0).getString("address");
					Constants.visitorIndividual = arr.getJSONObject(0).getString("individual");
					Constants.visitorCompanyname = arr.getJSONObject(0).getString("Companyname");
					Constants.visitorDeignation = arr.getJSONObject(0).getString("Deignation");
					
					status = 1;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();

				if(status==1)
				{
					status = 0;
					
					Toast.makeText(context, "Welcome " + Constants.visitorName, Toast.LENGTH_LONG).show();
					
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UpdateProfile.this);
					Editor edt = sp.edit();
					edt.putString("vID", Constants.visitorId);
					edt.putString("vName", Constants.visitorName);
					edt.putString("vMail", Constants.visitorEmail);
					edt.putString("vCountry", Constants.visitorCountry);
					edt.putString("vCity", Constants.visitorCity);
					edt.putString("vIndividual", Constants.visitorIndividual);
					edt.putString("vCompanyName", Constants.visitorCompanyname);
					edt.putString("vDesignation", Constants.visitorDeignation);
					edt.commit();
					
					
				}
				
			}

		}.execute();

	}


public void getProfile() {

// Fetch Data
new AsyncTask<String, String, String>() {

	private String responceString, status="";
	
	private String imageURL;
	
	ArrayList<NameValuePair> nameValuePairsget;
	
	String response = "null";
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
//				pDialog2.setMessage("Relax for a while...");
//				pDialog2.setCancelable(false);
//				pDialog2.show();
			}

	@Override
	protected String doInBackground(String... params) {
		
		nameValuePairsget = new ArrayList<NameValuePair>();
		nameValuePairsget.add(new BasicNameValuePair("visitorid",Constants.visitorId));
		//nameValuePairs.add(new BasicNameValuePair("profileimage",iagepath));
		
		Log.d("ProfileActivity", "ProfilePicList:"+nameValuePairsget);
		
			responceString = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorDetail.asmx/VisitorInfo", nameValuePairsget);

		
		Log.d("ProfileActivity", "Profile resp:"+responceString);
		
		JSONArray arr=null;
		
		try {				
			arr=new JSONArray(responceString);
			
			Constants.vDP= arr.getJSONObject(0).getString("profileimage");
			
			if( Constants.vDP.contains("licdn") ){
				imageURL = Constants.vDP;
			} else {				
			imageURL = "http://webservices.expomagik.com/VisitorProfileImage/"+Constants.vDP;
			}
			status = "1"; 
				 // imageLoader.DisplayImage(imagepath, dp);
				 
			} catch (JSONException e) {
				status = "0";
				e.printStackTrace();
			}  
	
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		if (pDialog2.isShowing())
//			pDialog2.dismiss();

		Log.d("Profile", "iamgeurl is-"+imageURL);
		if(status.equals("1")){
			
			//Picasso.with(context).load(imageURL).into(dp);
			
		}else{
			//Toast.makeText(getActivity(), "Profile picture not downloaded.", Toast.LENGTH_LONG).show();
		}
		
		try {
			JSONArray jsonARR = new JSONArray(responceString);
			JSONObject objJson = jsonARR.getJSONObject(0);
						
			txtUsername.setText(objJson.getString("Name"));
			txtDesgation.setText(objJson.getString("Deignation"));
			txtCompany.setText(objJson.getString("Companyname"));
			txtMobile.setText(objJson.getString("mobileNo"));
			txtPhone.setText(objJson.getString("phone"));
			//txtCity.setText(objJson.getString("City"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//dp.setImageBitmap(imagepath);
		
					
	}

}.execute();

}
}

