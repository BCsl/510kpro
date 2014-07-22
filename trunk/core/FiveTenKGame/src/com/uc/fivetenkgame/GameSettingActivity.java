package com.uc.fivetenkgame;

import my.example.fivetenkgame.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.SeekBar;

/**
 * 设置界面类
 * 
 * 通过SharedPreferences设置是否打开音效及音量大小
 * 
 * @author liuzd
 *
 */
public class GameSettingActivity extends Activity {

	private SharedPreferences mSharedPreference;
	private RadioButton mMusicOpenButton;
	private RadioButton mMusicCloseButton;
	private SeekBar mMusicVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		mMusicOpenButton = (RadioButton)findViewById(R.id.open_radio_id);
		mMusicCloseButton = (RadioButton)findViewById(R.id.close_radio_id);
		mMusicVolume = (SeekBar)findViewById(R.id.music_volume_seekBar_id);
		
		mSharedPreference = getSharedPreferences("five_ten_k_game", Activity.MODE_PRIVATE);
		
		//获得设置属性设置控件默认值
		boolean isOpenMusic = mSharedPreference.getBoolean("isOpenMusic", true);
		int volume = mSharedPreference.getInt("musicVolume", 50);
		
		mMusicVolume.setProgress(volume);
		if( isOpenMusic ){
			mMusicOpenButton.setChecked(true);
		}
		else{
			mMusicCloseButton.setChecked(true);
		}
	}

	@Override
	protected void onPause() {

		//保存设置
		SharedPreferences.Editor editor = mSharedPreference.edit();
		editor.clear();
		
		editor.putBoolean("isOpenMusic", mMusicOpenButton.isChecked());
		editor.putInt("musicVolume", mMusicVolume.getProgress());
		
		editor.commit();
		
		super.onPause();
	}

	
}
