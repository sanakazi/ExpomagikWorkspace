package com.conceptcandy.expomagik;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Logindemo extends Activity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {

	Context context;
	EditText username, password;
	
	Button submit;
	
	ImageView fb, btn_twitter, GPlus, linkedIn;
	
	boolean isLoggedInGPlus = false;

	private static final int RC_SIGN_IN = 3;
	private static final String TAG = "MainActivity";
	private static final int PROFILE_PIC_SIZE = 400;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	
	LoginButton loginButton;
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	
	LinkedInOAuthService oAuthService;
	LinkedInApiClientFactory factory;
	LinkedInApiClient client;
	static LinkedInRequestToken liToken;
	
	// Twitter

	// Shared preference keys 
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";

	 //Any number for uniquely distinguish your request 
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private ProgressDialog pDialog;

	private static Twitter twitter;
	private static RequestToken requestToken;
	
	private static SharedPreferences mSharedPreferences;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
    
	
	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	boolean netCheck = false;
	
	String cl_uname, cl_pwd;
	boolean status = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FacebookSdk.sdkInitialize(Logindemo.this);
		callbackManager = CallbackManager.Factory.create();
		
		setContentView(R.layout.login);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		context = Logindemo.this;
		
		pDialog = new ProgressDialog(context);

		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);
		
		submit = (Button) findViewById(R.id.btn_login);

		password.setTypeface(Typeface.DEFAULT);

		fb = (ImageView) findViewById(R.id.login_Facebook);
		btn_twitter = (ImageView) findViewById(R.id.login_Twitter);
		//GPlus = (ImageView) findViewById(R.id.login_GPlus);
		linkedIn = (ImageView) findViewById(R.id.login_LinkedIn);
		
		// Initializing google plus API client
		mGoogleApiClient = new GoogleApiClient.Builder(Logindemo.this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		
		// Initializing Facebook API
		loginButton = (LoginButton) findViewById(R.id.fb_login_button);
	    loginButton.setReadPermissions(Arrays.asList("public_profile", "email",
	            "user_birthday", "user_about_me"));
	    
		shareDialog = new ShareDialog(Logindemo.this);
	    

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginButton.performClick();
			}
		});
		
	    btn_twitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				loginToTwitter(context);
			}
		});
		
		GPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLoggedInGPlus == true)
				{
					signOutFromGplus();
				}
				else
				{
					signInWithGplus();
				}
			}
		});
		
		linkedIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken.getAuthorizationUrl()));
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}
		});
		
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (username.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Username.", context);
					return;
				}
				if (!isValidEmail(username.getText().toString())) {
					UserFunctions.dialogboxshow("Message","Please Enter Valid Email-Id.", context);
					return;
				}
				else if (password.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Password.", context);
					return;
				}
				else {
					netCheck = UserFunctions.isConnectionAvailable(context);
					if (netCheck == true) {
						
						cl_uname = username.getText().toString();
						cl_pwd = password.getText().toString();
						
						checkLogin();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
					}
				}
				
			}
		});
		
		
		
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if(accessToken==null)
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor edt = sp.edit();
			edt.clear();
			edt.commit();			
		}

		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			
			@Override
			public void onSuccess(LoginResult result) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Connected With Facebook.", Toast.LENGTH_LONG).show();
				
				GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),
		        new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object,
							GraphResponse response) {
						// TODO Auto-generated method stub
						
						String email;
						try {
							
							String id = object.getString("id");
							String name = object.getString("name");
							
							try{
								email = object.getString("email");
							}
							catch(Exception e)
							{
								email="";
							}
							
							String emp_dp_path = "https://graph.facebook.com/"+id+"/picture?type=large";
							
							SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							Editor edt = sp.edit();
							edt.putString("uname", name);
							edt.putString("dpurl", emp_dp_path);
							edt.commit();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                
					}

		            });

		            request.executeAsync();
			}
			
			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Error : " + error, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_LONG).show();
			}
		});
		
		// Initializing LinkedIn API
		oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
		factory = LinkedInApiClientFactory.newInstance(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		liToken = oAuthService.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
		

		// Login with Twitter
		
		initTwitterConfigs();
		
		// Enabling strict mode 
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		// Initialize application preferences 
		mSharedPreferences = context.getSharedPreferences(PREF_NAME, 0);

		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		
		 // if already logged in, then hide login layout and show share layout 
		if (isLoggedIn) {
			String username = mSharedPreferences.getString(PREF_USER_NAME, "");
			Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

		} else {
			Uri uri = getIntent().getData();
			
			if (uri != null && uri.toString().startsWith(callbackUrl)) {
			
				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {
					
					// Getting oAuth authentication token 
					twitter4j.auth.AccessToken tw_accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					 //Getting user id form access token 
					long userID = tw_accessToken.getUserId();
					final User user = twitter.showUser(userID);
					final String username = user.getName();

					 //save updated token 
					saveTwitterInfo(tw_accessToken);

					Toast.makeText(context, username, Toast.LENGTH_SHORT).show();
					
				} catch (Exception e) {
					Log.e("Failed to login Twitter!!", e.getMessage());
				}
			}

		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		try {
	        linkedInImport(intent);
	    } catch (NullPointerException e) {
	        e.printStackTrace();
	    }
	}

	private void linkedInImport(Intent intent) {
	    String verifier = intent.getData().getQueryParameter("oauth_verifier");
	    
	    LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
	    
	    client = factory.createLinkedInApiClient(accessToken);

	    com.google.code.linkedinapi.schema.Person profile = client.getProfileForCurrentUser(EnumSet.of(ProfileField.ID, ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.HEADLINE));

	    String li_name = profile.getFirstName() + " " + profile.getLastName();
	    String li_heading = profile.getHeadline();
	    String li_dpUrl = profile.getPictureUrl();
	    
	    
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor edt = sp.edit();
		edt.putString("uname", li_name);
		edt.putString("dpurl", li_dpUrl);
		edt.commit();
		
		Toast.makeText(getApplicationContext(), "Welcome " + li_name + ",\n" + li_heading, Toast.LENGTH_LONG).show();
		
		recreate();
	}
	
	
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
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

				namevalue.clear();
				namevalue.add(new BasicNameValuePair("name",cl_uname));
				namevalue.add(new BasicNameValuePair("pass",cl_pwd));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorLoginNew.asmx/VisitorLogin", namevalue);

				JSONObject arr = null;
				try {
					arr = new JSONObject(json);
					
					Constants.visitorId = arr.getString("visitorid");
					Constants.visitorName = arr.getString("fullname");
					
					status = true;
					
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

				if(status==true)
				{
					status = false;
					Toast.makeText(context, "Welcome " + Constants.visitorName, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, "Username or password is incorrect.", Toast.LENGTH_LONG).show();
				}
			}

		}.execute();

	}


	
	// Login with Twitter
	
	/**
	 * Saving user information, after user is authenticated for the first time.
	 * You don't need to show user to login, until user has a valid access toen
	 */
	private void saveTwitterInfo(twitter4j.auth.AccessToken accessToken) {
		
		long userID = accessToken.getUserId();
		
		User user;
		try {
			user = twitter.showUser(userID);
		
			String username = user.getName();

			// Storing oAuth tokens to shared preferences 
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();
			
		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
	}

	 //Reading twitter essential configuration parameters from strings.xml 
	public void initTwitterConfigs() {
		
		consumerKey = "bN0KMlYH7BHqMyEYtRNKAVvO0";
		consumerSecret = "mxZaba8u1j6TPPY9z3ZZMX7tBQ6DHcR9g3PBaewdKxqeCRCnqD";
		callbackUrl = "http://www.expomagik.com";
		oAuthVerifier = "oauth_verifier";
	}

	
	public void loginToTwitter(Context context) {
		
		consumerKey = "bN0KMlYH7BHqMyEYtRNKAVvO0";
		consumerSecret = "mxZaba8u1j6TPPY9z3ZZMX7tBQ6DHcR9g3PBaewdKxqeCRCnqD";
		callbackUrl = "http://www.expomagik.com";
		oAuthVerifier = "oauth_verifier";
		
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		
		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 *  Loading twitter login page on webview for authorization 
				 *  Once authorized, results are received at onActivityResult
				 *  */
				final Intent intent = new Intent(context, WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
				
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {

		}
	}
		
		
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			
			if (resultCode != Activity.RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		if (requestCode == Activity.RESULT_OK) {
			String verifier = data.getExtras().getString(oAuthVerifier);
			try {
				twitter4j.auth.AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

				long userID = accessToken.getUserId();
				final User user = twitter.showUser(userID);
				String username = user.getName();
				
				saveTwitterInfo(accessToken);

				Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}
	}
	// Login with Google Account
	
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
					Logindemo.this, 0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Toast.makeText(context, "User is connected!", Toast.LENGTH_LONG).show();
		isLoggedInGPlus = true;

		// Get user's information
		getGPLusProfileInformation();
		// Update the UI after signin
		updateUI(true);

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			
		} else {
			
		}
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}
	
	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
	    if (mGoogleApiClient.isConnected()) {
	        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	        mGoogleApiClient.disconnect();
	        mGoogleApiClient.connect();
	        updateUI(false);
	        
	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
			Editor edt = sp.edit();
			edt.clear();
			edt.commit();
	    }
	}
	
	/**
	 * Revoking access from google
	 * */
	private void revokeGplusAccess() {
	    if (mGoogleApiClient.isConnected()) {
	        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
	                .setResultCallback(new ResultCallback<Status>() {
	                    @Override
	                    public void onResult(Status arg0) {
	                        Log.e(TAG, "User access revoked!");
	                        mGoogleApiClient.connect();
	                        updateUI(false);
	                    }
	 
	                });
	    }
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(Logindemo.this,RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	
	private void getGPLusProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);
				
				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
				Editor edt = sp.edit();
				edt.putString("uname", personName);
				edt.putString("dpurl", personPhotoUrl);
				edt.commit();
				
//				new LoadProfileImage(GPlus).execute(personPhotoUrl);

			} else {
				Toast.makeText(context, "Person information is null",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Background Async task to load user profile picture from url
	 * */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
		}
	}