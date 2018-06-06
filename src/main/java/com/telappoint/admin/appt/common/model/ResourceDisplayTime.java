package com.telappoint.admin.appt.common.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class ResourceDisplayTime implements Serializable {
    private static final long serialVersionUID = 1L;

	public ResourceDisplayTime() {
	}

	private Long id;

	private int resource_id;
	
	private Timestamp start_time;

	private Timestamp end_time;

	private Timestamp display_time;
	
	private String display_time_range;

	private int display_duration;
	
	private String display_time_audio;
	
	private String display_time_tts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getResource_id() {
		return resource_id;
	}

	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	public Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public Timestamp getDisplay_time() {
		return display_time;
	}

	public void setDisplay_time(Timestamp display_time) {
		this.display_time = display_time;
	}

	public String getDisplay_time_range() {
		return display_time_range;
	}

	public void setDisplay_time_range(String display_time_range) {
		this.display_time_range = display_time_range;
	}

	public int getDisplay_duration() {
		return display_duration;
	}

	public void setDisplay_duration(int display_duration) {
		this.display_duration = display_duration;
	}

	public String getDisplay_time_audio() {
		return display_time_audio;
	}

	public void setDisplay_time_audio(String display_time_audio) {
		this.display_time_audio = display_time_audio;
	}

	public String getDisplay_time_tts() {
		return display_time_tts;
	}

	public void setDisplay_time_tts(String display_time_tts) {
		this.display_time_tts = display_time_tts;
	}
}