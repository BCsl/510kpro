package com.uc.fivetenkgame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * �ͻ������������ip��ַ�Ľ��棬����Ϊ�Ի�����ʽ
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
				if( isIPAddress(ipAddr) ){
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
	
	/**
	 * ���IP�Ƿ���Ч
	 * 
	 * @param ipaddr �����IP�ַ���
	 * @return   �Ƿ���Ч
	 */
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
		}
}
