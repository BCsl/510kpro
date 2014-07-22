package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				//注：需检查ip输入的正确性
				bundle.putString("IP", mServerIP.getText().toString());
				intent.putExtras(bundle);
				
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		
	};
}
