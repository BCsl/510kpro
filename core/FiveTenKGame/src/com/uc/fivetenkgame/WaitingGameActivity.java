package com.uc.fivetenkgame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.TCPClient;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class WaitingGameActivity extends Activity {

	private TextView mReadyPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_game);

		
		mReadyPlayer = (TextView)findViewById(R.id.ready_player_text_ID);
		
		
		Intent intent = getIntent();
		boolean isServer = intent.getBooleanExtra("isServer", false);
		if( isServer ){
			Toast.makeText(this, "server", Toast.LENGTH_SHORT).show();
			new Thread(){
				public void run(){
					ServerManager serverMgr = ServerManager.getInstance();
					serverMgr.startListen();
				}
			}.start();
		}
		else{
			final String ipAddr = intent.getStringExtra("IP");
			Toast.makeText(this, "client " + ipAddr, Toast.LENGTH_SHORT).show();
			
			new Thread(){
				public void run(){
					TCPClient.getInstance().initNetwork(ipAddr);
					TCPClient.getInstance().sendMessage("hello");
				}
			}.start();

			
		}
	}

}
