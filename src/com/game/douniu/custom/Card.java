package com.game.douniu.custom;


public class Card {
	private int id;			//鐗屽簭鍙�	
	private int value;			//鐗屽搴旂殑鍊�	
	private int cardType;		//鏂瑰潡銆佹鑺便�绾㈡銆侀粦妗�	
	private int cardValue;		//濡�,2,...,10,11,12,13
	private String cardFace;		//濡傛柟鍧�1',鏂瑰潡'2',...,鏂瑰潡'K'
	
	public Card() {
		this.id = 0;
		this.value = 1;
		this.cardType = 1;
		this.cardValue = 1;
		this.cardFace = "1";
	}
	
	public Card(int id) {
		this.id = id;
		this.value = CardUtil.getLogicValue(id);
	}
	
	public Card(int id, int value, int cardType, int cardValue, String cardFace) {
		this.id = id;
		this.value = value;
		this.cardType = cardType;
		this.cardValue = cardValue;
		this.cardFace = cardFace;
	}

	public Card(Card card) {
		this.id = card.id;
		this.value = card.value;
		this.cardType = card.cardType;
		this.cardValue = card.cardValue;
		this.cardFace = card.cardFace;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getCardValue() {
		return cardValue;
	}

	public void setCardValue(int cardValue) {
		this.cardValue = cardValue;
	}
	
	public String getCardFace() {
		return cardFace;
	}

	public void setCardFace(String cardFace) {
		this.cardFace = cardFace;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", value=" + value + ", cardType=" + cardType
				+ ", cardValue=" + cardValue + ", cardFace=" + cardFace + "]";
	}
	
}
