package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * ���ӳɹ����ȴ�server������Ϸ��ʼ�ź�
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
                NetworkCommon.PLAYER_STATE_CHANGE)) {// ����һ״̬��ConnectState��GameOverState)��ת�������ݲ�����

        } else if (CommonMsgDecoder
                .checkMessage(msg, NetworkCommon.PLAYER_NAME)) {// ���������
            String[] playerNames = CommonMsgDecoder.getPlayerNames(msg);
            mPlayerContext.setPlayersName(playerNames);

        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.BEGIN_GAME)) {// ��ʼ����
            int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            if (mPlayerContext.getPlayerNumber() == playerNumber) {// ���Լ�������,��ת����һ��״̬waitForMsg
                mPlayerContext.getHandler()
                        .obtainMessage(NetworkCommon.START_GAME).sendToTarget();
                String[] initCards = CommonMsgDecoder.getCards(msg);
                StringBuilder cards = new StringBuilder();
                for (String card : initCards) {
                    cards.append(card).append(',');
                }
                cards.deleteCharAt(cards.length() - 1);
                mPlayerContext.setInitPlayerCards(cards.toString());
                Log.i(tag, "��ʼ���ƣ�" + cards.toString());
                if (mPlayerContext.isRestart()) {
                    mPlayerContext.initView();
                }
                mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
                mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            }
            
        } else if (CommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_NUMBER_UPDATE)) {// ���µ�ǰ�������
            int playerNumber = CommonMsgDecoder.getUpdatePlayerNumber(msg);
            mPlayerContext
                    .getHandler()
                    .obtainMessage(NetworkCommon.UPDATE_WAITING_PLAYER_NUM,
                            playerNumber).sendToTarget();
            
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {// ��Ϸ������������˳�
            int number = CommonMsgDecoder.getPlayerNumber(msg);
            if (number != mPlayerContext.getPlayerNumber())
                mPlayerContext.getHandler()
                        .obtainMessage(NetworkCommon.PLAYER_LEFT, number)
                        .sendToTarget();
            mPlayerContext.resetPlayer();
        }
    }
}
