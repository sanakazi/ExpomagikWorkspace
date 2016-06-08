package com.conceptcandy.expomagik.util;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import com.conceptcandy.expomagik.App;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.AppViewBuilder;
import com.google.android.gms.analytics.Tracker;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static void trackActivity(Context cntxt,  Tracker  mTracker, String ScreenName){
    	App application = (App) cntxt.getApplicationContext();
    	mTracker = application.getDefaultTracker();
    	mTracker.setScreenName("ScreenName");
    	mTracker.send(new AppViewBuilder().build());
    }
}