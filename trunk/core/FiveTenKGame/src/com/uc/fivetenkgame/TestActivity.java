package com.uc.fivetenkgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import my.example.fivetenkgame.R;

import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends Activity{
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
		final GameView view =new GameView(getApplicationContext(),3,3,new EventListener() {
			
			@Override
			public boolean handCard(List<Card> handList) {
				return true;
			}
		});
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
			while(i<1){
				Vector<Card> cardList = new Vector<Card>();
				Bitmap t=BitmapFactory.decodeResource(getResources(),R.drawable.cardbg1);
				Random random =new Random();
				for(int j=1;j<18;j++)
					cardList.add(new Card(t.getWidth(), t.getHeight(), String.valueOf(random.nextInt(18))));
				view.handCards(cardList);
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
