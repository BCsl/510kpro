package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

public class SelectCardsState extends PlayerState{
	String TAG = "SelectCardsState";
	public SelectCardsState(PlayerContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void handle(String nullMsg){
		String cards = mPlayerContext.getCardsToBePlayed(),
				msgToBeSend = null;
		if ( cards == null ){
			msgToBeSend = new String(Common.GIVE_UP);
		}else{
			StringBuffer sb = new StringBuffer(Common.PLAY_CARDS);
			sb.append(cards);
			msgToBeSend = new String(sb);
		}
		mPlayerContext.sendMsg(msgToBeSend);
		mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
		Log.i(TAG, msgToBeSend);
	}

}
