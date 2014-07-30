package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 退出游戏
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
		if(msg==null){//有上一状态（waitForMsgState)跳转而来，退出游戏
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.END_GAME).sendToTarget();
		}
	}

}
