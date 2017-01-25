package com.game.douniu.custom;

import java.util.ArrayList;
import java.util.List;

import com.game.douniu.R;

import android.content.Context;

public class Player {
	private Context context;
	
	private int userid;
	private String username;		//鐢ㄦ埛鍚�   
	private long money;				//鏈挶
    
    private List<Card> cards;		//鎷垮埌鐨�寮犵墝
    private int pokerPattern;		//鐗屽瀷锛屽鐐稿脊锛屼簲鑺憋紝鐗涚墰锛屾湁鐗涳紝鏃犵墰
    private int points;			//鐐规暟
    private int maxCardValue;		//濡傛灉鏄墰鐗涙垨鏈夌墰浣嗙偣鏁颁竴鏍峰ぇ锛屾瘮杈冩渶澶х殑閭ｅ紶鐗�   
    private String resultStr;		//鏂楃墰缁撴灉瀛楃涓�    
    private int stake;				//璧屾敞锛屽崟浣嶆槸STAKE_UNIT
    private boolean isBanker;		//鏄惁搴勫
    private boolean isReady;
    private boolean isStake;
 
	public Player(Context context) {
		this.context = context;
		this.userid = -1;
		this.username = "";
		this.money = 10000;
		initialize();
	}
    
	public Player(Context context, String username) {
		this.context = context;
		this.userid = -1;
		this.username = username;
		this.money = 10000;
		initialize();
	}
	
	public Player(Context context, int userid, String username, long money) {
		this.context = context;
		this.userid = userid;
		this.username = username;
		this.money = money;
		initialize();
	}
	
	private void initialize() {
		reset();
	}
	
	public void reset() {
		if (cards != null) {
			cards.clear();
		}
		cards = new ArrayList<Card>();
		pokerPattern = DouNiuRule.POKER_PATTERN_WU_NIU;
		points = 0;
		maxCardValue = 0;
		resultStr = context.getResources().getString(R.string.str_no_niu);
		stake = DouNiuRule.STAKE_UNIT;
		isBanker = false;
		isReady = false;
		isStake = false;
	}

	public int getUserid() {
		return userid;
	}
	
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public long getMoney() {
		return money;
	}
	
	public void setMoney(long money) {
		this.money = money;
	}

	public void setStake(int count) {
		stake = DouNiuRule.STAKE_UNIT * count;
	}
	
	public int getStake() {
		return stake;
	}
	
	public boolean isBanker() {
		return isBanker;
	}

	public void setBanker(boolean isBanker) {
		this.isBanker = isBanker;
	}
	
	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public boolean isStake() {
		return isStake;
	}

	public void setIsStake(boolean isStake) {
		this.isStake = isStake;
	}

	public void pushCard(Card card) {
		cards.add(card);
	}

	public List<Card> getCards() {
		return cards;
	}
	
	public void setPokerPattern(int pokerPattern) {
		this.pokerPattern = pokerPattern;
	}

	public int getPokerPattern() {
		return pokerPattern;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getMaxCardValue() {
		return maxCardValue;
	}
	
	public String getResultStr() {
		return resultStr;
	}
	
	// 
	public void calculateResult() {
		maxCardValue = DouNiuRule.getMaxCardValue(cards);
		if (DouNiuRule.checkZhaDan(cards) == true) {//鐐稿脊
			pokerPattern = DouNiuRule.POKER_PATTERN_ZHA_DAN;
		} else if (DouNiuRule.checkFiveHua(cards) == true) {//浜旇姳
			pokerPattern = DouNiuRule.POKER_PATTERN_FIVE_HUA;
		} else if (DouNiuRule.checkFourHua(cards) == true) {//鍥涜姳
			pokerPattern = DouNiuRule.POKER_PATTERN_FOUR_HUA;
		} else {//涓嶆槸鐗规畩鐗屽瀷
			points = DouNiuRule.calculatePoints(cards);
			switch(points)
			{
			case 0:
				pokerPattern = DouNiuRule.POKER_PATTERN_WU_NIU;
				break;
			case 1:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_1;
				break;
			case 2:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_2;
				break;
			case 3:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_3;
				break;
			case 4:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_4;
				break;
			case 5:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_5;
				break;
			case 6:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_6;
				break;
			case 7:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_7;
				break;
			case 8:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_8;
				break;
			case 9:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_9;
				break;
			case 10:
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_NIU;
				break;
			default:
				break;
			}
		}
		resultStr = DouNiuRule.getResultStr(context, pokerPattern);
	}
	

	
	@Override
	public String toString() {
		String str = "Player [userid="+userid+" username="+username+" ";
		for (int i=0;i<cards.size();i++) {
			str += cards.get(i).toString() +"; ";
		}
		str += " ]";
		return str;
	}
    
}
