package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * ������Ϸ��������ѡ���˳�������
 * @author lm
 *
 */
public class GameOverState extends PlayerState {

	public GameOverState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg Ӯ�����
	 */
	@Override
	public void handle(String msg) {
<<<<<<< .mine
		if(msg.startsWith(Common.GAME_OVER)){ //����һ״̬��WaitForMsg����ת�����������ѡ���˳�������
			mPlayerContext.gameOver(Integer.parseInt(msg.substring(2, 3)));
=======
		if(msg==null){ //��¼��־����״̬�²���Ϊnull
			 Log.i("PlayerGameOverState","msgΪnull");
			
		}else{
//			mPlayerContext.gameOver(Integer.parseInt(msg));
			mPlayerContext.getHandler().obtainMessage(Common.END_GAME, Integer.parseInt(msg)).sendToTarget();
>>>>>>> .r128
			Log.i("GameOverState", msg);
		}
	}

}
