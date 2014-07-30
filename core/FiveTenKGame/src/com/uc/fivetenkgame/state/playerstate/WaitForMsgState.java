package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 
 * @author fuyx ,chensl@ucweb.com,lm
 * 
 * 
 */
public class WaitForMsgState extends PlayerState {
	String tag = "WaitForMsgState";

	public WaitForMsgState(PlayerContext player) {
		super(player);
	}

	@Override
	public void handle(String msg) {
	    Log.i(tag,"msg is " + msg);
		if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//����һ״̬��ת���� 
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.YOUR_TURN)) {
		    int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			mPlayerContext.setCurrentPlayer(playerNumber);
			// �Լ�����
			if (playerNumber == mPlayerContext.getPlayerNumber()) {
				mPlayerContext.setState(new SelectCardState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}else{
				mPlayerContext.setState(new UpdateOthersInfoState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}
			
		}  else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.ROUND_END)) {// �õ��غϽ�����Ϣ
			mPlayerContext.roundEndAction(mCommonMsgDecoder.getPlayerScores(msg));
			Log.i("RoundEnd", msg);
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {// �õ���Ϸ������Ϣ
			mPlayerContext.setState(new GameOverState(mPlayerContext));
			mPlayerContext.handle(msg);
			Log.i("GAME_OVER", msg);
		} 
	}
}
