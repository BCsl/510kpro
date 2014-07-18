package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.network.util.Common;
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
			
		}else if (msg.startsWith(Common.PLAYER_ACCEPTED)) {// 连接成功，处理msg后跳转到等待开始状态
			int playerNumber = Integer.parseInt(msg
					.substring(Common.PLAYER_ACCEPTED.length()));
			mPlayerContext.setPlayerNumber(playerNumber);//设置玩家序号
			mPlayerContext
					.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(null);

		} else if (msg.startsWith(Common.PLAYER_REFUSED)) {// 连接失败，跳转到退出状态
			mPlayerContext.setState(new ExitState(mPlayerContext));
			mPlayerContext.handle(null);

		}
	}

}
