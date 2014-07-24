package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;

public class GameViewActivity extends Activity {

	private AlertDialog backPressDialog;// ����Ұ����ؼ����ֵ�dialog
	private AlertDialog pauseDialog;// ���������ͣʱ����ҳ��ֵ�dialog
	private AlertDialog winningDialog;// ���������ͣʱ����ҳ��ֵ�dialog
	private boolean ifPause;
	private View winningView;
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
		((GameApplication) getApplication()).playSound(Common.SOUND_GAME_START);
		setContentView(view);

		initDialog();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("gameViewActivity", "handler receive msg:" + msg);
			switch (msg.what) {
			case Common.END_GAME:
				String[] res = (String[]) msg.obj;
				if (Integer.valueOf(res[0]) == Player.getInstance()
						.getPlayerNumber())
					((GameApplication) getApplication())
							.playSound(Common.SOUND_WIN);
				else
					((GameApplication) getApplication())
							.playSound(Common.SOUND_FAILD);
				showWinningDialog(res);
				break;

			case Common.GAME_STATE_CHANGE:
				String objMsg = (String) msg.obj;
				Log.i("objMsg is ", objMsg.length() + "");
				if (objMsg.startsWith(Common.GAME_PAUSE)) {
					if (!ifPause) {
						pauseDialog.show();// �������֪ͨ
						ifPause = true;
						Log.i("pauseDialog", "show");
					}
				} else if (objMsg.startsWith(Common.GAME_RESUME)) {
					if (ifPause) {
						pauseDialog.cancel();// �������֪ͨ
						ifPause = false;
						Log.i("pauseDialog", "cancel");
					}
				} else if (objMsg.startsWith(Common.GAME_EXIT)) {
					GameViewActivity.this.finish();
				}
				break;
			}

		}
	};

	private void initDialog() {
		// ����backPressDialog
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

		// ����pauseDialog
		pauseDialog = new AlertDialog.Builder(this)
				.setTitle("���������ͣ��Ϸ")
				.setPositiveButton("������ˣ��˳�������Ϸ",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Player.getInstance().sendMsg(Common.GAME_EXIT);
								GameViewActivity.this.finish();
							}
						}).create();
		pauseDialog.setCancelable(false);// �����˳���Ϸ�⣬ֻ�ܵȴ��������
	}

	protected void showWinningDialog(String[] res) {
		if(winningView==null)
			winningView=LayoutInflater.from(this).inflate(R.layout.dialog_winning, null);
		((TextView)winningView.findViewById(R.id.text_winning_player)).setText(getResources().getString(R.string.winner).replace("#", res[0]));
		((TextView)winningView.findViewById(R.id.text_score_player1)).setText(getResources().getString(R.string.player1_score).replace("#", res[1]));
		((TextView)winningView.findViewById(R.id.text_score_player2)).setText(getResources().getString(R.string.player2_score).replace("#", res[2]));
		((TextView)winningView.findViewById(R.id.text_score_player3)).setText(getResources().getString(R.string.player3_score).replace("#", res[3]));
			winningDialog=new AlertDialog.Builder(this).
					setTitle(getResources().getString(R.string.game_over))
					.setView(winningView)
					.setPositiveButton("�˳�",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							finish();
						}
					}).setCancelable(false).show();
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
		Log.i(TAG, "onResume " + ifPause);
		super.onResume();

		if (ifPause) {
			ifPause = false;
			Player.getInstance().sendMsg(Common.GAME_RESUME);// ֪ͨ������һָ���Ϸ
		}
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		super.onPause();
		//
		// Player.getInstance().sendMsg(Common.GAME_PAUSE);//֪ͨ���������ͣ��Ϸ
		// ifPause = true;
	};

	@Override
	protected void onStop() {
		super.onStop();
		Player.getInstance().sendMsg(Common.GAME_EXIT);
		Player.getInstance().resetPlayer();
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
