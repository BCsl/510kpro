package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 连接成功，等待server发出游戏开始信号
 * 
 * @author lm
 * 
 */
public class WaitForStartingState extends PlayerState {

    String tag = "player WaitForStartingState";

    public WaitForStartingState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {// 由上一状态（ConnectState或GameOverState)跳转而来，暂不处理

        } else if (CommonMsgDecoder
                .checkMessage(msg, NetworkCommon.PLAYER_NAME)) {// 各玩家名字
            String[] playerNames = CommonMsgDecoder.getPlayerNames(msg);
            mPlayerContext.setPlayersName(playerNames);

        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.BEGIN_GAME)) {// 开始发牌
            int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            if (mPlayerContext.getPlayerNumber() == playerNumber) {// 是自己的手牌,跳转到下一个状态waitForMsg
                mPlayerContext.getHandler()
                        .obtainMessage(NetworkCommon.START_GAME).sendToTarget();
                String[] initCards = CommonMsgDecoder.getCards(msg);
                StringBuilder cards = new StringBuilder();
                for (String card : initCards) {
                    cards.append(card).append(',');
                }
                cards.deleteCharAt(cards.length() - 1);
                mPlayerContext.setInitPlayerCards(cards.toString());
                Log.i(tag, "初始手牌：" + cards.toString());
                if (mPlayerContext.isRestart()) {
                    mPlayerContext.initView();
                }
                mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
                mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            }
            
        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_NUMBER_UPDATE)) {// 更新当前玩家人数
            int playerNumber = CommonMsgDecoder.getUpdatePlayerNumber(msg);
            mPlayerContext
                    .getHandler()
                    .obtainMessage(NetworkCommon.UPDATE_WAITING_PLAYER_NUM,
                            playerNumber).sendToTarget();
            
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {// 游戏结束，有玩家退出
            int number = CommonMsgDecoder.getPlayerNumber(msg);
            if (number != mPlayerContext.getPlayerNumber())
                mPlayerContext.getHandler()
                        .obtainMessage(NetworkCommon.PLAYER_LEFT, number)
                        .sendToTarget();
            mPlayerContext.resetPlayer();
        }
    }
}
