package com.conceptcandy.expomagik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class TwoTabs  extends Fragment{
	
	
	ImageView ic_drGridExb,linePinkSelcted,lineBlueSelcted;
	Button tab_InCity,tab_TopTrending;
	GridView grid_view;
	private View view;
	
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		view = inflater.inflate(R.layout.two_tabs, null);
		
		
		return view;
	}
	

}
