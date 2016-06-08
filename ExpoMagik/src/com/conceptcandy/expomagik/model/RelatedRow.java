package com.conceptcandy.expomagik.model;

public class RelatedRow {

	String exhibitionID;
	String exhibitionName;
	String startdate;
	String exhibitionDayEnd;
	String city;
	String country;
	String img;

	

	public RelatedRow(String exhibitionID, String exhibitionName,
			String startdate, String exhibitionDayEnd, String city,
			String country,String img) {
		super();
		this.exhibitionID = exhibitionID;
		this.exhibitionName = exhibitionName;
		this.startdate = startdate;
		this.exhibitionDayEnd = exhibitionDayEnd;
		this.city = city;
		this.country = country;
		this.img = img;
	}

	public String getExhibitionID() {
		return exhibitionID;
	}

	public void setExhibitionID(String exhibitionID) {
		this.exhibitionID = exhibitionID;
	}

	public String getExhibitionName() {
		return exhibitionName;
	}

	public void setExhibitionName(String exhibitionName) {
		this.exhibitionName = exhibitionName;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getExhibitionDayEnd() {
		return exhibitionDayEnd;
	}

	public void setExhibitionDayEnd(String exhibitionDayEnd) {
		this.exhibitionDayEnd = exhibitionDayEnd;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
