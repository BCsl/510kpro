package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * ���ӳɹ����ȴ�server������Ϸ��ʼ�ź�
 * 
 * @author lm
 *
 */
public class WaitForStartingState extends PlayerState {

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
		if (msg == null) {// ����һ״̬��ConnectState)��ת�������ݲ�����

		} else if (msg.startsWith(Common.BEGIN_GAME)) {
			String playerNumber = msg.substring(Common.BEGIN_GAME.length(),
					msg.indexOf(','));// ԭ��ϢΪ����־ͷ+������,�ƺ�,�ƺ�....
			if (mPlayerContext.getPlayerNumber() == Integer
					.parseInt(playerNumber)) {// ���Լ�������,��ת����һ��״̬waitForMsg
				mPlayerContext.getHandler().obtainMessage(Common.START_GAME)
						.sendToTarget();
				mPlayerContext
						.setInitPlayerCards(msg.substring(4, msg.length()));
				Log.i("��ʼ���ƣ�", msg.substring(4, msg.length()));
				mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
				mPlayerContext.handle(null);
			}
		} else if (msg.startsWith(Common.PLAYER_NUMBER_UPDATE)) {
			int playerNumber = Integer.parseInt(msg.substring(2, 3).trim());
			mPlayerContext
					.getHandler()
					.obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM,
							playerNumber).sendToTarget();
		} else if (msg.startsWith(Common.GAME_OVER)) {
			int number = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.getHandler().obtainMessage(Common.PLAYER_LEFT,number).sendToTarget();
			mPlayerContext.resetPlayer();
		}
	}
}
