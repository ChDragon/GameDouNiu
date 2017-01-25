package com.game.douniu.custom;


import java.util.List;

import com.game.douniu.MainActivity;
import com.game.douniu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author justin.wu
 *
 */
public class GameView extends ViewGroup implements OnClickListener {
	private static String TAG = "[wzj]GameView";

	public static final int		IDC_GAME_PREPARE	= 100;
	public static final int		IDC_GAME_YOUNIU		= 101;
	public static final int		IDC_GAME_WUNIU		= 102;
	public static final int		IDC_BANKER_TRY		= 103;
	public static final int		IDC_BANKER_SKIP		= 104;
	public static final int		IDC_STAKE_X1		= 105;
	public static final int		IDC_STAKE_X5		= 106;
	public static final int		IDC_STAKE_X10		= 107;
	
	public static final int		IDC_CARD_1			= 110;
	public static final int		IDC_CARD_2			= 111;
	public static final int		IDC_CARD_3			= 112;
	public static final int		IDC_CARD_4			= 113;
	public static final int		IDC_CARD_5			= 114;
	
	public static final int		IDC_USER_HEAD_SELF	= 120;
	public static final int		IDC_USER_HEAD_1		= 121;
	public static final int		IDC_USER_HEAD_2		= 122;
	public static final int		IDC_USER_HEAD_3		= 123;
	
	public static final int		IDC_FLAG_READY_SELF		= 130;
	public static final int		IDC_FLAG_READY_1		= 131;
	public static final int		IDC_FLAG_READY_2		= 132;
	public static final int		IDC_FLAG_READY_3		= 133;
	
	
	public final static int STATUS_CONTROL_PREPARE 			= 1;
	public final static int STATUS_CONTROL_TRYING_BANKER 	= 2;
	public final static int STATUS_CONTROL_STAKE 			= 3;
	public final static int STATUS_CONTROL_START 			= 4;
	
	public final static int MSG_LOGIN_CB 					= 0;
	public final static int MSG_OTHER_USER_IN_CB 			= 1;
	public final static int MSG_LOGOUT_CB 					= 2;
	public final static int MSG_OTHER_USER_OUT_CB 			= 3;
	public final static int MSG_PREPARE_CB 					= 4;
	public final static int MSG_OTHER_USER_PREPARE_CB 		= 5;
	public final static int MSG_WILL_BANKER_CB 				= 6;
	public final static int MSG_TRY_BANKER_CB 				= 7;
	public final static int MSG_WILL_STAKE_CB 				= 8;
	public final static int MSG_STAKE_CB 					= 9;
	public final static int MSG_OTHER_USER_STAKE_CB 		= 10;
	public final static int MSG_WILL_START_CB 				= 11;
	public final static int MSG_PLAY_CB 					= 12;
	public final static int MSG_OTHER_USER_CARD_PATTERN_CB 	= 13;
	public final static int MSG_GAME_RESULT_CB 				= 14;
	
	Context mContext;
	
	/** 游戏引用 **/
	MainActivity			mainActivity;
	private static int MAX_USERS = 6;
	
	View selfLayout;
	ImageView card1Iv;
	ImageView card2Iv;
	ImageView card3Iv;
	ImageView card4Iv;
	ImageView card5Iv;
	
	CardsView cardsViewSelf;
	CardsView[] otherCardsView = new CardsView[MAX_USERS - 1];
	
	View m_HeadViewSelf;
	View[] m_HeadViews = new View[MAX_USERS - 1];
	
	Button prepareBtn;
	Button youniuBtn;
	Button wuniuBtn;
	
	Button skipBankerBtn;
	Button tryBankerBtn;
	
	Button x1Btn;
	Button x5Btn;
	Button x10Btn;
	
	Paint mPaint;
	Bitmap readyBitmap;
	Bitmap bankerBitmap;
	Bitmap x1Bitmap;
	Bitmap x5Bitmap;
	Bitmap x10Bitmap;
	TextView toastTv;
	
	
	Point[]	m_ptUserHead		= new Point[MAX_USERS];// 0: self
	Point[]	m_ptUserCardView	= new Point[MAX_USERS];// 0: self
	Point[]	m_ptUserReady		= new Point[MAX_USERS];
	
	public final static int FLAG_STATUS_PREPARE 		= 1;
	public final static int FLAG_STATUS_BANKER_STAKE 	= 2;
	private int flagStatus = FLAG_STATUS_PREPARE;
	
	private int bankerIndex = -1;
	
	@SuppressWarnings("unused")
	public GameView(MainActivity activity, Context context) {
		super(context);
		mainActivity = activity;
		initView(context);
	}
	
