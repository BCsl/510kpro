/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.ArrayList;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.DrawerFactory;
import com.uc.fivetenkgame.view.util.IDrawer;

import my.example.fivetenkgame.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author chensl@ucweb.com
 * 
 *         上午11:28:20 2014-7-9
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
	private final String TAG = "GameView";
	private Context context;
	private Handler handler;
	private ScreenSizeHolder screenHolder;
	private CardSizeHolder cardSizeHolder ;
	private SurfaceHolder holder;
	private Thread drawThread;
	private boolean start;
	private boolean repaint;
	private  List<Card> cardList;		//玩家自己拥有的牌
	private boolean first;
	private int playerNO;				
	private int playersCount;			//遊戲參與玩家總數
	private IDrawer drawer;
	private boolean isMyTrun;
	private int gameScore;
	private List<Integer> cardNumber;
	private List<Integer> scroeList;
	
	public GameView(Context context, Handler handler,int playersCount,int playerNO) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.playersCount=playersCount;
		this.playerNO=playerNO;
		holder =this.getHolder();
		holder.addCallback(this); // 设置Surface生命周期回调
	}
	private void init(){
		first=true;
		start= true;
		gameScore=0;
		
		cardList=new ArrayList<Card>();
		cardNumber=new ArrayList<Integer>();
		cardNumber.add(1);
		cardNumber.add(3);
		cardNumber.add(2);
		
		scroeList=new ArrayList<Integer>();
		scroeList.add(100);
		scroeList.add(15);
		scroeList.add(63);
		
		repaint=true;
		Bitmap t=BitmapFactory.decodeResource(getResources(),R.drawable.cardbg1);
		for(int i=1;i<19;i++)
			cardList.add(new Card(t.getWidth(), t.getHeight(), String.valueOf(i)));
		
		screenHolder = new ScreenSizeHolder();
		screenHolder.width = getWidth();
		screenHolder.height = getHeight();
		cardSizeHolder = new CardSizeHolder();
		Bitmap temp=BitmapFactory.decodeResource(getResources(),R.drawable.cardbg1);
		cardSizeHolder.height=temp.getHeight();
		cardSizeHolder.width=temp.getWidth();
		Log.i(TAG, screenHolder.toString());
		Log.i(TAG, cardSizeHolder.toString());
		temp.recycle();
	}
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG," surfaceCreated");
		init();
		// 开始绘图进程
		drawer=DrawerFactory.getDrawer(playersCount,screenHolder,cardSizeHolder);
		if (drawThread == null) {
			drawThread = new Thread(this);
			drawThread.start();
			
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG," surfaceDestroyed");
		boolean reatry = true;
		start=false;
		while(reatry){
			try{
				drawThread.join();	
				reatry = false;
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		

	}
	/**
	 * 接受服务器发来的游戏分数
	 * @param score
	 */
	public void setGameScore(int score){
		gameScore=score;
		repaintNow();
	}
	/**
	 * 接受服務器的出牌信號
	 */
	public void  myTurn(){
		isMyTrun = true;
		repaintNow();
	}
	/**
	 * 接受服務器發來的玩家的牌
	 * @param cardList
	 */
	public void handCards(List<Card> cards){
		if(cardList == null)
			cardList = new ArrayList<Card>();
		cardList.clear();
		Log.i(TAG, "card size="+cards.size());
		cardList.addAll(cards);
		repaintNow();
	};	
	
	
	// 重绘
	private  void doDraw() {
		Log.i(TAG, "doDraw");
		Canvas canvas = null ;
		synchronized (holder) {
			try {
				  canvas = holder.lockCanvas();
				drawBackground(canvas);
//		if(isMyTrun)
			drawer.drawButton(canvas);
		drawer.drawGameScore(canvas, gameScore);
		drawer.drawPlayers(canvas,playerNO);
		drawer.drawCards(canvas,cardList, context);
		drawer.drawCardNumber(canvas, cardNumber, playerNO,context);
		drawer.drawPlayersScore(canvas, scroeList, playerNO);
			}
			 catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null)
						holder.unlockCanvasAndPost(canvas);
				}
		}
			
	};

	// 画背景
	private void drawBackground(Canvas canvas) {
		if(first){
		Bitmap bg=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		Rect src = new Rect(0, 0, bg.getWidth()*3/4,bg.getHeight()*2/3);
		Rect dst = new Rect(0, 0, screenHolder.width, screenHolder.height);
		canvas.drawBitmap(bg, src,dst,null);
		first=false;
		}
	};

	public void repaintNow(){
		Log.e(TAG, "repaintNow");
		repaint = true;
	}
	
	public void run() {
		while (start) {
			if (repaint) {
					doDraw();
					repaint = false;
					Sleep(50);
			}
		}
	}

	private void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public class ScreenSizeHolder{
		public int width;
		public int height;
		@Override
		public String toString() {
			return "ScreenWidth="+width+",ScreenHeight="+height;
		}
	}
	public class CardSizeHolder{
		public int width;
		public int height;
		@Override
		public String toString() {
			return "CardWidth="+width+",CardHeight="+height;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

}
