package com.game.douniu;


import java.util.List;

import com.game.douniu.custom.GameLogic;
import com.game.douniu.custom.IDouNiuListener;
import com.game.douniu.custom.Player;
import com.game.douniu.jni.DouniuClientInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

public class OfflinePlayActivity extends Activity implements OnClickListener, IDouNiuListener {
	private static String TAG = "[wzj]OfflinePlayActivity";
	
	private Button m_btStart;
	private GameLogic m_gameLogic;
	private List<Player> m_players;
	
	private TextView m_tvPlayer1Card1;
	private TextView m_tvPlayer1Card2;
	private TextView m_tvPlayer1Card3;
	private TextView m_tvPlayer1Card4;
	private TextView m_tvPlayer1Card5;
	private TextView m_tvPlayer2Card1;
	private TextView m_tvPlayer2Card2;
	private TextView m_tvPlayer2Card3;
	private TextView m_tvPlayer2Card4;
	private TextView m_tvPlayer2Card5;
	private TextView m_tvPlayer3Card1;
	private TextView m_tvPlayer3Card2;
	private TextView m_tvPlayer3Card3;
	private TextView m_tvPlayer3Card4;
	private TextView m_tvPlayer3Card5;
	
	private TextView m_tvPlayer1Result;
	private TextView m_tvPlayer2Result;
	private TextView m_tvPlayer3Result;
	
