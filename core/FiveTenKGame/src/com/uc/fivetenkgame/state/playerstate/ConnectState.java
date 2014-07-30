package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 *  等待server的消息，确定是否连接成功
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
	 * 				成功信号或者失败信号
	 */
	@Override
	public void handle(String msg) {
		if(mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)){//有上一状态（initState）跳转过来，暂不处理
			
		}else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_ACCEPTED)) {// 连接成功，处理msg后跳转到等待开始状态
			Log.i("连接server成功", "玩家号："+mPlayerContext.getPlayerNumber());
			int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			mPlayerContext.setPlayerNumber(playerNumber);//设置玩家序号
			Log.i("send Player Name to server", mPlayerContext.getPlayerName());
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);

		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_REFUSED)) {
			// 连接失败，跳转到开始界面
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL).sendToTarget();
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)){
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
		}
	}
}
