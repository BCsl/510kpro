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

	private AlertDialog backPressDialog;//����Ұ����ؼ����ֵ�dialog
	private AlertDialog pauseDialog;//���������ͣʱ����ҳ��ֵ�dialog
	private boolean ifPause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
//		 mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		

		// ���ݵ�ǰ����Ƿ��Ƿ���������ȡ��ͬ��ʵ��
		Intent intent = getIntent();
		boolean isServer = intent.getBooleanExtra("isServer", false);
		if (isServer) {
			// mPlayer = ServerPlayer.getInstance();
		}

		// ��������
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
						.setTitle("��Ϸ����")
						.setMessage("���" + msg.obj + "ʤ��")
						.setPositiveButton("ȷ��",
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
						pauseDialog.show();//�������֪ͨ
						ifPause = true;
						Log.i("pauseDialog","show");
					}
				}else if(objMsg.startsWith(Common.GAME_RESUME)){
					if(ifPause){
						pauseDialog.cancel();//�������֪ͨ
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
		//����backPressDialog
		backPressDialog = new AlertDialog.Builder(this).setTitle("��ͣ")
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ifPause = false;
						Player.getInstance().sendMsg(Common.GAME_RESUME);// �ָ���Ϸ
					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(Common.GAME_EXIT);
						GameViewActivity.this.finish();// �˳���Ϸ
					}
				}).create();

		backPressDialog.setCanceledOnTouchOutside(false);
		backPressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				ifPause = false;
				Player.getInstance().sendMsg(Common.GAME_RESUME);// �ٴΰ����ؼ���������Ϸ
			}
		});
		
		//����pauseDialog
		pauseDialog = new AlertDialog.Builder(this).setTitle("���������ͣ��Ϸ")
				.setPositiveButton("������ˣ��˳�������Ϸ", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(Common.GAME_EXIT);
						GameViewActivity.this.finish();
					}
				})
				.create();
		pauseDialog.setCancelable(false);//�����˳���Ϸ�⣬ֻ�ܵȴ��������	
	}
	
	@Override
	public void onBackPressed() {
		backPressDialog.show();
		ifPause = true;
		Player.getInstance().sendMsg(Common.GAME_PAUSE);// ��ͣ��Ϸ
	}

	private String TAG = "GameViewActivity";

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume "+ifPause);
		super.onResume();
		
		if(ifPause){
			ifPause = false;
			Player.getInstance().sendMsg(Common.GAME_RESUME);//֪ͨ������һָ���Ϸ
		}
	}
	
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		super.onPause();
//
//		Player.getInstance().sendMsg(Common.GAME_PAUSE);//֪ͨ���������ͣ��Ϸ
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
