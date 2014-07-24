package com.uc.fivetenkgame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uc.fivetenkgame.network.util.Common;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 客户端输入服务器ip地址的界面，主题为对话框形式
 * 
 * @author liuzd
 *
 */
public class InputServerIPActivity extends Activity {

	private EditText mServerIP;
	private Button mOKButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_server_ip);
		
		mServerIP = (EditText)findViewById(R.id.server_ip_text_ID);
		mOKButton = (Button)findViewById(R.id.ok_button_ID);
		
		mOKButton.setOnClickListener(mClickListener);
	}

	private OnClickListener mClickListener = new OnClickListener(){

		public void onClick(View v) {
			if( v == mOKButton ){
				String ipAddr = mServerIP.getText().toString();
				if( Common.isIPAddress(ipAddr) ){
					Intent intent = getIntent();
					Bundle bundle = new Bundle();
					bundle.putString("IP", ipAddr);
					intent.putExtras(bundle);
					
					setResult(RESULT_OK, intent);
					finish();
				}
				else{
					Toast.makeText(InputServerIPActivity.this, 
							getResources().getString(R.string.ip_input_error_str), 
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	

}
