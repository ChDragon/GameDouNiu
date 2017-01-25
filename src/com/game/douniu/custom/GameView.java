package com.game.douniu.custom;


import java.util.List;

import com.game.douniu.OnlinePlayActivity;
import com.game.douniu.R;
import com.game.douniu.utils.AudioPlayUtils;
import com.game.douniu.utils.UserClock;

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
	
	/** ��Ϸ���� **/
	OnlinePlayActivity onlinePlayActivity;
	private static int MAX_USERS = 6;
	
	View selfLayout;
	ImageView card1Iv;
	ImageView card2Iv;
	ImageView card3Iv;
	ImageView card4Iv;
	ImageView card5Iv;
	
	// ��ǰ�û������˿���ͼ�������ͼ�ĵ�һ������arrayXXX[0]
	//CardsView cardsViewSelf;
	//CardsView[] otherCardsView = new CardsView[MAX_USERS - 1];
	CardsView[] arrayCardsView = new CardsView[MAX_USERS];
	
	//View m_HeadViewSelf;
	//View[] m_HeadViews = new View[MAX_USERS - 1];
	View[] arrayHeadView = new View[MAX_USERS];
	
	
	
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
	Bitmap clockBitmap;
	Bitmap timeNumBitmap;
	TextView toastTv;
	
	
	Point[]	m_ptUserHead		= new Point[MAX_USERS];// 0: self
	Point[]	m_ptUserCardView	= new Point[MAX_USERS];// 0: self
	Point[]	m_ptUserReady		= new Point[MAX_USERS];
	Point[]	m_ptUserClock		= new Point[MAX_USERS];
	
	public final static int FLAG_STATUS_PREPARE 		= 1;
	public final static int FLAG_STATUS_BANKER_STAKE 	= 2;
	private int flagStatus = FLAG_STATUS_PREPARE;
	
	private int bankerIndex = -1;
	
	@SuppressWarnings("unused")
	public GameView(OnlinePlayActivity activity, Context context) {
		super(context);
		onlinePlayActivity = activity;
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
		clockBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock);
		timeNumBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.timenum);
		
		for (int i = 0; i < MAX_USERS; i++) {
			m_ptUserHead[i] = new Point();
			m_ptUserCardView[i] = new Point();
			m_ptUserReady[i] = new Point();
			m_ptUserClock[i] = new Point();
		}
		
		setBackground(mContext.getResources().getDrawable(R.drawable.bg));
		
		//1) ͷ��ؼ���ʼ��
		arrayHeadView[0] = inflate(context, R.layout.game_head, null);
		arrayHeadView[0].setOnClickListener(this);
		arrayHeadView[0].setTag(IDC_USER_HEAD_SELF);
		addView(arrayHeadView[0]);
		TextView usernameTv = (TextView)arrayHeadView[0].findViewById(R.id.game_head_txt_name);
		usernameTv.setText(OnlinePlayActivity.getInstance().m_players.get(0).getUsername());
		TextView moneyTv = (TextView)arrayHeadView[0].findViewById(R.id.game_head_txt_point);
		moneyTv.setText(""+OnlinePlayActivity.getInstance().m_players.get(0).getMoney());
		
		for (int i=1;i<arrayHeadView.length;i++) {
			arrayHeadView[i] = inflate(context, R.layout.game_head, null);
			arrayHeadView[i].setOnClickListener(this);
			arrayHeadView[i].setTag(IDC_USER_HEAD_1 + i);
			addView(arrayHeadView[i]);
		}
		
		//2) 5���Ƶ���ͼ��ʼ��
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
		
		
		//������ҵ�����ͼ��ʼ��
		arrayCardsView[0] = new CardsView(context, CardRes.FLAG_BITMAP_ORIGINAL);
		//cardsViewSelf.setCardsIndex(39,40,41,42,43);
		addView(arrayCardsView[0]);
		for (int i=1;i<arrayCardsView.length;i++) {
			arrayCardsView[i] = new CardsView(context);
			addView(arrayCardsView[i]);
		}
		
		
		//��ť��ʼ��
		prepareBtn = new Button(context);
		prepareBtn.setText("׼��");
		prepareBtn.setOnClickListener(this);
		prepareBtn.setTag(IDC_GAME_PREPARE);
		addView(prepareBtn);
		youniuBtn = new Button(context);
		youniuBtn.setText("��ţ");
		youniuBtn.setOnClickListener(this);
		youniuBtn.setTag(IDC_GAME_YOUNIU);
		addView(youniuBtn);
		wuniuBtn = new Button(context);
		wuniuBtn.setText("��ţ");
		wuniuBtn.setOnClickListener(this);
		wuniuBtn.setTag(IDC_GAME_WUNIU);
		addView(wuniuBtn);
		
		skipBankerBtn = new Button(context);
		skipBankerBtn.setText("����");
		skipBankerBtn.setOnClickListener(this);
		skipBankerBtn.setTag(IDC_BANKER_SKIP);
		addView(skipBankerBtn);
		tryBankerBtn = new Button(context);
		tryBankerBtn.setText("��ׯ");
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
		showOtherUserUI(OnlinePlayActivity.getInstance().m_players.size());
		showDividedCardView(true);
	}

	/**��������
	 * 					��head��
	 * 					    ���2
	 * 
	 *		       ��head�����1		���3��head��
	 * 
	 * ��head�����4						���5	��head��
	 * 
	 * ��head��    			�������0
	 * 
	 * @param w
	 * @param h
	 */
	protected void rectifyControl(int w, int h) {
		//Log.d(TAG, "[rectifyControl]start");
		m_ptUserHead[0].set(2, h - 2 - arrayHeadView[0].getMeasuredHeight());
		m_ptUserHead[1].set(2 + w / 4, h / 14);
		m_ptUserHead[2].set(w / 2 - arrayHeadView[2].getMeasuredWidth() / 2, 0);
		m_ptUserHead[3].set(w - 2 - w / 4 - arrayHeadView[3].getMeasuredWidth(), h / 14);
		m_ptUserHead[4].set(2, h / 2 - arrayHeadView[4].getMeasuredHeight() / 2 + 30);
		m_ptUserHead[5].set(w - 2 - arrayHeadView[5].getMeasuredWidth(), h / 2 - arrayHeadView[5].getMeasuredHeight() / 2 + 30);
		
		m_ptUserCardView[0].set(w / 2 - arrayCardsView[0].getRealWidth() / 2, h - 10 - arrayCardsView[0].getRealHeight());
		m_ptUserCardView[1].set(m_ptUserHead[1].x, m_ptUserHead[1].y + arrayHeadView[1].getMeasuredHeight());
		m_ptUserCardView[2].set(w / 2 - arrayCardsView[2].getRealWidth() / 2, m_ptUserHead[2].y + arrayHeadView[2].getMeasuredHeight());
		m_ptUserCardView[3].set(w - 2 - w / 4 - arrayCardsView[3].getRealWidth(), m_ptUserCardView[1].y);
		m_ptUserCardView[4].set(m_ptUserHead[4].x + arrayHeadView[4].getMeasuredWidth(), h / 2 - arrayCardsView[4].getRealHeight() / 2 + 50);
		m_ptUserCardView[5].set(m_ptUserHead[5].x - arrayCardsView[5].getRealWidth(), m_ptUserCardView[4].y);
		
		m_ptUserReady[0].set(m_ptUserHead[0].x + arrayHeadView[0].getMeasuredWidth(), m_ptUserHead[0].y);
		m_ptUserReady[1].set(m_ptUserHead[1].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[1].y);
		m_ptUserReady[2].set(m_ptUserHead[2].x + arrayHeadView[2].getMeasuredWidth(), m_ptUserHead[2].y);
		m_ptUserReady[3].set(m_ptUserHead[3].x + arrayHeadView[3].getMeasuredWidth(), m_ptUserHead[3].y);
		m_ptUserReady[4].set(m_ptUserHead[4].x + arrayHeadView[4].getMeasuredWidth(), m_ptUserHead[4].y);
		m_ptUserReady[5].set(m_ptUserHead[5].x + arrayHeadView[5].getMeasuredWidth(), m_ptUserHead[5].y);
		
		m_ptUserClock[0].set(m_ptUserHead[0].x + arrayHeadView[0].getMeasuredWidth(), m_ptUserHead[0].y + bankerBitmap.getHeight());
		m_ptUserClock[1].set(m_ptUserHead[1].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[1].y);
		m_ptUserClock[2].set(m_ptUserHead[2].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[2].y);
		m_ptUserClock[3].set(m_ptUserHead[3].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[3].y);
		m_ptUserClock[4].set(m_ptUserHead[4].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[4].y);
		m_ptUserClock[5].set(m_ptUserHead[5].x + arrayHeadView[1].getMeasuredWidth(), m_ptUserHead[5].y);
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
		
		//����Ϊ����
		int headSelfBtw = arrayHeadView[0].getMeasuredWidth();
		int headSelfBth = arrayHeadView[0].getMeasuredHeight();
		//Log.d(TAG, "[onLayout]m_HeadViewSelf btw:"+headSelfBtw+",bth:"+headSelfBth);
		arrayHeadView[0].layout(m_ptUserHead[0].x, m_ptUserHead[0].y, m_ptUserHead[0].x + headSelfBtw, m_ptUserHead[0].y + headSelfBth);
		
		//cardsViewSelf������
		btw = arrayCardsView[0].getRealWidth();
		bth = arrayCardsView[0].getRealHeight();
		//Log.d(TAG, "[onLayout]cardsViewSelf btw:"+btw+",bth:"+bth);
		arrayCardsView[0].layout(m_ptUserCardView[0].x, m_ptUserCardView[0].y, m_ptUserCardView[0].x + btw, m_ptUserCardView[0].y + bth);

		
		for (int i=1;i<MAX_USERS;i++) {
			int headBtw = arrayHeadView[i].getMeasuredWidth();
			int headBth = arrayHeadView[i].getMeasuredHeight();
			arrayHeadView[i].layout(m_ptUserHead[i].x, m_ptUserHead[i].y, m_ptUserHead[i].x + headBtw, m_ptUserHead[i].y + headBth);
			
			btw = arrayCardsView[i].getRealWidth();
			bth = arrayCardsView[i].getRealHeight();
			arrayCardsView[i].layout(m_ptUserCardView[i].x, m_ptUserCardView[i].y, m_ptUserCardView[i].x + btw, m_ptUserCardView[i].y + bth);
		}
		
		
		//����
		btw = selfLayout.getMeasuredWidth();
		bth = selfLayout.getMeasuredHeight();
		//Log.d(TAG, "[onLayout]selfLayout btw:"+btw+",bth:"+bth);
		int bthSelf = bth;
		selfLayout.layout(w / 2 - btw / 2, h - 2 - bth, w / 2 + btw / 2, h - 2);

		
		//׼����ť������
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
		
	
		//��������ׯ��ť��selfLayout֮�Ͼ���
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
			AudioPlayUtils.getInstance(mContext).play(R.raw.click);
			switch (tag) {
				case IDC_GAME_PREPARE:
					Log.v(TAG, "[onClick]IDC_GAME_PREPARE");
					OnlinePlayActivity.getInstance().prepareAction();
					break;
				case IDC_GAME_YOUNIU:
					Log.v(TAG, "[onClick]IDC_GAME_YOUNIU");
					OnlinePlayActivity.getInstance().startAction(true);
					break;
				case IDC_GAME_WUNIU:
					Log.v(TAG, "[onClick]IDC_GAME_WUNIU");
					OnlinePlayActivity.getInstance().startAction(false);
					break;
				case IDC_BANKER_TRY:
					Log.v(TAG, "[onClick]IDC_BANKER_TRY");
					OnlinePlayActivity.getInstance().tryingBankerAction(true);
					break;
				case IDC_BANKER_SKIP:
					Log.v(TAG, "[onClick]IDC_BANKER_SKIP");
					OnlinePlayActivity.getInstance().tryingBankerAction(false);
					break;
				case IDC_STAKE_X1:
					Log.v(TAG, "[onClick]IDC_STAKE_X1");
					OnlinePlayActivity.getInstance().stakeAction(Constant.VALUE_STAKE_LEVEL_1);
					break;
				case IDC_STAKE_X5:
					Log.v(TAG, "[onClick]IDC_STAKE_X5");
					OnlinePlayActivity.getInstance().stakeAction(Constant.VALUE_STAKE_LEVEL_2);
					break;
				case IDC_STAKE_X10:
					Log.v(TAG, "[onClick]IDC_STAKE_X10");
					OnlinePlayActivity.getInstance().stakeAction(Constant.VALUE_STAKE_LEVEL_3);
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
		int times = UserClock.getGameClock(0);
		if (times > 0) {
			Log.d(TAG, "[onDraw]times:"+times);
			onDrawUserClock(canvas, times);
		}
	}
	
	/** ׼��ͼ����� **/
	private void onDrawFlag(Canvas canvas) {
		int size = (OnlinePlayActivity.getInstance().m_players.size() < MAX_USERS) ? OnlinePlayActivity.getInstance().m_players.size() : MAX_USERS;
		for (int i=0;i<size;i++) {
			if (flagStatus == FLAG_STATUS_PREPARE) {
				final boolean isReady = OnlinePlayActivity.getInstance().m_players.get(i).isReady();
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
					final boolean isStake = OnlinePlayActivity.getInstance().m_players.get(i).isStake();
					Log.d(TAG, "[onDrawFlag]isStake:"+isStake);
					if (isStake) {
						final int stake = OnlinePlayActivity.getInstance().m_players.get(i).getStake();
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
	
	/** ʱ�ӻ��� **/
	private void onDrawUserClock(Canvas canvas, int clock) {
		canvas.save();
		canvas.drawBitmap(clockBitmap, m_ptUserClock[0].x, m_ptUserClock[0].y, mPaint);
		canvas.restore();
		
		int time = clock % 100;
		int Ten = time / 10;
		int Unit = time % 10;
		int w = timeNumBitmap.getWidth() / 10;
		int h = timeNumBitmap.getHeight();
		DrawImage(canvas, m_ptUserClock[0].x + clockBitmap.getWidth() / 2 - w, 	m_ptUserClock[0].y + clockBitmap.getHeight() / 2 - h / 2, w, h, Ten * w, 0, timeNumBitmap);
		DrawImage(canvas, m_ptUserClock[0].x + clockBitmap.getWidth() / 2, 		m_ptUserClock[0].y + clockBitmap.getHeight() / 2 - h / 2, w, h, Unit * w, 0, timeNumBitmap);
	}
	
	/** ʱ�Ӹ��� **/
	public void onUpdateClock(int viewid) {
		postInvalidate();
	}
	
	/**
	 * �������� ����
	 */
	public void DrawImage(Canvas canvas, int desx, int desy, int w, int h, int srcx, int srcy, Bitmap bitmap) {
		if (bitmap == null)
			return;
		canvas.save();
		canvas.clipRect(desx, desy, desx + w, desy + h);
		canvas.drawBitmap(bitmap, desx - srcx, desy - srcy, mPaint);
		canvas.restore();
	}

	public void showOtherUserUI(int userCount) {
		int size = (OnlinePlayActivity.getInstance().m_players.size() < MAX_USERS) ? OnlinePlayActivity.getInstance().m_players.size() : MAX_USERS;
		for (int i=1;i<MAX_USERS;i++) {
			if (i<=size-1) {
				arrayHeadView[i].setVisibility(View.VISIBLE);
				arrayCardsView[i].setVisibility(View.VISIBLE);
				TextView usernameTv = (TextView)arrayHeadView[i].findViewById(R.id.game_head_txt_name);
				usernameTv.setText(OnlinePlayActivity.getInstance().m_players.get(i).getUsername());
				TextView moneyTv = (TextView)arrayHeadView[i].findViewById(R.id.game_head_txt_point);
				moneyTv.setText(""+OnlinePlayActivity.getInstance().m_players.get(i).getMoney());
			} else {
				//arrayHeadView[i].setVisibility(View.INVISIBLE);
				//arrayCardsView[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public void showNewOtherUserUI(int playerIndex) {
		Log.w(TAG, "[showNewOtherUserUI]playerIndex:"+playerIndex);
		List<Player> players = OnlinePlayActivity.getInstance().m_players;
		Log.w(TAG, "[showNewOtherUserUI]size:"+players.size());
		for (int i=1;i<players.size();i++){
			Log.w(TAG, "[showNewOtherUserUI]i:"+i+" "+players.get(i));
		}
		if (playerIndex >= OnlinePlayActivity.getInstance().m_players.size() || playerIndex <= 0) {
			Log.w(TAG, "[showNewOtherUserUI]playerIndex too big or too small");
			return;
		}
		arrayHeadView[playerIndex].setVisibility(View.VISIBLE);
		arrayCardsView[playerIndex].setVisibility(View.VISIBLE);
		TextView usernameTv = (TextView)arrayHeadView[playerIndex].findViewById(R.id.game_head_txt_name);
		usernameTv.setText(OnlinePlayActivity.getInstance().m_players.get(playerIndex).getUsername());
		TextView moneyTv = (TextView)arrayHeadView[playerIndex].findViewById(R.id.game_head_txt_point);
		moneyTv.setText(""+OnlinePlayActivity.getInstance().m_players.get(playerIndex).getMoney());
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
			arrayCardsView[0].setVisibility(View.INVISIBLE);
		} else {
			card1Iv.setVisibility(View.INVISIBLE);
			card2Iv.setVisibility(View.INVISIBLE);
			card3Iv.setVisibility(View.INVISIBLE);
			card4Iv.setVisibility(View.INVISIBLE);
			card5Iv.setVisibility(View.INVISIBLE);
			arrayCardsView[0].setVisibility(View.VISIBLE);
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
				showNewOtherUserUI(OnlinePlayActivity.getInstance().m_players.size()-1);
				invalidate();
				break;
			case MSG_PREPARE_CB:
				{
					prepareBtn.setVisibility(View.INVISIBLE);
					//׼����������Ҫ��cardview�ָ�����
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
					List<Player> players = OnlinePlayActivity.getInstance().m_players;
					for (int i=1;i<players.size();i++) {
						arrayCardsView[i].setDisplayType(CardsView.TYPE_BACK);
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
				//TODO: �Լ���ͷ�񲿼�����ʾ��ע
				break;
			case MSG_OTHER_USER_STAKE_CB:
				//TODO: ��Ӧ��ͷ�񲿼�����ʾ��ע
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
					//TODO: �Լ��������ϵ��ӽ�����硰ţ�š�
					youniuBtn.setVisibility(View.INVISIBLE);
					wuniuBtn.setVisibility(View.INVISIBLE);
					showDividedCardView(false);
					List<Card> cards = OnlinePlayActivity.getInstance().m_players.get(0).getCards();
					if (cards != null && cards.size() >= 5) {
						String patternStr = DouNiuRule.getResultStr(mContext, OnlinePlayActivity.getInstance().m_players.get(0).getPokerPattern());
						Log.d(TAG, "[MSG_PLAY_CB]patternStr:"+patternStr);
						arrayCardsView[0].setDisplayType(CardsView.TYPE_FACE_PATTERN);
						arrayCardsView[0].setPatternStr(patternStr);
						Log.d(TAG, "[MSG_PLAY_CB]->setCardsIndex");
						arrayCardsView[0].setCardsIndex(cards.get(0).getId(), cards.get(1).getId(), cards.get(2).getId(), 
								cards.get(3).getId(), cards.get(4).getId());
					} else {
						Log.d(TAG, "[MSG_PLAY_CB]cards size:"+cards.size());
					}
				}
				break;
			case MSG_OTHER_USER_CARD_PATTERN_CB:
				//TODO: ��ʾ��Ӧ��ҵ��������棬�����ӽ�����硰ţ�š�
				{
					int userid = msg.arg1;
					List<Card> cards = null;
					for (int i=1;i<OnlinePlayActivity.getInstance().m_players.size();i++) {
						if (userid == OnlinePlayActivity.getInstance().m_players.get(i).getUserid()) {
							cards = OnlinePlayActivity.getInstance().m_players.get(i).getCards();
							if (cards != null && cards.size() >= 5) {
								Log.d(TAG, "[MSG_OTHER_USER_CARD_PATTERN_CB]->setCardsIndex");
								String patternStr = DouNiuRule.getResultStr(mContext, OnlinePlayActivity.getInstance().m_players.get(i).getPokerPattern());
								Log.d(TAG, "[MSG_OTHER_USER_CARD_PATTERN_CB]patternStr:"+patternStr);
								arrayCardsView[i].setDisplayType(CardsView.TYPE_FACE_PATTERN);
								arrayCardsView[i].setPatternStr(patternStr);
								arrayCardsView[i].setCardsIndex(cards.get(0).getId(), cards.get(1).getId(), cards.get(2).getId(), 
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
				//TODO: ��ʾ�мҵ���Ӯ���
				{
					data = (String)msg.obj;
					List<Player> players = OnlinePlayActivity.getInstance().m_players;
					String resultsInfo[] = data.split("#");
					for (int i=0;i<resultsInfo.length;) {
						int userid = Integer.parseInt(resultsInfo[i]);
						int moneyChanged = Integer.parseInt(resultsInfo[i+1]);
						long money = Long.parseLong(resultsInfo[i+2]);
						for (int j=0;j<players.size();j++) {
							if (players.get(j).getUserid() == userid) {
								players.get(j).setMoney(money);
								if (j == 0) {
									TextView moneyTv = (TextView)arrayHeadView[0].findViewById(R.id.game_head_txt_point);
									moneyTv.setText(""+money);
									arrayCardsView[0].setDisplayType(CardsView.TYPE_FACE_PATTERN_RESULT);
									arrayCardsView[0].setMoneyChanged(moneyChanged);
								} else {
									TextView moneyTv = (TextView)arrayHeadView[j].findViewById(R.id.game_head_txt_point);
									moneyTv.setText(""+money);
									arrayCardsView[j].setDisplayType(CardsView.TYPE_FACE_PATTERN_RESULT);
									arrayCardsView[j].setMoneyChanged(moneyChanged);
								}
								break;
							}
						}
						i = i + 3;
					}
					//��ʾ׼����ť��ͬʱ���������û���׼��ͼ��
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
			List<Card> cards = OnlinePlayActivity.getInstance().m_players.get(0).getCards();
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
	 * ��JNI�ص���������
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

	//��ʾ����
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
