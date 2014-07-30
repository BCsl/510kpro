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
	}

	/**
	 * 
	 * @param msg
	 * 				成功信号或者失败信号
	 */
	@Override
	public void handle(String msg) {
		if(msg==null){//有上一状态（initState）跳转过来，暂不处理
			
		}else if (msg.startsWith(NetworkCommon.PLAYER_ACCEPTED)) {// 连接成功，处理msg后跳转到等待开始状态
			Log.i("连接server成功", "玩家号："+mPlayerContext.getPlayerNumber());
			int playerNumber = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.setPlayerNumber(playerNumber);//设置玩家序号
			mPlayerContext.sendMsg(NetworkCommon.PLAYER_NAME+playerNumber+","+mPlayerContext.getPlayerName());//将玩家名字发给server
			Log.i("send Player Name to server", mPlayerContext.getPlayerName());
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(null);

		} else if (msg.startsWith(NetworkCommon.PLAYER_REFUSED)) {
			// 连接失败，跳转到开始界面
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.HOST_FULL).sendToTarget();
		} else if (msg.startsWith(NetworkCommon.GAME_OVER)){
			mPlayerContext.getHandler().obtainMessage(NetworkCommon.PLAYER_LEFT).sendToTarget();
		}
	}
}
