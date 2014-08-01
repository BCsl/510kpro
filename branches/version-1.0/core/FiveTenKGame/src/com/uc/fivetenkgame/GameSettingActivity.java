package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.uc.fivetenkgame.common.SharePerferenceCommon;

public class GameSettingActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup mQrcodeGroup;
	private RadioGroup musicGroup;
	private RadioGroup mConnectionMethodGroup;
	private RadioButton mMusicOpenButton, mMusicCloseButton;
	private RadioButton mScanOpenButton, mScanCloseButton;
	private RadioButton mWifiOpenButton, mBluetoothButton;
	private WifiManager wifiManager;
	private TextView mNameText;
	private AlertDialog mInputNameDialog;
	private View mInputNameDialogView;
	private AutoCompleteTextView mTextViewName;
	private BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mQrcodeGroup = (RadioGroup) findViewById(R.id.qrcode_radiogroup_id);
		mScanOpenButton = (RadioButton) findViewById(R.id.open_qrcode_id);
		mScanCloseButton = (RadioButton) findViewById(R.id.close_qrcode_id);

		musicGroup = (RadioGroup) findViewById(R.id.music_radiogroup_id);
		mMusicOpenButton = (RadioButton) findViewById(R.id.open_radio_id);
		mMusicCloseButton = (RadioButton) findViewById(R.id.close_radio_id);
		
		mConnectionMethodGroup = (RadioGroup) findViewById(R.id.method_radiogroup_id);
		mWifiOpenButton = (RadioButton) findViewById(R.id.open_wifi_id);
		mBluetoothButton = (RadioButton) findViewById(R.id.open_bluetooth_id);
		
		mNameText = (TextView) findViewById(R.id.edit_user_name);
		mQrcodeGroup.setOnCheckedChangeListener(this);
		musicGroup.setOnCheckedChangeListener(this);
		mConnectionMethodGroup.setOnCheckedChangeListener(this);
		mInputNameDialogView = getLayoutInflater().inflate(
				R.layout.dialog_input_name, null);
		mTextViewName = (AutoCompleteTextView) mInputNameDialogView
				.findViewById(R.id.name_autotext_ID);
		initAutoComplete(mTextViewName);
		mInputNameDialog = new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.input_user_name))
				.setView(mInputNameDialogView)
				.setPositiveButton(
						getResources().getString(R.string.confirm_str),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name;
								if ((name = mTextViewName.getText().toString().trim()) != null
										&& name.length() > 0) {
									saveHistory(name);
									mNameText.setText(name);
									saveUserName(name);
								}
							}
						}).create();
		mInputNameDialog.setCancelable(true);
		initData();
		mNameText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInputNameDialog();
			}
		});

	}

	protected void saveUserName(String name) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SharePerferenceCommon.TABLE_SETTING, MODE_PRIVATE);
		sp.edit().putString(SharePerferenceCommon.FIELD_MY_NAME, name).commit();
	}

	private void showInputNameDialog() {
		if (mInputNameDialog != null)
			mInputNameDialog.show();
	}

	private void initData() {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				SharePerferenceCommon.TABLE_SETTING, MODE_PRIVATE);
		if (sp.getBoolean(SharePerferenceCommon.FIELD_QRCODE_FLAG, false))
			mScanOpenButton.setChecked(true);
		else
			mScanCloseButton.setChecked(true);

		if (sp.getBoolean(SharePerferenceCommon.FIELD_MUSIC_FLAG, true))
			mMusicOpenButton.setChecked(true);
		else
			mMusicCloseButton.setChecked(true);

		if (wifiManager == null)
			wifiManager = (WifiManager) getSystemService(Service.WIFI_SERVICE);
			mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
		
		if (sp.getBoolean(SharePerferenceCommon.CONNECT_WAY,true)&&wifiManager.isWifiEnabled()){
			mWifiOpenButton.setChecked(true);
		}
		else{
			mBluetoothButton.setChecked(true);
		}
		mNameText.setText(sp.getString(SharePerferenceCommon.FIELD_MY_NAME,
				"player"));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		boolean flag = false;
		
		if (group == musicGroup) {
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences(SharePerferenceCommon.TABLE_SETTING,
							MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_radio_id:
				flag = true;
				break;
			case R.id.close_radio_id:
				flag = false;
				break;
			default:break;
			}
			sp.edit().putBoolean(SharePerferenceCommon.FIELD_MUSIC_FLAG, flag)
					.commit();

		} else if (group == mQrcodeGroup) {
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences(SharePerferenceCommon.TABLE_SETTING,
							MODE_PRIVATE);
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
			sp.edit().putBoolean(SharePerferenceCommon.FIELD_QRCODE_FLAG, flag)
					.commit();

		} else if (group == mConnectionMethodGroup) {
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences(SharePerferenceCommon.TABLE_SETTING,
							MODE_PRIVATE);
			switch (checkedId) {
			case R.id.open_wifi_id:
				wifiManager.setWifiEnabled(true);
				sp.edit().putBoolean(SharePerferenceCommon.CONNECT_WAY, true).commit();
				break;
			case R.id.open_bluetooth_id:
				mBluetoothAdapter.enable();
				sp.edit().putBoolean(SharePerferenceCommon.CONNECT_WAY, false).commit();
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
		SharedPreferences sp = getSharedPreferences(
				SharePerferenceCommon.TABLE_HISTORY, MODE_PRIVATE);
		String longhistory = sp.getString(SharePerferenceCommon.FIELD_HISTORY,
				"nothing");
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
		SharedPreferences sp = getSharedPreferences(
				SharePerferenceCommon.TABLE_HISTORY, MODE_PRIVATE);
		String longhistory = sp.getString(SharePerferenceCommon.FIELD_HISTORY,
				"nothing");
		if (!longhistory.contains(field + ",")) {
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, field + ",");
			sp.edit()
					.putString(SharePerferenceCommon.FIELD_HISTORY,
							sb.toString()).commit();
		}
	}
}
