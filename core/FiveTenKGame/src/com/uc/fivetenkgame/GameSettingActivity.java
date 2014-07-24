package com.uc.fivetenkgame;

import com.uc.fivetenkgame.network.util.Common;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GameSettingActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup qrcodeRadiogroup;
	private RadioGroup musicRadiogroup;
	private RadioButton musicOpen, musicClose;
	private RadioButton scanOpen, scanClose;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		musicOpen = (RadioButton) findViewById(R.id.open_radio_id);
		musicClose = (RadioButton) findViewById(R.id.close_radio_id);
		scanOpen = (RadioButton) findViewById(R.id.open_qrcode_id);
		scanClose = (RadioButton) findViewById(R.id.close_qrcode_id);
		qrcodeRadiogroup = (RadioGroup) findViewById(R.id.qrcode_radiogroup_id);
		musicRadiogroup = (RadioGroup) findViewById(R.id.music_radiogroup_id);
		qrcodeRadiogroup.setOnCheckedChangeListener(this);
		musicRadiogroup.setOnCheckedChangeListener(this);
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
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		boolean flag = false;
		if (group == musicRadiogroup) {
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
		} else if (group == qrcodeRadiogroup) {
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
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		

	}

	
}
