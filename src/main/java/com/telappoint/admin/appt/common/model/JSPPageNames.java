package com.telappoint.admin.appt.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


@JsonSerialize(include = Inclusion.NON_NULL)
public class JSPPageNames {
	
	private long id;
	private String jsp_page;
	private String description;
	private int placement;
	private char enable_flag = 'Y';
	
	//Populating from JspPagesPrivileges
	private String group_title;
	private String pages_title;
	private String jsp_pages;
	private String jsp_pages_description;
		
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getJsp_page() {
		return jsp_page;
	}
	public void setJsp_page(String jsp_page) {
		this.jsp_page = jsp_page;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPlacement() {
		return placement;
	}
	public void setPlacement(int placement) {
		this.placement = placement;
	}
	public char getEnable_flag() {
		return enable_flag;
	}
	public void setEnable_flag(char enable_flag) {
		this.enable_flag = enable_flag;
	}
	public String getGroup_title() {
		return group_title;
	}
	public void setGroup_title(String group_title) {
		this.group_title = group_title;
	}
	public String getPages_title() {
		return pages_title;
	}
	public void setPages_title(String pages_title) {
		this.pages_title = pages_title;
	}
	public String getJsp_pages() {
		return jsp_pages;
	}
	public void setJsp_pages(String jsp_pages) {
		this.jsp_pages = jsp_pages;
	}
	public String getJsp_pages_description() {
		return jsp_pages_description;
	}
	public void setJsp_pages_description(String jsp_pages_description) {
		this.jsp_pages_description = jsp_pages_description;
	}	
}
