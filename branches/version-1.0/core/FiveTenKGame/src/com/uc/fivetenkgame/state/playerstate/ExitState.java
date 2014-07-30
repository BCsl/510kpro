package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.common.NetworkCommon;
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
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.END_GAME).sendToTarget();
		}
	}

}
