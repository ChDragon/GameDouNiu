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
	
	public DouniuClientInterface() {
	}
	
	public void setDouniuCallback(IDouniuCallback callback) {
		this.callback = callback;
		nativeInit();
	}
	
	public String getString() {
		String str = stringFromJNI();
		Log.d(TAG, "[getString]str:"+str);
		return str;
	}
	
	public String connectAndLogin(String ipaddr, String username, String password) {
		String ret = nativeConnectAndLogin(ipaddr, username, password);
		Log.d(TAG, "[connectAndLogin]ret:"+ret);
		return ret;
	}
	
	public int logoutAndExit(String username) {
		int ret = nativeLogoutAndExit(username);
		Log.d(TAG, "[logoutAndExit]ret:"+ret);
		return ret;
	}
	
	public void prepareCMD() {
		Log.d(TAG, "[prepareCMD]start");
		nativePrepareCMD();
	}
	
	public void tryingBankerCMD(int value) {
		Log.d(TAG, "[tryingBankerCMD]value:"+value);
		nativeTryingBankerCMD(value);
	}
	
	public void stakeCMD(int stakeValue) {
		Log.d(TAG, "[stakeCMD]stakeValue:"+stakeValue);
		nativeStakeCMD(stakeValue);
	}
	
	public void playCMD(int niuValue) {
		Log.d(TAG, "[playCMD]niuValue:"+niuValue);
		nativePlayCMD(niuValue);
	}
	
	//native method
	public native String stringFromJNI();//test
	public native boolean nativeInit();//通知native层保存回调方法
	public native boolean nativeStop();
	public native String nativeConnectAndLogin(String ipaddr, String username, String password);
	public native int nativeLogoutAndExit(String username);
	public native void nativePrepareCMD();
	public native void nativeTryingBankerCMD(int value);
	public native void nativeStakeCMD(int stakeValue);
	public native void nativePlayCMD(int niuValue);
	

	//java method define for C
	public void loginCb(int userid, String str) {
        Log.d(TAG, "[loginCb]userid:"+userid+",str:"+str);
		//callback.loginCb(data, datalen);
	}
	
	public void otherLoginCb(String str) {
        Log.d(TAG, "[otherLoginCb]str:"+str);
        callback.otherLoginCb(str);
	}
	
	public void logoutCb(int value) {
		Log.d(TAG, "[logoutCb]value:"+value);
		callback.logoutCb(value);
	}
	
	public void otherLogoutCb(int value) {
		Log.d(TAG, "[otherLogoutCb]value:"+value);
		callback.otherLogoutCb(value);
	}
	
	public void prepareCb(String str) {
        Log.d(TAG, "[prepareCb]str:"+str);
        callback.prepareCb(str);
	}
	
	public void otherUserPrepareCb(String str) {
        Log.d(TAG, "[otherUserPrepareCb]str:"+str);
        callback.otherUserPrepareCb(str);
	}
	
	public void willBankerCb(String str) {
        Log.d(TAG, "[willBankerCb]str:"+str);
        callback.willBankerCb(str);
	}
	
	public void tryBankerCb(String str) {
        Log.d(TAG, "[tryBankerCb]str:"+str);
        callback.tryBankerCb(str);
	}
	
	public void willStakeCb(String str) {
        Log.d(TAG, "[willStakeCb]str:"+str);
        callback.willStakeCb(str);
	}
	
	public void stakeCb(String str) {
        Log.d(TAG, "[stakeCb]str:"+str);
        callback.stakeCb(str);
	}
	
	public void otherUserStakeValueCb(String str) {
        Log.d(TAG, "[otherUserStakeValueCb]str:"+str);
        callback.otherUserStakeValueCb(str);
	}
	
	public void willStartCb(String str) {
        Log.d(TAG, "[willStartCb]str:"+str);
        callback.willStartCb(str);
	}
	
	public void playCb(String str) {
        Log.d(TAG, "[playCb]str:"+str);
        callback.playCb(str);
	}
	
	public void otherUserCardPatternCb(String str) {
        Log.d(TAG, "[otherUserCardPatternCb]str:"+str);
        callback.otherUserCardPatternCb(str);
	}
	
	public void gameResultCb(String str) {
        Log.d(TAG, "[gameResultCb]str:"+str);
        callback.gameResultCb(str);
	}
}
