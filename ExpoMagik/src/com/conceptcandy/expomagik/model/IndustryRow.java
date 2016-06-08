package com.conceptcandy.expomagik.model;

public class IndustryRow {

	String id;
	String icon;
	String catName;
	String noOfUpcomingExhibition;

	public IndustryRow(String id, String icon, String catName,
			String noOfUpcomingExhibition) {
		super();
		this.id = id;
		this.icon = icon;
		this.catName = catName;
		this.noOfUpcomingExhibition = noOfUpcomingExhibition;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getNoOfUpcomingExhibition() {
		return noOfUpcomingExhibition;
	}

	public void setNoOfUpcomingExhibition(String noOfUpcomingExhibition) {
		this.noOfUpcomingExhibition = noOfUpcomingExhibition;
	}

}
