package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

public class SelectCardState extends PlayerState {
    String TAG = "selectCardState";

    public SelectCardState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        if (mCommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来

            mPlayerContext.setMyTurn(true);

            while (!mPlayerContext.doneHandCards()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mPlayerContext.setState(new HandCardState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
        }
    }

}
