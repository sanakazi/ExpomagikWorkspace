package com.conceptcandy.expomagik.model;

public class VisitorRow {

	String visitorID;
	String visitorName;
	String Email;
	String mobileNo;
	String address;
	String companyName;
	String designation;

	public VisitorRow(String visitorID, String visitorName, String email,
			String mobileNo, String address, String companyName,
			String designation) {
		super();
		this.visitorID = visitorID;
		this.visitorName = visitorName;
		Email = email;
		this.mobileNo = mobileNo;
		this.address = address;
		this.companyName = companyName;
		this.designation = designation;
	}

	public String getVisitorID() {
		return visitorID;
	}

	public void setVisitorID(String visitorID) {
		this.visitorID = visitorID;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

}
