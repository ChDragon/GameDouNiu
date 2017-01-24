package com.game.douniu.custom;

import java.util.ArrayList;
import java.util.List;

import com.game.douniu.R;

import android.content.Context;

public class Player {
	private Context context;
	
	private String username;		//鐢ㄦ埛鍚�   
	private int money;				//鏈挶
    
    private List<Card> cards;		//鎷垮埌鐨�寮犵墝
    private int pokerPattern;		//鐗屽瀷锛屽鐐稿脊锛屼簲鑺憋紝鐗涚墰锛屾湁鐗涳紝鏃犵墰
    private int points;			//鐐规暟
    private int maxCardValue;		//濡傛灉鏄墰鐗涙垨鏈夌墰浣嗙偣鏁颁竴鏍峰ぇ锛屾瘮杈冩渶澶х殑閭ｅ紶鐗�   
    private String resultStr;		//鏂楃墰缁撴灉瀛楃涓�    
    private boolean isBanker;		//鏄惁搴勫
    private int stake;				//璧屾敞锛屽崟浣嶆槸STAKE_UNIT
 
	public Player(Context context, String username) {
		this.context = context;
		this.username = username;
		this.money = 10000;
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
		isBanker = false;
		stake = DouNiuRule.STAKE_UNIT;
		resultStr = context.getResources().getString(R.string.str_no_niu);
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public void betStake(int count) {
		stake = DouNiuRule.STAKE_UNIT * count;
	}
	
	public int getStake() {
		return stake;
	}
	
	public void pushCard(Card card) {
		cards.add(card);
	}

	public List<Card> getCards() {
		return cards;
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
	
	private String getResultStr(int points) {
		String ret = "";
		switch (points) {
		case 1:
			ret = context.getResources().getString(R.string.str_niu_1);
			break;
		case 2:
			ret = context.getResources().getString(R.string.str_niu_2);
			break;
		case 3:
			ret = context.getResources().getString(R.string.str_niu_3);
			break;
		case 4:
			ret = context.getResources().getString(R.string.str_niu_4);
			break;
		case 5:
			ret = context.getResources().getString(R.string.str_niu_5);
			break;
		case 6:
			ret = context.getResources().getString(R.string.str_niu_6);
			break;
		case 7:
			ret = context.getResources().getString(R.string.str_niu_7);
			break;
		case 8:
			ret = context.getResources().getString(R.string.str_niu_8);
			break;
		case 9:
			ret = context.getResources().getString(R.string.str_niu_9);
			break;
		case 10:
			ret = context.getResources().getString(R.string.str_niu_niu);
			break;
		case 0:
		default:
			ret = context.getResources().getString(R.string.str_no_niu);
			break;
		}
		return ret;
	}
	
	// 
	public void caculateResult() {
		maxCardValue = DouNiuRule.getMaxCardValue(cards);
		if (DouNiuRule.checkZhaDan(cards) == true) {//鐐稿脊
			pokerPattern = DouNiuRule.POKER_PATTERN_ZHA_DAN;
			resultStr = context.getResources().getString(R.string.str_zha_dan);
		} else if (DouNiuRule.checkFiveHua(cards) == true) {//浜旇姳
			pokerPattern = DouNiuRule.POKER_PATTERN_FIVE_HUA;
			resultStr = context.getResources().getString(R.string.str_five_hua);
		} else if (DouNiuRule.checkFourHua(cards) == true) {//鍥涜姳
			pokerPattern = DouNiuRule.POKER_PATTERN_FOUR_HUA;
			resultStr = context.getResources().getString(R.string.str_four_hua);
		} else {//涓嶆槸鐗规畩鐗屽瀷
			points = DouNiuRule.caculatePoints(cards);
			resultStr = getResultStr(points);
			if (points == 10) {
				pokerPattern = DouNiuRule.POKER_PATTERN_NIU_NIU;//鐗涚墰
			} else if (points == 0) {
				pokerPattern = DouNiuRule.POKER_PATTERN_WU_NIU;//鏃犵墰
			} else {
				pokerPattern = DouNiuRule.POKER_PATTERN_YOU_NIU;//鏈夌墰
			}
		}
	}
	

	
	@Override
	public String toString() {
		String str = "Player [";
		for (int i=0;i<cards.size();i++) {
			str += cards.get(i).toString() +"; ";
		}
		str += " ]";
		return str;
	}
    
}
