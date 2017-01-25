package com.game.douniu.jni;


import com.game.douniu.custom.IDouniuCallback;
import com.game.douniu.custom.ILoginJoinCallback;

import android.util.Log;


public class DouniuClientInterface {
	private static String TAG = "[wzj]DouniuClientInterface";
	
	private static DouniuClientInterface sInstance;
	private IDouniuCallback callback;
	private ILoginJoinCallback loginJoinCallback;
	private boolean isInitSuccess = false;

	static {

		try {
			System.loadLibrary("douniuclient_jni");
		} catch (Throwable ex) {
			// ex.printStackTrace();
			Log.e(TAG, "load douniuclient_jni error!");
		}
	}
	
	public static DouniuClientInterface getInstance() {
		if (sInstance == null) {
			sInstance = new DouniuClientInterface();
		}
		return sInstance;
	}
	
	private DouniuClientInterface() {
	}
	
	public void setDouniuCallback(IDouniuCallback callback) {
		this.callback = callback;
		if (!isInitSuccess) {
			isInitSuccess = nativeInit();
			Log.d(TAG, "[setDouniuCallback]nativeInit "+isInitSuccess);
		}
	}
	
	public void setLoginJoinCallback(ILoginJoinCallback callback) {
		this.loginJoinCallback = callback;
		if (!isInitSuccess) {
			isInitSuccess = nativeInit();
			Log.d(TAG, "[setDouniuCallback]nativeInit "+isInitSuccess);
		}
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
	
	public void joinRoomCMD() {
		Log.d(TAG, "[joinRoomCMD]start");
		nativeJoinRoomCMD();
	}
	
	public void exitRoomCMD() {
		Log.d(TAG, "[exitRoomCMD]start");
		nativeExitRoomCMD();
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
	
	public void submitCMD(int niuValue) {
		Log.d(TAG, "[submitCMD]niuValue:"+niuValue);
		nativeSubmitCMD(niuValue);
	}
	
	//native method
	public native String stringFromJNI();//test
	public native boolean nativeInit();//通知native层保存回调方法
	public native boolean nativeStop();
	public native String nativeConnectAndLogin(String ipaddr, String username, String password);
	public native int nativeLogoutAndExit(String username);
	public native void nativeJoinRoomCMD();
	public native void nativeExitRoomCMD();
	public native void nativePrepareCMD();
	public native void nativeTryingBankerCMD(int value);
	public native void nativeStakeCMD(int stakeValue);
	public native void nativeSubmitCMD(int niuValue);
	

	//java method define for C
	public void loginCb(int userid, String str) {
        Log.d(TAG, "[loginCb]userid:"+userid+",str:"+str);
		//loginJoinCallback.loginCb(data, datalen);
	}

	public void logoutCb(int value) {
		Log.d(TAG, "[logoutCb]value:"+value);
		if (loginJoinCallback == null) {
			Log.d(TAG, "loginJoinCallback null");
			return;
		}
		loginJoinCallback.logoutCb(value);
	}

	public void joinRoomCb(String str) {
        Log.d(TAG, "[joinRoomCb]str:"+str);
		if (loginJoinCallback == null) {
			Log.d(TAG, "loginJoinCallback null");
			return;
		}
        loginJoinCallback.joinRoomCb(str);
	}
	
	public void exitRoomCb(String str) {
		Log.d(TAG, "[exitRoomCb]str:"+str);
		if (loginJoinCallback == null) {
			Log.d(TAG, "loginJoinCallback null");
			return;
		}
		loginJoinCallback.exitRoomCb(str);
	}


	public void otherJoinRoomCb(String str) {
        Log.d(TAG, "[otherJoinRoomCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.otherJoinRoomCb(str);
	}
	
	public void otherExitRoomCb(String str) {
		Log.d(TAG, "[otherExitRoomCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
		callback.otherExitRoomCb(str);
	}
	
	public void prepareCb(String str) {
        Log.d(TAG, "[prepareCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.prepareCb(str);
	}
	
	public void otherUserPrepareCb(String str) {
        Log.d(TAG, "[otherUserPrepareCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.otherUserPrepareCb(str);
	}
	
	public void willBankerCb(String str) {
        Log.d(TAG, "[willBankerCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.willBankerCb(str);
	}
	
	public void tryBankerCb(String str) {
        Log.d(TAG, "[tryBankerCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.tryBankerCb(str);
	}
	
	public void willStakeCb(String str) {
        Log.d(TAG, "[willStakeCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.willStakeCb(str);
	}
	
	public void stakeCb(String str) {
        Log.d(TAG, "[stakeCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.stakeCb(str);
	}
	
	public void otherUserStakeValueCb(String str) {
        Log.d(TAG, "[otherUserStakeValueCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.otherUserStakeValueCb(str);
	}
	
	public void willSubmitCb(String str) {
        Log.d(TAG, "[willSubmitCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.willSubmitCb(str);
	}
	
	public void submitCb(String str) {
        Log.d(TAG, "[playCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.submitCb(str);
	}
	
	public void otherUserCardPatternCb(String str) {
        Log.d(TAG, "[otherUserCardPatternCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.otherUserCardPatternCb(str);
	}
	
	public void gameResultCb(String str) {
        Log.d(TAG, "[gameResultCb]str:"+str);
		if (callback == null) {
			Log.d(TAG, "callback null");
			return;
		}
        callback.gameResultCb(str);
	}
}
