package com.game.douniu;


import java.util.List;

import com.game.douniu.custom.GameLogic;
import com.game.douniu.custom.IDouNiuListener;
import com.game.douniu.custom.Player;
import com.game.douniu.jni.DouniuClient;
import com.game.douniu.utils.BtBackGround;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
	
	private DouniuClient m_douniuClient;
	
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
		
		m_douniuClient = new DouniuClient();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		String username = usernameEt.getText().toString();
		String password = passwordEt.getText().toString();
		String ipaddr = ipAddrEt.getText().toString();
		switch (v.getId()) {
		case R.id.account_login_btn:
			Log.v(TAG, "account_login_btn");
			int ret = m_douniuClient.connectAndLogin(ipaddr, username, password);
			if (ret == 0) {
				Log.w(TAG, "connectAndLogin ERR.");
				Toast.makeText(this, "connectAndLogin ERR", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.putExtra("ipaddr", ipaddr);
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			startActivity(intent);
			break;
		case R.id.visitor_login_btn:
			Log.v(TAG, "account_login_btn");
			int ret2 = m_douniuClient.connectAndLogin(ipaddr, username, password);
			if (ret2 == 0) {
				Log.w(TAG, "connectAndLogin ERR.");
				Toast.makeText(this, "connectAndLogin ERR", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.putExtra("ipaddr", ipAddrEt.getText().toString());
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
