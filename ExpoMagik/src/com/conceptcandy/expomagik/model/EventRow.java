package com.conceptcandy.expomagik.model;

public class EventRow {

	String ExhibitionID;
	String date;
	String name;
	String place;
	String category;
	String attachment;
	String icon;

	public EventRow(String ExhibitionID, String date, String name, String place, String category, String attachment) {
		super();
		this.ExhibitionID = ExhibitionID;
		this.date = date;
		this.name = name;
		this.place = place;
		this.category = category;
		this.attachment = attachment;
	}

	public EventRow(String ExhibitionID, String icon, String date, String name, String place, String category,
			String attachment) {
		super();
		this.icon = icon;
		this.ExhibitionID = ExhibitionID;
		this.date = date;
		this.name = name;
		this.place = place;
		this.category = category;
		this.attachment = attachment;
	}

	public String getExhibitionID() {
		return ExhibitionID;
	}

	public void setExhibitionID(String exhibitionID) {
		ExhibitionID = exhibitionID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "EventRow [ExhibitionID=" + ExhibitionID + ", date=" + date + ", name=" + name + ", place=" + place
				+ ", category=" + category + ", attachment=" + attachment + ", icon=" + icon + "]";
	}

}
