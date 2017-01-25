package com.game.douniu.jni;


import com.game.douniu.custom.IDouniuCallback;
import android.util.Log;


public class DouniuClientInterface {
	private static String TAG = "[wzj]DouniuClientInterface";
	private IDouniuCallback callback;

	static {

		try {
			System.loadLibrary("douniuclient_jni");
		} catch (Throwable ex) {
			// ex.printStackTrace();
			Log.e(TAG, "load douniuclient_jni error!");
		}
	}
	
	public DouniuClientInterface(IDouniuCallback callback) {
		this.callback = callback;
		
		boolean bret = nativeInit();
		Log.d(TAG, "[nativeInit]bret:"+bret);
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
	
	//native method
	public native String stringFromJNI();//test
	public native boolean nativeInit();//通知native层保存回调方法
	public native boolean nativeStop();
	public native int nativeConnectAndLogin(String ipaddr, String username, String password);

	//java method define for C
	public void loginCb(int userid, String str) {
        Log.d(TAG, "[loginCb]userid:"+userid+",str:"+str);
		//callback.loginCb(data, datalen);
	}
	
	public void otherLoginCb(String str) {
        Log.d(TAG, "[otherLoginCb]str:"+str);

	}
	
	public void logoutCb(int value) {
		Log.d(TAG, "[logoutCb]value:"+value);
		callback.logoutCb(value);
	}
	
	public void prepareCb(String str) {
        Log.d(TAG, "[prepareCb]str:"+str);

	}
	
	public void otherUserPrepareCb(String str) {
        Log.d(TAG, "[otherUserPrepareCb]str:"+str);

	}
	
	public void willBankerCb(String str) {
        Log.d(TAG, "[willBankerCb]str:"+str);

	}
	
	public void tryBankerCb(String str) {
        Log.d(TAG, "[tryBankerCb]str:"+str);

	}
	
	public void willStakeCb(String str) {
        Log.d(TAG, "[willStakeCb]str:"+str);

	}
	
	public void stakeCb(String str) {
        Log.d(TAG, "[stakeCb]str:"+str);

	}
	
	public void otherUserStakeValueCb(String str) {
        Log.d(TAG, "[otherUserStakeValueCb]str:"+str);

	}
	
	public void willStartCb(String str) {
        Log.d(TAG, "[willStartCb]str:"+str);

	}
	
	public void playCb(String str) {
        Log.d(TAG, "[playCb]str:"+str);

	}
	
	public void otherUserCardPatternCb(String str) {
        Log.d(TAG, "[otherUserCardPatternCb]str:"+str);

	}
	
	public void gameResultCb(String str) {
        Log.d(TAG, "[gameResultCb]str:"+str);

	}
}
