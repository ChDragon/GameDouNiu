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
public class MainActivity extends Activity implements OnClickListener, IDouNiuListener, IDouniuCallback {
	private static String TAG = "[wzj]MainActivity";
	
	public	static MainActivity	instance;
	
	private GameLogic m_gameLogic;
	public List<Player> m_players;
	
	private DouniuClientInterface m_douniuClient;
	private int userid;
	private int bankerIndex;
	
	private GameView gameView;
	
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
	
	public static MainActivity getInstance() {
		return instance;
	}

	
	/**
	 * ����GameView��onClick�¼�
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
	 * ����GameView��onClick�¼�
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
	 * JNI�ص�����
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
		gameView.willBankerCb(str);
	}

	@Override
	public void tryBankerCb(String str) {
		Log.v(TAG, "[tryBankerCb]start");
		gameView.tryBankerCb(str);
	}

	
	/* ����ׯ�ҵ�userid����֪ͨ�����û���ʼ��ע
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
				gameView.willStakeCb(str, bankerIndex);
				break;
			}
		}
	}

	/* ������������ע
	 * str��5����עΪ5X
	 */
	@Override
	public void stakeCb(String str) {
		Log.v(TAG, "[stakeCb]start");
		int stake = Integer.parseInt(str);
		if (m_players.size() >= 1) {
			m_players.get(0).setIsStake(true);
			m_players.get(0).setStake(stake);
		}
		gameView.stakeCb(str);
	}

	/* ����������ĳ�û��Ķ�ע
	 * str��0#5��useridΪ0�Ķ�עΪ5X
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
	
	/* ��������������
	 * str��0#wu#17#27#37#40#31������Ϊ17#27#37#40#31
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
			gameView.willStartCb(str);
		} else {
			Log.v(TAG, "[willStartCb]cardsInfo is not valid");
		}
	}

	/* �������������ͽ��pokerPattern
	 * str��3
	 */
	@Override
	public void playCb(String str) {
		Log.v(TAG, "[playCb]str:"+str);
		int value = Integer.parseInt(str);
		if (m_players.size() >= 1) {
			m_players.get(0).setPokerPattern(value);
		}
		gameView.playCb(str);
	}

	/* ����������ĳ�û������ͽ����������
	 * str��1#3#18#19#15#13#26��useridΪ1������Ϊ3������Ϊ18#19#15#13#26
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

	/* �������������
	 * str��0#10#10010#1#-10#9990#2#0#10000
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
		
		gameView.gameResultCb(str);
	}
	/**
	 * 
	 */
}
