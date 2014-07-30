package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 *  �ȴ�server����Ϣ��ȷ���Ƿ����ӳɹ�
 * @author lm
 *
 */
public class ConnectState extends PlayerState {
	public ConnectState(PlayerContext context) {
		super(context);
		//mThread.start();
	}

	/**
	 * 
	 * @param msg
	 * 				�ɹ��źŻ���ʧ���ź�
	 */
	@Override
	public void handle(String msg) {
		if(mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)){//����һ״̬��initState����ת�������ݲ�����
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_ACCEPTED)) {// ���ӳɹ�������msg����ת���ȴ���ʼ״̬
			Log.i("����server�ɹ�", "��Һţ�"+mPlayerContext.getPlayerNumber());
			int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			mPlayerContext.setPlayerNumber(playerNumber);//����������
			Log.i("send Player Name to server", mPlayerContext.getPlayerName());
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);

		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_REFUSED)) {
			// ����ʧ�ܣ���ת����ʼ����
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL).sendToTarget();
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)){
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
		}
	}
}
