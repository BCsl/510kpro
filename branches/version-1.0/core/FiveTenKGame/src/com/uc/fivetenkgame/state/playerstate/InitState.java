package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * player初始化状态,向server发起连接
 * 
 * @author lm
 * 
 */
public class InitState extends PlayerState {
    String tag = "player InitState";

    public InitState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (msg == null) {// 记录日志，此状态下不能为null
            Log.i(tag, "msg(ip)为null");

        } else {// 根据ip(msg)连接到server
            mPlayerContext.initNetwork(msg);
            mPlayerContext.setState(new ConnectState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
        }
    }

}
