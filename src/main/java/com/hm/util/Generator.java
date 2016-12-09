package com.hm.util;

import java.util.UUID;

public class Generator {

	public static String genId () {
		return UUID.randomUUID().toString().substring(0,18);
	}

	public static String genBigId() {
		return UUID.randomUUID().toString();
	}

	public static String genSmallId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
