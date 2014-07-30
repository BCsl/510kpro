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
		if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来 
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.YOUR_TURN)) {
		    int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			mPlayerContext.setCurrentPlayer(playerNumber);
			// 自己出牌
			if (playerNumber == mPlayerContext.getPlayerNumber()) {
				mPlayerContext.setState(new SelectCardState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}else{
				mPlayerContext.setState(new UpdateOthersInfoState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}
			
		}  else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.ROUND_END)) {// 得到回合结束信息
			mPlayerContext.roundEndAction(mCommonMsgDecoder.getPlayerScores(msg));
			Log.i("RoundEnd", msg);
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {// 得到游戏结束信息
			mPlayerContext.setState(new GameOverState(mPlayerContext));
			mPlayerContext.handle(msg);
			Log.i("GAME_OVER", msg);
		} 
	}
}
