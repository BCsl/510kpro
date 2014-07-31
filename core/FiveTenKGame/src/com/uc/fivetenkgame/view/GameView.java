/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import my.example.fivetenkgame.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SharePreferenceCommon;
import com.uc.fivetenkgame.view.entity.ButtonHistory;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 游戏界面类
 * 
 * @author chensl@ucweb.com
 * 
 *         上午11:28:20 2014-7-9
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable, IViewControler {
	private String TAG = "GameView";

	private ScreenSizeHolder mScreenHolder;
	private CardSizeHolder mCardSizeHolder;

	private Context mContext;
	private SurfaceHolder mHolder;
	private Thread mDrawThread;
	private boolean startDraw;
	private AbsDrawer mRightPlayerDarwer, mLeftPlayerDrawer, mMainPlayerDrawer;

	private List<Card> mCardList;
	private int mPlayerId, mRightPlayeId = -1, mLeftPlayerId = -1;
	private String mPlayerName, mRightPlayerName, mLeftPlayerName;
	private boolean isMyTurn;
	private int mGameScore;
	private List<Integer> mCardNumber;
	private List<Integer> mScroeList;
	private Map<Integer, List<Card>> mOutList;

	private EventListener mEventListener;
	private EventHandler mEventHandler;
	private Handler mHandler;
	
	private Bitmap mBackgroudBitmap;
	private int mCurrentPlayerId;
	private Timer mtimer;
	private int mTimeRemind;
	private int TEXT_SIZE_BIG;
	private boolean isFirst;
	private boolean  myTurn;
	

	public GameView(Context context, int playerId ,Handler handler) {
		super(context);
		this.mContext = context;
		this.mPlayerId = playerId;
		this.mHandler=handler;
		initPlayersInfos(mPlayerId);
		init();
	}

	public ScreenSizeHolder getScreenHolder() {
		return mScreenHolder;
	}

	public CardSizeHolder getCardSizeHolder() {
		return mCardSizeHolder;
	}

	public void initHolder() {
		mHolder = this.getHolder();
		mHolder.addCallback(this); // 设置Surface生命周期回调
	}

	@Override
	public void setGameScore(int score) {
		mGameScore = score;

	}

	@Override
	public void setCardNumber(List<Integer> cardNumberList) {
		mCardNumber = cardNumberList;
		Log.i(TAG, "set CardNumber"+mCardNumber);
	}

	@Override
	public void setScroeList(List<Integer> playersScroeList) {
		mScroeList = playersScroeList;
		Log.i(TAG, "set scoreList"+mScroeList);
	}

	@Override
	public void setPlayersOutList(int number, List<Card> list) {
		if (number == -1) // 设置的是当前Player
		{
			mCardList.removeAll(list);
			mOutList.put(mPlayerId - 1, list);
		} else
			mOutList.put(number - 1, list);
	}

	@Override
	public void setCards(List<Card> cards) {
		mCardList.clear();
		mCardList.addAll(cards);
	}

	@Override
	public void setMyTurn(boolean flag) {
		isMyTurn = flag;
	}

	@Override
	public void setEventListener(EventListener listener) {
		mEventListener = listener;
		mEventHandler = new EventHandler(mEventListener, mContext);
	}

	@Override
	public void gameOver(int playId) {
	}

	@Override
	public void handCardFailed() {
		Toast.makeText(
				mContext,
				mContext.getResources().getString(R.string.please_base_on_rule),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void roundOver() {
		for (Map.Entry<Integer, List<Card>> temp : mOutList.entrySet())
			temp.getValue().clear();
		mGameScore = 0;

	}

	@Override
	public void setCurrentPlayer(int playerId) {
		mTimeRemind = 30;
		mtimer.cancel();
		mtimer = new Timer();
		mtimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!((GameApplication) mContext.getApplicationContext())
						.isPause()) {
					mTimeRemind -= 1;
					if (mEventHandler.checkForTimeOut(mTimeRemind))
						mtimer.cancel();
				}
			}
		}, 1000, 1300);
		mCurrentPlayerId = playerId;
		if (mOutList.get(playerId - 1) != null)
			mOutList.get(playerId - 1).clear();
	}

	private void init() {
		startDraw = true;
		isMyTurn = false;
		mGameScore = 0;
		mCardNumber = new Vector<Integer>();
		mCardNumber.add(0);
		mCardNumber.add(0);
		mCardNumber.add(0);

		mScroeList = new Vector<Integer>();
		mScroeList.add(0);
		mScroeList.add(0);
		mScroeList.add(0);
		mCardList = new Vector<Card>();
		mOutList = new HashMap<Integer, List<Card>>();
		mBackgroudBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg);
		mCurrentPlayerId = -1;
		mtimer = new Timer();
		isFirst = true;
	}

	private void initPlayersInfos(int playerId) {
		SharedPreferences sp = mContext.getSharedPreferences(
				SharePreferenceCommon.TABLE_PLAYERS, mContext.MODE_PRIVATE);
		switch (playerId) {
		case 1:
			mRightPlayeId = 2;
			mLeftPlayerId = 3;
			break;
		case 2:
			mRightPlayeId = 3;
			mLeftPlayerId = 1;
			break;
		case 3:
			mRightPlayeId = 1;
			mLeftPlayerId = 2;
			break;
		}
		mPlayerName = sp.getString(String.valueOf(playerId), mContext
				.getResources().getString(R.string.player) + playerId);
		mRightPlayerName = sp.getString(String.valueOf(mRightPlayeId), mContext
				.getResources().getString(R.string.player) + mRightPlayeId);
		mLeftPlayerName = sp.getString(String.valueOf(mLeftPlayerId), mContext
				.getResources().getString(R.string.player) + mLeftPlayerId);
	}

	public IViewControler getViewControler() {
		return this;
	}

	// 屏幕尺寸的获取需要在surfaceView创建之后才能获取
	private void initBeforeCreateView() {
		mScreenHolder = new ScreenSizeHolder();
		mScreenHolder.width = getWidth();
		mScreenHolder.height = getHeight();

		mCardSizeHolder = new CardSizeHolder();
		Bitmap temp = BitmapFactory.decodeResource(getResources(),
				R.drawable.cardbg1);
		mCardSizeHolder.height = temp.getHeight();
		mCardSizeHolder.width = temp.getWidth();
		TEXT_SIZE_BIG = mCardSizeHolder.width * 3 / 4;
		mRightPlayerDarwer = new RightPlayerDrawer(mContext, mScreenHolder,
				mCardSizeHolder);
		mLeftPlayerDrawer = new LeftPlayerDrawer(mContext, mScreenHolder,
				mCardSizeHolder);
		mMainPlayerDrawer = new MainPlayerDrawer(mContext, mScreenHolder,
				mCardSizeHolder);
		Log.i(TAG, mScreenHolder.toString());
		Log.i(TAG, mCardSizeHolder.toString());
		temp.recycle();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, " surfaceCreated");
		// 开始绘图进程
		// if (mDrawThread == null) {
		// initBeforeCreateView();
		// mDrawThread = new Thread(this);
		// mDrawThread.start();
		// }
		if (isFirst) {
			initBeforeCreateView();
			isFirst = false;
		}
		mDrawThread = new Thread(this);
		mDrawThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, " surfaceDestroyed");
		boolean reatry = true;
		startDraw = false;
		if (mtimer != null)
			mtimer.cancel();
		while (reatry) {
			try {
				mDrawThread.join();
				reatry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isMyTurn() {
		return isMyTurn;
	}

	// 重绘
	private void doDraw(Paint paint) {
		Canvas canvas = null;
		synchronized (mHolder) {
			try {
				canvas = mHolder.lockCanvas();
				// if(frameCount++<4)
				drawBackground(canvas);
				if (!isMyTurn)
					drawMeFirst(canvas, paint);
				else
					drawOtherFirst(canvas, paint);

				drawGameScore(canvas, paint);
				drawHositoryButton(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
		}

	}

	private void drawHositoryButton(Canvas canvas) {
		ButtonHistory.getInstance(mContext, canvas, mScreenHolder.width/2, mScreenHolder.height/2).doDraw();
		
	}

	private void drawMeFirst(Canvas canvas, Paint paint) {
		drawMainPlayer(canvas, paint);
		drawLeftPlayer(canvas, paint);
		drawRightPlayer(canvas, paint);
	}

	private void drawOtherFirst(Canvas canvas, Paint paint) {
		drawLeftPlayer(canvas, paint);
		drawRightPlayer(canvas, paint);
		drawMainPlayer(canvas, paint);
	}

	private void drawGameScore(Canvas canvas, Paint paint) {
		paint.setTextSize(TEXT_SIZE_BIG);
		paint.setColor(Color.rgb(255, 184, 15));
		canvas.drawText(mContext.getResources().getString(R.string.game_score)
				+ mGameScore, mScreenHolder.width / 2 - 3 * TEXT_SIZE_BIG,
				TEXT_SIZE_BIG, paint);
	}

	private void drawRightPlayer(Canvas canvas, Paint paint) {
		mRightPlayerDarwer.initCanvas(canvas);
		 myTurn = (mCurrentPlayerId == mRightPlayeId && !isMyTurn) ? true
				: false;
		((RightPlayerDrawer) mRightPlayerDarwer).doDraw(paint,
				mRightPlayerName, myTurn, mTimeRemind,
				mCardNumber.get(mRightPlayeId - 1),
				mScroeList.get(mRightPlayeId - 1),
				mOutList.get(mRightPlayeId - 1));

	}

	private void drawLeftPlayer(Canvas canvas, Paint paint) {
		mLeftPlayerDrawer.initCanvas(canvas);
		myTurn = (mCurrentPlayerId == mLeftPlayerId && !isMyTurn) ? true
				: false;
		((LeftPlayerDrawer) mLeftPlayerDrawer).doDraw(paint, mLeftPlayerName,
				myTurn, mTimeRemind, mCardNumber.get(mLeftPlayerId - 1),
				mScroeList.get(mLeftPlayerId - 1),
				mOutList.get(mLeftPlayerId - 1));
	}

	private void drawMainPlayer(Canvas canvas, Paint paint) {
		mMainPlayerDrawer.initCanvas(canvas);
		myTurn = isMyTurn ? true : false;
		((MainPlayerDrawer) mMainPlayerDrawer).doDraw(paint, myTurn,
				mTimeRemind, mPlayerName, mCardNumber.get(mPlayerId - 1),
				mScroeList.get(mPlayerId - 1), mCardList,
				mOutList.get(mPlayerId - 1));
	}

	// 画背景
	private void drawBackground(Canvas canvas) {
		Rect src = new Rect(0, 0, mBackgroudBitmap.getWidth() * 3 / 4,
				mBackgroudBitmap.getHeight() * 2 / 3);
		Rect dst = new Rect(0, 0, mScreenHolder.width, mScreenHolder.height);
		canvas.drawBitmap(mBackgroudBitmap, src, dst, null);
	};

	public void run() {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		while (startDraw) {
			doDraw(paint);
			Sleep(33);
		}
	}
	public void openHistoryDialog(){
		mHandler.obtainMessage(NetworkCommon.SHOW_HISTORY).sendToTarget();
		
	}

	private void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if (!isMyTrun)
		// return super.onTouchEvent(event);
		if (mEventHandler == null)
			throw new IllegalArgumentException(
					"EventHandler should not be null!!!");
		mEventHandler.handleTouchEvent(event, this, mCardList);
		return super.onTouchEvent(event);
	}

	public class ScreenSizeHolder {
		public int width;
		public int height;

		@Override
		public String toString() {
			return "ScreenWidth=" + width + ",ScreenHeight=" + height;
		}
	}

	public class CardSizeHolder {
		public int width;
		public int height;

		@Override
		public String toString() {
			return "CardWidth=" + width + ",CardHeight=" + height;
		}
	}
}
