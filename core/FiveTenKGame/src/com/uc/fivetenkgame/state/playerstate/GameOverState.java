package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * ������Ϸ��������ѡ���˳�������
 * 
 * @author lm
 * 
 */
public class GameOverState extends PlayerState {

    private String tag = "player GameOverState";
    private String[] winningInfo;// ����Ӯ�Һź͸���ҷ���

    public GameOverState(PlayerContext context) {
        super(context);
    }

    @Override
    public void handle(String msg) {
        Log.i(tag, "msg is " + msg);
        if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) { // ����һ״̬��WaitForMsg����ת�����������ѡ���˳�������
            String[] scores = CommonMsgDecoder.getPlayerScores(msg);
            String[] msgs = new String[scores.length + 1];
            msgs[0] = String.valueOf(CommonMsgDecoder.getWinnerNumber(msg));
            for (int i = 0; i < scores.length; i++) {
                msgs[i + 1] = scores[i];
            }
            mPlayerContext.gameOver(msgs);
            winningInfo = msgs;
            mPlayerContext.setRestart(false);
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_AGAIN)) { // ����
            mPlayerContext.setRestart(true);
            mPlayerContext.reStartGame(winningInfo);
            mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
            mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            mPlayerContext.getHandler()
                    .obtainMessage(NetworkCommon.PLAY_RESTART).sendToTarget();
        }
    }

}
