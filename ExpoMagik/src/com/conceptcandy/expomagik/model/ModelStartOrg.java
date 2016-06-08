package com.conceptcandy.expomagik.model;

public class ModelStartOrg {

	String ID = "";
	String Title = "";
	String Imageurl = "";
	String link = "";
	String organiserid = "";
	
	
	
	
	
	
	
	public ModelStartOrg(String id, String title, String imageurl, String link, String orgniserid) {
		// TODO Auto-generated constructor stub
		
		super();
		
		this.link= link;
		this.ID=id;
		this.Title=title;
		this.Imageurl=imageurl;
		this.organiserid=orgniserid;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getImageurl() {
		return Imageurl;
	}

	public void setImageurl(String imageurl) {
		Imageurl = imageurl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getOrganiserid() {
		return organiserid;
	}

	public void setOrganiserid(String organiserid) {
		this.organiserid = organiserid;
	}

	@Override
	public String toString() {
		return "ModelStartOrg [ID=" + ID + ", Title=" + Title + ", Imageurl=" + Imageurl + ", link=" + link
				+ ", organiserid=" + organiserid + "]";
	}

}
