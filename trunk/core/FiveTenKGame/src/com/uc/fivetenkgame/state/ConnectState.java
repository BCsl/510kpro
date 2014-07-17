package com.uc.fivetenkgame.state;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.Player;

public class ConnectState implements State{
	String TAG = "ConnectState";
	Player mPlayer;
	
	public ConnectState(Player player) {
		mPlayer = player;
	}
	
	public void handle() {
		mPlayer.getNetworkManager().setOnReceiveMessage(mReceiveMessage);
	}
	
	public OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {
		public void reveiveMessage(String msg) {
			//连接成功
			if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
				mPlayer.getPlayerModel().setPlayerNumber(Integer.parseInt(msg.substring(2,msg.length())));
				mPlayer.setState(new WaitForStartState(mPlayer));
				Log.i(TAG, msg);
			}
			//连接失败
			if( msg.startsWith(Common.PLAYER_REFUSED) ){
				mPlayer.setState(new EndState());
				Log.i(TAG, msg);
			}
		}
	};
}
