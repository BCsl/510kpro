package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 本次游戏结束，可选择退出或重玩
 * 
 * @author lm
 * 
 */
public class GameOverState extends PlayerState {

    public GameOverState(PlayerContext context) {
        super(context);
    }

    /**
     * 
     * @param msg
     *            赢家序号
     */
    @Override
    public void handle(String msg) {
        if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) { // 有上一状态（WaitForMsg）跳转而来，让玩家选择退出或重玩
            String[] scores = mCommonMsgDecoder.getPlayerScores(msg);
            String[] msgs = new String[scores.length + 1];
            msgs[0] = String.valueOf(mCommonMsgDecoder.getWinnerNumber(msg));
            for (int i = 0; i < scores.length; i++) {
                msgs[i + 1] = scores[i];
            }
            mPlayerContext.gameOver(msgs);
            Log.i("GameOverState", msg);
        }
    }

}
