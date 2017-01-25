package com.game.douniu.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefHelper {
	private static final String TAG = "[wzj]SharedPrefHelper";
	
	private String PREFS_NAME = "mysharepref";
	private SharedPreferences mySharePref;
	private Editor editor;
	
	private static SharedPrefHelper s_sharePrefHelper = null;
	
	public static synchronized SharedPrefHelper getInstance(Context context) {
		if (s_sharePrefHelper == null) {
				s_sharePrefHelper = new SharedPrefHelper(context);
		}
		return s_sharePrefHelper;
	}
	
	private SharedPrefHelper(Context context) {
		mySharePref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = mySharePref.edit();
	}

	public boolean putBoolean(String key, boolean value) {
		try {
			editor.putBoolean(key, value);
			editor.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean getBoolean(String key, boolean value) {
		return mySharePref.getBoolean(key, value);
	}
	
	public boolean putFloat(String key, float value) {
		try {
			editor.putFloat(key, value);
			editor.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public float getFloat(String key, float value) {
		return mySharePref.getFloat(key, 0f);
	}

	public boolean putInt(String key, int value) {
		try {
			editor.putInt(key, value);
			editor.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int getInt(String key, int value) {
		return mySharePref.getInt(key, 0);
	}

	public boolean putLong(String key, Long value) {
		try {
			editor.putFloat(key, value);
			editor.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public long getLong(String key, Long value) {
		return mySharePref.getLong(key, value);
	}

	public boolean putString(String key, String value) {
		try {
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getString(String key, String value) {
		return mySharePref.getString(key, value);
	}
	
	public void removeShare(String key) {
		editor.remove(key);
		editor.commit();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
