package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.uc.fivetenkgame.common.SharePreferenceCommon;

public class GameSettingActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup qrcodeRadioGroup;
	private RadioGroup musicRadioGroup;
	private RadioGroup wifiRadioGroup;
	private RadioButton musicOpen, musicClose;
	private RadioButton scanOpen, scanClose;
	private RadioButton wifiOpen, wifiClose;
	private WifiManager wifiManager;
	private TextView nameText;
	private AlertDialog inputNameDialog;
	private View inputNameDialogView;
	private AutoCompleteTextView tvName;
	private String deviceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		musicOpen = (RadioButton) findViewById(R.id.open_radio_id);
		musicClose = (RadioButton) findViewById(R.id.close_radio_id);
		scanOpen = (RadioButton) findViewById(R.id.open_qrcode_id);
		scanClose = (RadioButton) findViewById(R.id.close_qrcode_id);
		wifiOpen = (RadioButton) findViewById(R.id.open_wifi_id);
		wifiClose = (RadioButton) findViewById(R.id.close_wifi_id);
		qrcodeRadioGroup = (RadioGroup) findViewById(R.id.qrcode_radiogroup_id);
		musicRadioGroup = (RadioGroup) findViewById(R.id.music_radiogroup_id);
		wifiRadioGroup = (RadioGroup) findViewById(R.id.wifi_radiogroup_id);
		nameText = (TextView) findViewById(R.id.edit_user_name);
		qrcodeRadioGroup.setOnCheckedChangeListener(this);
		musicRadioGroup.setOnCheckedChangeListener(this);
		wifiRadioGroup.setOnCheckedChangeListener(this);
		inputNameDialogView = getLayoutInflater().inflate(
				R.layout.dialog_input_name, null);
		tvName = (AutoCompleteTextView) inputNameDialogView
				.findViewById(R.id.name_autotext_ID);
		initAutoComplete(tvName);
		Build bulid=new Build();
		deviceName=bulid.MODEL.length()>6?bulid.MODEL.substring(0,5):bulid.MODEL;
		Log.i("GameSettingActivity", deviceName);
		inputNameDialog = new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.input_user_name))
				.setView(inputNameDialogView)
				.setPositiveButton(
						getResources().getString(R.string.confirm_str),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name;
								if((name=tvName.getText().toString().trim())!=null &&name.length()>0){
									saveHistory(name);
									nameText.setText(name);
									saveUserName(name);
								}
							}
						}).create();
		inputNameDialog.setCancelable(true);
		initData();
		nameText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInputNameDialog();
			}
		});

	}
	protected void saveUserName(String name) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SharePreferenceCommon.TABLE_SETTING, MODE_PRIVATE);
		sp.edit().putString(SharePreferenceCommon.MY_NAME, name).commit();
	}
	private void showInputNameDialog() {
		if (inputNameDialog != null)
			inputNameDialog.show();
	}

	private void initData() {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SharePreferenceCommon.TABLE_SETTING, MODE_PRIVATE);
		if (sp.getBoolean(SharePreferenceCommon.SP_QRCODE_FLAG, false))
			scanOpen.setChecked(true);
		else
			scanClose.setChecked(true);

		if (sp.getBoolean(SharePreferenceCommon.SP_MUSIC_FLAG, true))
			musicOpen.setChecked(true);
		else
			musicClose.setChecked(true);

		if (wifiManager == null)
			wifiManager = (WifiManager) getSystemService(Service.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled())
			wifiOpen.setChecked(true);
		else
			wifiClose.setChecked(true);
		nameText.setText(sp.getString(SharePreferenceCommon.MY_NAME,deviceName));
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		boolean flag = false;
		if (group == musicRadioGroup) {
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences(SharePreferenceCommon.TABLE_SETTING, MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_radio_id:
				flag = true;
				break;
			case R.id.close_radio_id:
				flag = false;
				break;
			default:
				new IllegalArgumentException("checkedId not find!");
			}
			sp.edit().putBoolean(SharePreferenceCommon.SP_MUSIC_FLAG, flag).commit();

		} else if (group == qrcodeRadioGroup) {
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences(SharePreferenceCommon.TABLE_SETTING, MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_qrcode_id:
				flag = true;
				break;
			case R.id.close_qrcode_id:
				flag = false;
				break;
			default:
				new IllegalArgumentException("checkedId not find!");
			}
			sp.edit().putBoolean(SharePreferenceCommon.SP_QRCODE_FLAG, flag).commit();

		} else if (group == wifiRadioGroup) {
			switch (checkedId) {
			case R.id.open_wifi_id:
				wifiManager.setWifiEnabled(true);
				break;
			case R.id.close_wifi_id:
				wifiManager.setWifiEnabled(false);
				break;
			default:
				new IllegalArgumentException("checkedId not find!");
			}
		}
	}

	/**
	 * 初始化AutoCompleteTextView，最多显示3项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param auto
	 *            要操作的AutoCompleteTextView
	 */
	private void initAutoComplete(AutoCompleteTextView auto) {
		SharedPreferences sp = getSharedPreferences(SharePreferenceCommon.TABLE_HISTORY,
				MODE_PRIVATE);
		String longhistory = sp.getString(SharePreferenceCommon.SP_HISTORY, "nothing");
		String[] hisArrays = longhistory.split(",");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, hisArrays);
		// 只保留最近的10条的记录
		if (hisArrays.length > 10) {
			String[] newArrays = new String[10];
			System.arraycopy(hisArrays, 0, newArrays, 0, 10);
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, newArrays);
		}
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		auto.setAdapter(adapter);
		auto.setDropDownHeight((int) ((float) dm.widthPixels / 1.5f));
		auto.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView view = (AutoCompleteTextView) v;
				if (hasFocus) {
					view.showDropDown();
				}
			}
		});
	}

	/**
	 * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 */
	private void saveHistory(String field) {
		SharedPreferences sp = getSharedPreferences(SharePreferenceCommon.TABLE_HISTORY,
				MODE_PRIVATE);
		String longhistory = sp.getString(SharePreferenceCommon.SP_HISTORY, "nothing");
		if (!longhistory.contains(field + ",")) {
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, field + ",");
			sp.edit().putString(SharePreferenceCommon.SP_HISTORY, sb.toString()).commit();
		}
	}
}
