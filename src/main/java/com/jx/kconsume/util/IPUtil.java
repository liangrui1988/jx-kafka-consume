package com.jx.kconsume.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ip工具
 * 
 * @author rui
 *
 */
public class IPUtil {

	private static Logger log = LoggerFactory.getLogger(IPUtil.class);

	/**
	 * 获取网络中真实的IP地址
	 * 
	 * @param request
	 * @return ip
	 */
	public static String getIPFromRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String[] arrIp = ip.split(",");
		if (arrIp.length > 1) {
			ip = arrIp[1];
		}
		if (ip == null) {
			return "";
		}
		return ip.trim();
	}

}
