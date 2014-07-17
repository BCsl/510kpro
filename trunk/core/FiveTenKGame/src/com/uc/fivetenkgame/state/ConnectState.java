package com.uc.fivetenkgame.state;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.Player;

public class ConnectState implements State{
	private String TAG = "ConnectState";
	private Player mPlayer;
	private int mPlayerNumber = -1;
	private int playerNumber = 0;
	
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
				//未设置玩家号
				if( mPlayerNumber == -1 ){
					mPlayerNumber = Integer.parseInt(msg.substring(2));
					mPlayer.getPlayerModel().setPlayerNumber(mPlayerNumber);
				}
				playerNumber++;
				if( playerNumber == 3 ){
					mPlayer.setState(new WaitForStartState(mPlayer));
				}
				Log.i(TAG, msg);
			}else if( msg.startsWith(Common.PLAYER_REFUSED) &&
					mPlayerNumber == -1 ){
				//连接失败
				mPlayer.setState(new EndState());
				Log.i(TAG, msg);
			}
		}
	};
}
