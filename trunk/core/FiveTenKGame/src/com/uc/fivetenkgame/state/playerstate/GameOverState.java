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
		if(msg==null){ //��¼��־����״̬�²���Ϊnull
			 Log.i("PlayerGameOverState","msgΪnull");
			
		}else{
//			mPlayerContext.gameOver(Integer.parseInt(msg));
			mPlayerContext.getHandler().obtainMessage(Common.END_GAME, Integer.parseInt(msg)).sendToTarget();
			Log.i("GameOverState", msg);
		}
	}

}
