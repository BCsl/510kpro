package com.uc.fivetenkgame.state;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.Player;

public class SelectCardState implements State{
	String TAG = "SelectCardState";
	Player mPlayer;
		@Override
	public void handle() {
		String cards = mPlayer.getCardsToBePlayed(),
				Msg = null;
		if ( cards == null ){
			Msg = new String(Common.GIVE_UP);
		}else{
			StringBuffer msg = new StringBuffer(Common.PLAY_CARDS);
			msg.append(mPlayer.getCardsToBePlayed());
			Msg = new String(msg);
		}
		mPlayer.getNetworkManager().sendMessage(Msg);
		mPlayer.setState(new WaitForMsgState(mPlayer));
		Log.i(TAG, Msg);
	}

}
