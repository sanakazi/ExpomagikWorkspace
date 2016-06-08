package com.conceptcandy.expomagik;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.conceptcandy.expomagik.util.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Filter extends Activity {

	Context context;

	ImageView back;
	LinearLayout llFromDate, llToDate;
	TextView fromDate, fromMonthYear, fromDay, toDate, toMonthYear, toDay;
	Spinner spnType, spnCategory, spnCountry, spnCity;
	
	Button btnFilter;
	
	private Calendar cal;
	private int day , today;
	private int month , tomonth ;
	private int year , toyear;
	
	public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String sendFromDate, sendToDate, currentDate;
	
	String cateName, contName, cityName;
	
	List<String> sptp;
	ArrayAdapter<String> sptpAdapter;
	
	List<String> cate;
	HashMap<String, String> cateWithId;
	ArrayAdapter<String> cateAdapter;
	
	List<String> cont;
	HashMap<String, String> contWithId;
	ArrayAdapter<String> contAdapter;
	
	List<String> city;
	HashMap<String, String> cityWithId;
	ArrayAdapter<String> cityAdapter;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());

		context = Filter.this;

		pDialog = new ProgressDialog(context);
		
		back = (ImageView)findViewById(R.id.fl_back_arrow);
		
		fromDate = (TextView)findViewById(R.id.txtFFromDate);
		fromMonthYear = (TextView)findViewById(R.id.txtFFromMonthYear);
		fromDay = (TextView)findViewById(R.id.txtFFromDay);
		toDate = (TextView)findViewById(R.id.txtFToDate);
		toMonthYear = (TextView)findViewById(R.id.txtFToMonthYear);
		toDay = (TextView)findViewById(R.id.txtFToDay);
		
		llFromDate = (LinearLayout)findViewById(R.id.llFFromDate);
		llToDate = (LinearLayout)findViewById(R.id.llFToDate);
		
		spnType = (Spinner)findViewById(R.id.spnFType);
		spnCategory = (Spinner)findViewById(R.id.spnFCategory);
		spnCountry = (Spinner)findViewById(R.id.spnFCountry);
		spnCity = (Spinner)findViewById(R.id.spnFCity);
 		
 		btnFilter = (Button)findViewById(R.id.btn_Filter);
 		
 		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
 		
		String cmonth = (month + 1) + "";
		if (cmonth.length() == 1) {
			cmonth = "0" + cmonth;
		}
		String dayy = (day) + "";
		if (dayy.length() == 1) {
			dayy = "0" + dayy;
		}
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
	    Date date = new Date(year, month, day-1);
	    String dayOfWeek = simpledateformat.format(date);

	    fromDate.setText(dayy);
		fromMonthYear.setText(MONTHS[month] + ", " + year);
		fromDay.setText(dayOfWeek);
		sendFromDate = cmonth + "/" + dayy + "/" + year;
		
		
		Calendar calto = Calendar.getInstance();
		calto.add(Calendar.MONTH,3);
		 
		 String tocmonth = (month + 1) + "";
			if (tocmonth.length() == 1) {
				tocmonth = "0" + tocmonth;
			}
		 
		 String todayy = (day) + "";
			if (todayy.length() == 1) {
				todayy = "0" + todayy;
			}
			
			SimpleDateFormat tosimpledateformat = new SimpleDateFormat("EEEE");
		    Date todate = new Date(toyear, tomonth, today);
		    String todayOfWeek = tosimpledateformat.format(todate);
		 
		today = calto.get(Calendar.DAY_OF_MONTH);
		tomonth = calto.get((Calendar.MONTH));
		toyear = calto.get(Calendar.YEAR);
		
		toDate.setText(todayy);
		//toMonthYear.setText(MONTHS[tomonth] + ", " + toyear);
		toMonthYear.setText("Feb, " + toyear);
		toDay.setText(todayOfWeek);
		sendToDate = tocmonth + "/" + todayy + "/" + toyear;
		
		sptp = new ArrayList<String>();
		sptpAdapter = new ArrayAdapter<String>(Filter.this,R.layout.row_spinner , sptp);
		spnType.setAdapter(sptpAdapter);		
		
		sptp.add("Exhibition");
		sptp.add("Conference");
		sptpAdapter.notifyDataSetChanged();
		
		cate = new ArrayList<String>();
		cateWithId = new HashMap<String, String>();
		cateAdapter = new ArrayAdapter<String>(this,R.layout.row_spinner, cate);
		spnCategory.setAdapter(cateAdapter);
		
		cont = new ArrayList<String>();
		contWithId = new HashMap<String, String>();
		contAdapter = new ArrayAdapter<String>(this,R.layout.row_spinner, cont);
		spnCountry.setAdapter(contAdapter);
		
		city = new ArrayList<String>();
		cityWithId = new HashMap<String, String>();
		cityAdapter = new ArrayAdapter<String>(this,R.layout.row_spinner, city);
		spnCity.setAdapter(cityAdapter);
		
		Constants.FilterCategoryID = "0";
		Constants.FilterCountryID = "0";
		Constants.FilterCityID = "0";
		
 		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Intent i = new Intent(Filter.this,Home.class);