	private ImageView m_ivPlayer1Image;
	private ImageView m_ivPlayer2Image;
	private ImageView m_ivPlayer3Image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.offline_play_activity);
		
		m_btStart = (Button)findViewById(R.id.start_image);
		m_btStart.setOnClickListener(this);
		m_btStart.setOnTouchListener(new BtBackGround());
		
		m_tvPlayer1Card1 = (TextView)findViewById(R.id.player1_card1);
		m_tvPlayer1Card2 = (TextView)findViewById(R.id.player1_card2);
		m_tvPlayer1Card3 = (TextView)findViewById(R.id.player1_card3);
		m_tvPlayer1Card4 = (TextView)findViewById(R.id.player1_card4);
		m_tvPlayer1Card5 = (TextView)findViewById(R.id.player1_card5);
		m_tvPlayer2Card1 = (TextView)findViewById(R.id.player2_card1);
		m_tvPlayer2Card2 = (TextView)findViewById(R.id.player2_card2);
		m_tvPlayer2Card3 = (TextView)findViewById(R.id.player2_card3);
		m_tvPlayer2Card4 = (TextView)findViewById(R.id.player2_card4);
		m_tvPlayer2Card5 = (TextView)findViewById(R.id.player2_card5);
		m_tvPlayer3Card1 = (TextView)findViewById(R.id.player3_card1);
		m_tvPlayer3Card2 = (TextView)findViewById(R.id.player3_card2);
		m_tvPlayer3Card3 = (TextView)findViewById(R.id.player3_card3);
		m_tvPlayer3Card4 = (TextView)findViewById(R.id.player3_card4);
		m_tvPlayer3Card5 = (TextView)findViewById(R.id.player3_card5);
		
		m_tvPlayer1Result = (TextView)findViewById(R.id.player1_result);
		m_tvPlayer2Result = (TextView)findViewById(R.id.player2_result);
		m_tvPlayer3Result = (TextView)findViewById(R.id.player3_result);
		
		m_ivPlayer1Image = (ImageView)findViewById(R.id.player1_image);
		m_ivPlayer2Image = (ImageView)findViewById(R.id.player2_image);
		m_ivPlayer3Image = (ImageView)findViewById(R.id.player3_image);
		m_ivPlayer1Image.setOnClickListener(this);
		m_ivPlayer2Image.setOnClickListener(this);
		m_ivPlayer3Image.setOnClickListener(this);
		
		m_gameLogic = new GameLogic(this, this);
		m_gameLogic.initialize(3);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_image:
			Log.v(TAG, "start_image");
			onGameStart();
			break;
		case R.id.player1_image:
			showPlayerInfo(m_players.get(0));
			break;
		case R.id.player2_image:
			showPlayerInfo(m_players.get(1));
			break;
		case R.id.player3_image:
			showPlayerInfo(m_players.get(2));
			break;
		default:
			break;
		}
	}
	
	private void onGameStart() {
		Log.v(TAG, "[onGameStart]start");
		//m_ivStart.setVisibility(View.INVISIBLE);
		initPlayersView();
		m_gameLogic.resetGame();
		updatePlayersView();
		m_gameLogic.caculateResult();
	}
	
	private void initPlayersView() {
		m_tvPlayer1Result.setVisibility(View.INVISIBLE);
		m_tvPlayer2Result.setVisibility(View.INVISIBLE);
		m_tvPlayer3Result.setVisibility(View.INVISIBLE);
	}
	
	private void updatePlayersView() {
		Log.v(TAG, "[updatePlayersView]start");
		if (m_players == null) {
			Log.v(TAG, "[updatePlayersView]m_players null");
			return;
		}
		m_tvPlayer1Card1.setText(m_players.get(0).getCards().get(0).getCardFace());
		m_tvPlayer1Card2.setText(m_players.get(0).getCards().get(1).getCardFace());
		m_tvPlayer1Card3.setText(m_players.get(0).getCards().get(2).getCardFace());
		m_tvPlayer1Card4.setText(m_players.get(0).getCards().get(3).getCardFace());
		m_tvPlayer1Card5.setText(m_players.get(0).getCards().get(4).getCardFace());
		
		m_tvPlayer2Card1.setText(m_players.get(1).getCards().get(0).getCardFace());
		m_tvPlayer2Card2.setText(m_players.get(1).getCards().get(1).getCardFace());
		m_tvPlayer2Card3.setText(m_players.get(1).getCards().get(2).getCardFace());
		m_tvPlayer2Card4.setText(m_players.get(1).getCards().get(3).getCardFace());
		m_tvPlayer2Card5.setText(m_players.get(1).getCards().get(4).getCardFace());
		
		m_tvPlayer3Card1.setText(m_players.get(2).getCards().get(0).getCardFace());
		m_tvPlayer3Card2.setText(m_players.get(2).getCards().get(1).getCardFace());
		m_tvPlayer3Card3.setText(m_players.get(2).getCards().get(2).getCardFace());
		m_tvPlayer3Card4.setText(m_players.get(2).getCards().get(3).getCardFace());
		m_tvPlayer3Card5.setText(m_players.get(2).getCards().get(4).getCardFace());
	}

	private void showPlayerInfo(Player player) {
		Log.v(TAG, "[showPlayerInfo]start");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View playQueueLayout = layoutInflater.inflate(R.layout.player_info_layout, (ViewGroup)findViewById(R.id.player_info_layout));
		TextView tvUsername = (TextView)playQueueLayout.findViewById(R.id.player_username);
		TextView tvMoney = (TextView)playQueueLayout.findViewById(R.id.player_money);
		tvUsername.setText(player.getUsername());
		tvMoney.setText(""+player.getMoney());
		
		dialog.setView(playQueueLayout);
		dialog.show();
	}
	

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
		if (m_players == null) {
			Log.v(TAG, "[OnEventCalculatedEnd]m_players null");
			return false;
		}
		m_tvPlayer1Result.setVisibility(View.VISIBLE);
		m_tvPlayer1Result.setText(m_players.get(0).getResultStr());
		m_tvPlayer2Result.setVisibility(View.VISIBLE);
		m_tvPlayer2Result.setText(m_players.get(1).getResultStr());
		m_tvPlayer3Result.setVisibility(View.VISIBLE);
		m_tvPlayer3Result.setText(m_players.get(2).getResultStr());
		return true;
	}

	@Override
	public boolean OnEventcheckoutEnd() {
		Log.v(TAG, "[OnEventcheckoutEnd]start");
		return true;
	}

	
}
