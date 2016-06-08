package com.conceptcandy.expomagik;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreferences { 
	public static final String preferencFile = "preFile";
	//public static final String preferencSettingFile = "settingsFile";
	private static MySharedPreferences sharePre = null;


	public static MySharedPreferences getInstance(){
	if(sharePre == null)
	sharePre = new MySharedPreferences();

	return sharePre;
	}

	public SharedPreferences getSharedFile(Context context){
	SharedPreferences shareFile = context.getSharedPreferences(preferencFile, Context.MODE_MULTI_PROCESS);
	return shareFile;
	}

	/*public SharedPreferences getSettingsFile(Context context){
	SharedPreferences shareFile = context.getSharedPreferences(preferencSettingFile, Context.MODE_MULTI_PROCESS);
	return shareFile;
	}*/

	/* public void setValues(Context context, HashMap<String, Object> map){
	SharedPreferences shareFile = getSharedFile(context);
	Editor editor = shareFile.edit();
	if(map != null && map.size() > 0){
	Set<String> keySet = map.keySet();
	Iterator<String> iterator = keySet.iterator();
	while(iterator.hasNext()){
	String key = iterator.next();
	Object obj = map.get(key);
	if(obj instanceof String){
	editor.putString(key, String.valueOf(obj)); 
	}else if(obj instanceof Integer){
	editor.putInt(key, (Integer)obj); 
	}else if(obj instanceof Boolean){
	editor.putBoolean(key, (Boolean)obj); 
	}else{
	editor.putString(key, String.valueOf(obj));
	}
	}
	}
	editor.commit();
	}
	*/
	public void setValues(Context context, JSONObject map){
	SharedPreferences shareFile = getSharedFile(context);
	Editor editor = shareFile.edit();
	try {
	if(map != null && map.length() > 0){
	Iterator<String> iterator = map.keys();
	Object obj = null; 
	while(iterator.hasNext()){
	String key = iterator.next(); 
	obj = map.get(key);
	if(obj instanceof String){
	editor.putString(key, String.valueOf(obj)); 
	}else if(obj instanceof Integer){
	editor.putInt(key, (Integer)obj); 
	}else if(obj instanceof Boolean){
	editor.putBoolean(key, (Boolean)obj); 
	}else{
	editor.putString(key, String.valueOf(obj));
	}
	}
	}
	} catch (JSONException e) {
	e.printStackTrace();
	}

	editor.commit();
	}

	public Map<String, ?> getMap(Context context){ 
	SharedPreferences shareFile = getSharedFile(context);
	Map<String, ?> map = shareFile.getAll();
	return map;
	}

	/*public boolean isRegisteredS(Context context){
	SharedPreferences shareFile = getSettingsFile(context);
	if(shareFile.getString(URLsClass.p_clientUUID, null) == null || 
	shareFile.getString(URLsClass.p_farmUUID, null) == null)
	return false;
	else
	return true;
	}

	public void saveSettings(Context context, String farmName, String passKey, String hostName, String clientUUId, String farmUUID){
	SharedPreferences file = getSettingsFile(context);
	Editor ed = file.edit();
	ed.putString(URLsClass.p_farmName, farmName);
	ed.putString(URLsClass.p_activationKey, passKey);
	ed.putString(URLsClass.p_hostName, hostName); 
	ed.putString(URLsClass.p_clientUUID, clientUUId);
	ed.putString(URLsClass.p_farmUUID, farmUUID);
	ed.commit();
	}*/

	/*public Map<String, ?> getMapSettings(Context context){ 
	SharedPreferences shareFile = getSettingsFile(context);
	Map<String, ?> map = shareFile.getAll();
	return map;
	}*/




	public void clearShareFile(Context context){
	SharedPreferences shareFile = getSharedFile(context);
	shareFile.edit().clear().commit();
	} 

	public String getString(Context context, String key, String defaultValue){
	SharedPreferences sp = getInstance().getSharedFile(context);
	return sp.getString(key, defaultValue); 
	}

	/*public String getStringFromSettings(Context context, String key, String defaultValue){
	SharedPreferences sp = getInstance().getSettingsFile(context);
	return sp.getString(key, defaultValue); 
	}*/

	public void logout(Context context) {
	MySharedPreferences.getInstance().putStringKeyValue(context, Constants.visitorId, "-1");
	}

	public void putStringKeyValue(Context context, String key, String value){
	Editor ed = getInstance().getSharedFile(context).edit();
	ed.putString(key, value);
	ed.commit();
	}

	public void putIntegerKeyValue(Context context, String key, int value){
	Editor ed = getInstance().getSharedFile(context).edit();
	ed.putInt(key, value);
	ed.commit();
	}

	public void putBooleanKeyValue(Context context, String key, boolean value){
	Editor ed = getInstance().getSharedFile(context).edit();
	ed.putBoolean(key, value);
	ed.commit();
	} 
	public boolean getBoolean(Context context, String key, boolean defaultValue){
	SharedPreferences sp = getInstance().getSharedFile(context);
	return sp.getBoolean(key, defaultValue); 
	}

	public int getInteger(Context context, String key, int defaultValue){
	SharedPreferences sp = getInstance().getSharedFile(context);
	return sp.getInt(key, defaultValue); 
	}

	public String getStringFromDefaultFile(Context context, String key, String defaultValue){
	SharedPreferences sp = getInstance().getSharedFile(context);
	return sp.getString(key, defaultValue); 
	}

	public boolean getBooleanFromDefaultFile(Context context, String key, boolean defaultValue){
	SharedPreferences sp = getInstance().getSharedFile(context);
	return sp.getBoolean(key, defaultValue); 
	}

	}