package com.game.douniu;


import java.util.List;

import com.game.douniu.custom.CardRes;
import com.game.douniu.custom.CardsView;
import com.game.douniu.custom.Constant;
import com.game.douniu.custom.GameLogic;
import com.game.douniu.custom.GameView;
import com.game.douniu.custom.IDouNiuListener;
import com.game.douniu.custom.IDouniuCallback;
import com.game.douniu.custom.Player;
import com.game.douniu.jni.DouniuClientInterface;
import com.game.douniu.utils.AudioPlayUtils;
import com.game.douniu.utils.BtBackGround;
import com.game.douniu.utils.UserClock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

/**
 * @author justin.wu
 *
 */
public class OnlinePlayActivity extends Activity implements OnClickListener, IDouNiuListener, IDouniuCallback {
	private static String TAG = "[wzj]OnlinePlayActivity";
	
	public final static int		IDI_WILL_BANKER				= 1;
	public final static int		IDI_WILL_STAKE				= 2;
	public final static int		IDI_WILL_SUBMIT				= 3;

	public final static int		TIME_WILL_BANKER			= 30;
	public final static int		TIME_WILL_STAKE				= 30;
	public final static int		TIME_WILL_SUBMIT			= 30;
	
	public	static OnlinePlayActivity	instance;
	
	private GameLogic m_gameLogic;
	public List<Player> m_players;
	
	private DouniuClientInterface m_douniuClient;
	private int userid;
	private int bankerIndex;
	
	private GameView gameView;
	public UserClock m_ClockControl;
	
