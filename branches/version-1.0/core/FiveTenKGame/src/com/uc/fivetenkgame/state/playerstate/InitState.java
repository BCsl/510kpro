package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * player��ʼ��״̬,��server��������
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
        if (msg == null) {// ��¼��־����״̬�²���Ϊnull
            Log.i(tag, "msg(ip)Ϊnull");

        } else {// ����ip(msg)���ӵ�server
            mPlayerContext.initNetwork(msg);
            mPlayerContext.setState(new ConnectState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
        }
    }

}
