package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * �ȴ�server����Ϣ��ȷ���Ƿ����ӳɹ�
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
                NetworkCommon.PLAYER_STATE_CHANGE)) {// ����һ״̬��initState����ת�������ݲ�����

        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_ACCEPTED)) {// ���ӳɹ�������msg����ת���ȴ���ʼ״̬
            int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            mPlayerContext.setPlayerNumber(playerNumber);// ����������
            Log.i(tag,
                    "����server�ɹ�," + "��Һţ�" + mPlayerContext.getPlayerNumber());

            mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            mPlayerContext.sendMsg(NetworkCommon.PLAYER_NAME + playerNumber
                    + "," + mPlayerContext.getPlayerName());
            Log.i(tag, "����������ֵ�server:" + mPlayerContext.getPlayerName());

        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_REFUSED)) {// ����ʧ�ܣ���������
            mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL)
                    .sendToTarget();
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)) {// ����ʧ�ܣ���������˳�
            mPlayerContext.getHandler()
                    .obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
        }
    }
}
