package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

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
			
		}else{ //����һ״̬��WaitForMsg����ת�����������ѡ���˳�������
			mPlayerContext.gameOver(Integer.parseInt(msg));
			Log.i("GameOverState", msg);
		}
	}

}
