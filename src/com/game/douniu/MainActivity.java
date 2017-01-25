package com.game.douniu;


import java.util.List;

import com.game.douniu.custom.CardRes;
import com.game.douniu.custom.CardsView;
import com.game.douniu.custom.GameLogic;
import com.game.douniu.custom.GameView;
import com.game.douniu.custom.IDouNiuListener;
import com.game.douniu.custom.IDouniuCallback;
import com.game.douniu.custom.Player;
import com.game.douniu.jni.DouniuClientInterface;
import com.game.douniu.utils.AudioPlayUtils;
import com.game.douniu.utils.BtBackGround;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author justin.wu
 *
 */
public class MainActivity extends Activity implements OnClickListener {
	private static String TAG = "[wzj]MainActivity";
	
	public	static MainActivity	instance;
	
	private GameLogic m_gameLogic;
	public List<Player> m_players;
	
	private DouniuClientInterface m_douniuClient;
	private int userid;
	private int bankerIndex;
	
	private String ipaddr;
	private String username;
	private String password;
	private String loginRet;
	
	private Button level1Btn;
	private Button level2Btn;
	private Button level3Btn;
	
	private ImageView settingsIv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		Log.v(TAG, "onCreate");
		instance = this;
		
		Intent intent = getIntent();
		ipaddr = intent.getStringExtra("ipaddr");
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		loginRet = intent.getStringExtra("loginRet");
		
		level1Btn = (Button)findViewById(R.id.level1_image);
		level1Btn.setOnClickListener(this);
		level2Btn = (Button)findViewById(R.id.level2_image);
		level2Btn.setOnClickListener(this);
		level3Btn = (Button)findViewById(R.id.level3_image);
		level3Btn.setOnClickListener(this);
		settingsIv = (ImageView)findViewById(R.id.setting_image);
		settingsIv.setOnClickListener(this);

		m_douniuClient = new DouniuClientInterface();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "[onResume]");
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "[onDestroy]");
		instance = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.level1_image:
			Log.v(TAG, "level1_image");
			Intent intent2 = new Intent(MainActivity.this, OnlinePlayActivity.class);
			intent2.putExtra("ipaddr", ipaddr);
			intent2.putExtra("username", username);
			intent2.putExtra("password", password);
			intent2.putExtra("loginRet", loginRet);
			Log.v(TAG, "->startActivity OnlinePlayActivity");
			startActivity(intent2);
			break;
		case R.id.level2_image:
			Log.v(TAG, "level2_image");
			break;
		case R.id.level3_image:
			Log.v(TAG, "level3_image");
			break;
		case R.id.setting_image:
			Log.v(TAG, "setting_image");
			break;
		default:
			break;
		}
	}
	
	public static MainActivity getInstance() {
		return instance;
	}

}
