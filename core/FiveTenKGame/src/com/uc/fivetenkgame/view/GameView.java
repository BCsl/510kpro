/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.List;
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
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.common.SharePerferenceCommon;
import com.uc.fivetenkgame.view.EventHandler.IViewInfoAccess;
import com.uc.fivetenkgame.view.entity.AbsButton;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * 游戏界面类
 * 
 * @author chensl@ucweb.com
 * 
 *         上午11:28:20 2014-7-9
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable, IViewControler, IViewInfoAccess {
	private String TAG = "GameView";

	private ScreenSizeHolder mScreenHolder;
	private CardSizeHolder mCardSizeHolder;

	private Context mContext;
	private SurfaceHolder mHolder;
	private Thread mDrawThread;
	private boolean startDraw;
	private AbsDrawer mRightPlayerDarwer, mLeftPlayerDrawer, mMainPlayerDrawer;
	private AbsButton mGiveUpButton, mHistoryButton, mHandCardButton;

	private List<Card> mCardList;
	private int mPlayerId, mRightPlayeId = -1, mLeftPlayerId = -1;
	private String mPlayerName, mRightPlayerName, mLeftPlayerName;
	private boolean isMyTurn;
	private int mGameScore;
	private List<Integer> mCardNumber;
	private List<Integer> mScroeList;
	private SparseArray<List<Card>> mOutList;

	private EventHandler mEventHandler;
	private Handler mHandler;

	private Bitmap mBackgroudBitmap;
	private int mCurrentPlayerId;
	private Timer mTimer;
	private int mTimeRemind;
	private boolean isFirst;
	private boolean myTurn;

	private int mTextSize;
	private float mLeftButtonX;
	private float mRightButtonX;
	private float mButtonY;

	public GameView(Context context, int playerId, Handler handler) {
		super(context);
		this.mContext = context;
		this.mPlayerId = playerId;
		this.mHandler = handler;
		initPlayersInfos(mPlayerId);
		init();
	}
	public void initHolder() {
		mHolder = this.getHolder();
		mHolder.addCallback(this); // 设置Surface生命周期回调
	}

	private void init() {
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
//		mOutList = new HashMap<Integer, List<Card>>();
		mOutList = new SparseArray<List<Card>>();
		mBackgroudBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg);
		mCurrentPlayerId = -1;
		mTimer = new Timer();
		isFirst = true;
	}
	@Override
	public void setGameScore(int score) {
		mGameScore = score;

	}

	@Override
	public void setCardNumber(List<Integer> cardNumberList) {
		mCardNumber = cardNumberList;
		Log.i(TAG, "set CardNumber" + mCardNumber);
	}

	@Override
	public void setScroeList(List<Integer> playersScroeList) {
		mScroeList = playersScroeList;
		Log.i(TAG, "set scoreList" + mScroeList);
	}

	@Override
	public void setPlayersOutList(int number, List<Card> list) {
		if (number == -1) // 设置的是当前Player
		{
			CardUtil.removeCards(mCardList,list);
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
		mEventHandler = new EventHandler(listener, mContext);
	}

	@Override
	public void gameOver() {
		mCurrentPlayerId = -1;
		if (mTimer != null) {
			mTimer.cancel();
		}
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
//		for (Map.Entry<Integer, List<Card>> temp : mOutList.entrySet())
//			temp.getValue().clear();
		mGameScore = 0;

	}

	@Override
	public void setCurrentPlayer(int playerId) {
		mTimeRemind = 30;
		calculateTime();
		mCurrentPlayerId = playerId;
		if (playerId > 0 && mOutList.get(playerId - 1) != null)
			mOutList.get(playerId - 1).clear();
	}
	private void calculateTime(){
		mTimer.cancel();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!((GameApplication) mContext.getApplicationContext())
						.isPause()) {
					mTimeRemind -= 1;
					if (mEventHandler.checkForTimeOut(mTimeRemind))
						mTimer.cancel();
				}
			}
		}, 1000, 1300);
	}

	private void initPlayersInfos(int playerId) {
		SharedPreferences sp = mContext.getSharedPreferences(
				SharePerferenceCommon.TABLE_PLAYERS, mContext.MODE_PRIVATE);
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
		mPlayerName = sp.getString(String.valueOf(playerId),
				String.valueOf(playerId));
		mRightPlayerName = sp.getString(String.valueOf(mRightPlayeId),
				String.valueOf(mRightPlayeId));
		mLeftPlayerName = sp.getString(String.valueOf(mLeftPlayerId),
				String.valueOf(mLeftPlayerId));
	}
	@Override
	public IViewControler getViewControler() {
		return this;
	}
	@Override
	public AbsButton getGiveUpButton() {
		return mGiveUpButton;
	}
	@Override
	public AbsButton getHistoryButton() {
		return mHistoryButton;
	}
	@Override
	public AbsButton getHandCardButton() {
		return mHandCardButton;
	}

	@Override
	public ScreenSizeHolder getScreenSizeHolder() {
		return mScreenHolder;
	}
	@Override
	public CardSizeHolder getCardSizeHolder() {
		return mCardSizeHolder;
	}
	@Override
	public List<Card> getCardList() {
		return mCardList;
	}

	@Override
	public void openHistoryInfo() {
		mHandler.obtainMessage(NetworkCommon.SHOW_HISTORY).sendToTarget();
	}
	@Override
	public boolean isMyturn() {
		return isMyTurn;
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
		mTextSize = mCardSizeHolder.width * 2 /3 ;
		mLeftButtonX = (float) mScreenHolder.width / 2 - 2
				* mCardSizeHolder.width;
		mRightButtonX = (float) mScreenHolder.width / 2 + 2
				* mCardSizeHolder.width;
		mButtonY = (float) mScreenHolder.height - mCardSizeHolder.width * 3;

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
		if (isFirst) {
			initBeforeCreateView();
			isFirst = false;
		}
		if(mTimeRemind>0)
			calculateTime();
		startDraw = true;
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
		if (mTimer != null)
			mTimer.cancel();
		while (reatry) {
			try {
				mDrawThread.join();
				reatry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mDrawThread=null;
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
				drawButton(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
		}

	}

	private void drawButton(Canvas canvas) {
		if (mHistoryButton == null) {
			mHistoryButton = new AbsButton(
					mContext,
					mScreenHolder.width / 2,
					mScreenHolder.height / 4,
					CardUtil.getBitmap(mContext,
							ResourseCommon.BUTTON_HISTORY_NORMAL).getWidth() / 2,
					CardUtil.getBitmap(mContext,
							ResourseCommon.BUTTON_HISTORY_NORMAL).getHeight() / 2) {
				@Override
				public void drawButtonOnPressedState(Canvas canvas, float x,
						float y) {
					draw(canvas, CardUtil.getBitmap(mContext,
							ResourseCommon.BUTTON_HISTORY_PRESSED));
				}

				@Override
				public void drawButtonOnNormalState(Canvas canvas, float x,
						float y) {
					draw(canvas, CardUtil.getBitmap(mContext,
							ResourseCommon.BUTTON_HISTORY_NORMAL));
				}
			};
		} // end if mHistoryButton
		mHistoryButton.doDraw(canvas);
		if (isMyTurn) {
			if (mGiveUpButton == null) {
				mGiveUpButton = new AbsButton(
						mContext,
						mRightButtonX,
						mButtonY,
						CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_GIVEUP_NORMAL).getWidth() / 2,
						CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_GIVEUP_NORMAL)
								.getHeight() / 2) {
					@Override
					public void drawButtonOnPressedState(Canvas canvas,
							float x, float y) {
						draw(canvas, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_GIVEUP_PRESSED));
					}
					@Override
					public void drawButtonOnNormalState(Canvas canvas, float x,
							float y) {
						draw(canvas, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_GIVEUP_NORMAL));
					}
				};
			}// end if mGiveUpButton
			if (mHandCardButton == null) {
				mHandCardButton = new AbsButton(mContext, mLeftButtonX,
						mButtonY, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_HANDCARD_NORMAL)
								.getWidth() / 2, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_HANDCARD_NORMAL)
								.getHeight() / 2) {
					@Override
					public void drawButtonOnPressedState(Canvas canvas,
							float x, float y) {
						draw(canvas, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_HANDCARD_PRESSED));
					}

					@Override
					public void drawButtonOnNormalState(Canvas canvas, float x,
							float y) {
						draw(canvas, CardUtil.getBitmap(mContext,
								ResourseCommon.BUTTON_HANDCARD_NORMAL));
					}
				};
			}// end if mHandCardButton
			mHandCardButton.doDraw(canvas);
			mGiveUpButton.doDraw(canvas);
		}// end if myTurn
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
		paint.setTextSize(mTextSize);
		paint.setColor(Color.rgb(255, 184, 15));
		canvas.drawText(mContext.getResources().getString(R.string.game_score)
				+ mGameScore, mScreenHolder.width / 2 - 3 * mTextSize,
				mTextSize, paint);
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
	private void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mEventHandler == null)
			throw new IllegalArgumentException(
					"EventHandler should not be null!!!");
		mEventHandler.handleTouchEvent(event, this);
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
