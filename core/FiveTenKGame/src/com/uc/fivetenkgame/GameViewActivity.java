package com.uc.fivetenkgame;

import java.util.Timer;
import java.util.TimerTask;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.Toast;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SoundPoolCommon;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;

public class GameViewActivity extends Activity {

	private AlertDialog backPressDialog;// ����Ұ����ؼ����ֵ�dialog
	private AlertDialog pauseDialog;// ���������ͣʱ����ҳ��ֵ�dialog
	private AlertDialog winningDialog;// ���������ͣʱ����ҳ��ֵ�dialog
//	private boolean ifPause;
	private View winningView;
	private GameView view ;
	private GameApplication gameApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "oncreate");
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// ��������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 view = new GameView(getApplicationContext(), Player
				.getInstance().getPlayerNumber());
		Player.getInstance().setViewControler(view.getViewControler());
		Player.getInstance().setEventListener();
		Player.getInstance().setHandler(mHandler);
		Player.getInstance().initView();
		((GameApplication) getApplication()).playSound(SoundPoolCommon.SOUND_GAME_START);
		setContentView(view);
		gameApplication=(GameApplication) getApplication();
		initDialog();
	}
	

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i(TAG, "handler receive msg:" + msg);
			switch (msg.what) {
			case NetworkCommon.END_GAME:
				String[] res = (String[]) msg.obj;
				if (Integer.valueOf(res[0]) == Player.getInstance()
						.getPlayerNumber())
					((GameApplication) getApplication())
							.playSound(SoundPoolCommon.SOUND_WIN);
				else
					((GameApplication) getApplication())
							.playSound(SoundPoolCommon.SOUND_FAILD);
				showWinningDialog(res);
				break;

			case NetworkCommon.GAME_STATE_CHANGE:
				String objMsg = (String) msg.obj;
				Log.i("objMsg is ", objMsg.length() + "");
				if (objMsg.startsWith(NetworkCommon.GAME_PAUSE)) {
					if (!gameApplication.isPause()) {
						pauseDialog.show();// �������֪ͨ
//						ifPause = true;
						gameApplication.setPause(true);
						Log.i("pauseDialog", "show");
					}
				} else if (objMsg.startsWith(NetworkCommon.GAME_RESUME)) {
					if (gameApplication.isPause()) {
						pauseDialog.cancel();// �������֪ͨ
//						ifPause = false;
						gameApplication.setPause(false);
						Log.i("pauseDialog", "cancel");
					}
				} else if (objMsg.startsWith(NetworkCommon.GAME_EXIT)) {
					Timer mTimer = new Timer();
					mTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							GameViewActivity.this.finish();
						}
					}, 1000);
					Toast.makeText(getApplicationContext(), "��������˳���Ϸ",
							Toast.LENGTH_LONG).show();
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
//						ifPause = false;
						gameApplication.setPause(false);
						Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// �ָ���Ϸ
					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(
								NetworkCommon.GAME_EXIT
										+ Player.getInstance()
												.getPlayerNumber());
						GameViewActivity.this.finish();// �˳���Ϸ
					}
				}).create();
		backPressDialog.setCanceledOnTouchOutside(false);
		backPressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
//				ifPause = false;
				gameApplication.setPause(false);
				Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// �ٴΰ����ؼ���������Ϸ
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
								Player.getInstance().sendMsg(NetworkCommon.GAME_EXIT);
								GameViewActivity.this.finish();
							}
						}).create();
		pauseDialog.setCancelable(false);// �����˳���Ϸ�⣬ֻ�ܵȴ��������
	}

	protected void showWinningDialog(String[] res) {
		if (winningView == null)
			winningView = LayoutInflater.from(this).inflate(
					R.layout.dialog_winning, null);
		((TextView) winningView.findViewById(R.id.text_winning_player))
				.setText(getResources().getString(R.string.winner).replace("#",
						res[0]));
		((TextView) winningView.findViewById(R.id.text_score_player1))
				.setText(getResources().getString(R.string.player1_score)
						.replace("#", res[1]));
		((TextView) winningView.findViewById(R.id.text_score_player2))
				.setText(getResources().getString(R.string.player2_score)
						.replace("#", res[2]));
		((TextView) winningView.findViewById(R.id.text_score_player3))
				.setText(getResources().getString(R.string.player3_score)
						.replace("#", res[3]));
		winningDialog = new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.game_over))
				.setView(winningView)
				.setPositiveButton("�˳�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setCancelable(false).show();
	}

	@Override
	public void onBackPressed() {
		backPressDialog.show();
//		ifPause = true;
		gameApplication.setPause(true);
		Player.getInstance().sendMsg(NetworkCommon.GAME_PAUSE);// ��ͣ��Ϸ
	}

	private String TAG = "GameViewActivity";

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume " + gameApplication.isPause());
		super.onResume();
		view.initHolder();
		if ( gameApplication.isPause()) {
//			ifPause = false;
			gameApplication.setPause(true);
			Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// ֪ͨ������һָ���Ϸ
		}
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");

		Player.getInstance().sendMsg(NetworkCommon.GAME_PAUSE);// ֪ͨ���������ͣ��Ϸ
//		ifPause = true;
		gameApplication.setPause(true);
	};

	@Override
	protected void onDestroy() {
		Player.getInstance().resetPlayer();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

//		outState.putBoolean("ifPause", ifPause);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

//		ifPause = savedInstanceState.getBoolean("ifPause");
	}
}
