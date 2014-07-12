package com.uc.fivetenkgame;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.ClientPlayer;
import com.uc.fivetenkgame.player.ServerPlayer;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class WaitingGameActivity extends Activity {

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
		
		Intent intent = getIntent();
		isServer = intent.getBooleanExtra("isServer", false);
		
		//�����Ƿ��Ƿ�������ִ�в�ͬ�Ĳ���
		if( isServer ){
			mReadyPlayer.setText(getResources().getString(R.string.ready_player_str) + "1��");
			new Thread(){
				public void run(){
					ServerPlayer.getInstance().setHandler(mHandler);
					ServerPlayer.getInstance().startListen();
				}
			}.start();
		}
		else{
			final String ipAddr = intent.getStringExtra("IP");
			
			new Thread(){
				public void run(){
					ClientPlayer.getInstance().setHandler(mHandler);
					ClientPlayer.getInstance().initNetwork(ipAddr);
				}
			}.start();

		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ){
			case Common.UPDATE_WAITING_PLAYER_NUM:
				Integer num = (Integer)(msg.obj);
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
