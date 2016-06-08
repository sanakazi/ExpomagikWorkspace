package com.conceptcandy.expomagik;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.conceptcandy.expomagik.navdrawer.MLRoundedImageView;
import com.conceptcandy.expomagik.navdrawer.RoundedImageView;
import com.conceptcandy.expomagik.util.ImageLoader;
import com.conceptcandy.expomagik.util.Utils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat.Action;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 

public class Profile extends Fragment {
	
	public static final String TAG = "USER PROFILE";

	private static final int REQUEST_FOR_CAMERA = 1;
	private static final int REQUEST_FOR_GALLERY = 2;
	public String imagepath;
	
	public ImageLoader imageLoader;

	Context context;
	
	public static Context mcontext;
	
	private Uri fileUri;
	private static String file_name;
	
	// private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
		private static final int MEDIA_TYPE_IMAGE = 1;
		private static final int SELECT_PICTURE = 10;
		
		private static int RESULT_LOAD_IMG = 1;

		int VAL = 0;
		private static int RESULT_CANCELED = 0;
		//private static int RESULT_OK = 11;
		
		private static int RESULT_SPINNER_IMG = 100;
		// String imgDecodableString;
		private String selectedImagePath;
	
	ImageView icDrawer , editProfile , imgupdatePdp;
	TextView name, post, place;

	ImageView dp;
	
	private String galleyimagepath;

	private File cameraimagepath;
	
	
	
	//sneha
	
	ArrayList<NameValuePair> nameValuePairs;
	ArrayList<NameValuePair> nameValuePairsget;
	 
	
	String vId;
	
	 
	Bitmap userdp;
	 
	Bitmap bitmap1;
	
	ProgressDialog pDialog;
	ProgressDialog pDialog2;
	
	String ba1, imgUploadRes , visitorId;
	boolean success = false;
	
	int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	public static String encoded;
	public static Bitmap bm;

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(Constants.updateProfile==true)
		{
			Constants.updateProfile = false;
			
			name.setText(Constants.visitorName);
			post.setText(Constants.visitorDeignation);
			place.setText(Constants.visitorCompanyname+""+Constants.visitorCity);
			imageLoader.DisplayImage(imagepath, dp);
		}
		
		/*if(!Constants.vDP.equalsIgnoreCase(""))
		{
			getProfile();
		}*/
	}

	
	public static Profile newInstance(Bundle bundle) {
		Profile fragment = new Profile();
		if (bundle != null)
		fragment.setArguments(bundle);
		return fragment;
		}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.profile, container, false);
		

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Utils.trackActivity(getActivity(), App.mTracker, getClass().getSimpleName());
		
		context = getActivity();
		
		imageLoader = new ImageLoader(context);

		icDrawer = (ImageView) view.findViewById(R.id.ic_drP);
		
		editProfile = (ImageView) view.findViewById(R.id.ic_editProfile);
		
		dp = (ImageView) view.findViewById(R.id.imgPdp);
		imgupdatePdp = (ImageView) view.findViewById(R.id.imgupdatePdp);
		name = (TextView) view.findViewById(R.id.proName);
		post = (TextView) view.findViewById(R.id.proDesignation);
		place = (TextView) view.findViewById(R.id.proPlace);
		
		Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.default_dp);
		Bitmap roundedDp = RoundedImageView.getCroppedBitmap(photo, 500);
		//dp.setImageBitmap(roundedDp);
		pDialog= new ProgressDialog(context);
				
		
		SharedPreferences spDP = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String vpId = spDP.getString("vpID", "");
		if(!vpId.equalsIgnoreCase("")) {
			
			Constants.visitorId = vpId;
			Constants.vDP = spDP.getString("profileimage", "");
		
			if(!Constants.vDP.equalsIgnoreCase(""))
			{
				
				URL url;
				Bitmap bitmap;
				try {
					//String imgpth = MainActivity.imgpth;
					url = new URL(Constants.vDP);
					bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				} catch (Exception exception) {
					// TODO Auto-generated catch block
					bitmap = BitmapFactory.decodeResource(context
							.getResources(), R.drawable.default_dp);
				}
				Bitmap userdp = MLRoundedImageView.getCroppedBitmap(bitmap, 500);
				
				Log.d("Profile", "Url is-"+Constants.vDP);
				
				dp.setImageBitmap(userdp);
			}
		}
		
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String vId = sp.getString("vID", "");
		if(!vId.equalsIgnoreCase("")) {
			
			Constants.visitorId = vId;
			Constants.visitorName = sp.getString("vName", "");
			Constants.visitorEmail = sp.getString("vMail", "");
			Constants.visitorCountry = sp.getString("vCountry", "");
			Constants.visitorCity = sp.getString("vCity", "");
			Constants.visitorIndividual = sp.getString("vIndividual", "");
			Constants.visitorCompanyname = sp.getString("vCompanyName", "");
			Constants.visitorDeignation = sp.getString("vDesignation", "");
			Constants.vDP = sp.getString("vDP", "");
			Constants.vDP = sp.getString("profileimage", "");
			
			//getProfile();
			
			name.setText(Constants.visitorName);
			if(!Constants.visitorDeignation.equals(""))
			{
				post.setVisibility(View.VISIBLE);
				post.setText(Constants.visitorDeignation + ", " + Constants.visitorCompanyname);
			}
			if(!Constants.visitorCity.equals(""))
			{
				place.setVisibility(View.VISIBLE);
				place.setText(Constants.visitorCity + ", " + Constants.visitorCountry);
			}
			/*if(!Constants.vDP.equalsIgnoreCase(""))
			{
				URL url;
				Bitmap bitmap;
				try {
					String imgpth = MainActivity.imgpth;
					url = new URL(imgpth);
					bitmap = BitmapFactory.decodeStream(url.openConnection()
							.getInputStream());
				} catch (Exception exception) {
					// TODO Auto-generated catch block
					bitmap = BitmapFactory.decodeResource(context
							.getResources(), R.drawable.default_dp);
				}
				Bitmap userdp = MLRoundedImageView.getCroppedBitmap(bitmap, 500);
				dp.setImageBitmap(userdp);
			}*/
		}
		
		editProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(context, UpdateProfile.class);
				startActivity(i);
				
			}
		});
		
		imgupdatePdp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		icDrawer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MainActivity.openDrawer();
				
			}
		});
		
		SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String previouslyEncodedImage = shre.getString(Constants.galleyimagepath, "");

		if( !previouslyEncodedImage.equalsIgnoreCase("") ){
		    byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
		    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		    dp.setImageBitmap(bitmap);
		}
				
		getProfile();
			
		return view;
	}


	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					dialog.dismiss();
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_FOR_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					dialog.dismiss();
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(	Intent.createChooser(intent, "Select File"),
							REQUEST_FOR_GALLERY);
					/* Intent intent = new Intent();
                     intent.setType("image/*");
                     intent.setAction(Intent.ACTION_GET_CONTENT);
                     startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);*/
					//pickFromGallery();
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}

		});
		builder.show();
		
	}

