package com.uc.fivetenkgame;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameViewActivity extends Activity {
	// private GameView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Common.END_GAME:
				new  AlertDialog.Builder(GameViewActivity.this)
				.setTitle("��Ϸ����")
				.setMessage("���"+msg.obj + "ʤ��")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).show();
				break;
			}
		}
	};
	private String TAG="GameViewActivity";
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		
	};
	@Override
	protected void onStop() {
		super.onStop();

		//
	}
}