	public GameView(Context context) {
		super(context);
		initView(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public void initView(Context context) {
		mContext = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		readyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_ready);
		bankerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_banker);
		x1Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_x1);
		x5Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_x5);
		x10Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_x10);
		
		for (int i = 0; i < MAX_USERS; i++) {
			m_ptUserHead[i] = new Point();
			m_ptUserCardView[i] = new Point();
			m_ptUserReady[i] = new Point();
		}
		
		setBackground(mContext.getResources().getDrawable(R.drawable.bg));
		
		//1) 头像控件初始化
		m_HeadViewSelf = inflate(context, R.layout.game_head, null);
		m_HeadViewSelf.setOnClickListener(this);
		m_HeadViewSelf.setTag(IDC_USER_HEAD_SELF);
		addView(m_HeadViewSelf);
		TextView usernameTv = (TextView)m_HeadViewSelf.findViewById(R.id.game_head_txt_name);
		usernameTv.setText(MainActivity.getInstance().m_players.get(0).getUsername());
		TextView moneyTv = (TextView)m_HeadViewSelf.findViewById(R.id.game_head_txt_point);
		moneyTv.setText(""+MainActivity.getInstance().m_players.get(0).getMoney());
		
		for (int i=0;i<m_HeadViews.length;i++) {
			m_HeadViews[i] = inflate(context, R.layout.game_head, null);
			m_HeadViews[i].setOnClickListener(this);
			m_HeadViews[i].setTag(IDC_USER_HEAD_1 + i);
			addView(m_HeadViews[i]);
		}
		
		//2) 5张牌的视图初始化
		selfLayout = inflate(context, R.layout.self_layout, null);
		addView(selfLayout);
		card1Iv = (ImageView)selfLayout.findViewById(R.id.card1_image);
		card1Iv.setImageResource(R.drawable.card_big_back);
		card1Iv.setOnClickListener(this);
		card1Iv.setTag(IDC_CARD_1);
		card2Iv = (ImageView)selfLayout.findViewById(R.id.card2_image);
		card2Iv.setImageResource(R.drawable.card_big_back);
		card2Iv.setOnClickListener(this);
		card2Iv.setTag(IDC_CARD_2);
		card3Iv = (ImageView)selfLayout.findViewById(R.id.card3_image);
		card3Iv.setImageResource(R.drawable.card_big_back);
		card3Iv.setOnClickListener(this);
		card3Iv.setTag(IDC_CARD_3);
		card4Iv = (ImageView)selfLayout.findViewById(R.id.card4_image);
		card4Iv.setImageResource(R.drawable.card_big_back);
		card4Iv.setOnClickListener(this);
		card4Iv.setTag(IDC_CARD_4);
		card5Iv = (ImageView)selfLayout.findViewById(R.id.card5_image);
		card5Iv.setImageResource(R.drawable.card_big_back);
		card5Iv.setOnClickListener(this);
		card5Iv.setTag(IDC_CARD_5);
		
		
		//其他玩家的牌视图初始化
		cardsViewSelf = new CardsView(context, CardRes.FLAG_BITMAP_ORIGINAL);
		//cardsViewSelf.setCardsIndex(39,40,41,42,43);
		addView(cardsViewSelf);
		for (int i=0;i<otherCardsView.length;i++) {
			otherCardsView[i] = new CardsView(context);
			addView(otherCardsView[i]);
		}
		
		
		//按钮初始化
		prepareBtn = new Button(context);
		prepareBtn.setText("准备");
		prepareBtn.setOnClickListener(this);
		prepareBtn.setTag(IDC_GAME_PREPARE);
		addView(prepareBtn);
		youniuBtn = new Button(context);
		youniuBtn.setText("有牛");
		youniuBtn.setOnClickListener(this);
		youniuBtn.setTag(IDC_GAME_YOUNIU);
		addView(youniuBtn);
		wuniuBtn = new Button(context);
		wuniuBtn.setText("无牛");
		wuniuBtn.setOnClickListener(this);
		wuniuBtn.setTag(IDC_GAME_WUNIU);
		addView(wuniuBtn);
		
		skipBankerBtn = new Button(context);
		skipBankerBtn.setText("不抢");
		skipBankerBtn.setOnClickListener(this);
		skipBankerBtn.setTag(IDC_BANKER_SKIP);
		addView(skipBankerBtn);
		tryBankerBtn = new Button(context);
		tryBankerBtn.setText("抢庄");
		tryBankerBtn.setOnClickListener(this);
		tryBankerBtn.setTag(IDC_BANKER_TRY);
		addView(tryBankerBtn);
		
		x1Btn = new Button(context);
		x1Btn.setText("X1");
		x1Btn.setOnClickListener(this);
		x1Btn.setTag(IDC_STAKE_X1);
		addView(x1Btn);
		x5Btn = new Button(context);
		x5Btn.setText("X5");
		x5Btn.setOnClickListener(this);
		x5Btn.setTag(IDC_STAKE_X5);
		addView(x5Btn);
		x10Btn = new Button(context);
		x10Btn.setText("X10");
		x10Btn.setOnClickListener(this);
		x10Btn.setTag(IDC_STAKE_X10);
		addView(x10Btn);
		
		toastTv = new TextView(context);
		toastTv.setText(context.getResources().getText(R.string.STR_WAIT_TO_STAKE));
		addView(toastTv);
		toastTv.setVisibility(View.INVISIBLE);
		
		showControlUI(STATUS_CONTROL_PREPARE);
		showOtherUserUI(MainActivity.getInstance().m_players.size());
		showDividedCardView(true);
	}

	/**计算坐标
	 * 					（head）
	 * 					    玩家2
	 * 
	 *		       （head）玩家1		玩家3（head）
	 * 
	 * （head）玩家4						玩家5	（head）
	 * 
	 * （head）    			自身玩家0
	 * 
	 * @param w
	 * @param h
	 */
	protected void rectifyControl(int w, int h) {
		//Log.d(TAG, "[rectifyControl]start");
		m_ptUserHead[0].set(2, h - 2 - m_HeadViewSelf.getMeasuredHeight());
		m_ptUserHead[1].set(2 + w / 4, h / 14);
		m_ptUserHead[2].set(w / 2 - m_HeadViews[1].getMeasuredWidth() / 2, 0);
		m_ptUserHead[3].set(w - 2 - w / 4 - m_HeadViews[2].getMeasuredWidth(), h / 14);
		m_ptUserHead[4].set(2, h / 2 - m_HeadViews[3].getMeasuredHeight() / 2 + 30);
		m_ptUserHead[5].set(w - 2 - m_HeadViews[4].getMeasuredWidth(), h / 2 - m_HeadViews[4].getMeasuredHeight() / 2 + 30);
		
		m_ptUserCardView[0].set(w / 2 - cardsViewSelf.getRealWidth() / 2, h - 10 - cardsViewSelf.getRealHeight());
		m_ptUserCardView[1].set(m_ptUserHead[1].x, m_ptUserHead[1].y + m_HeadViews[0].getMeasuredHeight());
		m_ptUserCardView[2].set(w / 2 - otherCardsView[1].getRealWidth() / 2, m_ptUserHead[2].y + m_HeadViews[1].getMeasuredHeight());
		m_ptUserCardView[3].set(w - 2 - w / 4 - otherCardsView[2].getRealWidth(), m_ptUserCardView[1].y);
		m_ptUserCardView[4].set(m_ptUserHead[4].x + m_HeadViews[3].getMeasuredWidth(), h / 2 - otherCardsView[3].getRealHeight() / 2 + 50);
		m_ptUserCardView[5].set(m_ptUserHead[5].x - otherCardsView[4].getRealWidth(), m_ptUserCardView[4].y);
		
		m_ptUserReady[0].set(m_ptUserHead[0].x + m_HeadViewSelf.getMeasuredWidth(), m_ptUserHead[0].y);
		m_ptUserReady[1].set(m_ptUserHead[1].x + m_HeadViews[0].getMeasuredWidth(), m_ptUserHead[1].y);
		m_ptUserReady[2].set(m_ptUserHead[2].x + m_HeadViews[1].getMeasuredWidth(), m_ptUserHead[2].y);
		m_ptUserReady[3].set(m_ptUserHead[3].x + m_HeadViews[2].getMeasuredWidth(), m_ptUserHead[3].y);
		m_ptUserReady[4].set(m_ptUserHead[4].x + m_HeadViews[3].getMeasuredWidth(), m_ptUserHead[4].y);
		m_ptUserReady[5].set(m_ptUserHead[5].x + m_HeadViews[4].getMeasuredWidth(), m_ptUserHead[5].y);
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
	}

	/* (non-Javadoc)
	 * 
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		Log.d(TAG, "[onLayout]>>l:"+l+",t:"+t+",r:"+r+",b:"+b);
		int w = r - l, h = b - t;

		for (int i = 0; i < this.getChildCount(); i++) {
			View v = getChildAt(i);
			if (v != null)
				v.measure(w, h);
		}
		
		rectifyControl(w, h);
		
		int btw = 0, bth = 0;
		
		//左下为自身
		int headSelfBtw = m_HeadViewSelf.getMeasuredWidth();
		int headSelfBth = m_HeadViewSelf.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]m_HeadViewSelf btw:"+headSelfBtw+",bth:"+headSelfBth);
		m_HeadViewSelf.layout(m_ptUserHead[0].x, m_ptUserHead[0].y, m_ptUserHead[0].x + headSelfBtw, m_ptUserHead[0].y + headSelfBth);
		
		//cardsViewSelf：居中
		btw = cardsViewSelf.getRealWidth();
		bth = cardsViewSelf.getRealHeight();
		//Log.d(TAG, "[onLayout]cardsViewSelf btw:"+btw+",bth:"+bth);
		cardsViewSelf.layout(m_ptUserCardView[0].x, m_ptUserCardView[0].y, m_ptUserCardView[0].x + btw, m_ptUserCardView[0].y + bth);

		
		for (int i=0;i<MAX_USERS - 1;i++) {
			int headBtw = m_HeadViews[i].getMeasuredWidth();
			int headBth = m_HeadViews[i].getMeasuredHeight();
			m_HeadViews[i].layout(m_ptUserHead[i+1].x, m_ptUserHead[i+1].y, m_ptUserHead[i+1].x + headBtw, m_ptUserHead[i+1].y + headBth);
			
			btw = otherCardsView[1].getRealWidth();
			bth = otherCardsView[1].getRealHeight();
			otherCardsView[i].layout(m_ptUserCardView[i+1].x, m_ptUserCardView[i+1].y, m_ptUserCardView[i+1].x + btw, m_ptUserCardView[i+1].y + bth);
		}
		
		
		//居中
		btw = selfLayout.getMeasuredWidth();
		bth = selfLayout.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]selfLayout btw:"+btw+",bth:"+bth);
		int bthSelf = bth;
		selfLayout.layout(w / 2 - btw / 2, h - 2 - bth, w / 2 + btw / 2, h - 2);

		
		//准备按钮靠右下
		btw = prepareBtn.getMeasuredWidth();
		bth = prepareBtn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]prepareBtn btw:"+btw+",bth:"+bth);
		prepareBtn.layout(w - 10 - btw, h - 60 - bth, w - 10, h - 60);
		
		int wuniuBtw = wuniuBtn.getMeasuredWidth();
		int wuniuBth = wuniuBtn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]wuniuBtn btw:"+btw+",bth:"+bth);
		wuniuBtn.layout(w - 10 - wuniuBtw, h - 2 - wuniuBth, w - 10, h - 2);
		
		int youniuBtw = youniuBtn.getMeasuredWidth();
		int youniuBth = youniuBtn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]youniuBtn btw:"+btw+",bth:"+bth);
		youniuBtn.layout(w - 10 - youniuBtw, h - 32 - youniuBth - wuniuBth, w - 10, h - 32 - wuniuBth);
		
	
		//不抢、抢庄按钮在selfLayout之上居中
		btw = skipBankerBtn.getMeasuredWidth();
		bth = skipBankerBtn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]skipBankerBtn btw:"+btw+",bth:"+bth);
		skipBankerBtn.layout(w / 2 - btw - 5, h - 20 - bthSelf - bth, w / 2 - 5, h - 20 - bthSelf);
		
		btw = tryBankerBtn.getMeasuredWidth();
		bth = tryBankerBtn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]tryBankerBtn btw:"+btw+",bth:"+bth);
		tryBankerBtn.layout(w / 2 + 5, h - 20 - bthSelf - bth, w / 2 + btw + 5, h - 20 - bthSelf);
		
		//x1,x5,x10
		int x1Btw = x5Btn.getMeasuredWidth();
		int x1Bth = x5Btn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]x5Btn btw:"+btw+",bth:"+bth);
		x5Btn.layout(w / 2 - x1Btw/2, h - 20 - bthSelf - x1Bth, w / 2 + x1Btw/2, h - 20 - bthSelf);
		
		int x5Btw = x1Btn.getMeasuredWidth();
		int x5Bth = x1Btn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]x1Btn btw:"+btw+",bth:"+bth);
		x1Btn.layout(w / 2 - x1Btw/2 - 5 - x5Btw, h - 20 - bthSelf - x5Bth, w / 2 - x1Btw/2 - 5, h - 20 - bthSelf);
		
		btw = x10Btn.getMeasuredWidth();
		bth = x10Btn.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]x10Btn btw:"+btw+",bth:"+bth);
		x10Btn.layout(w / 2 + x1Btw/2 + 5 , h - 20 - bthSelf - bth, w / 2 + x1Btw/2 + 5 + btw, h - 20 - bthSelf);
		
		
		btw = toastTv.getMeasuredWidth();
		bth = toastTv.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]toastTv btw:"+btw+",bth:"+bth);
		toastTv.layout(w / 2 - btw/2, h - 20 - bthSelf - bth, w / 2 + btw/2, h - 20 - bthSelf);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			int tag = (Integer) v.getTag();
			Log.v(TAG, "[onClick]tag:"+tag);
			switch (tag) {
				case IDC_GAME_PREPARE:
					Log.v(TAG, "[onClick]IDC_GAME_PREPARE");
					MainActivity.getInstance().prepareAction();
					break;
				case IDC_GAME_YOUNIU:
					Log.v(TAG, "[onClick]IDC_GAME_YOUNIU");
					MainActivity.getInstance().startAction(true);
					break;
				case IDC_GAME_WUNIU:
					Log.v(TAG, "[onClick]IDC_GAME_WUNIU");
					MainActivity.getInstance().startAction(false);
					break;
				case IDC_BANKER_TRY:
					Log.v(TAG, "[onClick]IDC_BANKER_TRY");
					MainActivity.getInstance().tryingBankerAction(true);
					break;
				case IDC_BANKER_SKIP:
					Log.v(TAG, "[onClick]IDC_BANKER_SKIP");
					MainActivity.getInstance().tryingBankerAction(false);
					break;
				case IDC_STAKE_X1:
					Log.v(TAG, "[onClick]IDC_STAKE_X1");
					MainActivity.getInstance().stakeAction(1);
					break;
				case IDC_STAKE_X5:
					Log.v(TAG, "[onClick]IDC_STAKE_X5");
					MainActivity.getInstance().stakeAction(5);
					break;
				case IDC_STAKE_X10:
					Log.v(TAG, "[onClick]IDC_STAKE_X10");
					MainActivity.getInstance().stakeAction(10);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//Log.d(TAG, "[onDraw]start");
		onDrawFlag(canvas);
	}
	
	private void onDrawFlag(Canvas canvas) {
		int size = (MainActivity.getInstance().m_players.size() < MAX_USERS) ? MainActivity.getInstance().m_players.size() : MAX_USERS;
		for (int i=0;i<size;i++) {
			if (flagStatus == FLAG_STATUS_PREPARE) {
				final boolean isReady = MainActivity.getInstance().m_players.get(i).isReady();
				Log.d(TAG, "[onDrawFlag]isReady:"+isReady);
				if (isReady) {
					canvas.save();
					canvas.drawBitmap(readyBitmap, m_ptUserReady[i].x, m_ptUserReady[i].y, mPaint);
					canvas.restore();
				}
			} else if (flagStatus == FLAG_STATUS_BANKER_STAKE) {
				if (bankerIndex == i) {
					Log.d(TAG, "[onDrawFlag]draw flag banker");
					canvas.save();
					canvas.drawBitmap(bankerBitmap, m_ptUserReady[i].x, m_ptUserReady[i].y, mPaint);
					canvas.restore();
				} else {
					final boolean isStake = MainActivity.getInstance().m_players.get(i).isStake();
					Log.d(TAG, "[onDrawFlag]isStake:"+isStake);
					if (isStake) {
						final int stake = MainActivity.getInstance().m_players.get(i).getStake();
						Log.d(TAG, "[onDrawFlag]stake:"+stake);
						canvas.save();
						if (stake == 5 * DouNiuRule.STAKE_UNIT) {
							canvas.drawBitmap(x5Bitmap, m_ptUserReady[i].x, m_ptUserReady[i].y, mPaint);
						} else if (stake == 10 * DouNiuRule.STAKE_UNIT) {
							canvas.drawBitmap(x10Bitmap, m_ptUserReady[i].x, m_ptUserReady[i].y, mPaint);
						} else {
							canvas.drawBitmap(x1Bitmap, m_ptUserReady[i].x, m_ptUserReady[i].y, mPaint);
						}
						canvas.restore();
					}
				}
			}
		}
	}

	public void showOtherUserUI(int userCount) {
		int size = (MainActivity.getInstance().m_players.size() < MAX_USERS) ? MainActivity.getInstance().m_players.size() : MAX_USERS;
		for (int i=0;i<MAX_USERS-1;i++) {
			if (i<size-1) {
				m_HeadViews[i].setVisibility(View.VISIBLE);
				otherCardsView[i].setVisibility(View.VISIBLE);
				TextView usernameTv = (TextView)m_HeadViews[i].findViewById(R.id.game_head_txt_name);
				usernameTv.setText(MainActivity.getInstance().m_players.get(i+1).getUsername());
				TextView moneyTv = (TextView)m_HeadViews[i].findViewById(R.id.game_head_txt_point);
				moneyTv.setText(""+MainActivity.getInstance().m_players.get(i+1).getMoney());
			} else {
				m_HeadViews[i].setVisibility(View.INVISIBLE);
				otherCardsView[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public void showNewOtherUserUI(int playerIndex) {
		Log.w(TAG, "[showNewOtherUserUI]playerIndex:"+playerIndex);
		List<Player> players = MainActivity.getInstance().m_players;
		Log.w(TAG, "[showNewOtherUserUI]size:"+players.size());
		for (int i=0;i<players.size();i++){
			Log.w(TAG, "[showNewOtherUserUI]i:"+i+" "+players.get(i));
		}
		if (playerIndex >= MainActivity.getInstance().m_players.size() || playerIndex <= 0) {
			Log.w(TAG, "[showNewOtherUserUI]playerIndex too big or too small");
			return;
		}
		m_HeadViews[playerIndex-1].setVisibility(View.VISIBLE);
		otherCardsView[playerIndex-1].setVisibility(View.VISIBLE);
		TextView usernameTv = (TextView)m_HeadViews[playerIndex-1].findViewById(R.id.game_head_txt_name);
		usernameTv.setText(MainActivity.getInstance().m_players.get(playerIndex).getUsername());
		TextView moneyTv = (TextView)m_HeadViews[playerIndex-1].findViewById(R.id.game_head_txt_point);
		moneyTv.setText(""+MainActivity.getInstance().m_players.get(playerIndex).getMoney());
	}
	
	public void showControlUI(int status) {
		Log.d(TAG, "[showControlUI]status:"+status);
		switch (status) {
		case STATUS_CONTROL_PREPARE:
			prepareBtn.setVisibility(View.VISIBLE);
			youniuBtn.setVisibility(View.INVISIBLE);
			wuniuBtn.setVisibility(View.INVISIBLE);
			skipBankerBtn.setVisibility(View.INVISIBLE);
			tryBankerBtn.setVisibility(View.INVISIBLE);
			x1Btn.setVisibility(View.INVISIBLE);
			x5Btn.setVisibility(View.INVISIBLE);
			x10Btn.setVisibility(View.INVISIBLE);
			break;
		case STATUS_CONTROL_TRYING_BANKER:
			prepareBtn.setVisibility(View.INVISIBLE);
			youniuBtn.setVisibility(View.INVISIBLE);
			wuniuBtn.setVisibility(View.INVISIBLE);
			skipBankerBtn.setVisibility(View.VISIBLE);
			tryBankerBtn.setVisibility(View.VISIBLE);
			x1Btn.setVisibility(View.INVISIBLE);
			x5Btn.setVisibility(View.INVISIBLE);
			x10Btn.setVisibility(View.INVISIBLE);
			break;
		case STATUS_CONTROL_STAKE:
			prepareBtn.setVisibility(View.INVISIBLE);
			youniuBtn.setVisibility(View.INVISIBLE);
			wuniuBtn.setVisibility(View.INVISIBLE);
			skipBankerBtn.setVisibility(View.INVISIBLE);
			tryBankerBtn.setVisibility(View.INVISIBLE);
			x1Btn.setVisibility(View.VISIBLE);
			x5Btn.setVisibility(View.VISIBLE);
			x10Btn.setVisibility(View.VISIBLE);
			break;
		case STATUS_CONTROL_START:
			prepareBtn.setVisibility(View.INVISIBLE);
			youniuBtn.setVisibility(View.VISIBLE);
			wuniuBtn.setVisibility(View.VISIBLE);
			skipBankerBtn.setVisibility(View.INVISIBLE);
			tryBankerBtn.setVisibility(View.INVISIBLE);
			x1Btn.setVisibility(View.INVISIBLE);
			x5Btn.setVisibility(View.INVISIBLE);
			x10Btn.setVisibility(View.INVISIBLE);
			break;
		}
	}
	
	public void showDividedCardView(boolean flag) {
		if (flag) {
			card1Iv.setVisibility(View.VISIBLE);
			card2Iv.setVisibility(View.VISIBLE);
			card3Iv.setVisibility(View.VISIBLE);
			card4Iv.setVisibility(View.VISIBLE);
			card5Iv.setVisibility(View.VISIBLE);
			cardsViewSelf.setVisibility(View.INVISIBLE);
		} else {
			card1Iv.setVisibility(View.INVISIBLE);
			card2Iv.setVisibility(View.INVISIBLE);
			card3Iv.setVisibility(View.INVISIBLE);
			card4Iv.setVisibility(View.INVISIBLE);
			card5Iv.setVisibility(View.INVISIBLE);
			cardsViewSelf.setVisibility(View.VISIBLE);
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "[handleMessage]msg:"+msg);
			String data;
			switch (msg.what) {
			case MSG_OTHER_USER_IN_CB:
				data = (String)msg.obj;
				Log.d(TAG, "[MSG_OTHER_USER_IN_CB]data:"+data);
				showNewOtherUserUI(MainActivity.getInstance().m_players.size()-1);
				invalidate();
				break;
			case MSG_PREPARE_CB:
				{
					prepareBtn.setVisibility(View.INVISIBLE);
					//准备就绪后，需要将cardview恢复背面
					updateSelfCards(true);
					showDividedCardView(true);
					//List<Player> players = MainActivity.getInstance().m_players;
					//for (int i=0;i<players.size()-1;i++) {
					//	otherCardsView[i].setDisplayType(CardsView.TYPE_BACK);
					//}
				}
				//fall through
			case MSG_OTHER_USER_PREPARE_CB:
				{
					data = (String)msg.obj;
					Log.d(TAG, "[MSG_OTHER_USER_PREPARE_CB]data:"+data);
					flagStatus = FLAG_STATUS_PREPARE;
					List<Player> players = MainActivity.getInstance().m_players;
					for (int i=0;i<players.size()-1;i++) {
						otherCardsView[i].setDisplayType(CardsView.TYPE_BACK);
					}
					invalidate();
				}
				break;
			case MSG_WILL_BANKER_CB:
				showControlUI(GameView.STATUS_CONTROL_TRYING_BANKER);
				break;
			case MSG_TRY_BANKER_CB:
				skipBankerBtn.setVisibility(View.INVISIBLE);
				tryBankerBtn.setVisibility(View.INVISIBLE);
				break;
			case MSG_WILL_STAKE_CB:
				{
					bankerIndex = msg.arg1;
					flagStatus = FLAG_STATUS_BANKER_STAKE;
					if (bankerIndex == 0) {
						Log.d(TAG, "[MSG_WILL_STAKE_CB]I am banker, so wait other users to stake");
						toastTv.setVisibility(View.VISIBLE);
					} else {
						showControlUI(GameView.STATUS_CONTROL_STAKE);
					}
					invalidate();
				}
				break;
			case MSG_STAKE_CB:
				x1Btn.setVisibility(View.INVISIBLE);
				x5Btn.setVisibility(View.INVISIBLE);
				x10Btn.setVisibility(View.INVISIBLE);
				invalidate();
				//TODO: 自己的头像部件中显示赌注
				break;
			case MSG_OTHER_USER_STAKE_CB:
				//TODO: 对应的头像部件中显示赌注
				invalidate();
				break;
			case MSG_WILL_START_CB:
				{
					Log.d(TAG, "[MSG_WILL_START_CB]start");
					toastTv.setVisibility(View.INVISIBLE);
					updateSelfCards(false);
					showControlUI(GameView.STATUS_CONTROL_START);
					/*List<Card> cards = MainActivity.getInstance().m_players.get(0).getCards();
					if (cards != null && cards.size() >= 5) {
						Log.d(TAG, "[MSG_WILL_START_CB]->setCardsIndex");
						//cardsViewSelf.setIsDisplayCardBack(false);
						cardsViewSelf.setCardsIndex(cards.get(0).getId(), cards.get(1).getId(), cards.get(2).getId(), 
								cards.get(3).getId(), cards.get(4).getId());
						//showControlUI(GameView.STATUS_CONTROL_START);
					} else {
						Log.d(TAG, "[MSG_WILL_START_CB]cards size:"+cards.size());
					}*/
				}
				break;
			case MSG_PLAY_CB:
				{
					//TODO: 自己的牌型上叠加结果，如“牛九”
					youniuBtn.setVisibility(View.INVISIBLE);
					wuniuBtn.setVisibility(View.INVISIBLE);
					showDividedCardView(false);
					List<Card> cards = MainActivity.getInstance().m_players.get(0).getCards();
					if (cards != null && cards.size() >= 5) {
						String patternStr = DouNiuRule.getResultStr(mContext, MainActivity.getInstance().m_players.get(0).getPokerPattern());
						Log.d(TAG, "[MSG_PLAY_CB]patternStr:"+patternStr);
						cardsViewSelf.setDisplayType(CardsView.TYPE_FACE_PATTERN);
						cardsViewSelf.setPatternStr(patternStr);
						Log.d(TAG, "[MSG_PLAY_CB]->setCardsIndex");
						cardsViewSelf.setCardsIndex(cards.get(0).getId(), cards.get(1).getId(), cards.get(2).getId(), 
								cards.get(3).getId(), cards.get(4).getId());
					} else {
						Log.d(TAG, "[MSG_PLAY_CB]cards size:"+cards.size());
					}
				}
				break;
			case MSG_OTHER_USER_CARD_PATTERN_CB:
				//TODO: 显示对应玩家的牌型正面，并叠加结果，如“牛九”
				{
					int userid = msg.arg1;
					List<Card> cards = null;
					for (int i=1;i<MainActivity.getInstance().m_players.size();i++) {
						if (userid == MainActivity.getInstance().m_players.get(i).getUserid()) {
							cards = MainActivity.getInstance().m_players.get(i).getCards();
							if (cards != null && cards.size() >= 5) {
								Log.d(TAG, "[MSG_OTHER_USER_CARD_PATTERN_CB]->setCardsIndex");
								String patternStr = DouNiuRule.getResultStr(mContext, MainActivity.getInstance().m_players.get(i).getPokerPattern());
								Log.d(TAG, "[MSG_OTHER_USER_CARD_PATTERN_CB]patternStr:"+patternStr);
								otherCardsView[i-1].setDisplayType(CardsView.TYPE_FACE_PATTERN);
								otherCardsView[i-1].setPatternStr(patternStr);
								otherCardsView[i-1].setCardsIndex(cards.get(0).getId(), cards.get(1).getId(), cards.get(2).getId(), 
										cards.get(3).getId(), cards.get(4).getId());
							} else {
								Log.d(TAG, "[MSG_OTHER_USER_CARD_PATTERN_CB]cards size:"+cards.size());
							}
							break;
						}
					}
				}
				break;
			case MSG_GAME_RESULT_CB:
				//TODO: 显示闲家的输赢结果
				{
					data = (String)msg.obj;
					List<Player> players = MainActivity.getInstance().m_players;
					String resultsInfo[] = data.split("#");
					for (int i=0;i<resultsInfo.length;) {
						int userid = Integer.parseInt(resultsInfo[i]);
						int moneyChanged = Integer.parseInt(resultsInfo[i+1]);
						long money = Long.parseLong(resultsInfo[i+2]);
						for (int j=0;j<players.size();j++) {
							if (players.get(j).getUserid() == userid) {
								players.get(j).setMoney(money);
								if (j == 0) {
									TextView moneyTv = (TextView)m_HeadViewSelf.findViewById(R.id.game_head_txt_point);
									moneyTv.setText(""+money);
									cardsViewSelf.setDisplayType(CardsView.TYPE_FACE_PATTERN_RESULT);
									cardsViewSelf.setMoneyChanged(moneyChanged);
								} else {
									TextView moneyTv = (TextView)m_HeadViews[j-1].findViewById(R.id.game_head_txt_point);
									moneyTv.setText(""+money);
									otherCardsView[j-1].setDisplayType(CardsView.TYPE_FACE_PATTERN_RESULT);
									otherCardsView[j-1].setMoneyChanged(moneyChanged);
								}
								break;
							}
						}
						i = i + 3;
					}
					//显示准备按钮，同时隐藏所有用户的准备图标
					showControlUI(STATUS_CONTROL_PREPARE);
					for (int i=0;i<players.size();i++) {
						players.get(i).reset();//
						//players.get(i).setReady(false);
					}
				}
				break;
			default:
				break;
			}
		}

	};

	public void updateSelfCards(boolean isBack) {
		if (isBack) {
			card1Iv.setImageResource(R.drawable.card_big_back);
			card2Iv.setImageResource(R.drawable.card_big_back);
			card3Iv.setImageResource(R.drawable.card_big_back);
			card4Iv.setImageResource(R.drawable.card_big_back);
			card5Iv.setImageResource(R.drawable.card_big_back);
		} else {
			List<Card> cards = MainActivity.getInstance().m_players.get(0).getCards();
			if (cards != null && cards.size() >= 5) {
				for (int i=0;i<5;i++){
					Log.d(TAG, "[updateSelfCards]"+i+": "+cards.get(i).getId());
				}
				card1Iv.setImageResource(R.drawable.card_big_00 + cards.get(0).getId());
				card2Iv.setImageResource(R.drawable.card_big_00 + cards.get(1).getId());
				card3Iv.setImageResource(R.drawable.card_big_00 + cards.get(2).getId());
				card4Iv.setImageResource(R.drawable.card_big_00 + cards.get(3).getId());
				card5Iv.setImageResource(R.drawable.card_big_00 + cards.get(4).getId());
			}
		}
	}
	
	
	/**
	 * 被JNI回调函数调用
	 */
	public void otherLoginCb(String str) {
		Log.v(TAG, "[otherLoginCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_OTHER_USER_IN_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}
	
	public void prepareCb(String str) {
		Log.d(TAG, "[prepareCb]start");
		Message msg =new Message();
		msg.what = MSG_PREPARE_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}
	
	public void otherUserPrepareCb(String str) {
		Log.v(TAG, "[otherUserPrepareCb]start");
		Message msg =new Message();
		msg.what = MSG_OTHER_USER_PREPARE_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}
	
	public void willBankerCb(String str) {
		Log.v(TAG, "[willBankerCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_WILL_BANKER_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}
	
	public void tryBankerCb(String str) {
		Log.v(TAG, "[tryBankerCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_TRY_BANKER_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}

	public void willStakeCb(String str, int bankerIndex) {
		Log.v(TAG, "[willStakeCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_WILL_STAKE_CB;
        msg.obj = str;
        msg.arg1 = bankerIndex;
        mHandler.sendMessage(msg);
	}

	public void stakeCb(String str) {
		Log.v(TAG, "[stakeCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_STAKE_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}

	public void otherUserStakeValueCb(String str) {
		Log.v(TAG, "[otherUserStakeValueCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_OTHER_USER_STAKE_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}

	//显示牌面
	public void willStartCb(String str) {
		Log.v(TAG, "[willStartCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_WILL_START_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}

	public void playCb(String str) {
		Log.v(TAG, "[playCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_PLAY_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}

	public void otherUserCardPatternCb(int userid) {
		Log.v(TAG, "[otherUserCardPatternCb]userid:"+userid);
		Message msg =new Message();
		msg.what = MSG_OTHER_USER_CARD_PATTERN_CB;
        msg.arg1 = userid;
        mHandler.sendMessage(msg);
	}

	public void gameResultCb(String str) {
		Log.v(TAG, "[gameResultCb]str:"+str);
		Message msg =new Message();
		msg.what = MSG_GAME_RESULT_CB;
        msg.obj = str;
        mHandler.sendMessage(msg);
	}
	/**
	 * 
	 */
	

}
