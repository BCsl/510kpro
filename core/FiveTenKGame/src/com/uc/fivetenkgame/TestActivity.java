package com.uc.fivetenkgame;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;
import com.uc.fivetenkgame.view.util.IViewControler;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends Activity{
	private String TAG="TestActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Òþ²Ø±êÌâÀ¸
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Òþ²Ø×´Ì¬À¸
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Ëø¶¨ºáÆÁ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		final GameView view =new GameView(getApplicationContext(),3);
		setContentView(view);
		new LoopThread(view).start();
		
		
	}
	public class LoopThread extends Thread{
		GameView view;
		LoopThread(GameView view ){
			this.view=view;
		}
		@Override
		public void run() {
			int i=0;
			IViewControler viewControler=view.getViewControler();
			viewControler.setEventListener(new EventListener() {
			
			@Override
			public boolean handCard(List<Card> handList) {
					if(handList==null)
						return true;
					Log.e(TAG, handList.toString());
					return true;
					}
			}
			);
			while(i<10){
				Vector<Card> cardList = new Vector<Card>();
				Vector<Integer> cardNumber=new Vector<Integer>();
				Random random =new Random();
				cardNumber.add(random.nextInt(18));
				cardNumber.add(random.nextInt(18));
				cardNumber.add(random.nextInt(18));
				for(int j=1;j<18;j++)
					cardList.add(new Card(String.valueOf(random.nextInt(18))));
				viewControler.setCards(cardList);
				viewControler.setCardNumber(cardNumber);
				viewControler.setGameScore(random.nextInt(100));
				viewControler.setMyTurn(random.nextBoolean());
				i++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

}
