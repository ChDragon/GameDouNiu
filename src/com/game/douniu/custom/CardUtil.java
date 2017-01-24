package com.game.douniu.custom;

import java.util.ArrayList;
import java.util.List;


public class CardUtil {

	public static int COUNT_CARDS = 52;
	
	public static int getLogicValue(int pokerNumber) {
		int value = 0;
		if (pokerNumber >= 0 && pokerNumber < 13) {//方块0~12
			value = pokerNumber + 1;
			if (value > 10) {//J,Q,K
				value = 10;
			}
		} else if(pokerNumber >= 13 && pokerNumber < 26){//梅花13~25
			value = pokerNumber +1 - 13;
			if (value > 10) {//J,Q,K
				value = 10;
			}
		} else if(pokerNumber >= 26 && pokerNumber < 39){//红桃26~38
			value = pokerNumber + 1 - 26;
			if (value > 10) {//J,Q,K
				value = 10;
			}
		} else if(pokerNumber >= 39 && pokerNumber < 52){//黑桃39~51
			value = pokerNumber + 1 - 39;
			if (value > 10) {//J,Q,K
				value = 10;
			}
		}
		return value;
	}
	
	public static Card upateCardById(int id) {
		Card card = new Card();
		card.setId(id);
		card.setValue(getLogicValue(id));
		int num1 = id%13+1;
		int num2 = id/13+1;
		card.setCardValue(num1);
		card.setCardType(num2);
		String face = "1";
		if (num1 >= 2 && num1 <= 9) {
			char ch = (char)(num1 + '0');
			face = "" + (ch);
		} else if (num1 == 10){
			face = "10";
		} else if (num1 == 11){
			face = "J";
		} else if (num1 == 12){
			face = "Q";
		} else if (num1 == 13){
			face = "K";
		} else if (num1 == 1){
			face = "A";
		}
		card.setCardFace(face);
		return card;
	}
	
	public static List<Card> getCards() {
		List<Card> cards = new ArrayList<Card>();
		try {
			/*for (int i = 0; i < 13; i++) {
				Card card = new Card(i);
				card.setCardType(CardType.FANGKUAI);
				card.setCardValue(i);
				cards.add(card);
			}
			for (int i = 13; i < 26; i++) {
				Card card = new Card(i);
				card.setCardType(CardType.MEIHUA);
				card.setCardValue(i-13);
				cards.add(card);
			}
			for (int i = 26; i < 39; i++) {
				Card card = new Card(i);
				card.setCardType(CardType.HONGXIN);
				card.setCardValue(i-26);
				cards.add(card);
			}
			for (int i = 39; i < 52; i++) {
				Card card = new Card(i);
				card.setCardType(CardType.HEITAO);
				card.setCardValue(i-39);
				cards.add(card);
			}*/
			
			for (int i = 0; i < COUNT_CARDS; i++) {
				/*Card card = new Card();
				card.setId(i);
				card.setValue(getLogicValue(i));
				int num1 = i%13+1;
				int num2 = i/13+1;
				card.setCardValue(num1);
				card.setCardType(num2);
				String face = "1";
				if (num1 >= 2 && num1 <= 9) {
					char ch = (char)(num1 + '0');
					face = "" + (ch);
				} else if (num1 == 10){
					face = "10";
				} else if (num1 == 11){
					face = "J";
				} else if (num1 == 12){
					face = "Q";
				} else if (num1 == 13){
					face = "K";
				} else if (num1 == 1){
					face = "A";
				}
				card.setCardFace(face);*/
				Card card = upateCardById(i);
				cards.add(card);
			}
		} catch (Exception e) {
		}

		return cards;

	}

}
