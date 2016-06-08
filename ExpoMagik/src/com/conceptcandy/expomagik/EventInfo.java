package com.conceptcandy.expomagik;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.conceptcandy.expomagik.banner.CheckNetworkConnection;
import com.conceptcandy.expomagik.banner.CirclePageIndicator;
import com.conceptcandy.expomagik.banner.GetJSONObject;
import com.conceptcandy.expomagik.banner.ImageSlideAdapter;
import com.conceptcandy.expomagik.banner.PageIndicator;
import com.conceptcandy.expomagik.model.AgendaAdapter;
import com.conceptcandy.expomagik.model.AgendaRow;
import com.conceptcandy.expomagik.model.ExhInfoAdapter;
import com.conceptcandy.expomagik.model.ExhibitorRow;
import com.conceptcandy.expomagik.model.Product;
import com.conceptcandy.expomagik.model.RelatedAdapter;
import com.conceptcandy.expomagik.model.RelatedRow;
import com.conceptcandy.expomagik.util.ImageLoader;
import com.conceptcandy.expomagik.util.Utils;
import com.meetme.android.horizontallistview.HorizontalListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventInfo extends Activity {
	
	DateFormat formatter = null;
    Date convertedDate = null;
    
    DateFormat formatterEnd = null;
    Date convertedDateEnd = null;

	ImageView add;
	Context context;
	
	TextView txtMainTitle, txtTitle, txtDate, txtCity, txtBrief, txtExbProfile, txtVisitorProfile, txtVisitors;
	ImageView back , arrownext , arrowprevious;
	
	// Exihibitor List
	ListView listExhibitors;
	ExhInfoAdapter adapterExb;
	List<ExhibitorRow> dataExb;
	
	
	// Exihibitor List
	HorizontalListView listRelated;
	RelatedAdapter adapterRel;
	List<RelatedRow> dataRel;
		
	
	// Hide Views
	TextView lblSponsers, lblExList, lblExList1, lblRelExb, lblRelExb1 , tvOrganiser, tvOrganiserName;
	RelativeLayout sponserHsv;
	LinearLayout lblExbPro, lblVisPro;
	
	//Footer icons
	ImageView ftrAttend, ftrMap, ftrFav, ftrCal, frtEnquiry;
	
	
	private static final long ANIM_VIEWPAGER_DELAY = 2000;
	private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 5000;
	
	// UI References
	private ViewPager mViewPager;
	PageIndicator mIndicator;

	AlertDialog alertDialog;
	RequestImgTask task;
	List<Product> products;
	
	boolean stopSliding = false;
	String message;

	private Runnable animateViewPager;
	private Handler handler;
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String exType = "exhibition";
	
	Dialog dialogAgenda;
	ListView listAgenda;
	AgendaAdapter adapterAgenda;
	List<AgendaRow> dataAgenda;
	
	LinearLayout btnAgenda, btnVisitors, btnSpeakers;
	
	// Sponser List
	public ImageLoader imageLoader;
	LinearLayout hScroll;
	List<String> sponserData;
	
	String ExhibitionID, ExhibitionName, website, venueDetails, briefOfExhibition,
	ExhibitorProfile, exibitionIdentity, ExhibitionHrsStart, ExhibitionHrsEnd, BusinessHrs, PublicHrs,
	ExhibitionDayStart, ExhibitionDayEnd, BusinessDay, PublicDay, country, city, industry,
	organizer, venue, LG, LT, agendaCount, visitorCount="0", speakerCount, isConf,organiserID;
	
	public void openOrganiser(View v){
		Intent i = new Intent(EventInfo.this, StartOrgSecond.class);
		i.putExtra("orgnizationid", organiserID);
		startActivity(i);
		finish();
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_info);
		
		Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		context = EventInfo.this;
		
		pDialog = new ProgressDialog(context);
		
		Intent i = getIntent();
 		ExhibitionID = i.getStringExtra("ExhibitionID");
 		Constants.exhibitionId = ExhibitionID;
 		
 		Log.d("EventInfo", "ExhibitionID is -"+ExhibitionID);
 		
		add = (ImageView) findViewById(R.id.imgPlus);
		back = (ImageView)findViewById(R.id.ei_back_arrow);
		
		arrownext = (ImageView) findViewById(R.id.arrownext);
		arrowprevious = (ImageView) findViewById(R.id.arrowprevious);
		
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		
		txtMainTitle = (TextView)findViewById(R.id.ei_mainTitle);
		txtTitle = (TextView)findViewById(R.id.ei_title);
 		txtDate = (TextView)findViewById(R.id.ei_date);
 		txtCity = (TextView)findViewById(R.id.ei_city_country);
 		txtBrief = (TextView)findViewById(R.id.ei_brief);
 		txtExbProfile = (TextView)findViewById(R.id.ei_exb_profile);
 		txtVisitorProfile = (TextView)findViewById(R.id.ei_visitor_profile);
 		txtVisitors = (TextView)findViewById(R.id.eb_txtVisitors);
 		tvOrganiser = (TextView) findViewById(R.id.event_organiser);
 		tvOrganiserName = (TextView) findViewById(R.id.event_organiser_name);
 		
 		lblExbPro = (LinearLayout)findViewById(R.id.lblExhibitorProFile);
 		lblVisPro = (LinearLayout)findViewById(R.id.lblVisitorProFile); 		
 		
 		lblExList = (TextView)findViewById(R.id.lblExhiList);
 		lblExList1 = (TextView)findViewById(R.id.lblExhiList1);
 		
 		lblRelExb = (TextView)findViewById(R.id.lblRelExb);
 		lblRelExb1 = (TextView)findViewById(R.id.lblRelExb1);
 		
 		lblSponsers = (TextView)findViewById(R.id.lblSponser);
 		sponserHsv = (RelativeLayout)findViewById(R.id.lblSponserHSV);
 		
 		ftrAttend = (ImageView)findViewById(R.id.ei_ftr_add_visitor);
 		ftrMap = (ImageView)findViewById(R.id.ei_ftr_map);
 		ftrFav = (ImageView)findViewById(R.id.ei_ftr_fav);
 		ftrCal = (ImageView)findViewById(R.id.ei_ftr_cal);
 		frtEnquiry = (ImageView)findViewById(R.id.ei_ftr_enquiry);
 		
 		btnAgenda = (LinearLayout)findViewById(R.id.eb_agenda);
 		btnVisitors = (LinearLayout)findViewById(R.id.eb_visitors);
 		btnSpeakers = (LinearLayout)findViewById(R.id.eb_speakers);
 		
 		listExhibitors = (ListView)findViewById(R.id.listExhibitors_ei);
		dataExb = new ArrayList<ExhibitorRow>();
		adapterExb = new ExhInfoAdapter(context, R.layout.row_exb_info, dataExb);
		listExhibitors.setAdapter(adapterExb);
		UserFunctions.setListViewHeightBasedOnChildren(listExhibitors);
		
		listRelated = (HorizontalListView)findViewById(R.id.listRelated_ei);
		dataRel = new ArrayList<RelatedRow>();
		adapterRel = new RelatedAdapter(context, R.layout.row_related_exb, dataRel);
		listRelated.setAdapter(adapterRel);
		//UserFunctions.setListViewHeightBasedOnChildren(listRelated);	
		 
		
		// <com.meetme.android.horizontallistview.HorizontalListView
		
		listRelated.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String exbId = dataRel.get(position).getExhibitionID();
				
				if(!exbId.equalsIgnoreCase(""))
				{
					Intent relExb = new Intent(context, EventInfo.class);
					relExb.putExtra("ExhibitionID", exbId);
					finish();
					startActivity(relExb);
				}
				else
				{
					Toast.makeText(context, "Problem while loading Exhibition.", Toast.LENGTH_SHORT).show();
				}
			}
		});
 		
		hScroll = (LinearLayout)findViewById(R.id.hScrollEI);
		imageLoader = new ImageLoader(context);
		
		sponserData = new ArrayList<String>();
		
		add.setOnClickListener(new View.OnClickListener() {
			// Start new list activity
			public void onClick(View v) {

				showPopup(context);
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		arrownext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				/*if(sponserData.size()==0)
				{
					lblSponsers.setVisibility(View.GONE);
					sponserHsv.setVisibility(View.GONE);
				}*/
				
				final HorizontalScrollView s = 
		                 (HorizontalScrollView) findViewById(R.id.horizontalScroll);
				
				s.postDelayed(new Runnable() {
				    public void run() {
				    	
				    	/*for(int i=0; i<sponserData.size(); i++)
						{
							ImageView imgSponser = new ImageView(context);
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							lp.setMargins(7, 3, 7, 3);
							imgSponser.setLayoutParams(lp);
							imageLoader.DisplayImage(sponserData.get(i), imgSponser);
							s.addView(imgSponser);
						}*/
				        s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				    }
				}, 100L);
				
				
				//Toast.makeText(getApplicationContext(), "nest pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		arrowprevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				/*if(sponserData.size()==0)
				{
					lblSponsers.setVisibility(View.GONE);
					sponserHsv.setVisibility(View.GONE);
				}*/
				
				final HorizontalScrollView s = 
		                 (HorizontalScrollView) findViewById(R.id.horizontalScroll);
				
				s.postDelayed(new Runnable() {
				    public void run() {
				    	
				    	/*for(int i=0; i<sponserData.size(); i++)
						{
							ImageView imgSponser = new ImageView(context);
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							lp.setMargins(7, 3, 7, 3);
							imgSponser.setLayoutParams(lp);
							imageLoader.DisplayImage(sponserData.get(i), imgSponser);
							s.addView(imgSponser);
						}*/
				        s.fullScroll(HorizontalScrollView.FOCUS_BACKWARD);
				    }
				}, 100L);
				
				
				//Toast.makeText(getApplicationContext(), "nest pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ftrAttend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Constants.visitorId.equalsIgnoreCase(""))
				{
					attendExhibition();
				}
				else
				{
					Constants.redirectToHome = false;
					Intent login = new Intent(EventInfo.this, Login.class);
					startActivity(login);
				}
			}
		});
		
		ftrFav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Constants.visitorId.equalsIgnoreCase(""))
				{
					favouriteExhibition();
				}
				else
				{
					Constants.redirectToHome = false;
					Intent login = new Intent(EventInfo.this, Login.class);
					startActivity(login);
				}
			}
		});

		ftrMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent map = new Intent(context, MapScreen.class);
				map.putExtra("latitude", LT);
				map.putExtra("longitude", LG);
				startActivity(map);
			}
		});

		ftrCal.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint({ "NewApi", "SimpleDateFormat" })
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

				Calendar beginTime = Calendar.getInstance();
				
				try {
					beginTime.setTime(df.parse(ExhibitionDayStart));
					Toast.makeText(getApplicationContext(), "start date"+beginTime, Toast.LENGTH_LONG).show();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				/*long timeInMilliseconds = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("ExhibitionDayStart");
				
				    Date mDate = (Date) sdf.parse(ExhibitionDayStart);
				    timeInMilliseconds = mDate.getTime();
				    System.out.println("Date in milli :: " + timeInMilliseconds);
				*/
				
				formatter = new SimpleDateFormat("dd MMM yyyy, EEE");
		        try {
					convertedDate = (Date) formatter.parse(ExhibitionDayStart);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);
		        Calendar startTime = Calendar.getInstance();
		        startTime.setTime(convertedDate);
		        
		        try {
		        formatterEnd = new SimpleDateFormat("dd MMM yyyy, EEE");
		        
					convertedDateEnd = (Date) formatter.parse(ExhibitionDayEnd);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        System.out.println("Date from yyyyMMdd String in Java : " + convertedDateEnd);
		        
				Calendar endTime = Calendar.getInstance();
				endTime.setTime(convertedDateEnd);
				//endTime.set(2015, 10, 30);
				Intent intent = new Intent(Intent.ACTION_INSERT)
				    .setData(Events.CONTENT_URI)
				    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis())
				    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
				    .putExtra(Events.TITLE, ExhibitionName)
				    .putExtra(Events.DESCRIPTION, "Event from ExpoMagik App")
				    .putExtra(Events.EVENT_LOCATION, venue)
				    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
				startActivity(intent);
			}
		});
		
		frtEnquiry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent login = new Intent(EventInfo.this, ActivityEnquiryForm.class);
				startActivity(login);
			}
		});

		lblExList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					Intent exbList = new Intent(EventInfo.this, ExhibitorList.class);
					exbList.putExtra("ExhibitionID", ExhibitionID);
					exbList.putExtra("ExhibitionName", ExhibitionName);
					startActivity(exbList);
			}
		});
		
		btnAgenda.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showAgenda(context);
			}
		});
 		 		
 		btnVisitors.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent visList = new Intent(EventInfo.this, Visitors.class);
				visList.putExtra("ExhibitionID", ExhibitionID);
				visList.putExtra("ExhibitionName", ExhibitionName);
				startActivity(visList);
				
			}
		});
 		
 		btnSpeakers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent spkList = new Intent(EventInfo.this, Speakers.class);
				spkList.putExtra("ExhibitionID", ExhibitionID);
				spkList.putExtra("ExhibitionName", ExhibitionName);
				startActivity(spkList);
			}
		});

		mIndicator.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {

				case MotionEvent.ACTION_CANCEL:
					break;

				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					if (products != null && products.size() != 0) {
						stopSliding = false;
						runnable(products.size());
						handler.postDelayed(animateViewPager,
								ANIM_VIEWPAGER_DELAY_USER_VIEW);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					if (handler != null && stopSliding == false) {
						stopSliding = true;
						handler.removeCallbacks(animateViewPager);
					}
					break;
				}
				return false;
			}
		});

		
 		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getEventInfo();
			getSponserList();
			getExhibitorList();
			getRelatedExhibitions();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
		
	}
	
	public void runnable(final int size) {
		handler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (mViewPager.getCurrentItem() == size - 1) {
						mViewPager.setCurrentItem(0);
					} else {
						mViewPager.setCurrentItem(
								mViewPager.getCurrentItem() + 1, true);
					}
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}


	@Override
	public void onResume() {
		if (products == null) {
			sendRequest();
		} else {
			mViewPager.setAdapter(new ImageSlideAdapter(EventInfo.this, products));

			mIndicator.setViewPager(mViewPager);
			
			runnable(products.size());
			//Re-run callback
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
		}
		super.onResume();
	}

	
	@Override
	public void onPause() {
		if (task != null)
			task.cancel(true);
		if (handler != null) {
			//Remove callback
			handler.removeCallbacks(animateViewPager);
		}
		super.onPause();
	}

	private void sendRequest() {
		if (CheckNetworkConnection.isConnectionAvailable(EventInfo.this)) {
			task = new RequestImgTask(EventInfo.this);
			task.execute();
		} else {
			message = "No Internet Connection";
			showAlertDialog(message, true);
		}
	}

	public void showAlertDialog(String message, final boolean finish) {
		alertDialog = new AlertDialog.Builder(EventInfo.this).create();
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finish)
							EventInfo.this.finish();
					}
				});
		alertDialog.show();
	}

	private class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				if (products != null) {
					
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
		private final WeakReference<Activity> activityWeakRef;
		Throwable error;

		public RequestImgTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pDialog.setMessage("Relax for a while...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected List<Product> doInBackground(String... urls) {
			
			
			// **Code**

			namevalue.clear();
			namevalue.add(new BasicNameValuePair("ExhibitionID",ExhibitionID));
			
			String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionBanner.asmx/BannerImages", namevalue);

			products = new ArrayList<Product>();
			
			JSONArray arr = null;
			try {
				arr = new JSONArray(json);
				
				for (int i = 0; i < arr.length(); i++) {
					try {
						
		
						int bannerId = Integer.parseInt(arr.getJSONObject(i).getString("BannerID"));
						
						products.add(new Product(
								bannerId, 
								arr.getJSONObject(i).getString("title"),
								"http://www.expomagik.com/ImageExhibitionBanner/" + arr.getJSONObject(i).getString("bannerImage")
								));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				Uri otherPath = Uri.parse("android.resource://com.callndata.expomagik/drawable/no_image");
				String path = otherPath.toString();
				
				products.add(new Product(
						1, 
						"No Image",
						path
						));
			}
			
			return products;
		}

		/**
		 * It returns jsonObject for the specified url.
		 * 
		 * @param url
		 * @return JSONObject
		 */
		public JSONObject getJsonObject(String url) {
			JSONObject jsonObject = null;
			try {
				jsonObject = GetJSONObject.getJSONObject(url);
			} catch (Exception e) {
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);

			if (pDialog.isShowing())
				pDialog.dismiss();

			
			if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
				if (error != null && error instanceof IOException) {
					message = "Time Out";
					showAlertDialog(message, true);
				} else if (error != null) {
					message = "Error";
					showAlertDialog(message, true);
				} else {
					Product product;
					if (result != null) {
						if (products != null && products.size() != 0) {

							mViewPager.setAdapter(new ImageSlideAdapter(
									EventInfo.this, products));

							mIndicator.setViewPager(mViewPager);
							
							runnable(products.size());
							handler.postDelayed(animateViewPager,
									ANIM_VIEWPAGER_DELAY);
						}
					} else {
					}
				}
			}
		}
	}
	
	public void showPopup(final Context c)
	{
		final Dialog dialog = new Dialog(c);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM;
		window.setAttributes(wlp);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.plus_popup);
		
		ImageView exPro = (ImageView) dialog.findViewById(R.id.ic_ep);
		ImageView exList = (ImageView) dialog.findViewById(R.id.ic_el);
		ImageView connect = (ImageView) dialog.findViewById(R.id.ic_cnt);
		ImageView attend = (ImageView) dialog.findViewById(R.id.ic_atnd);
		ImageView close = (ImageView) dialog.findViewById(R.id.ic_close);
		
		exPro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(context, "Exhibitor Profile Clicked", Toast.LENGTH_LONG).show();
			}
		});
		
		exList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent exbList = new Intent(EventInfo.this, ExhibitorList.class);
				exbList.putExtra("ExhibitionID", ExhibitionID);
				exbList.putExtra("ExhibitionName", ExhibitionName);
				startActivity(exbList);
			}
		});
		
		connect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(!Constants.visitorId.equalsIgnoreCase(""))
				{
					favouriteExhibition();
				}
				else
				{
					Intent login = new Intent(EventInfo.this, Login.class);
					startActivity(login);
					finish();
				}
			}
		});
		
		attend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(!Constants.visitorId.equalsIgnoreCase(""))
				{
					attendExhibition();
				}
				else
				{
					Intent login = new Intent(EventInfo.this, Login.class);
					startActivity(login);
					finish();
				}
			}
		});
		
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
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
					
					for (int i = 0; i < 5; i++) {
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
				UserFunctions.setListViewHeightBasedOnChildren(listExhibitors);
				
				if(dataExb.size()==0)
				{
					lblExList.setVisibility(View.GONE);
					lblExList1.setVisibility(View.GONE);
				}
				
			}

		}.execute();

	}
	
	public void getRelatedExhibitions() {
		
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
				namevalue.add(new BasicNameValuePair("type",exType));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/RelatedExhibitions.asmx/Exhibitions", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							dataRel.add(new RelatedRow(
									arr.getJSONObject(i).getString("ExhibitionID"), 
									arr.getJSONObject(i).getString("ExhibitionName"),
									arr.getJSONObject(i).getString("startdate"),
									arr.getJSONObject(i).getString("ExhibitionDayEnd"),
									arr.getJSONObject(i).getString("City"),
									arr.getJSONObject(i).getString("Country"),
									arr.getJSONObject(i).getString("img") ));
							Log.d("Related Adapter", "photo path from webservice is-"+arr.getJSONObject(i).getString("img"));

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
				
				adapterRel.notifyDataSetChanged();
				//UserFunctions.setListViewHeightBasedOnChildren(listRelated);
				if(dataRel.size()==0)
				{
					lblRelExb.setVisibility(View.GONE);
					lblRelExb1.setVisibility(View.GONE);
				}
			}

		}.execute();

	}


	public void getSponserList() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
				sponserData.clear();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
				// **Code**
	
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("ExhibitionID",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Sponsers.asmx/SponserList", namevalue);
	
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
							String sponserLogoPath =  arr.getJSONObject(i).getString("SponsorLogo");
							sponserLogoPath = "http://www.expomagik.com/ImageSponsors/" + sponserLogoPath;
							sponserData.add(sponserLogoPath);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							
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

				for(int i=0; i<sponserData.size(); i++)
				{
					ImageView imgSponser = new ImageView(context);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(7, 3, 7, 3);
					imgSponser.setLayoutParams(lp);
					imageLoader.DisplayImage(sponserData.get(i), imgSponser);
					hScroll.addView(imgSponser);
					
					 
				}
				if(sponserData.size()==0)
				{
					lblSponsers.setVisibility(View.GONE);
					sponserHsv.setVisibility(View.GONE);
				}
				
			}
	
		}.execute();
	
	}
	
	
	public void getEventInfo() {
		
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
				namevalue.add(new BasicNameValuePair("exhibitionid", ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ExhibitionDetail.asmx/ExhibitionInfo", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					
					ExhibitionName = arr.getJSONObject(0).getString("ExhibitionName");
					website = arr.getJSONObject(0).getString("website");
					venueDetails = arr.getJSONObject(0).getString("venueDetails");
					briefOfExhibition = arr.getJSONObject(0).getString("briefOfExhibition");
					ExhibitorProfile = arr.getJSONObject(0).getString("ExhibitorProfile");
					exibitionIdentity = arr.getJSONObject(0).getString("exibitionIdentity");
					ExhibitionHrsStart = arr.getJSONObject(0).getString("ExhibitionHrsStart");
					ExhibitionHrsEnd = arr.getJSONObject(0).getString("ExhibitionHrsEnd");
					BusinessHrs = arr.getJSONObject(0).getString("BusinessHrs");
					PublicHrs = arr.getJSONObject(0).getString("PublicHrs");
					ExhibitionDayStart = arr.getJSONObject(0).getString("startdate") + ", " + 
					arr.getJSONObject(0).getString("startday");
					
					ExhibitionDayEnd = arr.getJSONObject(0).getString("Enddate") + ", " + 
							arr.getJSONObject(0).getString("endday");
					BusinessDay = arr.getJSONObject(0).getString("BusinessDay");
					PublicDay = arr.getJSONObject(0).getString("PublicDay");
					country = arr.getJSONObject(0).getString("country");
					city = arr.getJSONObject(0).getString("city");
					industry = arr.getJSONObject(0).getString("industry");
					organizer = arr.getJSONObject(0).getString("organizer");
					venue = arr.getJSONObject(0).getString("venue");
					LG = arr.getJSONObject(0).getString("LG");
					LT = arr.getJSONObject(0).getString("LT");
					agendaCount = arr.getJSONObject(0).getString("agendaCount");
					visitorCount = arr.getJSONObject(0).getString("visitorCount");
					speakerCount = arr.getJSONObject(0).getString("speakerCount");
					isConf = arr.getJSONObject(0).getString("ExhibitionType");					
					organiserID = arr.getJSONObject(0).getString("Organizerid");
					
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
				
				
				Log.d("EventInfo", "brief is -"+briefOfExhibition);
				
				String brief = briefOfExhibition.replaceAll("â", "\'");
				
				Log.d("EventInfo", "brief is -***"+brief);
				
				txtTitle.setText(ExhibitionName);
				txtMainTitle.setText(ExhibitionName);
		 		txtDate.setText(ExhibitionDayStart);
		 		txtCity.setText(city + " - " + country);
		 		txtBrief.setText(brief);
		 		brief = ExhibitorProfile.replaceAll("'", "\'");
		 		txtExbProfile.setText(ExhibitorProfile);
		 		txtVisitorProfile.setText("");
		 		tvOrganiserName.setText(""+organizer);
		 		Constants.venue = venue;
		 		
		 		txtVisitors.setText("Visitors (" + visitorCount + ")");
		 		
		 		if(isConf.equalsIgnoreCase("Exhibition"))
		 		{
		 			btnAgenda.setVisibility(View.GONE);
		 			btnSpeakers.setVisibility(View.GONE);
		 		}
		 		else
		 		{
		 			lblRelExb.setText("EVENTS ALIKE!");
		 		}
		 		
		 		if(LT!=null && LG!=null)
		 		{
		 			if(!LT.startsWith("0") && !LG.startsWith("0"))
			 		{
		 				Constants.latitude = Double.parseDouble(LT);
		 				Constants.longitude = Double.parseDouble(LG);
			 		}
		 		}
		 		if(ExhibitorProfile.equals(""))
		 		{
		 			lblExbPro.setVisibility(View.GONE);
		 		}
		 		if(!isConf.equalsIgnoreCase("Exhibition"))
		 		{
		 			exType = "conference";
		 		}
		 		
		 		lblVisPro.setVisibility(View.GONE);
		 		
			}

		}.execute();

	}

	public void attendExhibition() {
		
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
				namevalue.add(new BasicNameValuePair("visitorid",Constants.visitorId));
				namevalue.add(new BasicNameValuePair("exhibitionid",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Attend.asmx/InsertRecord", namevalue);
				
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				Toast.makeText(context, "Added in Attend List Successfully", Toast.LENGTH_LONG).show();
			}
	
		}.execute();
	}
	
	public void favouriteExhibition() {
		
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
				namevalue.add(new BasicNameValuePair("visitorid",Constants.visitorId));
				namevalue.add(new BasicNameValuePair("exhibitionid",ExhibitionID));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Connect.asmx/InsertRecord", namevalue);
				
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				Toast.makeText(context, "We will remember you love that! We will keep you posted on the same", Toast.LENGTH_LONG).show();
			}
	
		}.execute();
	}
	
	
	public void showAgenda(final Context c)
	{
		dialogAgenda = new Dialog(c);
		dialogAgenda.setCancelable(true);
		dialogAgenda.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAgenda.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogAgenda.setContentView(R.layout.agenda);
		dialogAgenda.show();
		
		listAgenda = (ListView)dialogAgenda.findViewById(R.id.listAgenda);
		dataAgenda = new ArrayList<AgendaRow>();
		adapterAgenda = new AgendaAdapter(c, R.layout.row_agenda, dataAgenda);
		listAgenda.setAdapter(adapterAgenda);
		
		netCheck = UserFunctions.isConnectionAvailable(c);
		if (netCheck == true) {
			getAgenda();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
	}

	public void getAgenda() {
		
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
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/Agenda.asmx/AgendaList", namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					
					for (int i = 0; i < arr.length(); i++) {
						try {
							
			
							dataAgenda.add(new AgendaRow(
											arr.getJSONObject(i).getString("agendaID"),
											arr.getJSONObject(i).getString("title"), 
											arr.getJSONObject(i).getString("description")
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
				
				adapterAgenda.notifyDataSetChanged();
				if(dataAgenda.size()==0)
				{
					dialogAgenda.dismiss();
					UserFunctions.dialogboxshow("Message", "No data available.", context);
				}
			}

		}.execute();

	}
}