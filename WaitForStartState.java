package com.uc.fivetenkgame.state;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.Player;

/**
 * 等待开始信号状态类
 * @author fuyx
 *
 */
public class WaitForStartState implements State{
	String TAG = "WaitForStartState";
	Player mPlayer;
	//private ArrayList<Card> mCardList;   //写在player中可以减少该状态的复杂度
	
	public WaitForStartState(Player player){
		mPlayer = player;
	}
	
	@Override
	public void handle() {
		mPlayer.getNetworkManager().setOnReceiveMessage(mReceiveMessage);
	}
	
	public OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {
		public void reveiveMessage(String msg) {
			//得到开始信息
			if( msg.startsWith(Common.BEGIN_GAME) &&
					Integer.parseInt( msg.substring(2, 3) ) ==
					mPlayer.getPlayerModel().getPlayerNumber() ){
					mPlayer.setPlayerCards(msg.substring(3, msg.length()));
				}
			mPlayer.setState(new WaitForMsgState(mPlayer));
			Log.i(TAG, msg);
			}
	};
}
