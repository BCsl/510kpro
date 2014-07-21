package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
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
			
		}else if (msg.startsWith(Common.PLAYER_ACCEPTED)) {// ���ӳɹ�������msg����ת���ȴ���ʼ״̬
			Log.i("����״̬", msg);
			
			int playerNumber = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.setPlayerNumber(playerNumber);//����������
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(null);

		} else if (msg.startsWith(Common.PLAYER_REFUSED)) {// ����ʧ�ܣ���ת���˳�״̬
			mPlayerContext.setState(new ExitState(mPlayerContext));
			mPlayerContext.handle(null);

		}
	}

}
