/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.DrawerFactory;
import com.uc.fivetenkgame.view.util.EventListener;
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
 *         ����11:28:20 2014-7-9
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
	private  String TAG = "GameView";
	private Context context;
	public ScreenSizeHolder screenHolder;
	public CardSizeHolder cardSizeHolder ;
	private SurfaceHolder holder;
	private Thread drawThread;
	private boolean start;
	private   List<Card> cardList;		//����Լ�ӵ�е���
	private int playerNO;				
	private int playersCount;			//�[���c��ҿ���
	private IDrawer drawer;
	private boolean isMyTrun;
	private int gameScore;
	private List<Integer> cardNumber;
	private List<Integer> scroeList;
	private Map<Integer, List<Card>> outList;
	private EventListener eventListener;
	
	public GameView(Context context,int playersCount,int playerNO,EventListener eventListener) {
		super(context);
		this.context = context;
		this.playersCount=playersCount;
		this.playerNO=playerNO;
		holder =this.getHolder();
		holder.addCallback(this); // ����Surface�������ڻص�
		this.eventListener=eventListener;
		init();
	}
	private void init(){
		Random random=new Random();
		start= true;
		isMyTrun=true;
		gameScore=random.nextInt(100);
		cardNumber=new Vector<Integer>();
		cardNumber.add(random.nextInt(18));
		cardNumber.add(random.nextInt(18));
		cardNumber.add(random.nextInt(18));
		
		scroeList=new Vector<Integer>();
		scroeList.add(random.nextInt(100));
		scroeList.add(random.nextInt(100));
		scroeList.add(random.nextInt(100));
		
		cardList=new Vector<Card>();
		outList=new HashMap<Integer, List<Card>>();
		
		
//		for(int i=0;i<3;i++){
//			List<Card> tempList=new ArrayList<Card>();
//			for(int j=1;j<random.nextInt(18);j++){
//				tempList.add(new Card(t.getWidth(), t.getHeight(), String.valueOf(j)));
//			}
//			outList.put(i,tempList);
//		}
//			
		
	}
	//��Ļ�ߴ�Ļ�ȡ��Ҫ��surfaceView����֮����ܻ�ȡ
	private void initBeforeCreateView(){
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
		initBeforeCreateView();
		// ��ʼ��ͼ����
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
//		Log.i(TAG," surfaceDestroyed");
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
	public boolean isMyTurn(){
		return  isMyTrun;
	}
	
	/**
	 * ���ܷ������ĳ�����̖
	 */
	public void setMyTurn(boolean flag){
		 isMyTrun=flag;
	}
	/**
	 * ���ܷ�������������Ϸ����
	 * @param score  ��Ϸ����
	 */
	public void setGameScore(int score){
		gameScore=score;
	}
	/**
	 * ���ܷ������l�����ҵ���
	 * @param cardList   ����Լ�����
	 */
	public void handCards(List<Card> cards){
		cardList.clear();
		cardList.addAll(cards);
	};

	/** �O�ø������ʣ�N�Ɣ�
	 * 
	 * @param cardNumber  
	 */
	public void setCardNumber(List<Integer> cardNumber) {
		this.cardNumber = cardNumber;
	}
	/**
	 * �O�ø���ҷ֔�
	 * @param scroeList  
	 */
	public void setScroeList(List<Integer> scroeList) {
		this.scroeList = scroeList;
	}

	/**
	 * 
	 * @param number  ��Һ�
	 * @param list	��ҳ�����
	 */
	public void setPlayersOutList(int number, List<Card> list) {
		if(number ==-1)
			outList.put(playerNO-1, list);
		else
		outList.put(number-1, list);
	
	}
	
	
	// �ػ�
	private  void doDraw() {
		Canvas canvas = null ;
		synchronized (holder) {
			try {
				  canvas = holder.lockCanvas();
				drawBackground(canvas);
		if(isMyTrun)
			drawer.drawButton(canvas);
		drawer.drawGameScore(canvas, gameScore);
		drawer.drawPlayers(canvas,playerNO);
		drawer.drawCards(canvas,cardList, context);
		drawer.drawCardNumber(canvas, cardNumber, playerNO,context);
		drawer.drawPlayersScore(canvas, scroeList, playerNO);
		drawer.drawOutList(context, canvas, outList, playerNO);
			}
			 catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null)
						holder.unlockCanvasAndPost(canvas);
				}
		}
			
	};

	// ������
	private void drawBackground(Canvas canvas) {
		Bitmap bg=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		Rect src = new Rect(0, 0, bg.getWidth()*3/4,bg.getHeight()*2/3);
		Rect dst = new Rect(0, 0, screenHolder.width, screenHolder.height);
		canvas.drawBitmap(bg, src,dst,null);

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
		//ֻ���ܰ����¼�
		if(event.getAction()!=MotionEvent.ACTION_UP)
			return true;
		
		eventListener.handleTouchEvent(event, this, cardList);
		return super.onTouchEvent(event);
	}

}
