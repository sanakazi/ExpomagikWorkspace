package com.conceptcandy.expomagik.model;

public class SpeakerRow {

	String speakerID;
	String agenda;
	String name;
	String topic;
	String brief;
	String photo;

	public SpeakerRow(String speakerID, String agenda, String name,
			String topic, String brief, String photo) {
		super();
		this.speakerID = speakerID;
		this.agenda = agenda;
		this.name = name;
		this.topic = topic;
		this.brief = brief;
		this.photo = photo;
	}

	public String getSpeakerID() {
		return speakerID;
	}

	public void setSpeakerID(String speakerID) {
		this.speakerID = speakerID;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
