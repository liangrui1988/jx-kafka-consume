package com.jx.kconsume.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http请求协议
 * 
 * @author rui
 * @date 2017/08/30
 */
public class HttpUtils {
	public static final String key = "987GAMEadmin*&^%";
	static CloseableHttpClient client = HttpClients.createDefault();
	public static final String PROTO_ID = "proto_id";
	public static final String SERVER_ID = "proto_id";
	/**
	 * 游戏项目名
	 */
	public static final String CONTENT = "/admin";

	/**
	 * http post json
	 * 
	 * @param url
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static String httpPostJSON(String url, String args, int socketTimeout, int connectTimeout) throws Exception {
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		HttpPost httpPost = new HttpPost(url);
		String respContent = null;
		StringEntity entity = new StringEntity(args, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
		httpPost.setConfig(requestConfig);
		// 表单方式
		// List<BasicNameValuePair> pairList = new
		// ArrayList<BasicNameValuePair>();
		// pairList.add(new BasicNameValuePair("name", "admin"));
		// pairList.add(new BasicNameValuePair("pass", "123456"));
		// httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			respContent = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return respContent;
	}

	// 方法重载
	public static String httpPostJSON(String url, String args) throws Exception {
		return httpPostJSON(url, args, 6000, 6000);
	}

	/**
	 * http post json
	 * 
	 * @param url
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static String httpGet(String url, int socketTimeout, int connectTimeout) throws Exception {
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		// 创建http请求(get方式)
		HttpGet httpget = new HttpGet(url);
//		System.out.println("executing request" + httpget.getRequestLine());
		CloseableHttpResponse resp = client.execute(httpget);
		String respContent = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
		httpget.setConfig(requestConfig);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			respContent = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return respContent;
	}

	// 方法重载
	public static String httpGet(String url) throws Exception {
		return httpGet(url, 6000, 6000);
	}

	public static void main(String[] args) throws Exception {
		String msg = httpGet(
				"http://api.map.baidu.com/location/ip?ak=tp3bYrVMLPXxFu27Vwio5q9UcH3pEmov&ip=119.129.74.219");
		System.out.println(msg);
	}

}
