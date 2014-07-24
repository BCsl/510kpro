/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.application;

import java.util.HashMap;

import com.uc.fivetenkgame.network.util.Common;

import my.example.fivetenkgame.R;

import android.app.Application;
import android.media.AudioManager;
import android.media.SoundPool;
/**
 * @author chensl@ucweb.com
 *
 * ÏÂÎç2:40:38 2014-7-24
 */
public class GameApplication extends Application {
	private SoundPool soundPool;
	private HashMap<Integer,Integer> soundMap; 
	@Override
	public void onCreate() {
		super.onCreate();
		initSoundPool();
	}
	private void initSoundPool() {
		soundMap=new HashMap<Integer, Integer>();
		 soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		 soundMap.put(Common.SOUND_BUTTON_PRESS, soundPool.load(getApplicationContext(), R.raw.button, 1));
		 soundMap.put(Common.SOUND_GAME_START, soundPool.load(getApplicationContext(), R.raw.start, 1));
		 soundMap.put(Common.SOUND_WIN, soundPool.load(getApplicationContext(), R.raw.win, 1));
		 soundMap.put(Common.SOUND_FAILD, soundPool.load(getApplicationContext(), R.raw.fail, 1));
	}
	public void playSound(int soundKey){
		if (getSharedPreferences(Common.TABLE_SETTING, MODE_PRIVATE)
				.getBoolean(Common.SP_MUSIC_FLAG, true))
		soundPool.play(soundMap.get(soundKey), 1.0f, 1.0f, 0, 0, 1);
	}
	public void relese(){
		if(soundPool!=null)
			soundPool.release();
	}
}
