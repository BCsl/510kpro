package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.server.Server;

public class WaitingGameActivity extends Activity {
	private String TAG = "WaitingGameActivity";
	private Player mPlayer;
	private Server mServer;
	
	private TextView mIpAddress;
	private TextView mReadyPlayer;
	private boolean isServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_waiting_game);

		//���ò�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		
		//���ô����Ի�����Ľ��治�رձ��Ի���
		setFinishOnTouchOutside(false);
		
		mIpAddress = (TextView)findViewById(R.id.ip_addr_text_ID);
		mReadyPlayer = (TextView)findViewById(R.id.ready_player_text_ID);
		
		//��ȡ����ʾwifi��ַ
		WifiManager wifiService = (WifiManager)getSystemService(WIFI_SERVICE); 
		WifiInfo    wifiInfo     = wifiService.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String strIp = "" + (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." 
							+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF); 
		mIpAddress.setText(mIpAddress.getText() + strIp);
		
		mPlayer = Player.getInstance();
		mPlayer.setHandler(mHandler);
		
		Intent intent = getIntent();
		isServer = intent.getBooleanExtra("isServer", false);
		//�����Ƿ��Ƿ�������ִ�в�ͬ�Ĳ���
		if( isServer ){
			//mReadyPlayer.setText(getResources().getString(R.string.ready_player_str) + "1��");
			Log.i(TAG, strIp);
			mServer = Server.getInstance();
			mServer.setHandler(mHandler);
			mServer.startListen();
			mPlayer.startPlay("127.0.0.1");
			/*
			new Thread(){
				public void run(){
					//ServerPlayer.getInstance().setHandler(mHandler);
					//ServerPlayer.getInstance().startListen();
				}
			}.start();
			
			new Thread(){
				public void run(){
					//ClientPlayer.getInstance().setHandler(mHandler);
					//ClientPlayer.getInstance().initNetwork("127.0.0.1");
				}
			}.start();
			*/
		}
		else{
			final String ipAddr = intent.getStringExtra("IP");
			mPlayer.startPlay(ipAddr);
			/*
			new Thread(){
				public void run(){
					//ClientPlayer.getInstance().setHandler(mHandler);
					//ClientPlayer.getInstance().initNetwork(ipAddr);
				}
			}.start();
			*/
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ){
			case Common.UPDATE_WAITING_PLAYER_NUM:
				Integer num = (Integer)(msg.obj);
				//Toast.makeText(WaitingGameActivity.this, num+"==", Toast.LENGTH_SHORT).show();
				mReadyPlayer.setText(getResources().getString(R.string.ready_player_str) + num + "��");
				
				break;
			case Common.START_GAME:
				Toast.makeText(WaitingGameActivity.this, 
						getResources().getString(R.string.start_game_str), Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent();
				intent.putExtra("isServer", isServer);
				intent.setClass(WaitingGameActivity.this, GameViewActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
		
	};
}
