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
        if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//����һ״̬��ת���� 
            
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_CARDS)) {//�Լ�ѡ����
            Log.i(tag, "�ͻ��˳��ƣ�" + msg);
            
            mPlayerContext.playSound(SoundPoolCommon.SOUND_OUTPUT_CARDS);
            mPlayerContext.setMyTurn(false);
            mPlayerContext.setDoneHandCards(false);
            mPlayerContext.setState(new WaitForMsgState(mPlayerContext));//�ڷ���Ϣ֮ǰ�л�״̬
            mPlayerContext.sendMsg(msg);
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {//�Լ���������
            mPlayerContext.playSound(SoundPoolCommon.SOUND_PASS);
            mPlayerContext.setMyTurn(false);
            mPlayerContext.setDoneHandCards(false);
            mPlayerContext.setState(new WaitForMsgState(mPlayerContext));//�ڷ���Ϣ֮ǰ�л�״̬
            mPlayerContext.sendMsg(msg);
        }
    }

}