public void click() {
	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	cameraIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);

	startActivityForResult(cameraIntent, VAL);
}

private void pickFromGallery() {

	Intent intent = new Intent();
	intent.setType("image/*");
	intent.setAction(Intent.ACTION_GET_CONTENT);
	getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"),
			SELECT_PICTURE);
}

public Uri getOutputMediaFileUri(int type) {
	return Uri.fromFile(getOutputMediaFile(type));
}

private static File getOutputMediaFile(int type) {

	File mediaStorageDir = new File(
			Environment.getExternalStorageDirectory() + "/"
					+ "expoMagik");
	if (!mediaStorageDir.exists()) {
		if (!mediaStorageDir.mkdirs()) {
			Log.d("TAG", "Oops! Failed create "
					+ "expoMagik" + " directory");
			return null;
		}
	}
	// Create a media file name
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss",
			Locale.getDefault()).format(new Date());


	File mediaFile;
	if (type == MEDIA_TYPE_IMAGE) {
		file_name = "EXPO_" + timeStamp + ".jpg";

		// image path
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "EXPO" + timeStamp + ".jpg");
	} else {
		return null;
	}
	return mediaFile;
}


@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d("Profile", "onActivityResult image selected from gallery onActivityResult");
	if (resultCode == getActivity().RESULT_OK) {
		if (requestCode == REQUEST_FOR_GALLERY){
			Log.d("Profile", "image selected from gallery onActivityResult");
			onSelectFromGalleryResult(data);
		}
		else if (requestCode == REQUEST_FOR_CAMERA)
			onCaptureImageResult(data);
	}
}

