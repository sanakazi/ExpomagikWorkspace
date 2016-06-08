package com.conceptcandy.expomagik.model;

public class AgendaRow {

	String agendaID;
	String agendaTitle;
	String agendaDesc;

	public AgendaRow(String agendaID, String agendaTitle, String agendaDesc) {
		super();
		this.agendaID = agendaID;
		this.agendaTitle = agendaTitle;
		this.agendaDesc = agendaDesc;
	}

	public String getAgendaID() {
		return agendaID;
	}

	public void setAgendaID(String agendaID) {
		this.agendaID = agendaID;
	}

	public String getAgendaTitle() {
		return agendaTitle;
	}

	public void setAgendaTitle(String agendaTitle) {
		this.agendaTitle = agendaTitle;
	}

	public String getAgendaDesc() {
		return agendaDesc;
	}

	public void setAgendaDesc(String agendaDesc) {
		this.agendaDesc = agendaDesc;
	}

}
