package com.conceptcandy.expomagik;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.conceptcandy.expomagik.util.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Registration extends Activity {

	Context context;
	
	EditText txtUsername, txtMail, txtDesgation, txtCompany, txtMobile, txtPhone, txtPassword, txtCity;
	
	RadioButton rbIndividual;
    RadioButton rbCompany;
    
    Button btnRegister;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	int status = 5;
	
	String individual="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		context = Registration.this;
		
		pDialog = new ProgressDialog(context);
		
		txtUsername = (EditText) findViewById(R.id.reg_username);
		txtMail = (EditText) findViewById(R.id.reg_mail);
		txtDesgation = (EditText) findViewById(R.id.reg_designation);
		txtCompany = (EditText) findViewById(R.id.reg_comapny);
		txtMobile = (EditText) findViewById(R.id.reg_mobile);
		txtPhone = (EditText) findViewById(R.id.reg_phone);
		txtPassword = (EditText) findViewById(R.id.reg_password);
		txtCity = (EditText) findViewById(R.id.reg_city);
		
		rbIndividual = (RadioButton) findViewById(R.id.rdo_indiviual);
		rbCompany = (RadioButton) findViewById(R.id.rdo_company);
		
		btnRegister = (Button) findViewById(R.id.btn_Registration);
		
		txtPassword.setTypeface(Typeface.DEFAULT);
		
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (txtUsername.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Name.", context);
					return;
				}
				else if (txtMail.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter E-Mail.", context);
					return;
				}
				else if (!isValidEmail(txtMail.getText().toString())) {
					UserFunctions.dialogboxshow("Message","Please Enter valid E-Mail.", context);
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
				else if (txtPassword.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Password.", context);
					return;
				}
				else if (txtCity.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter City.", context);
					return;
				}
				else {
					netCheck = UserFunctions.isConnectionAvailable(context);
					if (netCheck == true) {
						register();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
					}
				}

			}
		});
	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
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
				
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("visitorName",txtUsername.getText().toString()));
				namevalue.add(new BasicNameValuePair("Email",txtMail.getText().toString()));
				namevalue.add(new BasicNameValuePair("designation",txtDesgation.getText().toString()));
				namevalue.add(new BasicNameValuePair("companyName",txtCompany.getText().toString()));
				namevalue.add(new BasicNameValuePair("individual",individual));
				namevalue.add(new BasicNameValuePair("mobileNo",txtMobile.getText().toString()));
				namevalue.add(new BasicNameValuePair("phone",phoneNo));
				namevalue.add(new BasicNameValuePair("Password",txtPassword.getText().toString()));
				namevalue.add(new BasicNameValuePair("address",txtCity.getText().toString()));
				namevalue.add(new BasicNameValuePair("cityid","0" ));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorRegistration.asmx/InsertRecord", namevalue);

				JSONObject arr = null;
				try {
					arr = new JSONObject(json);
					
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Registration.this);
					Editor edt = sp.edit();
					
					edt.putString("visitorName", Constants.visitorName);
					edt.putString("Email", Constants.visitorEmail);
					edt.putString("mobileNo", Constants.visitorMobile);
					edt.putString("phone", Constants.visitorPhone);
					edt.putString("address", Constants.visitorCity);
					edt.putString("individual", Constants.visitorIndividual);
					edt.putString("companyName", Constants.visitorCompanyname);
					edt.putString("designation", Constants.visitorDeignation);
									      
				    edt.putString("vCity", Constants.visitorCity);
					edt.commit();
					
					/* MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.name, Constants.visitorName);
				      MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.designation, Constants.visitorDeignation);
				      MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.company, Constants.visitorCompanyname);
				      MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.mobile, arr.getJSONObject(0).getString("mobileNo"));
				      MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.phone, arr.getJSONObject(0).getString("phone"));
				      MySharedPreferences.getInstance().putStringKeyValue(Login.this, Constants.city, arr.getJSONObject(0).getString("City"));*/
					
					
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

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();

				if(status==1)
				{
					status = 5;
					Toast.makeText(context, "Thanking you for registering with ExpoMagik. Welcome aboard!", Toast.LENGTH_LONG).show();
					finish();
				}
				else if(status==0)
				{
					status = 5;
					Toast.makeText(context, "E-Mail ID Already Exists.", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, "Problem while Registration.", Toast.LENGTH_LONG).show();
				}
			}

		}.execute();

	}	
}
