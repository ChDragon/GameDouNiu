package com.game.douniu.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.douniu.R;

import android.content.Context;
import android.util.Log;

public class DouNiuRule {
	private static String TAG = "[wzj]DouNiuRule";
	
	public static final int POKER_PATTERN_WU_NIU = 0;			//鏃犵墰
	public static final int POKER_PATTERN_NIU_1 = 1;
	public static final int POKER_PATTERN_NIU_2 = 2;
	public static final int POKER_PATTERN_NIU_3 = 3;
	public static final int POKER_PATTERN_NIU_4 = 4;
	public static final int POKER_PATTERN_NIU_5 = 5;
	public static final int POKER_PATTERN_NIU_6 = 6;
	public static final int POKER_PATTERN_NIU_7 = 7;
	public static final int POKER_PATTERN_NIU_8 = 8;
	public static final int POKER_PATTERN_NIU_9 = 9;
	public static final int POKER_PATTERN_NIU_NIU = 10;
	//public static final int POKER_PATTERN_YOU_NIU = 2;		//鏈夌墰锛屽鐗�锛岀墰2绛�	
	public static final int POKER_PATTERN_FOUR_HUA = 20;		//鍥涜姳锛屼竴寮犱负10锛屽叾浣欏潎涓鸿姳鐗岋紝濡�0,J,J,Q,Q
	public static final int POKER_PATTERN_FIVE_HUA = 30;		//浜旇姳锛屽叏閮ㄤ负鑺辩墝锛屽J,J,Q,Q,K
	public static final int POKER_PATTERN_ZHA_DAN = 40;		//鐐稿脊锛屽嵆4寮犵墝涓�紶锛屽A,5,5,5,5
	
	
	public static final int STAKE_UNIT = 10;					//璧屾敞鍗曚綅锛�0鍏�	
	public static final int DEFAULT_STAKE = STAKE_UNIT * 1;	//榛樿璧屾敞10鍏�	
	public static boolean checkZhaDan(List<Card> cards) {	
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[checkZhaDan]i:"+cards.get(i));
		}
		
