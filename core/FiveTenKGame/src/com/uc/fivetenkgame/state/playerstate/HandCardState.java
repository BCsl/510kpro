package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

public class HandCardState extends PlayerState {
	String TAG = "handCardState";
	
	public HandCardState(PlayerContext context) {
		super(context);
	}

	@Override
	public void handle(String msg) {
	    if (mCommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {//����һ״̬��ת����
			String cards = mPlayerContext.getCardsToBePlayed();
			Log.i(TAG, "�ͻ��˳��ƣ�" + cards);
			if (cards == null) {
				mPlayerContext.sendMsg(NetworkCommon.GIVE_UP);
			} else {
				mPlayerContext.sendMsg(NetworkCommon.PLAY_CARDS + cards);
			}
			mPlayerContext.setMyTurn(false);
			mPlayerContext.setDoneHandCards(false);
			mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
			mPlayerContext.handle(null);
		}
	}

}
