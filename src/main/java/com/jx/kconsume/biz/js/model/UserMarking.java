package com.jx.kconsume.biz.js.model;

/**
 * 用户关联设备标识
 * 
 * @author ruiliang
 * @date 20180903
 *
 */
public class UserMarking {

	private long user_id;
	private String client_id;
	private String user_project;
	private long insert_date;
	private long update_date;

	public String getUser_project() {
		return user_project;
	}

	public void setUser_project(String user_project) {
		this.user_project = user_project;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public long getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(long insert_date) {
		this.insert_date = insert_date;
	}

	public long getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(long update_date) {
		this.update_date = update_date;
	}

	@Override
	public String toString() {
		return "UserMarking [user_id=" + user_id + ", client_id=" + client_id + ", user_project=" + user_project
				+ ", insert_date=" + insert_date + ", update_date=" + update_date + "]";
	}

}
