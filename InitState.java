package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.player.PlayerContext;

import android.util.Log;

/**
 * ³õÊ¼×´Ì¬Àà
 * @author fuyx
 *
 */
public class InitState implements State{
	String TAG = "InitState";
	private Player mPlayer;
	private String mAddr;
	
	public  InitState(Player player, String addr) {
		mPlayer = player;
		mAddr = addr;
	}
	
	@Override
	public void handle() {
		mPlayer.initNetwork(mAddr);
		Log.i(TAG, "");
		mPlayer.setState(new ConnectState(mPlayer));
	}

}
