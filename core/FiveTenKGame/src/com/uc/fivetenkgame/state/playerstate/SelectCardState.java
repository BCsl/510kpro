package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * Íæ¼Ò³öÅÆ×´Ì¬Àà
 * @author fuyx
 *
 */
public class SelectCardState extends PlayerState {
	String TAG = "SelectCardState";
	
	public SelectCardState(PlayerContext playerContext){
		super(playerContext);
	}
	
	public void handle(String nullMsg) {	
		String cards = mPlayerContext.getCardsToBePlayed(),
				msgToBeSend = null;
		if ( cards == null ){
			msgToBeSend = new String(Common.GIVE_UP);
		}else{
			StringBuffer msg = new StringBuffer(Common.PLAY_CARDS);
			msg.append(cards);
			msgToBeSend = new String(msg);
		}
		mPlayerContext.sendMsg(msgToBeSend);
		Log.i(TAG, msgToBeSend);
		mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
	}

}
