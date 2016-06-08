package com.conceptcandy.expomagik;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.conceptcandy.expomagik.menu.ResideMenu;
import com.conceptcandy.expomagik.menu.ResideMenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<LoadPeopleResult> {

    public static ResideMenu resideMenu;
    private ResideMenuItem li_home, li_login, li_exhibition, li_conf, li_myExb, li_profile ,li_pref, li_industry, 
    li_cities, li_venues, li_exit;
  
    // User Management
    
    private GoogleApiClient mGoogleApiClient;
	boolean mSignInClicked;
	
	String strLogInLogOut = "Login";
	boolean netCheck = false;
	private ResideMenuItem li_organizer;
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*if(Build.VERSION.SDK_INT >= 21){
        getWindow().getDecorView().setSystemUiVisibility(
        	    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        	    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        
        if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}*/
        
        setContentView(R.layout.activity_main);
        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this).addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String vId = sp.getString("vID", "");
		if(!vId.equalsIgnoreCase("")) {
			
			strLogInLogOut = "Logout";
			
			Constants.visitorId = vId;
			Constants.visitorName = sp.getString("vName", "");
			Constants.visitorEmail = sp.getString("vMail", "");
			Constants.visitorCountry = sp.getString("vCountry", "");
			Constants.visitorCity = sp.getString("vCity", "");
			Constants.visitorIndividual = sp.getString("vIndividual", "");
			Constants.visitorCompanyname = sp.getString("vCompanyName", "");
			Constants.visitorDeignation = sp.getString("vDesignation", "");
			Constants.vDP = sp.getString("vDP", "");
		}
		
		setUpMenu();
        setPaddings(); 
        changeFragment(new Home());
        
        //showHashKey(MainActivity.this);
    }
    
    
    public static void showHashKey(Context context) {
        try { 
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.conceptcandy.expomagik", PackageManager.GET_SIGNATURES); //Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                } 
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        } 
    } 
    
    
    private void setUpMenu() {
    	
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.loginscreen_bg);
        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(true);
        resideMenu.setHeaderView(findViewById(R.id.actionbar));
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        li_home = new ResideMenuItem(this, R.drawable.icn_home, "Home", R.color.ml_pink);
        li_login = new ResideMenuItem(this, R.drawable.icn_login, strLogInLogOut, R.color.ml_yellow);
        li_exhibition = new ResideMenuItem(this, R.drawable.icn_exhibitions, "Exhibitions", R.color.ml_green);
        li_conf = new ResideMenuItem(this, R.drawable.icn_conferences, "Conferences", R.color.ml_blue);
        li_organizer = new ResideMenuItem(this, R.drawable.organiser, "Organisers", R.color.ml_pink);
        li_myExb = new ResideMenuItem(this, R.drawable.icn_myexhibitions, "My Exhibitions", R.color.ml_yellow);
        li_profile = new ResideMenuItem(this, R.drawable.icn_profile, "Profile", R.color.ml_purple);
//      li_pref = new ResideMenuItem(this, R.drawable.icn_preferences, "Preferences", R.color.ml_pink);
        li_industry = new ResideMenuItem(this, R.drawable.icn_industry, "Industries", R.color.ml_yellow);
        li_cities = new ResideMenuItem(this, R.drawable.icn_city, "Cities", R.color.ml_green);
        li_venues = new ResideMenuItem(this, R.drawable.icn_venue, "Venues", R.color.ml_blue);
        

        li_home.setOnClickListener(this);
        li_login.setOnClickListener(this);
        li_exhibition.setOnClickListener(this);
        li_conf.setOnClickListener(this);
        li_profile.setOnClickListener(this);
        li_myExb.setOnClickListener(this);
//        li_pref.setOnClickListener(this);
        li_industry.setOnClickListener(this);
        li_cities.setOnClickListener(this);
        li_venues.setOnClickListener(this);
       // li_exit.setOnClickListener(this);        
        li_organizer.setOnClickListener(this);

        resideMenu.addMenuItem(li_home);
        resideMenu.addMenuItem(li_login);
        resideMenu.addMenuItem(li_exhibition);
        resideMenu.addMenuItem(li_conf);
        resideMenu.addMenuItem(li_organizer);
        resideMenu.addMenuItem(li_myExb);
        resideMenu.addMenuItem(li_profile);
