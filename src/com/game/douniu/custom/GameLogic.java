package com.game.douniu.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.location.GpsStatus.Listener;
import android.util.Log;

public class GameLogic {
	private static String TAG = "[wzj]GameLogic";
	
	private Context context;
	private IDouNiuListener douNiuListener;

	private static int COUNT_PLAYERS = 3;
	private static int COUNT_XIPAI = 1000;//20;//
	private static int COUNT_CARD_EACH_PLAYER = 5;
	
	private List<Player> players;
	private List<Card> cards;
	
	private int indexBanker;	//搴勫搴忓彿
	
	
	public GameLogic(Context context, IDouNiuListener listener) {
		this.context = context;
		this.douNiuListener = listener;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void resetGame() {
		Log.v(TAG, "[resetGame]start");
		//initialize(COUNT_PLAYERS);
		tryingBeBanker();
		betStake();
		initializePai();
		xiPai();
		faPai();
		caculateResult();
		checkoutStake();
	}
	
	public void initialize(int num) {
		Log.v(TAG, "[initialize]num:"+num);
		if (players == null) {
			players = new ArrayList<Player>();
		}
		
		for (int i = 0; i < players.size(); i++) {
			players.get(i).reset();
		}
		
		while (players.size() < num) {
			Player player = new Player(context, "xiao"+players.size());
			players.add(player);
		}
		douNiuListener.OnEventInitializeEnd();
	}
	
	/**
	 * @param str format such as: 0#wu#10000#1#zhou#10000
	 * @param useridSelf userid of myself
	 */
	public void initialize(String str, int useridSelf) {
		Log.v(TAG, "[initialize]str:"+str+",useridSelf:"+useridSelf);
		if (players == null) {
			players = new ArrayList<Player>();
		} else {
			players.clear();
		}
		
		String usersInfo[] = str.split("#");
		int num = 0;
		for (int i=0;i<usersInfo.length;) {
			int userid = Integer.parseInt(usersInfo[i]);
			long money = Long.parseLong(usersInfo[i+2]);
			Player player = new Player(context, userid, usersInfo[i+1], money);
			Log.v(TAG, "[initialize]i:"+i+",player:"+player);
			if (userid == useridSelf) {
				Log.v(TAG, "[initialize]userid is self");
				players.add(0, player);
			} else {
				Log.v(TAG, "[initialize]userid is not self");
				players.add(player);
			}

			num++;
			i = i + 3;
		}
		douNiuListener.OnEventInitializeEnd();
	}
	
	/**
	 * @param str such as: 1#zhou#10000
	 */
	public void addPlayer(String str) {
		Log.v(TAG, "[addPlayer]str:"+str);
		String usersInfo[] = str.split("#");
		if (usersInfo.length >= 3) {
			int userid = Integer.parseInt(usersInfo[0]);
			long money = Long.parseLong(usersInfo[2]);
			Player player = new Player(context, userid, usersInfo[1], money);
			Log.v(TAG, "[addPlayer]old size:"+players.size());
			players.add(player);
		}
		Log.v(TAG, "[addPlayer]end size:"+players.size());
	}
	
	public void tryingBeBanker() {
		Log.v(TAG, "[tryingBeBanker]start");
		//TODO
		indexBanker = 0;
		douNiuListener.OnEventTryingBankerEnd();
	}
	
	public void betStake() {
		
	}
	
	public void pushCardsForPlayer(int i, int card1, int card2, int card3, int card4, int card5) {
		Card card = CardUtil.upateCardById(card1);
		players.get(i).pushCard(card);
		card = CardUtil.upateCardById(card2);
		players.get(i).pushCard(card);
		card = CardUtil.upateCardById(card3);
		players.get(i).pushCard(card);
		card = CardUtil.upateCardById(card4);
		players.get(i).pushCard(card);
		card = CardUtil.upateCardById(card5);
		players.get(i).pushCard(card);
	}
	
	//only for offline
	public void initializePai() {
		Log.v(TAG, "[initializePai]start");
		if (cards != null) {
			cards.clear();
		}
		cards = CardUtil.getCards();
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[initializePai]i:"+cards.get(i));
		}
	}
	
