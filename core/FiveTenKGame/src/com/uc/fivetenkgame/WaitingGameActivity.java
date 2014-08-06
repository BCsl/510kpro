package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SharePerferenceCommon;
import com.uc.fivetenkgame.network.bluetooth.BluetoothClientManager;
import com.uc.fivetenkgame.network.bluetooth.BluetoothLocalClient;
import com.uc.fivetenkgame.network.bluetooth.BluetoothServerManager;
import com.uc.fivetenkgame.network.wifi.ClientManager;
import com.uc.fivetenkgame.network.wifi.ServerManager;
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
	private float qrcodeWidth;
	private float qrcodeHeight;
	private TextView mIpAddress;
	private TextView mReadyPlayer;
	private boolean isServer;
	private boolean isConnect = false;
	private String mName = null;

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
		qrcodeWidth = dm.widthPixels / 3.0f;
		qrcodeHeight = qrcodeWidth;
		mIpAddress = (TextView) findViewById(R.id.ip_addr_text_ID);
		mReadyPlayer = (TextView) findViewById(R.id.ready_player_text_ID);
		Intent intent = getIntent();
		isServer = intent.getBooleanExtra(NetworkCommon.PLAYER_MODE, false);
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SharePerferenceCommon.TABLE_SETTING, MODE_PRIVATE);
		mName = sp.getString(SharePerferenceCommon.FIELD_MY_NAME, "Player");

		boolean isUseWifi = sp.getBoolean(SharePerferenceCommon.CONNECT_WAY,
				true);

		String strIp = null;

		if (isUseWifi) {
			// 获取并显示wifi地址
			WifiManager wifiService = (WifiManager) getSystemService(WIFI_SERVICE);
			WifiInfo wifiInfo = wifiService.getConnectionInfo();
			int ip = wifiInfo.getIpAddress();
			strIp = "" + (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
					+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
		} else {
			// 蓝牙地址
			strIp = BluetoothAdapter.getDefaultAdapter().getAddress();
		}
		mIpAddress.setText(mIpAddress.getText() + strIp);

		if (isServer) {
			Bitmap bitmap = QRcodeGenerator.createImage(strIp, (int) qrcodeWidth,
					(int) qrcodeHeight);
			if (bitmap != null)
				((ImageView) findViewById(R.id.ip_qrcode_ID))
						.setImageBitmap(bitmap);
		}

		mPlayer = Player.getInstance();
		mPlayer.setContext(getApplicationContext());
		mPlayer.setHandler(mHandler);

		// 根据是否是服务器，执行不同的操作
		if (isServer) {
			Log.i(TAG, strIp);
			mServer = Server.getInstance();
			mServer.setHandler(mHandler);
			if (isUseWifi) {
				mServer.setNetworkManager(ServerManager.getInstance());
			} else {
				// 未打开蓝牙，打开后重新进入本界面
				if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					BluetoothAdapter.getDefaultAdapter().enable();
					Toast.makeText(this,
							getResources().getString(R.string.open_bluetooth),
							Toast.LENGTH_SHORT).show();
					finish();
					return;
				}
				mServer.setNetworkManager(BluetoothServerManager.getInstance());
			}

			// 设置本地玩家
			if (isUseWifi) {
				mPlayer.setNetworkManager(ClientManager.getInstance());
				mServer.startListen();
				mPlayer.startPlay(strIp, mName);
			} else {

				mPlayer.setNetworkManager(BluetoothLocalClient.getInstance());
				mPlayer.startPlay(strIp, mName);
				// 蓝牙本地玩家直接通过函数调用实现，需先设置玩家，再开监听器
				mServer.startListen();
			}

		} else {
			String ipAddr = intent.getStringExtra(NetworkCommon.IP_ADDRESS);
			// 设置本地玩家
			if (isUseWifi) {
				mPlayer.setNetworkManager(ClientManager.getInstance());
			} else {
				mPlayer.setNetworkManager(BluetoothClientManager.getInstance());
			}
			mPlayer.startPlay(ipAddr, mName);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetworkCommon.UPDATE_WAITING_PLAYER_NUM:
				isConnect = true;
				Integer num = (Integer) (msg.obj);
				mReadyPlayer.setText(getResources().getString(
						R.string.ready_player_str)
						+ num);
				break;
			case NetworkCommon.START_GAME:
				Toast.makeText(WaitingGameActivity.this,
						getResources().getString(R.string.start_game_str),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra(NetworkCommon.PLAYER_MODE, isServer);
				intent.setClass(WaitingGameActivity.this,
						GameViewActivity.class);
				startActivity(intent);
				finish();
				break;
			case NetworkCommon.HOST_FULL:
				new AlertDialog.Builder(WaitingGameActivity.this)
						.setTitle(getResources().getString(R.string.full))
						.setMessage(
								getResources().getString(R.string.confirm_back))
						.setPositiveButton(
								getResources().getString(R.string.confirm_str),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
				break;
			case NetworkCommon.TIME_OUT:
				if (isServer)
					break;
				new AlertDialog.Builder(WaitingGameActivity.this)
						.setTitle(R.string.time_out_str)
						.setMessage(
								getResources().getString(R.string.confirm_back))
						.setPositiveButton(
								getResources().getString(R.string.confirm_str),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
				break;
			case NetworkCommon.PLAYER_LEFT:
				Integer number = (Integer) (msg.obj);
				Log.i("PLAYER_LEFT:", String.valueOf(number));
				Toast.makeText(WaitingGameActivity.this,
						getResources().getString(R.string.connect_exception),
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}

	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.e(TAG, "这里需要关闭网络！");
			mPlayer.sendMsg(NetworkCommon.GIVE_UP + mPlayer.getPlayerNumber());
		}
		return super.onKeyDown(keyCode, event);
	};
}
