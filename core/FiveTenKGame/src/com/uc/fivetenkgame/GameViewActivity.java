package com.uc.fivetenkgame;

import java.util.Random;
import java.util.Vector;

import my.example.fivetenkgame.R;

import com.uc.fivetenkgame.player.ClientPlayer;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.player.ServerPlayer;
import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameViewActivity extends Activity {

	private Player mPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//���ݵ�ǰ����Ƿ��Ƿ���������ȡ��ͬ��ʵ��
		Intent intent = getIntent();
		boolean isServer = intent.getBooleanExtra("isServer", false);
		if( isServer ){
			mPlayer = ServerPlayer.getInstance();
		}
		else{
			mPlayer = ClientPlayer.getInstance();
		}
		
		// ��������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		final GameView view =new GameView(getApplicationContext(), null,3,3);
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
			while(i<10){
				Vector<Card> cardList = new Vector<Card>();
				Bitmap t=BitmapFactory.decodeResource(getResources(),R.drawable.cardbg1);
				Random random =new Random();
				for(int j=1;j<random.nextInt(18);j++)
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