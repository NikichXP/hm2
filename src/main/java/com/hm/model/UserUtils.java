package com.hm.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserUtils {

	public static final int CRYPTOLENGTH = 128;
	private static MessageDigest mDigest = null;

	static {
		try {
			mDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String encryptPass(String login, String pass) {
		if (pass == null) {
			pass = "";
			System.err.println("Pass null on login " + login);
		}
		if (pass.length() == CRYPTOLENGTH) {
			return pass;
		}
		byte[] bytes = new byte[pass.getBytes().length + login.getBytes().length];
		int ptr = 0;
		for (byte b : login.getBytes()) {
			bytes[ptr++] = b;
		}
		for (byte b : pass.getBytes()) {
			bytes[ptr++] = b;
		}

		byte[] result;
		synchronized (mDigest) {
			result = mDigest.digest(bytes);
		}
		short[] secret = {228, 41, 34, 116, 78, 43, 30, 139, 229, 192, 122, 181, 0, 80, 112, 106};

		for (int i = 0; i < result.length; i++) {
			result[i] ^= secret[ptr++ % secret.length];
		}

		StringBuilder sb = new StringBuilder();
		for (byte aResult : result) {
			sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
		}
		if (sb.length() == 0) {
			throw new NullPointerException("Where is DATA");
		}
		return sb.toString();
	}

}
