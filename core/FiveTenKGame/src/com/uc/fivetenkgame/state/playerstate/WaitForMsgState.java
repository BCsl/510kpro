package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
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
		if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来 
			
		}else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.YOUR_TURN)) {//轮到某个玩家
		    int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            mPlayerContext.setCurrentPlayer(playerNumber);
            // 自己出牌
            if (playerNumber == mPlayerContext.getPlayerNumber()) {
                mPlayerContext.setMyTurn(true);
                
                mPlayerContext.setState(new PlayCardState(mPlayerContext));
                mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            }
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {//其他玩家放弃出牌
            mPlayerContext.playerGiveUp(CommonMsgDecoder.getPlayerNumber(msg));

        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_END)) {//其他玩家成功出牌
            mPlayerContext.playCardsEndAction(
                    CommonMsgDecoder.getCards(msg),
                    String.valueOf(CommonMsgDecoder.getPlayerNumber(msg)),
                    String.valueOf(CommonMsgDecoder.getTableScore(msg)),
                    CommonMsgDecoder.getRemainCardNumbers(msg));
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.ROUND_END)) {// 得到回合结束信息
            mPlayerContext.roundEndAction(CommonMsgDecoder.getPlayerScores(msg));
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) { // 得到游戏结束信息
            mPlayerContext.setState(new GameOverState(mPlayerContext));
            mPlayerContext.handle(msg);
        } 
    }
}
