package com.conceptcandy.expomagik.model;

public class ExhibitorRow {

	String exbid;
	String exbName;
	String exbAddress;
	String exbCity;
	String exbCountry;
	String compLogo;

	public ExhibitorRow(String exbid, String exbName, String exbAddress,
			String exbCity, String exbCountry, String compLogo) {
		super();
		this.exbid = exbid;
		this.exbName = exbName;
		this.exbAddress = exbAddress;
		this.exbCity = exbCity;
		this.exbCountry = exbCountry;
		this.compLogo = compLogo;
	}

	public String getCompLogo() {
		return compLogo;
	}

	public void setCompLogo(String compLogo) {
		this.compLogo = compLogo;
	}

	public String getExbid() {
		return exbid;
	}

	public void setExbid(String exbid) {
		this.exbid = exbid;
	}

	public String getExbName() {
		return exbName;
	}

	public void setExbName(String exbName) {
		this.exbName = exbName;
	}

	public String getExbAddress() {
		return exbAddress;
	}

	public void setExbAddress(String exbAddress) {
		this.exbAddress = exbAddress;
	}

	public String getExbCity() {
		return exbCity;
	}

	public void setExbCity(String exbCity) {
		this.exbCity = exbCity;
	}

	public String getExbCountry() {
		return exbCountry;
	}

	public void setExbCountry(String exbCountry) {
		this.exbCountry = exbCountry;
	}

}
