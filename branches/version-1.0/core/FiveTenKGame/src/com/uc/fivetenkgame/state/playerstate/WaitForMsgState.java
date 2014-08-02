package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 
 * @author fuyx ,chensl@ucweb.com,lm
 * 
 * 
 */
public class WaitForMsgState extends PlayerState {
	String tag = "WaitForMsgState";

	public WaitForMsgState(PlayerContext player) {
		super(player);
	}

	@Override
	public void handle(String msg) {
	    Log.i(tag,"msg is " + msg);
		if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {//����һ״̬��ת���� 
			
		}else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.YOUR_TURN)) {//�ֵ�ĳ�����
		    int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
            mPlayerContext.setCurrentPlayer(playerNumber);
            // �Լ�����
            if (playerNumber == mPlayerContext.getPlayerNumber()) {
                mPlayerContext.setMyTurn(true);
                
                mPlayerContext.setState(new PlayCardState(mPlayerContext));
                mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
            }
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {//������ҷ�������
            mPlayerContext.playerGiveUp(CommonMsgDecoder.getPlayerNumber(msg));

        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_END)) {//������ҳɹ�����
            mPlayerContext.playCardsEndAction(
                    CommonMsgDecoder.getCards(msg),
                    String.valueOf(CommonMsgDecoder.getPlayerNumber(msg)),
                    String.valueOf(CommonMsgDecoder.getTableScore(msg)),
                    CommonMsgDecoder.getRemainCardNumbers(msg));
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.ROUND_END)) {// �õ��غϽ�����Ϣ
            mPlayerContext.roundEndAction(CommonMsgDecoder.getPlayerScores(msg));
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) { // �õ���Ϸ������Ϣ
            mPlayerContext.setState(new GameOverState(mPlayerContext));
            mPlayerContext.handle(msg);
        } 
    }
}
