package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameMainActivity extends Activity {

	private static final int REQUEST_SERVER_IP = 90000;
	
	private Button mNewGameButton;
	private Button mJoinGameButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);
		
		mNewGameButton = (Button)findViewById(R.id.main_new_game_id);
		mJoinGameButton = (Button)findViewById(R.id.main_joid_game_id);
		
		mNewGameButton.setOnClickListener(mClickListener);
		mJoinGameButton.setOnClickListener(mClickListener);
	}
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			
			if( v == mNewGameButton ){
				Intent intent = new Intent();
				
				intent.putExtra("isServer", true);
				intent.setClass(GameMainActivity.this, WaitingGameActivity.class);
				startActivity(intent);
			}
			else if( v == mJoinGameButton ){
				Intent intent = new Intent();
				intent.setClass(GameMainActivity.this, InputServerIPActivity.class);
				startActivityForResult(intent, REQUEST_SERVER_IP);
			}
			
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if( requestCode == REQUEST_SERVER_IP ){
			
			if( resultCode == RESULT_OK ){
				Bundle bundle = data.getExtras();
				
				if( bundle != null ){
					String ipAddr = bundle.getString("IP");
					
					Intent intent = new Intent();

					intent.putExtra("isServer", false);
					intent.putExtra("IP", ipAddr);
					intent.setClass(GameMainActivity.this, WaitingGameActivity.class);
					startActivity(intent);
				}
			}
			else{
				System.out.println("input server ip cancel");
			}
			
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}
	
	
}
