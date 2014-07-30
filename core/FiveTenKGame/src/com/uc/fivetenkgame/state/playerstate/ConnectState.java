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
	}

	/**
	 * 
	 * @param msg
	 * 				�ɹ��źŻ���ʧ���ź�
	 */
	@Override
	public void handle(String msg) {
		if(msg==null){//����һ״̬��initState����ת�������ݲ�����
			
		}else if (msg.startsWith(NetworkCommon.PLAYER_ACCEPTED)) {// ���ӳɹ�������msg����ת���ȴ���ʼ״̬
			Log.i("����server�ɹ�", "��Һţ�"+mPlayerContext.getPlayerNumber());
			int playerNumber = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.setPlayerNumber(playerNumber);//����������
			mPlayerContext.sendMsg(NetworkCommon.PLAYER_NAME+playerNumber+","+mPlayerContext.getPlayerName());//��������ַ���server
			Log.i("send Player Name to server", mPlayerContext.getPlayerName());
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(null);

		} else if (msg.startsWith(NetworkCommon.PLAYER_REFUSED)) {
			// ����ʧ�ܣ���ת����ʼ����
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL).sendToTarget();
		} else if (msg.startsWith(NetworkCommon.GAME_OVER)){
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
		}
	}
}
