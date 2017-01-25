package com.game.douniu.custom;

import com.game.douniu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class CardsView extends View {
	private static String TAG = "[wzj]CardsView";

	public final static int TYPE_BACK 				= 1;
	public final static int TYPE_FACE 				= 2;
	public final static int TYPE_FACE_PATTERN 		= 3;
	public final static int TYPE_FACE_PATTERN_RESULT 	= 4;
	
	private Context mContext;
	private Paint mPaint;
	private Rect mBounds;
    
    private Bitmap[] cardBitmaps;
    private Bitmap cardBack;
    
    private static int GAP_BETWEEN_TWO_CARDS = 40;
    private static int CARD_NUM = 5;
    private int[] cardIndex = new int[CARD_NUM];
    
    //private boolean isDisplayCardBack = true;
    //private boolean isDisplayResult = false;
    
    private String patternStr;
    private int moneyChanged;
    private int displayType = TYPE_BACK;
    
    private int mBitmapFlag = CardRes.FLAG_BITMAP_SCALE;
   
	
	public CardsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, mBitmapFlag);
	}

	public CardsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, mBitmapFlag);
	}

	public CardsView(Context context) {
		super(context);
		init(context, mBitmapFlag);
	}
	
	public CardsView(Context context, int bitmapFlag) {
		super(context);
		mBitmapFlag = bitmapFlag;
		init(context, mBitmapFlag);
	}
	
	public void init(Context context, int bitmapFlag) {
		mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        cardBitmaps = CardRes.getInstance().getCardBitmaps(bitmapFlag);
        cardBack = CardRes.getInstance().getCardBack(bitmapFlag);
        //isDisplayCardBack = true;
        //isDisplayResult = false;
        displayType = TYPE_BACK;
        patternStr = "Å£¾Å";
        for (int i=0;i<CARD_NUM;i++) {
        	cardIndex[i] = 0;
        }
	}

	@Override  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cardBitmaps == null || cardBitmaps.length < CARD_NUM) {
        	Log.w(TAG, "cardBitmaps null or not enough");
        	return;
        }
        if (cardBack == null) {
        	Log.w(TAG, "cardBack null");
        	return;
        }
        
        canvas.save();
        int delX = 0;
        for (int i=0;i<CARD_NUM;i++){
        	if (displayType == TYPE_BACK) {
        		canvas.drawBitmap(cardBack, delX, 0, mPaint);
        	} else {
        		canvas.drawBitmap(cardBitmaps[cardIndex[i]], delX, 0, mPaint);
        	}
        	if (mBitmapFlag == CardRes.FLAG_BITMAP_SCALE) {
        		delX += GAP_BETWEEN_TWO_CARDS * CardRes.SCALE;
        	} else {
        		delX += GAP_BETWEEN_TWO_CARDS;
        	}
        }
        if (displayType >= TYPE_FACE_PATTERN && !patternStr.isEmpty() && patternStr.length() > 0) {
        	Log.w(TAG, "draw pattern");
        	mPaint.setColor(Color.RED);
        	mPaint.setTextSize(50);
        	mPaint.getTextBounds(patternStr, 0, patternStr.length(), mBounds);
        	float textWidth = mBounds.width();
            float textHeight = mBounds.height();
            canvas.drawText(patternStr, getWidth() / 2 - textWidth / 2, getHeight() / 2  
                    + textHeight / 2, mPaint);
        }
        if (displayType == TYPE_FACE_PATTERN_RESULT) {
        	Log.w(TAG, "draw result");
        	mPaint.setColor(Color.BLUE);
        	mPaint.setTextSize(50);
        	String resultStr = "Same";
        	if (moneyChanged > 0) {
        		resultStr = "Win";
        	}
        	else if (moneyChanged < 0) {
        		resultStr = "Lost";
        	}
        	mPaint.getTextBounds(resultStr, 0, resultStr.length(), mBounds);
        	float textWidth = mBounds.width();
            float textHeight = mBounds.height();
            canvas.drawText(resultStr, getWidth() / 2 - textWidth / 2,  
                   10 + textHeight, mPaint);
        }
        canvas.restore();
    }
	
	public void setCardsIndex(int cardIndex1, int cardIndex2, int cardIndex3, int cardIndex4, int cardIndex5) {
		cardIndex[0] = cardIndex1;
		cardIndex[1] = cardIndex2;
		cardIndex[2] = cardIndex3;
		cardIndex[3] = cardIndex4;
		cardIndex[4] = cardIndex5;
		invalidate();
	}
	
	public void setPatternStr(String patternStr) {
		this.patternStr = patternStr;
	}

	public void setMoneyChanged(int moneyChanged) {
		this.moneyChanged = moneyChanged;
		invalidate();
	}

	public void setDisplayType(int displayType) {
		if (this.displayType != displayType) {
			this.displayType = displayType;
			invalidate();
		}
	}

	public int getRealWidth() {
		float scale = 1;
		if (mBitmapFlag == CardRes.FLAG_BITMAP_SCALE) {
			scale *= CardRes.SCALE;
			//Log.w(TAG, "getRealWidth scale:"+scale);
		}
		int ret = (int) (cardBack.getWidth() + (CARD_NUM - 1) * GAP_BETWEEN_TWO_CARDS * scale + 1);
		//Log.w(TAG, "getRealWidth:"+ret);
		//Log.w(TAG, "cardBack.getWidth():"+cardBack.getWidth());
		return ret;
	}
	
	public int getRealHeight() {
		float scale = 1;
		if (mBitmapFlag == CardRes.FLAG_BITMAP_SCALE) {
			scale *= CardRes.SCALE;
			//Log.w(TAG, "getRealHeight scale:"+scale);
		}
		int ret = (int) (cardBack.getHeight() + 1 * GAP_BETWEEN_TWO_CARDS * scale + 1);
		//Log.w(TAG, "getRealHeight:"+ret);
		//Log.w(TAG, "cardBack.getHeight():"+cardBack.getHeight());
		return ret;
	}
}
