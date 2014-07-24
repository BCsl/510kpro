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
		//mThread.start();
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

		} else if (msg.startsWith(Common.PLAYER_REFUSED)) {
			// ����ʧ�ܣ���ת����ʼ����
			mPlayerContext.getHandler().obtainMessage(Common.HOST_FULL).sendToTarget();
		} else if (msg.startsWith(Common.GAME_OVER)){
			mPlayerContext.getHandler().obtainMessage(Common.PLAYER_LEFT).sendToTarget();
		}
	}
	
	private Thread mThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Log.i("TimeOut", null);
			mPlayerContext.getHandler().obtainMessage(Common.TIME_OUT).sendToTarget();;
			mPlayerContext.timeOutAction();
		}
	});
}
