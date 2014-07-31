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
                mPlayerContext.setMyTurn(true);
                
                while (!mPlayerContext.doneHandCards()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String cards = mPlayerContext.getCardsToBePlayed();
                Log.i(tag, "客户端出牌：" + cards);
                if (cards == null) {
                    mPlayerContext.sendMsg(NetworkCommon.GIVE_UP);
                } else {
                    mPlayerContext.sendMsg(NetworkCommon.PLAY_CARDS + cards);
                }
                mPlayerContext.setMyTurn(false);
                mPlayerContext.setDoneHandCards(false);
            }
        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {
            mPlayerContext.playerGiveUp(mCommonMsgDecoder.getPlayerNumber(msg));

        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_END)) {
            mPlayerContext.playCardsEndAction(
                    mCommonMsgDecoder.getCardsNumber(msg),
                    String.valueOf(mCommonMsgDecoder.getPlayerNumber(msg)),
                    String.valueOf(mCommonMsgDecoder.getTableScore(msg)),
                    mCommonMsgDecoder.getRemainCards(msg));
        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.ROUND_END)) {// 得到回合结束信息
            mPlayerContext.roundEndAction(mCommonMsgDecoder.getPlayerScores(msg));
        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) { // 得到游戏结束信息
            mPlayerContext.setState(new GameOverState(mPlayerContext));
            mPlayerContext.handle(msg);
        } 
    }
}