		// 鍒涘缓鏁扮粍锛屽垎鍒瓨鏀�锝�3鐨勬暟閲忥紝鐒跺悗鐪嬶紝鑰屼笉蹇呬竴涓�瘮杈�		
		List<Integer> countList = new ArrayList<Integer>();
		for (int i=0;i<13;i++){
			countList.add(i, 0);
		}
		for (int i=0;i<cards.size();i++) {
			Integer index = (Integer)cards.get(i).getCardValue() - 1;
			Integer oldValue = countList.get(index);
			countList.set(index, oldValue+1);
		}
		for (int i=0;i<countList.size();i++){
			Integer value = countList.get(i);
			Log.v(TAG, "[checkZhaDan]i:"+i+",value:"+value);
			if (value >= 4) {
				Log.v(TAG, "[checkZhaDan]is zhadan");
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkFiveHua(List<Card> cards) {
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[checkFiveHua]i:"+cards.get(i));
		}
		
		for (int i=0;i<cards.size();i++) {
			int cardValue = cards.get(i).getCardValue();
			if (cardValue != 11 && cardValue != 12 && cardValue != 13) {
				Log.v(TAG, "[checkFiveHua]is not fivehua");
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkFourHua(List<Card> cards) {
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[checkFourHua]i:"+cards.get(i));
		}
		
		int countMoreThenTen = 0;
		for (int i=0;i<cards.size();i++) {
			int cardValue = cards.get(i).getCardValue();
			if (cardValue < 10) {
				Log.v(TAG, "[checkFourHua]some card is less than 10, is not fourhua");
				return false;
			} else if (cardValue > 10) {
				countMoreThenTen++;
			}
		}
		if (countMoreThenTen >= 4) {
			Log.v(TAG, "[checkFourHua]more then 4 is more than 10, and none is less than 10, so fourhua");
			return true;
		}
		
		return false;
	}
	
	public static boolean checkNiuNiu(List<Card> cards) {
		boolean ret = checkHasNiu(cards);
		if (ret == true) {
			int sum = getSumOfCards(cards) % 10;
			if (sum % 10 == 0) {
				Log.v(TAG, "[checkNiuNiu]is NiuNiu");
				return true;
			}
		}
		
		return false;
	}
	
	public static int getSumOfCards(List<Card> cards) {
		int sum = 0;
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[getSumOfCards]i:"+cards.get(i));
			sum+=cards.get(i).getValue();
		}
		return sum;
	}
	
	// 绠楁硶鏍稿績锛歛+b+c+d+e=sum锛岃嫢鍏朵腑浠绘剰涓変釜涔嬪拰涓�0鍊嶆暟锛屾瘮濡俛+b+c=10n锛屽嵆sum-d-e涓�0n銆�	
	public static boolean checkHasNiu(List<Card> cards) {
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[checkHasNiu]i:"+cards.get(i));
		}
		
		int sum = getSumOfCards(cards);
		Log.v(TAG, "[checkHasNiu]sum:"+sum);
		
		for (int i=0;i<cards.size()-1;i++) {//i:0~3
			Log.v(TAG, "[checkHasNiu]i:"+i);
			for (int j=i+1;j<cards.size();j++) {//j:i~4
				Log.v(TAG, "[checkHasNiu]  j:"+j);
				int value1 = cards.get(i).getValue();
				int value2 = cards.get(j).getValue();
				int temp = sum - value1 - value2;
				Log.v(TAG, "[checkHasNiu]value1:"+value1+",value2:"+value2+",temp:"+temp);
				if (temp % 10 == 0) {
					Log.v(TAG, "[checkHasNiu]has Niu, i:"+i+"; j:"+j);
					return true;
				}
			}
		}
		Log.v(TAG, "[checkHasNiu]end. no Niu");
		return false;
	}
	
	/* 璁＄畻
	 *   鑻ユ湁鐗涳紝鍒欏厛鍒ゆ柇鏄惁鐗涚墰锛岃嫢鏄墰鐗涘垯杩斿洖10锛屽惁鍒欒繑鍥炴�鍜屾眰浣�0鐨勪釜浣嶆暟銆�	 *   鑻ユ棤鐗涳紝杩斿洖0銆�	 */
	public static int calculatePoints(List<Card> cards) {
		int points = 0;
		boolean ret = checkHasNiu(cards);
		if (ret == true) {
			points = getSumOfCards(cards) % 10;
			if (points == 0) {
				return 10;
			} else {
				return points;
			}
		}
		
		return 0;
	}
	
	public static int getMaxCardValue(List<Card> cards) {
		int max = 0;
		for (int i=0;i<cards.size();i++) {
			Log.v(TAG, "[getMaxCardValue]i:"+cards.get(i));
			if (max < cards.get(i).getCardValue()) {
				max = cards.get(i).getCardValue();
			}
		}
		
		return max;
	}
	
	public static String getResultStr(Context context, int pokerPattern) {
		Log.v(TAG, "[getResultStr]pokerPattern:"+pokerPattern);
		String ret = "wuniu";
		switch (pokerPattern) {
		case DouNiuRule.POKER_PATTERN_WU_NIU:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_WU_NIU");
			ret = context.getResources().getString(R.string.str_no_niu);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_1:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_1");
			ret = context.getResources().getString(R.string.str_niu_1);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_2:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_2");
			ret = context.getResources().getString(R.string.str_niu_2);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_3:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_3");
			ret = context.getResources().getString(R.string.str_niu_3);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_4:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_4");
			ret = context.getResources().getString(R.string.str_niu_4);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_5:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_5");
			ret = context.getResources().getString(R.string.str_niu_5);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_6:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_6");
			ret = context.getResources().getString(R.string.str_niu_6);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_7:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_7");
			ret = context.getResources().getString(R.string.str_niu_7);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_8:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_8");
			ret = context.getResources().getString(R.string.str_niu_8);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_9:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_9");
			ret = context.getResources().getString(R.string.str_niu_9);
			break;
		case DouNiuRule.POKER_PATTERN_NIU_NIU:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_NIU_NIU");
			ret = context.getResources().getString(R.string.str_niu_niu);
			break;
		case DouNiuRule.POKER_PATTERN_FOUR_HUA:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_FOUR_HUA");
			ret = context.getResources().getString(R.string.str_four_hua);
			break;
		case DouNiuRule.POKER_PATTERN_FIVE_HUA:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_FIVE_HUA");
			ret = context.getResources().getString(R.string.str_five_hua);
			break;
		case DouNiuRule.POKER_PATTERN_ZHA_DAN:
			Log.v(TAG, "[getResultStr]POKER_PATTERN_ZHA_DAN");
			ret = context.getResources().getString(R.string.str_zha_dan);
			break;	
		default:
			Log.v(TAG, "[getResultStr]default");
			ret = context.getResources().getString(R.string.str_no_niu);
			break;
		}
		Log.v(TAG, "[getResultStr]ret:"+ret);
		return ret;
	}
	
}
