package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.uc.fivetenkgame.network.util.Common;

public class GameSettingActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup qrcodeRadioGroup;
	private RadioGroup musicRadioGroup;
	private RadioGroup wifiRadioGroup;
	private RadioButton musicOpen, musicClose;
	private RadioButton scanOpen, scanClose;
	private RadioButton wifiOpen, wifiClose;
	private WifiManager wifiManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		musicOpen = (RadioButton) findViewById(R.id.open_radio_id);
		musicClose = (RadioButton) findViewById(R.id.close_radio_id);
		scanOpen = (RadioButton) findViewById(R.id.open_qrcode_id);
		scanClose = (RadioButton) findViewById(R.id.close_qrcode_id);
		//wifiOpen = (RadioButton) findViewById(R.id.open_wifi_id);
		//wifiClose = (RadioButton) findViewById(R.id.close_wifi_id);
		qrcodeRadioGroup = (RadioGroup) findViewById(R.id.qrcode_radiogroup_id);
		musicRadioGroup = (RadioGroup) findViewById(R.id.music_radiogroup_id);
		//wifiRadioGroup = (RadioGroup) findViewById(R.id.wifi_radiogroup_id);
		
		qrcodeRadioGroup.setOnCheckedChangeListener(this);
		musicRadioGroup.setOnCheckedChangeListener(this);
		wifiRadioGroup.setOnCheckedChangeListener(this);
		initData();

	}

	private void initData() {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(Common.TABLE_SETTING, MODE_PRIVATE);
		if (sp.getBoolean(Common.SP_QRCODE_FLAG, false))
			scanOpen.setChecked(true);
		else
			scanClose.setChecked(true);

		if (sp.getBoolean(Common.SP_MUSIC_FLAG, true))
			musicOpen.setChecked(true);
		else
			musicClose.setChecked(true);
		
		if(wifiManager==null)
			wifiManager = (WifiManager)getSystemService(Service.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled())
			wifiOpen.setChecked(true);
		else
			wifiClose.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		boolean flag = false;
		if (group == musicRadioGroup) {
			SharedPreferences sp = getApplicationContext().getSharedPreferences(Common.TABLE_SETTING, MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_radio_id:
				flag = true;
				break;
			case R.id.close_radio_id:
				flag = false;
				break;
				default :new IllegalArgumentException("checkedId not find!");
			}
			sp.edit().putBoolean(Common.SP_MUSIC_FLAG, flag).commit();
			
		} else if (group == qrcodeRadioGroup) {
			SharedPreferences sp = getApplicationContext().getSharedPreferences(Common.TABLE_SETTING, MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_qrcode_id:
				flag = true;
				break;
			case R.id.close_qrcode_id:
				flag = false;
				break;
			default :new IllegalArgumentException("checkedId not find!");
			}
			sp.edit().putBoolean(Common.SP_QRCODE_FLAG, flag).commit();
			
//		}else if(group == wifiRadioGroup){
//			switch(checkedId){
//			case R.id.open_wifi_id:
//				wifiManager.setWifiEnabled(true);
//				break;
//			case R.id.close_wifi_id:
//				wifiManager.setWifiEnabled(false);
//				break;
//			default :new IllegalArgumentException("checkedId not find!");
//			}
		}
	}

}
