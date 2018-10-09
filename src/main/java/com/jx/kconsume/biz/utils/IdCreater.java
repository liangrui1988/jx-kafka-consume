package com.jx.kconsume.biz.utils;

import java.util.UUID;

public class IdCreater {

	public static String getUUID(String prefix) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return prefix + uuid;
	}

	public static void main(String[] args) {
		System.out.println(IdCreater.getUUID("web-"));
	}

}
