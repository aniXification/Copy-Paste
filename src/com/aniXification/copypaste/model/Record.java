package com.aniXification.copypaste.model;

public class Record {

	private Long id;
	private String record;
	private String fav;
	private Long timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getFav() {
		return fav;
	}

	public void setFav(String fav) {
		this.fav = fav;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
