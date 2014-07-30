package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

public class UpdateOthersInfoState extends PlayerState {

    String tag = "updateOthersInfoState";

    public UpdateOthersInfoState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (mCommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来

        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {
            mPlayerContext.playerGiveUp(mCommonMsgDecoder.getPlayerNumber(msg));

        } else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_END)) {
            mPlayerContext.playCardsEndAction(
                    mCommonMsgDecoder.getCardsNumber(msg),
                    String.valueOf(mCommonMsgDecoder.getPlayerNumber(msg)),
                    String.valueOf(mCommonMsgDecoder.getTableScore(msg)),
                    mCommonMsgDecoder.getRemainCards(msg));
            
        }
        mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
        mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
    }

}
