package com.game.douniu.custom;

import com.game.douniu.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class CardRes {

	public static int FLAG_BITMAP_ORIGINAL = 1;
	public static int FLAG_BITMAP_SCALE = 2;
    public static float SCALE = 0.6f;
	
    private Bitmap[] cardBitmaps = new Bitmap[CardUtil.COUNT_CARDS];
    private Bitmap[] cardBitmapsScale = new Bitmap[CardUtil.COUNT_CARDS];
    private Bitmap cardBack;
    private Bitmap cardBackScale;
    private Matrix matrix;

    
    private static CardRes instance = null;
    
    public static CardRes getInstance() {
    	if (instance == null) {
    		synchronized (CardRes.class) {
    			if (instance == null) {
    				instance = new CardRes();
    			}
    		}
    	}
    	return instance;
    }

	private CardRes() {
	}

	public void onLoadImage(Resources res) {
        for (int i=0;i<CardUtil.COUNT_CARDS;i++) {
        	cardBitmaps[i] = BitmapFactory.decodeResource(res, R.drawable.card_big_00 + i);
        }
        cardBack = BitmapFactory.decodeResource(res, R.drawable.card_big_back);
        
        //scale
		matrix = new Matrix();
		matrix.postScale(SCALE, SCALE);
        for (int i=0;i<CardUtil.COUNT_CARDS;i++) {
        	cardBitmapsScale[i] = Bitmap.createBitmap(cardBitmaps[i], 0, 0, cardBitmaps[i].getWidth(), cardBitmaps[i].getHeight(),  
                    matrix, true);
        }
        cardBackScale = Bitmap.createBitmap(cardBack, 0, 0, cardBack.getWidth(), cardBack.getHeight(),  
                matrix, true);
	}

	public void onDestroy() {
        for (int i=0;i<CardUtil.COUNT_CARDS;i++) {
    		if (cardBitmaps[i] != null && !cardBitmaps[i].isRecycled()) {
    			cardBitmaps[i].recycle();
    		}
    		cardBitmaps[i] = null;
    		if (cardBitmapsScale[i] != null && !cardBitmapsScale[i].isRecycled()) {
    			cardBitmapsScale[i].recycle();
    		}
    		cardBitmapsScale[i] = null;
    		
        }
        
        if (cardBack != null && !cardBack.isRecycled()) {
        	cardBack.recycle();
        }
        cardBack = null;
        if (cardBackScale != null && !cardBackScale.isRecycled()) {
        	cardBackScale.recycle();
        }
        cardBackScale = null;
	}
	
	public Bitmap[] getCardBitmaps(int flag) {
		if (flag == FLAG_BITMAP_SCALE) {
			return cardBitmapsScale;
		}
		return cardBitmaps;
	}
	
	public Bitmap getCardBack(int flag) {
		if (flag == FLAG_BITMAP_SCALE) {
			return cardBackScale;
		}
		return cardBack;
	}
}
