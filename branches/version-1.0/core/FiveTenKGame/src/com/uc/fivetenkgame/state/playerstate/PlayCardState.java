package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SoundPoolCommon;
import com.uc.fivetenkgame.player.PlayerContext;

public class PlayCardState extends PlayerState {

    String tag = "PlayCardState";
    
    public PlayCardState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来 
            
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_CARDS)) {//自己选完牌
            Log.i(tag, "客户端出牌：" + msg);
            
            mPlayerContext.playSound(SoundPoolCommon.SOUND_OUTPUT_CARDS);
            mPlayerContext.setMyTurn(false);
            mPlayerContext.setDoneHandCards(false);
            mPlayerContext.setState(new WaitForMsgState(mPlayerContext));//在发信息之前切换状态
            mPlayerContext.sendMsg(msg);
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {//自己放弃出牌
            mPlayerContext.playSound(SoundPoolCommon.SOUND_PASS);
            mPlayerContext.setMyTurn(false);
            mPlayerContext.setDoneHandCards(false);
            mPlayerContext.setState(new WaitForMsgState(mPlayerContext));//在发信息之前切换状态
            mPlayerContext.sendMsg(msg);
        }
    }

}
