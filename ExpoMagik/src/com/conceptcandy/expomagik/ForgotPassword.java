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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity{
ProgressDialog pDialog;

private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
boolean netCheck = false;

String cl_email;
boolean status = false;
Context context;

EditText username;

Button submit;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	if (Build.VERSION.SDK_INT >= 9) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	Utils.trackActivity(this, App.mTracker, getClass().getSimpleName());
	
	context = ForgotPassword.this;
	
	setContentView(R.layout.forgot_password);
	
	submit = (Button) findViewById(R.id.btn_login);
	
	username = (EditText) findViewById(R.id.login_username);
	
	submit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (!isValidEmail(username.getText().toString().trim() )) {
				UserFunctions.dialogboxshow("Message","Please Enter Valid Email-Id.", context);
				return;
			}			
			else {
				netCheck = UserFunctions.isConnectionAvailable(context);
				if (netCheck == true) {
					
					cl_email = username.getText().toString().trim();					
					checkLogin();
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

	
public void checkLogin() {
		
		// Fetch Data
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... params) {
				
				//android.os.Debug.waitForDebugger();
				
				// **Code**
	
				namevalue.clear();
				namevalue.add(new BasicNameValuePair("EmailID",cl_email));
				
				String json = UserFunctions.loadJson("http://webservices.expomagik.com/ForgotPassword.asmx/Forgot", namevalue);

				JSONArray arr = null;
				try {
					
					//arr = new JSONArray(json);
					
//					Constants.visitorId = arr.getJSONObject(0).getString("visitorid");//					
//					Constants.visitorEmail = arr.getJSONObject(0).getString("EmailID");
					
					JSONObject jobj = new JSONObject(json);
					
					status = true;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
					//status = true;
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				
				if(status==true){
					Toast.makeText(context, "Check Your email-id.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(ForgotPassword.this, Login.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
					finish();
					
				}else
					Toast.makeText(context, "Your email-id is not present into our system.", Toast.LENGTH_LONG).show();
				

				/*if(status==true)
				{
					//status = false;
					
					if(Constants.redirectToHome==false)
					{
						Constants.redirectToHome=true;
						finish();
					}
					else
					{
						Toast.makeText(context, "Check Your email-id.", Toast.LENGTH_LONG).show();
						Intent i = new Intent(ForgotPassword.this, Login.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(i);
						finish();
					}
				}
				else
				{
					
					status = false;
					Toast.makeText(context, "Your email-id is not present into our system.", Toast.LENGTH_LONG).show();
				}*/
			}

		}.execute();

	}

}
