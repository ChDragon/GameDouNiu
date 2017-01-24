package com.game.douniu.jni;


import android.util.Log;


public class DouniuClient {
	private static String TAG = "[wzj]DouniuClient";

	static {

		try {
			System.loadLibrary("douniuclient_jni");
		} catch (Throwable ex) {
			// ex.printStackTrace();
			Log.e(TAG, "load douniuclient_jni error!");
		}
	}
	
	public DouniuClient() {
	}
	
	public String getString() {
		String str = stringFromJNI();
		Log.d(TAG, "[getString]str:"+str);
		return str;
	}
	
	public int connectAndLogin(String ipaddr, String username, String password) {
		int ret = nativeConnectAndLogin(ipaddr, username, password);
		Log.d(TAG, "[connectAndLogin]ret:"+ret);
		return ret;
	}

	public native String stringFromJNI();
	
	public native int nativeConnectAndLogin(String ipaddr, String username, String password);
}