//				startActivity(i);
				finish();
			}
		});
 		
 		spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

				if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Exhibitions"))
				{
					Constants.FilterType = "exhibitions";
				}
				if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Conferences"))
				{
					Constants.FilterType = "conferences";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
 		
 		spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

				cateName = parent.getItemAtPosition(position).toString();
				if(cateWithId.containsValue(cateName))
				{
					for (Entry<String, String> entry : cateWithId.entrySet()) {
			            if (entry.getValue().equalsIgnoreCase(cateName)) {
			            	Constants.FilterCategoryID = entry.getKey();
			        		break;
			            }
			        }
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
 		
 		spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

				contName = parent.getItemAtPosition(position).toString();
				if(contWithId.containsValue(contName))
				{
					for (Entry<String, String> entry : contWithId.entrySet()) {
			            if (entry.getValue().equalsIgnoreCase(contName)) {
			            	Constants.FilterCountryID = entry.getKey();
			            	break;
			            }
			        }
				}
				if(!Constants.FilterCountryID.equalsIgnoreCase("0"))
				{
					getCity();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
 		
 		spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

				cityName = parent.getItemAtPosition(position).toString();
				if(cityWithId.containsValue(cityName))
				{
					for (Entry<String, String> entry : cityWithId.entrySet()) {
			            if (entry.getValue().equalsIgnoreCase(cityName)) {
			            	Constants.FilterCityID = entry.getKey();
			            	break;
			            }
			        }
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
 		
 		btnFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Constants.IndustryID = "Filter";
				
				// Date Format : MM/DD/YYYY
				// Example     : 09/01/2015
				
				Constants.FilterFromDate = sendFromDate;
				Constants.FilterTODate = sendToDate;
				
				netCheck = UserFunctions.isConnectionAvailable(context);
				if (netCheck == true) {
					
					Intent filter = new Intent(Filter.this, MainActivity.class);
					startActivity(filter);
					finish();
					
				} else {
					UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
				}
				
			}
		});
 		
 		llFromDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DatePickerDialog dpd = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {

								String month = (monthOfYear + 1) + "";
								if (month.length() == 1) {
									month = "0" + month;
								}
								String dayy = (dayOfMonth) + "";
								if (dayy.length() == 1) {
									dayy = "0" + dayy;
								}
								
								SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
							    Date date = new Date(year, monthOfYear, dayOfMonth-1);
							    String dayOfWeek = simpledateformat.format(date);

								fromDate.setText(dayy);
								fromMonthYear.setText(MONTHS[monthOfYear] + ", " + year);
								fromDay.setText(dayOfWeek);
								
								sendFromDate = month + "/" + dayy + "/" + year;

							}
						}, year, month, day);
				dpd.show();
			}
		});
 		
 		llToDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DatePickerDialog dpd = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int toyear,
									int tomonthOfYear, int todayOfMonth) {

								String tomonth = (tomonthOfYear + 1) + "";
								if (tomonth.length() == 1) {
									tomonth = "0" + tomonth;
								}
								String todayy = (todayOfMonth) + "";
								if (todayy.length() == 1) {
									todayy = "0" + todayy;
								}
								
								SimpleDateFormat tosimpledateformat = new SimpleDateFormat("EEEE");
							    Date todate = new Date(toyear, tomonthOfYear, todayOfMonth-1);
							    String todayOfWeek = tosimpledateformat.format(todate);

								toDate.setText(todayy);
								toMonthYear.setText(MONTHS[tomonthOfYear] + ", " + toyear);
								toDay.setText(todayOfWeek);

								sendToDate = tomonth + "/" + todayy + "/" + toyear;
								
							}
						}, toyear, tomonth, today);
				dpd.show();
			}
		});
 		
 		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getCategory();
			getCountry();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
 		
	}
	
	public void getCategory() {
		
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				cate.clear();
				cateWithId.clear();
				cateAdapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// Send Data
				
				namevalue.clear();
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Filters.asmx/Category", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				cate.add("Select Category");
				cateWithId.put("0", "Select Category");
				for (int i = 0; i < arr.length(); i++) {
					try {
						
						cate.add(arr.getJSONObject(i).getString("industryName"));
						cateWithId.put(arr.getJSONObject(i).getString("industryID"), arr.getJSONObject(i).getString("industryName"));

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
				
				cateAdapter.notifyDataSetChanged();

			}

		}.execute();		
	}

	public void getCountry() {
		
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				cont.clear();
				contWithId.clear();
				contAdapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// Send Data
				
				namevalue.clear();
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Filters.asmx/CountryList", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				cont.add("Select Country");
				contWithId.put("0", "Select Country");
				for (int i = 0; i < arr.length(); i++) {
					try {
						
						cont.add(arr.getJSONObject(i).getString("countryName"));
						contWithId.put(arr.getJSONObject(i).getString("countryID"), arr.getJSONObject(i).getString("countryName"));

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
				
				contAdapter.notifyDataSetChanged();

			}

		}.execute();		
	}
	
	public void getCity() {
		
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				
				city.clear();
				cityWithId.clear();
				cityAdapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

				// Send Data
				
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("countryid",Constants.FilterCountryID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Filters.asmx/CityList", namevalue);

				JSONArray arr = null;
				try {

					arr = new JSONArray(json);
					
					city.add("Select City");
					cityWithId.put("0", "Select City");
					for (int i = 0; i < arr.length(); i++) {
						try {
							
							city.add(arr.getJSONObject(i).getString("cityName"));
							cityWithId.put(arr.getJSONObject(i).getString("cityID"), arr.getJSONObject(i).getString("cityName"));
	
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					city.add("Please Select Country");
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				cityAdapter.notifyDataSetChanged();

			}

		}.execute();		
	}
	
}