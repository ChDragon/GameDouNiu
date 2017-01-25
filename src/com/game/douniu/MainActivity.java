package com.game.douniu;


import java.util.List;

import com.game.douniu.custom.CardRes;
import com.game.douniu.custom.CardsView;
import com.game.douniu.custom.GameLogic;
import com.game.douniu.custom.GameView;
import com.game.douniu.custom.IDouNiuListener;
import com.game.douniu.custom.IDouniuCallback;
import com.game.douniu.custom.ILoginJoinCallback;
import com.game.douniu.custom.Player;
import com.game.douniu.custom.SharedPrefHelper;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * @author justin.wu
 *
 */
public class MainActivity extends Activity implements OnClickListener, ILoginJoinCallback {
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
	
	private TextView usernameTv;
	private TextView moneyTv;
	private TextView levelTv;
	
	private Button level1Btn;
	private Button level2Btn;
	private Button level3Btn;
	private Button randonLevelBtn;
	
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
		
		usernameTv = (TextView)findViewById(R.id.player_username);
		usernameTv.setText(username);
		moneyTv = (TextView)findViewById(R.id.player_money);
		levelTv = (TextView)findViewById(R.id.player_level);
		
		level1Btn = (Button)findViewById(R.id.level1_image);
		level1Btn.setOnClickListener(this);
		level2Btn = (Button)findViewById(R.id.level2_image);
		level2Btn.setOnClickListener(this);
		level3Btn = (Button)findViewById(R.id.level3_image);
		level3Btn.setOnClickListener(this);
		randonLevelBtn = (Button)findViewById(R.id.random_level_image);
		randonLevelBtn.setOnClickListener(this);
		settingsIv = (ImageView)findViewById(R.id.setting_image);
		settingsIv.setOnClickListener(this);

		m_douniuClient = DouniuClientInterface.getInstance();//new DouniuClientInterface();
		m_douniuClient.setLoginJoinCallback(this);
		
		
		Log.v(TAG, "loginRet:"+loginRet);
		String splitdata[] = loginRet.split(":");
		if (splitdata.length < 2) {
			Log.v(TAG, "loginRet is not valid as content not enough");
		} else {
			userid = Integer.parseInt(splitdata[0]);
			String ret = splitdata[1];
			String splitdata2[] = ret.split("#");
			long money = Long.parseLong(splitdata2[1]);
			moneyTv.setText(""+money);
			int level = Integer.parseInt(splitdata2[2]);
			levelTv.setText(""+level);
		}
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
			{
				Log.v(TAG, "level1_image");
				//Intent intent2 = new Intent(MainActivity.this, OnlinePlayActivity.class);
				//intent2.putExtra("ipaddr", ipaddr);
				//intent2.putExtra("username", username);
				//intent2.putExtra("password", password);
				//intent2.putExtra("loginRet", loginRet);
				//Log.v(TAG, "[level1_image]->startActivity OnlinePlayActivity");
				//startActivity(intent2);
			}
			break;
		case R.id.level2_image:
			Log.v(TAG, "level2_image");
			break;
		case R.id.level3_image:
			Log.v(TAG, "level3_image");
			break;
		case R.id.random_level_image:
			Log.v(TAG, "random_level_image");
			{
				m_douniuClient.nativeJoinRoomCMD();
			}
			break;
		case R.id.setting_image:
			Log.v(TAG, "setting_image");
			showSettingsPage();
			break;
		default:
			break;
		}
	}
	
	public static MainActivity getInstance() {
		return instance;
	}
	
	public void showSettingsPage() {
		Log.v(TAG, "[showSettingsPage]start");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View settings_layout = layoutInflater.inflate(R.layout.settings_layout, (ViewGroup)findViewById(R.id.settings_layout));
		
		boolean isSound = SharedPrefHelper.getInstance(this).getBoolean("sound", true);
		final ToggleButton soundToggleButton1 = (ToggleButton)settings_layout.findViewById(R.id.sound_togglebtn);
		soundToggleButton1.setChecked(isSound);
		soundToggleButton1.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.v(TAG, "[soundToggleButton1]isChecked:"+isChecked);
				soundToggleButton1.setChecked(isChecked);
				SharedPrefHelper.getInstance(MainActivity.this).putBoolean("sound", isChecked);
			}
		});
		
		boolean isMusic = SharedPrefHelper.getInstance(this).getBoolean("music", true);
		final ToggleButton musicToggleButton2 = (ToggleButton)settings_layout.findViewById(R.id.music_togglebtn);
		musicToggleButton2.setChecked(isMusic);
		musicToggleButton2.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.v(TAG, "[musicToggleButton2]isChecked:"+isChecked);
				musicToggleButton2.setChecked(isChecked);
				SharedPrefHelper.getInstance(MainActivity.this).putBoolean("music", isChecked);
			}
		});
		dialog.setView(settings_layout);
		dialog.show();
	}

	/*@Override
	public void loginCb(int userid, String str) {
	}*/

	@Override
	public void logoutCb(int value) {
	}

	@Override
	public void joinRoomCb(String str) {
		Log.v(TAG, "[joinRoomCb]str:"+str);
		if (str == null || str.isEmpty()) {
			Log.v(TAG, "[joinRoomCb]str null or empty");
			Toast.makeText(this, "joinRoomCb ERR", Toast.LENGTH_SHORT).show();
			return;
		}
		if (str.equals("Fail")) {
			Toast.makeText(this, "Room full", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent2 = new Intent(MainActivity.this, OnlinePlayActivity.class);
		//intent2.putExtra("ipaddr", ipaddr);
		//intent2.putExtra("username", username);
		//intent2.putExtra("password", password);
		intent2.putExtra("joinRoomRet", str);
		intent2.putExtra("userid", userid);
		Log.v(TAG, "[joinRoomCb]->startActivity OnlinePlayActivity");
		startActivity(intent2);
	}

	@Override
	public void exitRoomCb(String str) {
		// TODO Auto-generated method stub
		
	}

}
