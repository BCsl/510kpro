package com.uc.fivetenkgame;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class GameViewActivity extends Activity {
	//private GameView mGameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//根据当前玩家是否是服务器来获取不同的实例
		Intent intent = getIntent();
		boolean isServer = intent.getBooleanExtra("isServer", false);
		if( isServer ){
//			mPlayer = ServerPlayer.getInstance();
		}
		
		// 锁定横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 GameView view =new GameView(getApplicationContext(),Player.getInstance().getPlayerNumber());
		Player.getInstance().setViewControler(view.getViewControler());
		Player.getInstance().setEventListener();
		Player.getInstance().setHandler(mHandler);
		Player.getInstance().initView();
		setContentView(view);
		
		//mPlayer = Player.getInstance();
		//mPlayer.setIViewControler(view);
		//new LoopThread(view).start();
		
		
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch( msg.what ){
			case Common.END_GAME:
				finish();
				break;
			}
		}
	};
	/*
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
					cardList.add(new Card(String.valueOf(random.nextInt(18))));
				view.getViewControler().setCards(cardList);
				i++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		//
	}
}
