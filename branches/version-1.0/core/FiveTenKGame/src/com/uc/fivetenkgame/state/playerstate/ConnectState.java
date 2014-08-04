package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 等待server的消息，确定是否连接成功
 * 
 * @author lm
 * 
 */
public class ConnectState extends PlayerState {
    String tag = "player ConnectState";

    public ConnectState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {// 由上一状态（initState）跳转过来，暂不处理

        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_ACCEPTED)) {// 连接成功，处理msg后跳转到等待开始状态
            int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            mPlayerContext.setPlayerNumber(playerNumber);// 设置玩家序号
            Log.i(tag,
                    "连接server成功," + "玩家号：" + mPlayerContext.getPlayerNumber());

            mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            mPlayerContext.sendMsg(NetworkCommon.PLAYER_NAME + playerNumber
                    + "," + mPlayerContext.getPlayerName());
            Log.i(tag, "发送玩家名字到server:" + mPlayerContext.getPlayerName());

        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_REFUSED)) {// 连接失败，人数已满
            mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL)
                    .sendToTarget();
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)) {// 连接失败，其他玩家退出
            mPlayerContext.getHandler()
                    .obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
        }
    }
}