	public Handler m_ClockHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "[m_ClockHandler]msg:"+msg);
			if (msg.what < 100) {
				onEventUserClockInfo(msg.what, msg.arg1, msg.arg2);
			}
			/*else
				GameClientActivity.this.onUIHandlerMsg(msg);*/
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Resources res = getResources();
		if (CardRes.getInstance() != null) {
			CardRes.getInstance().onLoadImage(res);
		}
		Log.v(TAG, "onCreate");
		instance = this;
		Intent intent = getIntent();
		String ipaddr = intent.getStringExtra("ipaddr");
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");
		String loginRet = intent.getStringExtra("loginRet");
		Log.v(TAG, "ipaddr:"+ipaddr+",username:"+username+",password:"+password+",loginRet:"+loginRet);
		String splitdata[] = loginRet.split(":");
		if (splitdata.length < 2) {
			Log.v(TAG, "loginRet is not valid as content not enough");
		} else {
			userid = Integer.parseInt(splitdata[0]);
			m_gameLogic = new GameLogic(this, this);
			m_gameLogic.initialize(splitdata[1], userid);
		}
		
		gameView = new GameView(this);
		setContentView(gameView);
		//setContentView(R.layout.activity_main);

		m_douniuClient = new DouniuClientInterface();
		m_douniuClient.setDouniuCallback(this);
		
		m_ClockControl = new UserClock(m_ClockHandler);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "[onResume]");
		super.onResume();
		Resources res = getResources();
		if (CardRes.getInstance() != null) {
			CardRes.getInstance().onLoadImage(res);
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "[onDestroy]");
		instance = null;
		if (CardRes.getInstance() != null) {
			CardRes.getInstance().onDestroy();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}
	
	public static OnlinePlayActivity getInstance() {
		return instance;
	}

	//真实的座位
	private int getMeChairID()
	{
		return 0;//TODO
	}

	private void onEventUserClockInfo(int clockid, int chairid, int time) {
		Log.d(TAG, "[onEventUserClockInfo]clockid:"+clockid+",chairid:"+chairid+",time:"+time);
		if (gameView != null) {
			gameView.onUpdateClock(0);
		}
		switch (clockid) {
			case IDI_WILL_BANKER:
				if (chairid == getMeChairID()) {
					if (time == 0) {
						tryingBankerAction(true);
					}
					if (time > 0 && time < 5) {
						AudioPlayUtils.getInstance(this).play(R.raw.game_warn);
					}
				}
				break;
			case IDI_WILL_STAKE:
				if (chairid == getMeChairID()) {
					if (time == 0) {
						stakeAction(Constant.VALUE_STAKE_LEVEL_1);
					}
					if (time > 0 && time < 5) {
						AudioPlayUtils.getInstance(this).play(R.raw.game_warn);
					}
				}
				break;
			case IDI_WILL_SUBMIT:
				if (chairid == getMeChairID()) {
					if (time == 0) {
						startAction(false);
					}
					if (time > 0 && time < 5) {
						AudioPlayUtils.getInstance(this).play(R.raw.game_warn);
					}
				}
				break;
		}
	}
	
	/**
	 * 来自GameView的onClick事件
	 */
	@Override
	public boolean OnEventInitializeEnd() {
		Log.v(TAG, "[OnEventInitializeEnd]start");
		m_players = m_gameLogic.getPlayers();
		return true;
	}
	
	@Override
	public boolean OnEventTryingBankerEnd() {
		Log.v(TAG, "[OnEventTryingBankerEnd]start");
		return true;
	}

	@Override
	public boolean OnEventFaPaiEnd() {
		Log.v(TAG, "[OnEventFaPaiEnd]start");
		return true;
	}

	@Override
	public boolean OnEventCalculatedEnd() {
		Log.v(TAG, "[OnEventCalculatedEnd]start");
		return true;
	}

	
	@Override
	public boolean OnEventcheckoutEnd() {
		Log.v(TAG, "[OnEventcheckoutEnd]start");
		return true;
	}
	/**
	 * 
	 */
	
	
	/**
	 * 来自GameView的onClick事件
	 */
	public void prepareAction() {
		Log.v(TAG, "[prepareAction]start");
		m_douniuClient.prepareCMD();
	}
	
	public void tryingBankerAction(boolean isTrying) {
		Log.v(TAG, "[tryingBankerAction]isTrying:"+isTrying);
		int value = (isTrying == true)?1:0;
		m_douniuClient.tryingBankerCMD(value);
	}
	
	public void stakeAction(int stake) {
		Log.v(TAG, "[stakeAction]stake:"+stake);
		m_douniuClient.stakeCMD(stake);
	}
	
	public void startAction(boolean hasNiu) {
		Log.v(TAG, "[playAction]hasNiu:"+hasNiu);
		int value = (hasNiu == true)?1:0;
		m_douniuClient.playCMD(value);
	}
	/**
	 * 
	 */
	
	
	/**
	 * JNI回调函数
	 */
	@Override
	public void loginCb(int userid, String str) {
		Log.v(TAG, "[loginCb]start");
		//TODO
	}

	@Override
	public void otherLoginCb(String str) {
		Log.v(TAG, "[otherLoginCb]str:"+str);
		m_gameLogic.addPlayer(str);
		Log.v(TAG, "[otherLoginCb]size:"+m_players.size());
		gameView.otherLoginCb(str);
	}

	@Override
	public void logoutCb(int value) {
		Log.v(TAG, "[logoutCb]start");
		//TODO
	}

	@Override
	public void otherLogoutCb(int value) {
		Log.v(TAG, "[otherLogoutCb]start");
		//TODO
	}

	@Override
	public void prepareCb(String str) {
		Log.v(TAG, "[prepareCb]str:"+str);
		if (m_players.size() >= 1) {
			m_players.get(0).setReady(true);
		}
		gameView.prepareCb(str);
	}

	@Override
	public void otherUserPrepareCb(String str) {
		Log.v(TAG, "[otherUserPrepareCb]str:"+str);
		int userid = Integer.parseInt(str);
		for (int i=0;i<m_players.size();i++) {
			if (m_players.get(i).getUserid() == userid) {
				m_players.get(i).setReady(true);
				break;
			}
		}
		gameView.otherUserPrepareCb(str);
	}

	@Override
	public void willBankerCb(String str) {
		Log.v(TAG, "[willBankerCb]str:"+str);
		AudioPlayUtils.getInstance(this).play(R.raw.game_start);
		m_ClockControl.setGameClock(getMeChairID(), IDI_WILL_BANKER, TIME_WILL_BANKER);
		gameView.willBankerCb(str);
	}

	@Override
	public void tryBankerCb(String str) {
		Log.v(TAG, "[tryBankerCb]start");
		m_ClockControl.killGameClock(IDI_WILL_BANKER);
		gameView.tryBankerCb(str);
	}

	
	/* 返回庄家的userid，并通知所有用户开始下注
	 * @see com.game.douniu.custom.IDouniuCallback#willStakeCb(java.lang.String)
	 */
	@Override
	public void willStakeCb(String str) {
		Log.v(TAG, "[willStakeCb]str:"+str);
		int userid = Integer.parseInt(str);
		for (int i=0;i<m_players.size();i++) {
			if (m_players.get(i).getUserid() == userid) {
				m_players.get(i).setBanker(true);
				bankerIndex = i;
				if (bankerIndex != 0) {
					m_ClockControl.setGameClock(getMeChairID(), IDI_WILL_STAKE, TIME_WILL_STAKE);
				}
				gameView.willStakeCb(str, bankerIndex);
				break;
			}
		}
	}

	/* 服务器反馈赌注
	 * str如5，赌注为5X
	 */
	@Override
	public void stakeCb(String str) {
		Log.v(TAG, "[stakeCb]start");
		int stake = Integer.parseInt(str);
		if (m_players.size() >= 1) {
			m_players.get(0).setIsStake(true);
			m_players.get(0).setStake(stake);
		}
		m_ClockControl.killGameClock(IDI_WILL_STAKE);
		gameView.stakeCb(str);
	}

	/* 服务器反馈某用户的赌注
	 * str如0#5，userid为0的赌注为5X
	 */
	@Override
	public void otherUserStakeValueCb(String str) {
		Log.v(TAG, "[otherUserStakeValueCb]start");
		String stakeInfo[] = str.split("#");
		if (stakeInfo.length >= 2) {
			int userid = Integer.parseInt(stakeInfo[0]);
			int stake = Integer.parseInt(stakeInfo[1]);
			for (int i=0;i<m_players.size();i++) {
				if (m_players.get(i).getUserid() == userid) {
					m_players.get(i).setIsStake(true);
					m_players.get(i).setStake(stake);
					break;
				}
			}
		}
		gameView.otherUserStakeValueCb(str);
	}
	
	/* 服务器反馈牌面
	 * str如0#wu#17#27#37#40#31，牌面为17#27#37#40#31
	 */
	@Override
	public void willStartCb(String str) {
		Log.v(TAG, "[willStartCb]str:"+str);
		String cardsInfo[] = str.split("#");
		if (cardsInfo.length >= 7) {
			Log.v(TAG, "[willStartCb]--->pushCardsForPlayer ["+Integer.parseInt(cardsInfo[2])+","+Integer.parseInt(cardsInfo[3])+","+
					Integer.parseInt(cardsInfo[4])+","+Integer.parseInt(cardsInfo[5])+","+ Integer.parseInt(cardsInfo[6])+"]");
			m_gameLogic.pushCardsForPlayer(0, Integer.parseInt(cardsInfo[2]), Integer.parseInt(cardsInfo[3]), 
					Integer.parseInt(cardsInfo[4]), Integer.parseInt(cardsInfo[5]), Integer.parseInt(cardsInfo[6]));
			m_ClockControl.setGameClock(getMeChairID(), IDI_WILL_SUBMIT, TIME_WILL_SUBMIT);
			gameView.willStartCb(str);
		} else {
			Log.v(TAG, "[willStartCb]cardsInfo is not valid");
		}
	}

	/* 服务器反馈牌型结果pokerPattern
	 * str如3
	 */
	@Override
	public void playCb(String str) {
		Log.v(TAG, "[playCb]str:"+str);
		int value = Integer.parseInt(str);
		if (m_players.size() >= 1) {
			m_players.get(0).setPokerPattern(value);
		}
		m_ClockControl.killGameClock(IDI_WILL_SUBMIT);
		gameView.playCb(str);
	}

	/* 服务器反馈某用户其牌型结果，及牌面
	 * str如1#3#18#19#15#13#26，userid为1的牌型为3，牌面为18#19#15#13#26
	 */
	@Override
	public void otherUserCardPatternCb(String str) {
		Log.v(TAG, "[otherUserCardPatternCb]str:"+str);
		String cardsInfo[] = str.split("#");
		if (cardsInfo.length >= 7) {
			int userid = Integer.parseInt(cardsInfo[0]);
			int value = Integer.parseInt(cardsInfo[1]);
			for (int i=0;i<m_players.size();i++) {
				if (m_players.get(i).getUserid() == userid) {
					m_players.get(i).setPokerPattern(value);
					Log.v(TAG, "[otherUserCardPatternCb]->pushCardsForPlayer "+i);
					m_gameLogic.pushCardsForPlayer(i, Integer.parseInt(cardsInfo[2]), Integer.parseInt(cardsInfo[3]), 
							Integer.parseInt(cardsInfo[4]), Integer.parseInt(cardsInfo[5]), Integer.parseInt(cardsInfo[6]));
					gameView.otherUserCardPatternCb(userid);
					break;
				}
			}
		} else {
			Log.v(TAG, "[otherUserCardPatternCb]cardsInfo is not valid");
		}
	}

	/* 服务器反馈结果
	 * str如0#10#10010#1#-10#9990#2#0#10000
	 */
	@Override
	public void gameResultCb(String str) {
		Log.v(TAG, "[gameResultCb]str:"+str);
		/*String resultsInfo[] = str.split("#");
		for (int i=0;i<resultsInfo.length;) {
			int userid = Integer.parseInt(resultsInfo[i]);
			int result = Integer.parseInt(resultsInfo[i+1]);
			long money = Long.parseLong(resultsInfo[i+2]);
			for (int j=0;j<m_players.size();j++) {
				if (m_players.get(i).getUserid() == userid) {
					m_players.get(i).setMoney(money);
					break;
				}
			}
			i = i + 3;
		}*/
		AudioPlayUtils.getInstance(this).play(R.raw.game_end);
		gameView.gameResultCb(str);
	}
	/**
	 * 
	 */
}
