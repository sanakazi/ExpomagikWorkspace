package com.conceptcandy.expomagik.model;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conceptcandy.expomagik.EventInfo;
import com.conceptcandy.expomagik.Home;
import com.conceptcandy.expomagik.R;
import com.conceptcandy.expomagik.util.ImageLoader;
import com.conceptcandy.expomagik.utils.MyLinearLayout;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

public class PagerExbAdapter extends PagerAdapter {
 
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<HomePagerRow> data;
    
    String photoPath, pname;
	Bitmap image, bitmap;
	public ImageLoader imageLoader;
	public int index;
	public static RelativeLayout imageContainer;
	
	/*private boolean swipedLeft=false;
	private int lastPage=9;
	private MyLinearLayout cur = null;
	private MyLinearLayout next = null;
	private MyLinearLayout prev = null;
	private MyLinearLayout prevprev = null;
	private MyLinearLayout nextnext = null;
	private FragmentManager fm;
	private float scale;
	private boolean IsBlured;
	private static float minAlpha=0.6f;
	private static float maxAlpha=1f;
	private static float minDegree=60.0f;
	private int counter=0;*/
 
    public PagerExbAdapter(Context context, List<HomePagerRow> data) {
        
    	mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        
        imageLoader = new ImageLoader(context);
    }
 
    @Override
    public int getCount() {
        return data.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    
    @Override
    public void destroyItem(View container, int position, Object object) {
         ((ViewPager) container).removeView((View) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        
    	View itemView = mLayoutInflater.inflate(R.layout.row_home_pager, container, false);
    	
    	imageContainer = (RelativeLayout) itemView.findViewById(R.id.main_container);
 
    	ImageView imageExhibition = (ImageView) itemView.findViewById(R.id.imgHomePager);
    	TextView txtTime = (TextView) itemView.findViewById(R.id.txtHomeFromToDate);
    	TextView txtVenue = (TextView) itemView.findViewById(R.id.txtHomeVenue);
        
        final HomePagerRow itemdata = data.get(position);
		
		photoPath = itemdata.getMobileImage();
		photoPath = "http://www.expomagik.com/ImageExhibitionMobile/" + photoPath;
				
		Picasso.with(mContext).load(photoPath).into(imageExhibition);
		
		txtTime.setText(itemdata.getStartDate());
		txtVenue.setText(itemdata.getCity() + " - " + itemdata.getCountry());
        
		itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String ExhibitionID = itemdata.getExhibitionID();
				
				Intent eventDetails = new Intent(mContext, EventInfo.class);
				eventDetails.putExtra("ExhibitionID", ExhibitionID);
				mContext.startActivity(eventDetails);
			}
		});
		
        container.addView(itemView);
        return itemView;
    }
    
    public void imageTransform(int position){
    	
    }

	
    
}