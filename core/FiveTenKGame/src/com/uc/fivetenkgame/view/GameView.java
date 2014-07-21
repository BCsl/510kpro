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
import java.util.Vector;

import my.example.fivetenkgame.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;
import com.uc.fivetenkgame.view.util.IViewControler;
import com.uc.fivetenkgame.view.util.MainPlayerInfoDrawer;
import com.uc.fivetenkgame.view.util.OtherPlayerInfoDrawer;

/**
 * 游戏界面类
 * 
 * @author chensl@ucweb.com
 * 
 *         上午11:28:20 2014-7-9
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	private String TAG = "GameView";
	private Context context;
	public ScreenSizeHolder screenHolder;
	public CardSizeHolder cardSizeHolder;
	private SurfaceHolder holder;
	private Thread drawThread;
	private boolean start;
	private List<Card> cardList; // 玩家自己拥有的牌
	private int playerId, right_player_id = -1, left_player_id = -1;
	private boolean isMyTrun;
	private int gameScore;
	private List<Integer> cardNumber;
	private List<Integer> scroeList;
	private Map<Integer, List<Card>> outList;
	private EventListener eventListener;
	private IViewControler viewControler;
	private MainPlayerInfoDrawer mainPlayerDrawer;
	private OtherPlayerInfoDrawer leftPlayerDrawer, rightPlayerDrawer;
	private float BUTTON_BASE_HEIGHT, SCORE_TEXT_LENGTH, PLAYER_TEXT_LENGTH;
	private float TEXT_SIZE, TEXT_SIZE_SMALL, TEXT_SIZE_BIG;
	private float MAIN_CARDS__BASEY, MAIN_OUT_CARDS_BASEY;
	private float LEFT_CARDS_BASEX, RIGHT_CARDS_BASEX, LEFT_OUTCARDS_BASEX,
			RIGHT_OUTCARDS_BASEX;

	public GameView(Context context, int playerId) {
		super(context);
		this.context = context;
		this.playerId = playerId;
		holder = this.getHolder();
		holder.addCallback(this); // 设置Surface生命周期回调
		init();
	}

	private class ViewControler implements IViewControler {

		public void setGameScore(int score) {
			gameScore = score;

		}

		public void setCardNumber(List<Integer> cardNumberList) {
			cardNumber = cardNumberList;

		}

		public void setScroeList(List<Integer> playersScroeList) {
			scroeList = playersScroeList;
		}

		public void setPlayersOutList(int number, List<Card> list) {
			if (number == -1) // 设置的是当前Player
				outList.put(playerId - 1, list);
			else
				outList.put(number - 1, list);
		}

		public void setCards(List<Card> cards) {
			cardList.clear();
			cardList.addAll(cards);
		}

		public void setMyTurn(boolean flag) {
			isMyTrun = flag;
		}

		@Override
		public void setEventListener(EventListener listener) {
			eventListener = listener;
		}

		@Override
		public void gameOver(int playId) {
			new AlertDialog.Builder(context)
					.setTitle("游戏结束")
					.setMessage(playId + "胜利")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 退出操作
								}
							});
		}

		@Override
		public void handCardFailed() {
			Toast.makeText(context, "请按规则出牌喔", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void roundOver() {
			for(Map.Entry<Integer, List<Card>> temp:outList.entrySet())
					temp.getValue().clear();
				
		}
	}

	private void init() {
		initPlayersId(playerId);

		viewControler = new ViewControler();
		start = true;
		isMyTrun = false;
		gameScore = 0;
		cardNumber = new Vector<Integer>();
		cardNumber.add(0);
		cardNumber.add(0);
		cardNumber.add(0);

		scroeList = new Vector<Integer>();
		scroeList.add(0);
		scroeList.add(0);
		scroeList.add(0);

		cardList = new Vector<Card>();
		outList = new HashMap<Integer, List<Card>>();

	}

	private void initPlayersId(int playerId2) {
		switch (playerId) {
		case 1:
			right_player_id = 2;
			left_player_id = 3;
			break;
		case 2:
			right_player_id = 3;
			left_player_id = 1;
			break;
		case 3:
			right_player_id = 1;
			left_player_id = 2;
			break;
		}
	}

	public IViewControler getViewControler() {
		return viewControler;
	}

	// 屏幕尺寸的获取需要在surfaceView创建之后才能获取
	private void initBeforeCreateView() {
		screenHolder = new ScreenSizeHolder();
		screenHolder.width = getWidth();
		screenHolder.height = getHeight();

		cardSizeHolder = new CardSizeHolder();
		Bitmap temp = BitmapFactory.decodeResource(getResources(),
				R.drawable.cardbg1);
		cardSizeHolder.height = temp.getHeight();
		cardSizeHolder.width = temp.getWidth();

		mainPlayerDrawer = new MainPlayerInfoDrawer(context, screenHolder,
				cardSizeHolder);
		leftPlayerDrawer = new OtherPlayerInfoDrawer(context, screenHolder,
				cardSizeHolder);
		rightPlayerDrawer = new OtherPlayerInfoDrawer(context, screenHolder,
				cardSizeHolder);

		BUTTON_BASE_HEIGHT = (float) screenHolder.height - cardSizeHolder.width
				* 3;
		TEXT_SIZE = cardSizeHolder.width * 2 / 3;
		TEXT_SIZE_SMALL = cardSizeHolder.width * 1 / 2;
		TEXT_SIZE_BIG = cardSizeHolder.width * 3 / 4;
		Paint paint = new Paint();
		paint.setTextSize(TEXT_SIZE);
		PLAYER_TEXT_LENGTH = paint.measureText("玩家11");
		paint.setTextSize(TEXT_SIZE_SMALL);
		SCORE_TEXT_LENGTH = paint.measureText("分数:000");
		MAIN_CARDS__BASEY = (float) screenHolder.height - cardSizeHolder.width
				* 2 / 3 - cardSizeHolder.height;
		MAIN_OUT_CARDS_BASEY = (float) screenHolder.height
				- cardSizeHolder.height - cardSizeHolder.width * 3;
		LEFT_OUTCARDS_BASEX = 3 * cardSizeHolder.width;
		RIGHT_OUTCARDS_BASEX = screenHolder.width - 4 * cardSizeHolder.width;
		LEFT_CARDS_BASEX = cardSizeHolder.width;
		RIGHT_CARDS_BASEX = screenHolder.width - 2 * cardSizeHolder.width;
		Log.i(TAG, screenHolder.toString());
		Log.i(TAG, cardSizeHolder.toString());
		temp.recycle();

	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, " surfaceCreated");
		initBeforeCreateView();
		// 开始绘图进程
		if (drawThread == null) {
			drawThread = new Thread(this);
			drawThread.start();

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Log.i(TAG," surfaceDestroyed");
		boolean reatry = true;
		start = false;
		while (reatry) {
			try {
				drawThread.join();
				reatry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isMyTurn() {
		return isMyTrun;
	}

	// 重绘
	private void doDraw() {
		Canvas canvas = null;
		synchronized (holder) {
			try {
				canvas = holder.lockCanvas();
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setStyle(Style.FILL);
				drawBackground(canvas);
				drawMainPlayer(canvas, paint);
				drawLeftPlayer(canvas, paint);
				drawRightPlayer(canvas, paint);
				drawGameScore(canvas, paint);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
		}

	}

	private void drawGameScore(Canvas canvas, Paint paint) {
		paint.setTextSize(TEXT_SIZE_BIG);
		paint.setColor(Color.rgb(255, 184, 15));
		canvas.drawText("本轮分数：" + gameScore, screenHolder.width / 2 - 3
				* TEXT_SIZE_BIG, TEXT_SIZE_BIG, paint);
	}

	private void drawRightPlayer(Canvas canvas, Paint paint) {
		rightPlayerDrawer.initCanvas(canvas);
		rightPlayerDrawer.drawPlayer(right_player_id, paint, screenHolder.width
				- PLAYER_TEXT_LENGTH, TEXT_SIZE);
		rightPlayerDrawer.drawScore(scroeList.get(right_player_id - 1), paint,
				screenHolder.width - SCORE_TEXT_LENGTH - 20, 2 * TEXT_SIZE);
		rightPlayerDrawer.drawCardsNumber(cardNumber.get(right_player_id - 1),
				paint, screenHolder.width - PLAYER_TEXT_LENGTH
						- SCORE_TEXT_LENGTH - 20, TEXT_SIZE, RIGHT_CARDS_BASEX);
		rightPlayerDrawer.drawOutList(outList.get(right_player_id - 1),
				RIGHT_OUTCARDS_BASEX);
	}

	private void drawLeftPlayer(Canvas canvas, Paint paint) {
		leftPlayerDrawer.initCanvas(canvas);
		leftPlayerDrawer.drawPlayer(left_player_id, paint, 10, TEXT_SIZE);
		leftPlayerDrawer.drawScore(scroeList.get(left_player_id - 1), paint,
				10, 2 * TEXT_SIZE);
		leftPlayerDrawer.drawCardsNumber(cardNumber.get(left_player_id - 1),
				paint, PLAYER_TEXT_LENGTH + 10, TEXT_SIZE, LEFT_CARDS_BASEX);
		leftPlayerDrawer.drawOutList(outList.get(left_player_id - 1),
				LEFT_OUTCARDS_BASEX);
	}

	private void drawMainPlayer(Canvas canvas, Paint paint) {
		mainPlayerDrawer.initCanvas(canvas);
		if (isMyTrun)
			mainPlayerDrawer.drawButton(paint, BUTTON_BASE_HEIGHT);
		mainPlayerDrawer.drawPlayer(playerId, paint, 10.0f,
				(float) screenHolder.height - 10);
		mainPlayerDrawer.drawCardsNumber(cardNumber.get(playerId - 1), paint,
				PLAYER_TEXT_LENGTH + TEXT_SIZE_SMALL, screenHolder.height - 10);
		mainPlayerDrawer.drawScore(scroeList.get(playerId - 1), paint,
				screenHolder.width - 2 * SCORE_TEXT_LENGTH,
				screenHolder.height - 10);
		mainPlayerDrawer.drawCards(cardList, MAIN_CARDS__BASEY,
				cardSizeHolder.height / 2);
		mainPlayerDrawer.drawOutList(outList.get(playerId - 1),
				MAIN_OUT_CARDS_BASEY);
	}

	// 画背景
	private void drawBackground(Canvas canvas) {
		Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		Rect src = new Rect(0, 0, bg.getWidth() * 3 / 4, bg.getHeight() * 2 / 3);
		Rect dst = new Rect(0, 0, screenHolder.width, screenHolder.height);
		canvas.drawBitmap(bg, src, dst, null);
	};

	public void run() {
		while (start) {
			doDraw();
			Sleep(50);
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
		if (eventListener == null)
			throw new IllegalArgumentException(
					"EventListener should not be null!!!");
		Log.e(TAG, " onTouchEvent2");
		eventListener.handleTouchEvent(event, this, cardList);
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
