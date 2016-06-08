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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityEnquiryForm extends Activity{
	
	private EditText edt_companyname, edt_contactPerson, edt_designation, edt_address, edt_city,
					edt_emailId, edt_contactNumber, edt_comment;
	private Spinner sp_country, sp_enquiryType;
	private String selectedCountry, selectedEnquiry, status;
	private ProgressDialog pDialog;
	private ArrayList<String> countryList;
	private ArrayList<String> countryID;
	private ArrayList<String> enquiryTYpe;
	private ArrayAdapter<String> countryAdapter;
	private ArrayAdapter<String> enquiryAdapter;
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_enquiry_form);
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
		
		init();
		getCountryNames();
	}

	/**
	 * function to initialize this activity
	 */
	private void init() {
		// TODO Auto-generated method stub
		edt_companyname = (EditText) findViewById(R.id.enquiry_company_name);
		edt_contactPerson = (EditText) findViewById(R.id.enquiry_contact_person);
		edt_designation = (EditText) findViewById(R.id.enquiry_designation);
		edt_address = (EditText) findViewById(R.id.enquiry_address);
		edt_city = (EditText) findViewById(R.id.enquiry_city);
		edt_emailId = (EditText) findViewById(R.id.enquiry_email);
		edt_contactNumber = (EditText) findViewById(R.id.enquiry_contact);
		edt_comment = (EditText) findViewById(R.id.enquiry_comment);
		
		sp_country = (Spinner) findViewById(R.id.enquiry_country);
		sp_enquiryType = (Spinner) findViewById(R.id.enquiry_type);
		
		sp_country.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				selectedCountry = countryID.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sp_enquiryType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				selectedEnquiry = enquiryTYpe.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		pDialog = new ProgressDialog(this);
		countryList = new ArrayList<String>();
		countryID = new ArrayList<String>();
		enquiryTYpe = new ArrayList<String>();
		enquiryTYpe.add("Select Enquiry Type");
		enquiryTYpe.add("Exhibitor");
		enquiryTYpe.add("Sponsorship");
		enquiryTYpe.add("Trade Enquiry");
		enquiryAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_spinner, enquiryTYpe);
		sp_enquiryType.setAdapter(enquiryAdapter);
	}
	
	/**
	 * function to update enquiry form to server
	 * @param v
	 */
	public void SubmitEnquiry (View v){
		if (edt_companyname.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter Company Name.", this);
			return;
		}
		else if (edt_contactPerson.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter Contact Person Name.", this);
			return;
		}
		else if (!isValidEmail(edt_emailId.getText().toString())) {
			UserFunctions.dialogboxshow("Message","Please Enter valid E-Mail.", this);
			return;
		}
		else if (edt_designation.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter Designation.", this);
			return;
		}
		else if (edt_address.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter Address.", this);
			return;
		}
		else if (edt_city.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter City.", this);
			return;
		}
		else if (edt_contactNumber.getText().toString().equals("")) {
			UserFunctions.dialogboxshow("Message","Please Enter Mobile number.", this);
			return;
		}else if(selectedCountry.equals("Select Country") || selectedCountry.equals("")){
			UserFunctions.dialogboxshow("Message","Please Select Country", this);
			return;
		}
		else if(selectedEnquiry.equals("Select Enquiry Type") || selectedCountry.equals("")){
			UserFunctions.dialogboxshow("Message","Please Select Enquiry Type", this);
			return;
		}
		else {
			boolean netCheck = UserFunctions.isConnectionAvailable(this);
			if (netCheck == true) {
				submitForm();
			} else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", this);
			}
		}
	}	
	

	/**
	 * function to go back to previous activity
	 * @param v
	 */
	public void Back (View v){
		finish();
	}
	
	/**
	 * function to check email is valid or not
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * function to submit form to server
	 */
	private void submitForm() {
		// TODO Auto-generated method stub
		
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
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				namevalue.add(new BasicNameValuePair("exhibitionid",Constants.exhibitionId));
				namevalue.add(new BasicNameValuePair("CountryID",selectedCountry));
				namevalue.add(new BasicNameValuePair("CompanyName",edt_companyname.getText().toString()));
				namevalue.add(new BasicNameValuePair("ContactPerson",edt_contactPerson.getText().toString()));
				namevalue.add(new BasicNameValuePair("Designation",edt_designation.getText().toString()));
				namevalue.add(new BasicNameValuePair("Address",edt_address.getText().toString()));
				namevalue.add(new BasicNameValuePair("City",edt_city.getText().toString()));
				namevalue.add(new BasicNameValuePair("EmailID",edt_emailId.getText().toString()));
				namevalue.add(new BasicNameValuePair("ContactNumber",edt_contactNumber.getText().toString()));
				namevalue.add(new BasicNameValuePair("EnquiryType",selectedEnquiry));
				namevalue.add(new BasicNameValuePair("Comment",edt_comment.getText().toString() ));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Exhibitors.asmx/InsertExhibitorEnquiry", namevalue);
				
				Log.d("Enquiry Form","json is"+json);
				
				try {
					JSONObject jsonObj = new JSONObject(json);
					status = jsonObj.getString("status");
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
				
				if(status.equals("1")){
					Toast.makeText(getApplicationContext(), "Successfully submited",  Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "Something went wrong",  Toast.LENGTH_SHORT).show();
				}
			}
			
		}.execute();
		
	}
	
	/**
	 * function to get li..st of countries
	 */
	public void getCountryNames(){
		

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
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
				namevalue.clear();
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Filters.asmx/CountryList", namevalue);
				
				Log.d("Enquiry Form","json is"+json);
				
				try {
					JSONArray jsonarr = new JSONArray(json);
					countryList.add("Select Country");
					countryID.add("Select Country");
					for(int i = 0 ; i < jsonarr.length() ; i++){
						JSONObject jsonobj = jsonarr.getJSONObject(i);
						countryList.add(jsonobj.getString("countryName"));
						countryID.add(jsonobj.getString("countryID"));
					}
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
				
				countryAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_spinner, countryList);
				sp_country.setAdapter(countryAdapter);
			}
			
		}.execute();
		
	}

}
