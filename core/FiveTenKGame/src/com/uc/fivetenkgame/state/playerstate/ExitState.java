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
	    if (mCommonMsgDecoder.checkMessage(msg,
                NetworkCommon.PLAYER_STATE_CHANGE)) {//����һ״̬��ת����
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.END_GAME).sendToTarget();
		}
	}

}
