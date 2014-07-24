package com.uc.fivetenkgame;

import com.google.zxing.qr_codescan.MipcaActivityCapture;
import com.uc.fivetenkgame.network.util.Common;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 游戏主界面，界面有四个按钮,点击后分别跳转到相应的界面
 * 
 * @author liuzd,chensl
 * 
 */
public class GameMainActivity extends Activity {
	private static final int REQUEST_SERVER_IP = 90000;
	private static final int REQUEST_SCAN_IP = 1;
	private static boolean INPUT = true;
	private Button mNewGameButton;
	private Button mJoinGameButton;
	private Button mHelpButton;
	private Button mSettingButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);

		mNewGameButton = (Button) findViewById(R.id.main_new_game_id);
		mJoinGameButton = (Button) findViewById(R.id.main_joid_game_id);
		mHelpButton = (Button) findViewById(R.id.main_help_id);
		mSettingButton = (Button) findViewById(R.id.main_setting_id);
		mNewGameButton.setOnClickListener(mClickListener);
		mJoinGameButton.setOnClickListener(mClickListener);
		mHelpButton.setOnClickListener(mClickListener);
		mSettingButton.setOnClickListener(mClickListener);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		public void onClick(View v) {

			if (v == mNewGameButton) {
				Intent intent = new Intent();

				intent.putExtra("isServer", true);
				intent.setClass(GameMainActivity.this,
						WaitingGameActivity.class);
				startActivity(intent);
			} else if (v == mJoinGameButton) {
				// 在这里开启二维码扫描
				if (!getApplicationContext().getSharedPreferences(
						Common.TABLE_SETTING, MODE_PRIVATE).getBoolean(
						Common.SP_QRCODE_FLAG, false)) {
					Intent intent = new Intent();
					intent.setClass(GameMainActivity.this,
							InputServerIPActivity.class);
					startActivityForResult(intent, REQUEST_SERVER_IP);
				} else {
					Intent intent = new Intent();
					intent.setClass(GameMainActivity.this,
							MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, REQUEST_SCAN_IP);
				}
			} else if (v == mSettingButton) {
				Intent intent = new Intent();
				intent.setClass(GameMainActivity.this,
						GameSettingActivity.class);
				startActivity(intent);
			} else if (v == mHelpButton) {
				Intent intent = new Intent();
				intent.setClass(GameMainActivity.this, GameHelpActivity.class);
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SERVER_IP) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String ipAddr = bundle.getString("IP");
					Intent intent = new Intent();
					intent.putExtra("isServer", false);
					intent.putExtra("IP", ipAddr);
					intent.setClass(GameMainActivity.this,
							WaitingGameActivity.class);
					startActivity(intent);
				}
			} else
				System.out.println("input server ip cancel");
		}// REQUEST_SERVER_IP
		else if (requestCode == REQUEST_SCAN_IP) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String ipAddr = bundle.getString("result");
					Intent intent = new Intent();
					intent.putExtra("isServer", false);
					intent.putExtra("IP", ipAddr);
					intent.setClass(GameMainActivity.this,
							WaitingGameActivity.class);
					startActivity(intent);
				}
			}
		}// REQUEST_SCAN_IP
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

}
