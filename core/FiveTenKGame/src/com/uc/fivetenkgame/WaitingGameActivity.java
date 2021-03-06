package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.qrcode.util.QRcodeGenerator;
import com.uc.fivetenkgame.server.Server;

/**
 * 等待开始游戏界面
 * 
 * @author liuzd
 * 
 */
public class WaitingGameActivity extends Activity {
	private String TAG = "WaitingGameActivity";
	private Player mPlayer;
	private Server mServer;
	private float QR_WIDTH;
	private float QR_HEIGHT;
	private TextView mIpAddress;
	private TextView mReadyPlayer;
	private boolean isServer;
	private boolean isConnect = false;
	private static Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_game);
		// 设置不黑屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 设置触摸对话框外的界面不关闭本对话框
		setFinishOnTouchOutside(false);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		QR_WIDTH = dm.widthPixels / 3.0f;
		QR_HEIGHT = QR_WIDTH;
		mIpAddress = (TextView) findViewById(R.id.ip_addr_text_ID);
		mReadyPlayer = (TextView) findViewById(R.id.ready_player_text_ID);
		Intent intent = getIntent();
		isServer = intent.getBooleanExtra("isServer", false);
		// 获取并显示wifi地址
		WifiManager wifiService = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiService.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String strIp = "" + (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
		mIpAddress.setText(mIpAddress.getText() + strIp);
		if (isServer) {
			Bitmap bitmap = QRcodeGenerator.createImage(strIp, (int) QR_WIDTH,
					(int) QR_HEIGHT);
			if (bitmap != null)
				((ImageView) findViewById(R.id.ip_qrcode_ID))
						.setImageBitmap(bitmap);
		}
		mPlayer = Player.getInstance();
		mPlayer.setHandler(mHandler);

		// 根据是否是服务器，执行不同的操作
		if (isServer) {
			Log.i(TAG, strIp);
			mServer = Server.getInstance();
			mServer.setHandler(mHandler);
			mServer.startListen();
			
			mPlayer.startPlay(strIp);
		} else {
			String ipAddr = intent.getStringExtra("IP");
			mPlayer.startPlay(ipAddr);
		}
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Log.i(TAG, "延迟检查连接情况");
				if (!isConnect) {
					finish();
					showToast("连接异常");
				}
			}
		}, 2000);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Common.UPDATE_WAITING_PLAYER_NUM:
				isConnect = true;
				Integer num = (Integer) (msg.obj);
				mReadyPlayer.setText(getResources().getString(
						R.string.ready_player_str)
						+ num + "人");
				break;
			case Common.START_GAME:
				showToast(getResources().getString(R.string.start_game_str));
				Intent intent = new Intent();
				intent.putExtra("isServer", isServer);
				intent.setClass(WaitingGameActivity.this,
						GameViewActivity.class);
				startActivity(intent);
				finish();
				break;
			case Common.HOST_FULL:
				new AlertDialog.Builder(WaitingGameActivity.this)
						.setTitle("人数已满")
						.setMessage("点击确定返回上一页")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
				break;
			case Common.TIME_OUT:
				if (isServer)
					break;
				new AlertDialog.Builder(WaitingGameActivity.this)
						.setTitle(R.string.time_out_str)
						.setMessage("点击确定返回")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
				break;
			case Common.PLAYER_LEFT:
				Integer number = (Integer) (msg.obj);
				Log.i("PLAYER_LEFT:", String.valueOf(number));
				showToast("连接异常");
				finish();
				break;
			}
		}

	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.e(TAG, "这里需要关闭网络！");
			mPlayer.sendMsg(Common.GIVE_UP + mPlayer.getPlayerNumber());
		}
		return super.onKeyDown(keyCode, event);
	};

	private static Handler mToastHandler = new Handler();
	private static Runnable toastRunnable = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public void showToast(String text) {
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(WaitingGameActivity.this, text,
					Toast.LENGTH_SHORT);
		mToastHandler.postDelayed(toastRunnable, 1000);
		mToast.show();
	}
}