	//only for offline
	public void xiPai() {
		Log.v(TAG, "[xiPai]start");
		if (cards.size() <= 0) {
			Log.v(TAG, "[xiPai]cards null");
			return;
		}
		
		Random rand = new Random(System.currentTimeMillis());
		Card temp = new Card();
		for (int i=0;i<COUNT_XIPAI;i++) {
			int pos1 = rand.nextInt(CardUtil.COUNT_CARDS);
			int pos2 = rand.nextInt(CardUtil.COUNT_CARDS);
			temp = cards.get(pos1);
			cards.set(pos1, cards.get(pos2));
			cards.set(pos2, temp);
		}
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[xiPai]i:"+cards.get(i));
		}
	}
	
	//only for offline
	public void faPai() {
		Log.v(TAG, "[faPai]start");
		if (COUNT_PLAYERS * COUNT_CARD_EACH_PLAYER > CardUtil.COUNT_CARDS) {
			Log.v(TAG, "[faPai]player is too much, cards is not empty");
			return;
		}
		int index = 0;
		for (int i=0;i<COUNT_PLAYERS;i++) {
			for(int j=0;j<COUNT_CARD_EACH_PLAYER;j++){
				players.get(i).pushCard(cards.get(index));
				index++;
			}
		}
		
		for (int i=0;i<COUNT_PLAYERS;i++) {
			Log.v(TAG, "[faPai]player i:"+i+","+players.get(i));
		}
		douNiuListener.OnEventFaPaiEnd();
	}
	
	public void caculateResult() {
		Log.v(TAG, "[caculateResult]caculateResult");
		for (int i=0;i<COUNT_PLAYERS;i++) {
			for(int j=0;j<COUNT_CARD_EACH_PLAYER;j++){
				players.get(i).calculateResult();
			}
		}
		douNiuListener.OnEventCalculatedEnd();
	}
	
	//缁撴竻璧屾敞
	public void checkoutStake() {
		Log.v(TAG, "[checkoutStake]start");
		for (int i=0;i<players.size();i++) {
			if (i == indexBanker) {
				Log.v(TAG, "[checkoutStake]self, skip");
				continue;
			}
			checkout(players.get(indexBanker), players.get(i));
		}
		douNiuListener.OnEventcheckoutEnd();
	}
	
	public int getMultiple(int pattern) {
		int multiple = 1;
		if (pattern == DouNiuRule.POKER_PATTERN_ZHA_DAN ||pattern == DouNiuRule.POKER_PATTERN_FIVE_HUA) {
			multiple = 5;
		} else if (pattern == DouNiuRule.POKER_PATTERN_FOUR_HUA) {
			multiple = 4;
		} else if (pattern == DouNiuRule.POKER_PATTERN_NIU_NIU) {
			multiple = 3;
		} else if (pattern <= DouNiuRule.POKER_PATTERN_NIU_9 && pattern >= DouNiuRule.POKER_PATTERN_NIU_7) {
			multiple = 2;
		}
		
		return multiple;
	}
	
	//搴勫鍜岄棽瀹剁粨娓呰祵娉�	
	public void checkout(Player banker, Player player) {
		Log.v(TAG, "[checkout]banker:"+banker+",player:"+player);
		int bankerPattern = banker.getPokerPattern();
		int playerPattern = player.getPokerPattern();
		int multiple = 1;
		if (bankerPattern > playerPattern) {
			Log.v(TAG, "[checkout]banker win as pattern");
			multiple = getMultiple(bankerPattern);
			banker.setMoney(banker.getMoney() + player.getStake() * multiple);
			player.setMoney(player.getMoney() - player.getStake() * multiple);
			Log.v(TAG, "[checkout]11 banker money:"+banker.getMoney());
		} else if (bankerPattern < playerPattern) {
			Log.v(TAG, "[checkout]player win as pattern");
			multiple = getMultiple(playerPattern);
			banker.setMoney(banker.getMoney() - player.getStake() * multiple);
			player.setMoney(player.getMoney() + player.getStake() * multiple);
			Log.v(TAG, "[checkout]22 banker money:"+banker.getMoney());
		} else {//鐩稿悓鐗屽瀷
			int bankerMaxCard = banker.getMaxCardValue();
			int playerMaxCard = player.getMaxCardValue();
			if (bankerMaxCard > playerMaxCard) {
				Log.v(TAG, "[checkout]banker win as maxCard");
				multiple = getMultiple(bankerPattern);
				banker.setMoney(banker.getMoney() + player.getStake() * multiple);
				player.setMoney(player.getMoney() - player.getStake() * multiple);
				Log.v(TAG, "[checkout]55 banker money:"+banker.getMoney());
			} else if (bankerMaxCard < playerMaxCard) {
				Log.v(TAG, "[checkout]player win as maxCard");
				multiple = getMultiple(playerPattern);
				banker.setMoney(banker.getMoney() - player.getStake() * multiple);
				player.setMoney(player.getMoney() + player.getStake() * multiple);
				Log.v(TAG, "[checkout]66 banker money:"+banker.getMoney());
			} else {
				Log.v(TAG, "[checkout]pattern, points and maxCard is equal");
				Log.v(TAG, "[checkout]66 banker money:"+banker.getMoney());
			}
		}
	}
	
}