//        resideMenu.addMenuItem(li_pref);
        resideMenu.addMenuItem(li_industry);
        resideMenu.addMenuItem(li_cities);
        resideMenu.addMenuItem(li_venues);
       

        
//        findViewById(R.id.ic_drH).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu();
//            }
//        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    
	@Override
    public void onClick(View view) {

		Constants.homepage = false;
        
        if (view == li_home)
        {
            changeFragment(new Home());
            Constants.homepage = true;
        }
        else if (view == li_login)
        {
        	resideMenu.clearIgnoredViewList();
            
			if(strLogInLogOut.equalsIgnoreCase("Login"))
			{
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String vId = sp.getString("vID", "");
				if(!vId.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "You are already logged in.", Toast.LENGTH_LONG).show();
					recreate();
				}
				else
				{
					Intent login = new Intent(MainActivity.this, Login.class);
					startActivity(login);
				}
			}
			else
			{
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(false);
		        dialog.setContentView(R.layout.log_out);

		        dialog.show();
		        
		        Button logoutYes = (Button) dialog.findViewById(R.id.btnLogoutYes);
		        Button logoutNo = (Button) dialog.findViewById(R.id.btnLogoutNo);
		        
		        logoutYes.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                // Close dialog
		            	
		            	if (mGoogleApiClient.isConnected()) {
		                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		                    mGoogleApiClient.disconnect();
		                    mGoogleApiClient.connect();
		                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		            		Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
	        					@Override
	        					public void onResult(Status arg0) {
	        						mGoogleApiClient.connect();
	        					}
	        				});
		                }
						
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        		Editor edt = sp.edit();
		        		edt.clear();
		        		edt.commit();
		        		Constants.visitorId = "";
		        		
		        		dialog.dismiss();
		        		recreate();
		            }
		        });
		        
		        logoutNo.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                // Close dialog
		            	dialog.dismiss();
		            }
		        });
				
			}			
        }
        else if (view == li_exhibition)
        {
        	changeFragment(new ExhibitonsGrid("exhibition"));
        }
        else if (view == li_conf)
        {
        	Constants.lbl_top_heading = "Conferences";
        	Constants.flag = "conferences";
        	changeFragment(new ExhibitonsGrid());
        }
        else if (view==li_organizer) {
			
        	/*Intent po = new Intent(MainActivity.this, PopulerOrganizers.class);
			startActivity(po);*/
        	changeFragment(new PopulerOrganizers());
        	
		}
        
        
        else if (view == li_myExb)
        {
        	if(!Constants.visitorId.equalsIgnoreCase(""))
			{
        		changeFragment(new MyExhibitions());
			}
			else
			{
				Constants.redirectToHome = false;
				Intent login = new Intent(MainActivity.this, Login.class);
				startActivity(login);
			}
        }
        else if (view == li_profile)
        {
        	
        	Log.i("Visiter id", Constants.visitorId);
        	
        	if(!Constants.visitorId.equalsIgnoreCase(""))
			{
        		changeFragment(new Profile());
			}
			else
			{
				Constants.redirectToHome = false;
				Intent login = new Intent(MainActivity.this, Login.class);
				startActivity(login);
			}
        }
        else if (view == li_industry)
        {
        	changeFragment(new Industries());
        }
        else if (view == li_cities)
        {
        	Constants.isCityList = true;
        	changeFragment(new CountryList());
        }
        else if (view == li_venues)
        {
        	changeFragment(new CountryList());
        	
        }
        else if (view == li_exit)
        {
        	System.exit(0);
        }

        resideMenu.closeMenu();
    }

    //Example of menuListener
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() { }

        @Override
        public void closeMenu() { }
    };

    public void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
    
    public static void openDrawer()
    {
    	Constants.VenueID = "";
    	Constants.cityId = "";
    	resideMenu.openMenu();
    }
    
    public static void closeDrawer()
    {
    	resideMenu.closeMenu();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setPaddings();
    }
    
    /**
     * Due to a bug in Lollipop/Material theme the navigationbar overlaps the layout.
     * We fix this by manually adding padding to our main view, and (scrollable)menu view.
     */
    private void setPaddings() {
		if (Build.VERSION.SDK_INT >= 21) {

			int bottomPadding = 0;
			int rightPadding = 0;
			// int topPadding = insets.top;
			int leftPadding = 0;

			int size = getNavBarHeight(this);
			if (size > 0 && !isTablet(this)) {
				WindowManager manager = (WindowManager) this
						.getSystemService(Context.WINDOW_SERVICE);
				switch (manager.getDefaultDisplay().getRotation()) {
				case Surface.ROTATION_90:
					rightPadding += size;
					break;
				case Surface.ROTATION_180:
					// topPadding += size;
					break;
				case Surface.ROTATION_270:
					rightPadding += size;
					break;
				default:
					bottomPadding += size;
				}
			} else if (size > 0) {
				// on tablets, the navigationbar is always at the bottom.
				bottomPadding += size;
			}

			View scrollViewMenu = findViewById(R.id.sv_left_menu);
			scrollViewMenu.setPadding(scrollViewMenu.getPaddingLeft(), 0 +
			getStatusBarHeight(),scrollViewMenu.getPaddingRight(),scrollViewMenu.getPaddingBottom());

			View menu = findViewById(R.id.menu);
			menu.setPadding(0 + leftPadding,
					menu.getPaddingTop(), 0 + rightPadding,
					0 + bottomPadding);
			View main = findViewById(R.id.main);
			main.setPadding(0 + leftPadding,
					main.getPaddingTop(), 0 + rightPadding,
					0 + bottomPadding);
		}
    }
    
    @SuppressLint("NewApi")
	public int getNavBarHeight(Context c) {
    	 int result = 0;
    	 boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey(); //method is only called on API21+
    	 boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

    	 if(!hasMenuKey && !hasBackKey) {
    	     //The device has a navigation bar
    		 Resources resources = getResources();
    	 
    		 int orientation = getResources().getConfiguration().orientation;
    		 int resourceId;
    		 if (isTablet(c)){
    			 resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
    		 }  else {
    			 resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");	 
    		 }
         
    		 if (resourceId > 0) {
    			 return getResources().getDimensionPixelSize(resourceId);
    		 }
    	 }
         return result;
	} 
    
    private boolean isTablet(Context c) {
    	return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    public int getStatusBarHeight() { 
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        } 
        return result;
    }
    
    
    // Custom Starts
    
    @Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(LoadPeopleResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mSignInClicked = false;
		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	protected void onStart() {
	    super.onStart();
	    mGoogleApiClient.connect();
	}

	protected void onStop() {
	    super.onStop();
	    if (mGoogleApiClient.isConnected()) {
	        mGoogleApiClient.disconnect();
	    }
	}

	private static long back_pressed_time;
	private static long PERIOD = 2000;
	
	@Override
	public void onBackPressed() {
		
		int count=getFragmentManager().getBackStackEntryCount();
		if (Constants.homepage == true) {
			if (back_pressed_time + PERIOD > System.currentTimeMillis())
				super.onBackPressed();
			else
				Toast.makeText(getBaseContext(), "Press back again to exit..", Toast.LENGTH_SHORT).show();
			back_pressed_time = System.currentTimeMillis();
		} else {
			netCheck = UserFunctions.isConnectionAvailable(MainActivity.this);
			if (netCheck == true) {
				changeFragment(new Home());
			} else {
				UserFunctions.dialogboxshow("Message", "Internet Connection not available.", MainActivity.this);
			}
		}if (count==0) {
			super.onBackPressed();
		}else {
			getFragmentManager().popBackStack();
		}
	}
}
