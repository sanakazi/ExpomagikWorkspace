package com.conceptcandy.expomagik.banner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	/*
	 * 
	 * Declaration of URL
	 * 
	 */
	public static String SITE_URL ="";
//	public static String SITE_URL ="http://caware.org/mrguniiweb/android";
	
	static String url;
	static String result;
	static HttpPost httpPost;
	static HttpClient httpClient;
	static JSONObject jsonObject;
	static HttpEntity httpEntity;
	static InputStream inputStream;
	static HttpResponse httpResponse;
	static List<NameValuePair> pairs;
	BufferedReader reader = null;
	
	static String json = "";

	private HttpURLConnection httpConnection;
	

	/**
     * Initialization of Variables
     */
    private static void init() {
        // TODO Auto-generated constructor stub
        result = "";
        jsonObject = null;
        httpPost = null;
        inputStream = null;
        httpClient = new DefaultHttpClient();
    }

    /** Converting InputStream to Result String */
    /**
     * @param is
     * @return
     */

    private static String getResult(InputStream is)
    {
        BufferedReader reader;
        StringBuilder stringBuilder = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            is.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringBuilder.toString();
    	
    }
    
    
    public JSONObject getData(String page_name,int id){
    	try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("id", ""+id));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject sendOrder(String page_name,String user_id,ArrayList<String> product_id,ArrayList<String> qty){
    	try
		{
			init();
			
			url = "http://caware.org/mrguniiweb/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("user_id", ""+user_id));
			for(int i=0;i<product_id.size();i++){
				pairs.add(new BasicNameValuePair("product_id["+i+"]", ""+product_id.get(i)));
				pairs.add(new BasicNameValuePair("qty["+i+"]", ""+qty.get(i)));
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject doLogin(String page_name,String email,String password){
    	try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", ""+email));
			pairs.add(new BasicNameValuePair("password", ""+password));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject resetPass(String page_name,String email){
    	try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", ""+email));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject signUp(String page_name,String fname,String lname,
    		String email,String password,String mno,String address,String city,String postal){
    	try
		{
			init();
			
			url = "http://caware.org/mrguniiweb" + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("fname", fname));
			pairs.add(new BasicNameValuePair("lname", lname));
			pairs.add(new BasicNameValuePair("mno", mno));
			pairs.add(new BasicNameValuePair("address", address));
			pairs.add(new BasicNameValuePair("city", city));
			pairs.add(new BasicNameValuePair("postal", postal));
			pairs.add(new BasicNameValuePair("email", email));
			pairs.add(new BasicNameValuePair("pass", password));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject CustomerUpdate(String page_name,String c_id,String fname,String lname,
    		String email,String password,String mno,String address,String city,String postal){
    	try
		{
			init();
			
			url = "http://caware.org/mrguniiweb" + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("cust_id", c_id));
			pairs.add(new BasicNameValuePair("fname", fname));
			pairs.add(new BasicNameValuePair("lname", lname));
			pairs.add(new BasicNameValuePair("mobile", mno));
			pairs.add(new BasicNameValuePair("shipping_address", address));
			pairs.add(new BasicNameValuePair("city", city));
			pairs.add(new BasicNameValuePair("postcode", postal));
			pairs.add(new BasicNameValuePair("email", email));
			pairs.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject getData(String page_name,String id){
    	try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("id", ""+id));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject changeAddress(String page_name,String email,String address){
    	try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", email));
			pairs.add(new BasicNameValuePair("shipping_address", address));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
    }
    
    public JSONObject getCategories(String page_name) {
		// TODO Auto-generated method stub
		Log.d("TAG"," ENTER IN SEND SUGGESTION");
		try
		{
			init();
			
			url = SITE_URL + "/"+page_name;
			
			httpPost=new HttpPost(url.toString());
			
			httpResponse=httpClient.execute(httpPost);
			
			Log.d("LOG", "Entered " + httpResponse);
			Log.d("LOG", httpResponse.getStatusLine().getStatusCode() + "");
			
			httpEntity = httpResponse.getEntity();
			inputStream=httpEntity.getContent();

			result = getResult(inputStream);
			
			Log.d("TAG", "RESULT :  "+result);
			
			jsonObject = new JSONObject(result);
		
		} catch (UnsupportedEncodingException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (ClientProtocolException e) {
			Log.d("LOG", e.getMessage() + "");
		} catch (JSONException e) {
			Log.e("LOG", "Error parsing data " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("LOG", e.getMessage() + "");
		}
		return jsonObject;
	}
    
    private void openHttpUrlConnection(String urlString) throws IOException {
		Log.d("urlstring in parser", urlString + "");
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		httpConnection = (HttpURLConnection) connection;
		httpConnection.setConnectTimeout(30000);
		httpConnection.setRequestMethod("GET");

		httpConnection.connect();
	}

	private void openHttpClient(String urlString) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

		HttpGet httpGet = new HttpGet(urlString);

		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		inputStream = httpEntity.getContent();

		reader = new BufferedReader(
				new InputStreamReader(inputStream, "UTF-8"), 8);
	}

	// using HttpClient for <= Froyo
	public JSONObject getJSONHttpClient(String url)
			throws ClientProtocolException, IOException, JSONException {
		JSONObject jsonObject = null;
		try {
			openHttpClient(url);

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			json = sb.toString();

			Log.d("json", json);
			jsonObject = new JSONObject(json);

		} finally {
			FileUtils.close(inputStream);
			FileUtils.close(reader);
		}
		return jsonObject;
	}

	// using HttpURLConnection for > Froyo
	public JSONObject getJSONHttpURLConnection(String urlString)
			throws IOException, JSONException {

		BufferedReader reader = null;
		StringBuffer output = new StringBuffer("");
		InputStream stream = null;
		JSONObject jsonObject = null;
		try {

			openHttpUrlConnection(urlString);

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();

				reader = new BufferedReader(new InputStreamReader(stream,
						"UTF-8"), 8);
				String line = "";
				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
				json = output.toString();
				jsonObject = new JSONObject(json);
			}

		} finally {
			FileUtils.close(stream);
			FileUtils.close(reader);
		}
		return jsonObject;
	}
	

	public String getJSONFromUrl(String url) {

		InputStream is = null;
		
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			json = sb.toString();
			is.close();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return json;

	}
}
