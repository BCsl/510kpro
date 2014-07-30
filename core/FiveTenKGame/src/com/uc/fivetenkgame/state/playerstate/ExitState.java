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
	    if (mCommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {//由上一状态跳转而来
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.END_GAME).sendToTarget();
		}
	}

}
