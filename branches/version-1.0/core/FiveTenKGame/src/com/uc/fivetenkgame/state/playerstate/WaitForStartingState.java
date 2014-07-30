package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * ���ӳɹ����ȴ�server������Ϸ��ʼ�ź�
 * 
 * @author lm
 *
 */
public class WaitForStartingState extends PlayerState {

    String tag = "waitForStartingState";
    
	public WaitForStartingState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg
	 *            server����������Ϸ��ʼ�źţ����������ź��ƺ�
	 */
	@Override
	public void handle(String msg) {
	    Log.i(tag,"msg is " + msg);
		if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {// ����һ״̬��ConnectState)��ת�������ݲ�����

		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_NAME)) {
		    String[] playerNames = mCommonMsgDecoder.getPlayerNames(msg);
		    for (int i=1;i<=NetworkCommon.TOTAL_PLAYER_NUM;i++){
		        mPlayerContext.setPlayersName(i, playerNames[i-1]);
		    }
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.BEGIN_GAME)) {
			int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			if (mPlayerContext.getPlayerNumber() == playerNumber) {// ���Լ�������,��ת����һ��״̬waitForMsg
				mPlayerContext.getHandler().obtainMessage(NetworkCommon.START_GAME)
						.sendToTarget();
				String[] initCards = mCommonMsgDecoder.getCardsNumber(msg);
                StringBuilder cards = new StringBuilder();
                for (String card : initCards) {
                    cards.append(card+",");
				}
                cards.deleteCharAt(cards.length()-1);
				mPlayerContext
						.setInitPlayerCards(cards.toString());
				Log.i("��ʼ���ƣ�", cards.toString());
				mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_NUMBER_UPDATE)) {
			int playerNumber = mCommonMsgDecoder.getUpdatePlayerNumber(msg);
			mPlayerContext
					.getHandler()
					.obtainMessage(NetworkCommon.UPDATE_WAITING_PLAYER_NUM,
							playerNumber).sendToTarget();
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {
			int number = mCommonMsgDecoder.getPlayerNumber(msg);
			if (number != mPlayerContext.getPlayerNumber())
				mPlayerContext.getHandler()
						.obtainMessage(NetworkCommon.PLAYER_LEFT, number)
						.sendToTarget();
			mPlayerContext.resetPlayer();
		}
	}
}
