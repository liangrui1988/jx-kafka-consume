package com.jx.kconsume.biz.js.model;

public class UserMarkingBean {
	private String user_id;
	private String client_id;
	private String user_project;
	private long mark_user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUser_project() {
		return user_project;
	}

	public void setUser_project(String user_project) {
		this.user_project = user_project;
	}

	public long getMark_user_id() {
		return mark_user_id;
	}

	public void setMark_user_id(long mark_user_id) {
		this.mark_user_id = mark_user_id;
	}

	@Override
	public String toString() {
		return "UserMarkingBean [user_id=" + user_id + ", client_id=" + client_id + ", user_project=" + user_project
				+ ", mark_user_id=" + mark_user_id + "]";
	}
	
	
	

}
