package com.uc.fivetenkgame;

import java.util.Timer;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.qrcode.util.QRcodeGenerator;
import com.uc.fivetenkgame.server.Server;

/**
 * �ȴ���ʼ��Ϸ����
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
	private int currentPlayerNumber;
	private boolean isConnect = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_game);
		// ���ò�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// ���ô����Ի�����Ľ��治�رձ��Ի���
		setFinishOnTouchOutside(false);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		QR_WIDTH = dm.widthPixels / 2.5f;
		QR_HEIGHT = QR_WIDTH;
		mIpAddress = (TextView) findViewById(R.id.ip_addr_text_ID);
		mReadyPlayer = (TextView) findViewById(R.id.ready_player_text_ID);
		Intent intent = getIntent();
		isServer = intent.getBooleanExtra("isServer", false);
		// ��ȡ����ʾwifi��ַ
		WifiManager wifiService = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiService.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String strIp = "" + (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
		mIpAddress.setText(mIpAddress.getText() + strIp);
		if (isServer) {
			Drawable bd = new BitmapDrawable(QRcodeGenerator.createImage(strIp,
					(int) QR_WIDTH, (int) QR_HEIGHT));
			bd.setBounds(0, 0, bd.getMinimumWidth(), bd.getMinimumHeight());
			if (bd != null) {
				Log.i(TAG, "bitmap ��Ϊ��");
				mIpAddress.setCompoundDrawables(null, null, null, bd);
			}
		}
		mPlayer = Player.getInstance();
		mPlayer.setHandler(mHandler);

		// �����Ƿ��Ƿ�������ִ�в�ͬ�Ĳ���
		if (isServer) {
			Log.i(TAG, strIp);
			mServer = Server.getInstance();
			mServer.setHandler(mHandler);
			mServer.startListen();
			mPlayer.startPlay("127.0.0.1");
		} else {
			String ipAddr = intent.getStringExtra("IP");
			mPlayer.startPlay(ipAddr);
		}
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Log.i(TAG,"�ӳټ���������");
				if (!isConnect) {
					finish();
					Toast.makeText(WaitingGameActivity.this, "�����쳣",
							Toast.LENGTH_SHORT).show();
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
				currentPlayerNumber = num;
				Log.i("currentPlayerNumber:",String.valueOf(currentPlayerNumber));
				mReadyPlayer.setText(getResources().getString(
						R.string.ready_player_str)
						+ num + "��");
				break;
			case Common.START_GAME:
				Toast.makeText(WaitingGameActivity.this,
						getResources().getString(R.string.start_game_str),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("isServer", isServer);
				intent.setClass(WaitingGameActivity.this,
						GameViewActivity.class);
				startActivity(intent);
				finish();
				break;
			case Common.HOST_FULL:
				new AlertDialog.Builder(WaitingGameActivity.this)
						.setTitle("��������")
						.setMessage("���ȷ��������һҳ")
						.setPositiveButton("ȷ��",
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
						.setMessage("���ȷ������")
						.setPositiveButton("ȷ��",
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
				if (number != currentPlayerNumber)
					Toast.makeText(WaitingGameActivity.this, "�����쳣",
							Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}

	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.e(TAG, "������Ҫ�ر����磡");
			mPlayer.sendMsg(Common.GIVE_UP + mPlayer.getPlayerNumber());
			currentPlayerNumber = mPlayer.getPlayerNumber();
		}
		return super.onKeyDown(keyCode, event);
	};
}
