package com.game.douniu;


import com.game.douniu.custom.DouNiuRule;
import com.game.douniu.jni.DouniuClientInterface;
import com.game.douniu.utils.AudioPlayUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private static String TAG = "[wzj]LoginActivity";
	
	private EditText usernameEt;
	private EditText passwordEt;
	private EditText ipAddrEt;
	private Button accountLoginBtn;
	private Button visitorLoginBtn;
	
	private String username;
	
	private DouniuClientInterface m_douniuClient;
	private static AudioPlayUtils audioPlayUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		
		usernameEt = (EditText)findViewById(R.id.username_et);
		passwordEt = (EditText)findViewById(R.id.password_et);
		ipAddrEt = (EditText)findViewById(R.id.network_et);
		accountLoginBtn = (Button)findViewById(R.id.account_login_btn);
		accountLoginBtn.setOnClickListener(this);
		visitorLoginBtn = (Button)findViewById(R.id.visitor_login_btn);
		visitorLoginBtn.setOnClickListener(this);
		
		//String resultStr = DouNiuRule.getResultStr(this, 7);//String resultStr = DouNiuRule.getResultStr(mContext, MainActivity.getInstance().m_players.get(0).getPokerPattern());
		//Log.d(TAG, "[onCreate]++++++++resultStr:"+resultStr);
		
		m_douniuClient = DouniuClientInterface.getInstance();//new DouniuClientInterface();
		audioPlayUtils = AudioPlayUtils.getInstance(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		username = usernameEt.getText().toString();
		String password = passwordEt.getText().toString();
		String ipaddr = ipAddrEt.getText().toString();
		switch (v.getId()) {
		case R.id.account_login_btn:
			Log.v(TAG, "account_login_btn");
			AudioPlayUtils.getInstance(this).play(R.raw.click);
			String loginRet = m_douniuClient.connectAndLogin(ipaddr, username, password);
			Log.v(TAG, "connectAndLogin loginRet:"+loginRet);
			if (loginRet.isEmpty() || loginRet.length() == 0) {
				Log.w(TAG, "connectAndLogin ERR.");
				Toast.makeText(this, "connectAndLogin ERR", Toast.LENGTH_SHORT).show();
				return;
			} else if (loginRet.equals("NE")) {
				Log.w(TAG, "connectAndLogin ACCOUNT NOT EXIST.");
				Toast.makeText(this, "ACCOUNT NOT EXIST", Toast.LENGTH_SHORT).show();
				return;
			}
			Log.v(TAG, "connectAndLogin end");
			intent = new Intent(LoginActivity.this, MainActivity.class);//intent = new Intent(LoginActivity.this, OnlinePlayActivity.class);
			intent.putExtra("ipaddr", ipaddr);
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			intent.putExtra("loginRet", loginRet);
			Log.v(TAG, "->startActivity MainActivity");
			startActivity(intent);
			break;
		case R.id.visitor_login_btn:
			Log.v(TAG, "visitor_login_btn");
			AudioPlayUtils.getInstance(this).play(R.raw.click);
			intent = new Intent(LoginActivity.this, OfflinePlayActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		m_douniuClient.logoutAndExit(username);
		super.onDestroy();
	}

	
	
}
