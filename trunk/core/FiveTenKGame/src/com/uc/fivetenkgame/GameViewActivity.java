package com.uc.fivetenkgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;

public class GameViewActivity extends Activity {
//	private PowerManager.WakeLock mWakeLock;

	private AlertDialog backPressDialog;//本玩家按返回键出现的dialog
	private AlertDialog pauseDialog;//其他玩家暂停时本玩家出现的dialog
	private boolean ifPause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
//		 mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		

		// 根据当前玩家是否是服务器来获取不同的实例
		Intent intent = getIntent();
		boolean isServer = intent.getBooleanExtra("isServer", false);
		if (isServer) {
			// mPlayer = ServerPlayer.getInstance();
		}

		// 锁定横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		GameView view = new GameView(getApplicationContext(), Player
				.getInstance().getPlayerNumber());
		Player.getInstance().setViewControler(view.getViewControler());
		Player.getInstance().setEventListener();
		Player.getInstance().setHandler(mHandler);
		Player.getInstance().initView();
		setContentView(view);

		initDialog();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("gameViewActivity","handler receive msg:"+msg);
			switch (msg.what) {
			case Common.END_GAME:
				new AlertDialog.Builder(GameViewActivity.this)
						.setTitle("游戏结束")
						.setMessage("玩家" + msg.obj + "胜利")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
				break;
				
			case Common.GAME_STATE_CHANGE:
				String objMsg = (String)msg.obj;
				Log.i("objMsg is ",objMsg.length()+"");
				if(objMsg.startsWith(Common.GAME_PAUSE)){
					if(!ifPause){
						pauseDialog.show();//其他玩家通知
						ifPause = true;
						Log.i("pauseDialog","show");
					}
				}else if(objMsg.startsWith(Common.GAME_RESUME)){
					if(ifPause){
						pauseDialog.cancel();//其他玩家通知
						ifPause = false;
						Log.i("pauseDialog","cancel");
					}
				}else if(objMsg.startsWith(Common.GAME_EXIT)){
					GameViewActivity.this.finish();
				}
				break;
			}
			
			
		}
	};

	private void initDialog(){
		//设置backPressDialog
		backPressDialog = new AlertDialog.Builder(this).setTitle("暂停")
				.setPositiveButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ifPause = false;
						Player.getInstance().sendMsg(Common.GAME_RESUME);// 恢复游戏
					}
				})
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(Common.GAME_EXIT);
						GameViewActivity.this.finish();// 退出游戏
					}
				}).create();

		backPressDialog.setCanceledOnTouchOutside(false);
		backPressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				ifPause = false;
				Player.getInstance().sendMsg(Common.GAME_RESUME);// 再次按返回键，返回游戏
			}
		});
		
		//设置pauseDialog
		pauseDialog = new AlertDialog.Builder(this).setTitle("其他玩家暂停游戏")
				.setPositiveButton("不想等了，退出本次游戏", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(Common.GAME_EXIT);
						GameViewActivity.this.finish();
					}
				})
				.create();
		pauseDialog.setCancelable(false);//除了退出游戏外，只能等待其他玩家	
	}
	
	@Override
	public void onBackPressed() {
		backPressDialog.show();
		ifPause = true;
		Player.getInstance().sendMsg(Common.GAME_PAUSE);// 暂停游戏
	}

	private String TAG = "GameViewActivity";

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume "+ifPause);
		super.onResume();
		
		if(ifPause){
			ifPause = false;
			Player.getInstance().sendMsg(Common.GAME_RESUME);//通知其他玩家恢复游戏
		}
	}
	
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		super.onPause();
//
//		Player.getInstance().sendMsg(Common.GAME_PAUSE);//通知其他玩家暂停游戏
//		ifPause = true;
	};

	@Override
	protected void onStop() {
		super.onStop();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putBoolean("ifPause", ifPause);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		ifPause = savedInstanceState.getBoolean("ifPause");
	}
}
