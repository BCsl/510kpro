package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * �˳���Ϸ
 * @author lm
 *
 */
public class ExitState extends PlayerState {

	public ExitState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg
	 * 			 	null
	 */
	@Override
	public void handle(String msg) {
		if(msg==null){//����һ״̬��waitForMsgState)��ת�������˳���Ϸ
			mPlayerContext.getHandler().obtainMessage(Common.END_GAME).sendToTarget();
		}
	}

}
