package com.uc.fivetenkgame;

import java.util.Timer;
import java.util.TimerTask;

import com.google.zxing.qr_codescan.MipcaActivityCapture;
import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.network.util.Common;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 游戏主界面，界面有四个按钮,点击后分别跳转到相应的界面
 * 
 * @author liuzd,chensl,lm
 * 
 */
public class GameMainActivity extends Activity {
	private String TAG = "GameMainActivity";
	private static final int REQUEST_SERVER_IP = 90000;
	private static final int REQUEST_SCAN_IP = 1;
	private Button mNewGameButton;
	private Button mJoinGameButton;
	private Button mHelpButton;
	private Button mSettingButton;
	private int EXIT_TIME;
	private Timer task;
	private WifiManager wifiManager;
	private static Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);
		EXIT_TIME = 0;
		mNewGameButton = (Button) findViewById(R.id.main_new_game_id);
		mJoinGameButton = (Button) findViewById(R.id.main_joid_game_id);
		mHelpButton = (Button) findViewById(R.id.main_help_id);
		mSettingButton = (Button) findViewById(R.id.main_setting_id);
		mNewGameButton.setOnClickListener(mClickListener);
		mJoinGameButton.setOnClickListener(mClickListener);
		mHelpButton.setOnClickListener(mClickListener);
		mSettingButton.setOnClickListener(mClickListener);

		checkAndOpenWifi();
	}

	private void checkAndOpenWifi() {
		if (wifiManager == null)
			wifiManager = (WifiManager) getSystemService(Service.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle("wifi未开启，是否现在打开")
					.setPositiveButton("开启",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (wifiManager.setWifiEnabled(true))
										showToast("WiFi已开启", 1000);
									else
										showToast("WiFi打开失败", 1000);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		public void onClick(View v) {
			((GameApplication) getApplication())
					.playSound(Common.SOUND_BUTTON_PRESS);
			if (v == mNewGameButton) {
				if (wifiManager.isWifiEnabled()) {
					Intent intent = new Intent();

					intent.putExtra("isServer", true);
					intent.setClass(GameMainActivity.this,
							WaitingGameActivity.class);
					startActivity(intent);
				} else
					showToast("请打开wifi", 1000);
			} else if (v == mJoinGameButton) {
				if (wifiManager.isWifiEnabled()) {
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
						// IntentIntegrator integrator = new IntentIntegrator(
						// GameMainActivity.this);
						// integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
					}
				} else {
					showToast("请打开wifi", 1000);
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
					Log.i(TAG, "扫描结果：" + ipAddr);

					if (Common.isIPAddress(ipAddr)) {
						Intent intent = new Intent();
						intent.putExtra("isServer", false);
						intent.putExtra("IP", ipAddr);
						intent.setClass(GameMainActivity.this,
								WaitingGameActivity.class);
						startActivity(intent);
					} else
						showToast(
								getResources().getString(
										R.string.ip_scan_error_str).replace(
										"#", ipAddr), 2000);
				}
			}
		}// REQUEST_SCAN_IP
			// else
		// if (requestCode == IntentIntegrator.REQUEST_CODE) {
		// IntentResult scanResult = IntentIntegrator.parseActivityResult(
		// requestCode, resultCode, data);
		// if (scanResult != null) {
		// String ipAddr = scanResult.getContents();
		// Log.i(TAG, "扫描结果：" + ipAddr);
		// if (ipAddr != null)
		// if (Common.isIPAddress(ipAddr)) {
		// Intent intent = new Intent();
		// intent.putExtra("isServer", false);
		// intent.putExtra("IP", ipAddr);
		// intent.setClass(GameMainActivity.this,
		// WaitingGameActivity.class);
		// startActivity(intent);
		// } else
		// Toast.makeText(
		// GameMainActivity.this,
		// getResources().getString(
		// R.string.ip_scan_error_str).replace(
		// "#", ipAddr), Toast.LENGTH_LONG).show();
		// }
		// }
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
			doubleClickToExit();
		return false;
	}

	public void doubleClickToExit() {
		++EXIT_TIME;
		if (EXIT_TIME == 2)
			finish();
		showToast(getResources().getString(R.string.exit_game_str), 2000);
		task = new Timer();
		task.schedule(new TimerTask() {
			@Override
			public void run() {
				EXIT_TIME = 0;
			}
		}, 2000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((GameApplication) getApplication()).relese();
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	private static Handler mToastHandler = new Handler();
	private static Runnable toastRunnable = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public void showToast(String text, int duration) {
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(GameMainActivity.this, text,
					Toast.LENGTH_SHORT);
		mToastHandler.postDelayed(toastRunnable, duration);
		mToast.show();
	}
}