private void onCaptureImageResult(Intent data) {
	bm = (Bitmap) data.getExtras().get("data");
	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

	File destination = new File(Environment.getExternalStorageDirectory(),
			System.currentTimeMillis() + ".jpg");

	FileOutputStream fo;
	try {
		destination.createNewFile();
		fo = new FileOutputStream(destination);
		fo.write(bytes.toByteArray());
		fo.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	// convrting into base64
	/*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);*/
	byte[] byteArray = bytes.toByteArray();
	encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
	System.out.println("Base64encodedvalue:" + encoded);
	


	new ProfilePicture().execute();

}

private void onSelectFromGalleryResult(Intent data) {
	Log.d("Profile", "image selected from gallery");
	Uri selectedImageUri = data.getData();
	String[] projection = { MediaColumns.DATA };
	Cursor cursor = context.getContentResolver().query(selectedImageUri, projection, null, null,
			null);
	
	int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	cursor.moveToFirst();

	String selectedImagePath = cursor.getString(column_index);

	
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(selectedImagePath, options);
	final int REQUIRED_SIZE = 200;
	int scale = 1;
	while (options.outWidth / scale / 2 >= REQUIRED_SIZE
			&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
		scale *= 2;
	options.inSampleSize = scale;
	options.inJustDecodeBounds = false;
	bm = BitmapFactory.decodeFile(selectedImagePath, options);

	//convrting into base64
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
	bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
	byte[] byteArray = byteArrayOutputStream .toByteArray();
	encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
	System.out.println("Base64encodedvalue:"+encoded);
	
	new ProfilePicture().execute();
	
}


public boolean isExternalStorageWritable() {
	String state = Environment.getExternalStorageState();
	if (Environment.MEDIA_MOUNTED.equals(state)) {
		return true;
	}
	return false;
}

@Override
public void onSaveInstanceState(Bundle outState) {
	outState.putBoolean("taken", true);
}

/*public String getPath(Uri uri) {
	String[] projection = { MediaStore.Images.Media.DATA };
	Cursor cursor = managedQuery(uri, projection, null, null, null);
	int column_index = cursor
			.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
}*/

class ProfilePicture extends AsyncTask<String, Void, Void> {

	private String responceString, status="";
	
	String response = "null";
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected Void doInBackground(String... params) {

				// **Code**
				
				//android.os.Debug.waitForDebugger();

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("visitorid",Constants.visitorId));
				nameValuePairs.add(new BasicNameValuePair("imageData",encoded));
				
				Log.d("ProfileActivity", "ProfilePicList:"+nameValuePairs);
				
				responceString = UserFunctions.loadJson("http://webservices.expomagik.com/VisitorProfileImageSave.asmx/SaveImage", nameValuePairs);

				Log.d("ProfileActivity", "Profile resp:"+responceString);
				
				if (responceString != null) {
					try {
						JSONObject jsonObject = new JSONObject(responceString);
						status = (jsonObject.getString("status")).toString();
						
					}
				catch (JSONException e) {
						status = "0";
						e.printStackTrace();
					}  
						
				}
				
				return null;
				
			}

			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				pDialog.cancel();
				
				if(status.equals("1")){
					//ImageView iv_profPic = (ImageView) findViewById(R.id.imageView1);
					dp.setImageBitmap(bm);
					
					Toast.makeText(context, "Profile picture uploaded successfully.", Toast.LENGTH_LONG).show();
				
				}else{
					Toast.makeText(context, "Profile picture not updated.", Toast.LENGTH_LONG).show();
				}
				Constants.vDP = encoded;
				/*Log.i("image",Constants.vDP);
				SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(context);
				Editor edt1 = spp.edit();
				edt1.putString("vDP", Constants.vDP);
				
				edt1.commit();*/
				
			
				
				super.onPostExecute(result);
				
			}

		}


	public void getProfile() {

	// Fetch Data
	new AsyncTask<String, String, String>() {

		private String responceString, status="";
		
		private String imageURL;
		
		String response = "null";
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					
//					pDialog2.setMessage("Relax for a while...");
//					pDialog2.setCancelable(false);
//					pDialog2.show();
				}

		@Override
		protected String doInBackground(String... params) {
			imagepath="http://webservices.expomagik.com/VisitorProfileImage/"+encoded;
			
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
//			if (pDialog2.isShowing())
//				pDialog2.dismiss();

			Log.d("Profile", "iamgeurl is-"+imageURL);
			Picasso.with(context).load(imageURL).into(dp);
			if(status.equals("1")){
				
				//Picasso.with(context).load(imageURL).into(dp);
				
			}else{
				//Toast.makeText(getActivity(), "Profile picture not downloaded.", Toast.LENGTH_LONG).show();
			}
			
			
			//dp.setImageBitmap(imagepath);
			
			Log.i("imagepath", imagepath);
			
						
		}

	}.execute();

}


/*@Override
public void onStart() {
  super.onStart();
  getProfile();
}*/


/*public void DownloadImage() {
	
	imagepath="http://webservices.expomagik.com/VisitorProfileImage/"+encoded;
	
	ImageRequest ir = new ImageRequest(imagepath, new Response.Listener<Bitmap>() 
			{ 
		@Override 
		public void onResponse(Bitmap response) 
		{ 
			dp.setImageBitmap(response); 
			} 
		}, 0, 0, null, null); 
	
}
*/
}