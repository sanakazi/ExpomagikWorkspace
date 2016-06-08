package com.conceptcandy.expomagik.model;

public class HomePagerRow {

	String exhibitionID;
	String exhibitionName;
	String startDate;
	String country;
	String city;
	String industry;
	String mobileImage;

	public HomePagerRow(String exhibitionID, String exhibitionName,
			String startDate, String country, String city, String industry,
			String mobileImage) {
		super();
		this.exhibitionID = exhibitionID;
		this.exhibitionName = exhibitionName;
		this.startDate = startDate;
		this.country = country;
		this.city = city;
		this.industry = industry;
		this.mobileImage = mobileImage;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getMobileImage() {
		return mobileImage;
	}

	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

}
