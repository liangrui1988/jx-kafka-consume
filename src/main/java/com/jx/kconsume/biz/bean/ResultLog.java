package com.jx.kconsume.biz.bean;

import java.io.Serializable;

/**
 * 返回参数
 * 
 * @author liangrui
 *
 */
public class ResultLog implements Serializable {
	private static final long serialVersionUID = 3475946724048234071L;
	// private boolean success = true;
	private int code = 0;// 0成功，其它失败
	private Object data = "";
	private Object msg = "";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
