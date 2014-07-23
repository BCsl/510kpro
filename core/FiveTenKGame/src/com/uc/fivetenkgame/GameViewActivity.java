package com.uc.fivetenkgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;

public class GameViewActivity extends Activity {

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
		// ���ò�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
				if(objMsg.equals(Common.GAME_PAUSE)){
					if(!ifPause)
						pauseDialog.show();//�������֪ͨ
				}else if(objMsg.equals(Common.GAME_RESUME)){
					if(ifPause)
						pauseDialog.cancel();//�������֪ͨ
				}else if(objMsg.equals(Common.GAME_EXIT)){
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
				Player.getInstance().sendMsg(Common.GAME_RESUME);// �ٴΰ����ؼ���������Ϸ
			}
		});
		
		//����pauseDialog
		pauseDialog = new AlertDialog.Builder(this).setTitle("���������ͣ��Ϸ")
				.setPositiveButton("������ˣ��˳�������Ϸ", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GameViewActivity.this.finish();
					}
				})
				.create();
		pauseDialog.setCancelable(false);//�����˳���Ϸ�⣬ֻ�ܵȴ��������	
	}
	
	@Override
	public void onBackPressed() {
		Player.getInstance().sendMsg(Common.GAME_PAUSE);// ��ͣ��Ϸ
		backPressDialog.show();
	}

	private String TAG = "GameViewActivity";

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		
		if(ifPause){
			Player.getInstance().sendMsg(Common.GAME_RESUME);//֪ͨ������һָ���Ϸ
			ifPause = false;
		}
	}
	
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();

		Player.getInstance().sendMsg(Common.GAME_PAUSE);//֪ͨ���������ͣ��Ϸ
		ifPause = true;
	};

	@Override
	protected void onStop() {
		super.onStop();

	}
}
