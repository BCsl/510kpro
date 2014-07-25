package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 连接成功，等待server发出游戏开始信号
 * 
 * @author lm
 *
 */
public class WaitForStartingState extends PlayerState {

	public WaitForStartingState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg
	 *            server发过来的游戏开始信号，带有玩家序号和牌号
	 */
	@Override
	public void handle(String msg) {
		if (msg == null) {// 有上一状态（ConnectState)跳转而来，暂不处理

		} else if (msg.startsWith(Common.BEGIN_GAME)) {
			String playerNumber = msg.substring(Common.BEGIN_GAME.length(),
					msg.indexOf(','));// 原信息为：标志头+玩家序号,牌号,牌号....
			if (mPlayerContext.getPlayerNumber() == Integer
					.parseInt(playerNumber)) {// 是自己的手牌,跳转到下一个状态waitForMsg
				mPlayerContext.getHandler().obtainMessage(Common.START_GAME)
						.sendToTarget();
				mPlayerContext
						.setInitPlayerCards(msg.substring(4, msg.length()));
				Log.i("初始手牌：", msg.substring(4, msg.length()));
				mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
				mPlayerContext.handle(null);
			}
		} else if (msg.startsWith(Common.PLAYER_NUMBER_UPDATE)) {
			int playerNumber = Integer.parseInt(msg.substring(2, 3).trim());
			mPlayerContext
					.getHandler()
					.obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM,
							playerNumber).sendToTarget();
		} else if (msg.startsWith(Common.GAME_OVER)) {
			int number = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.getHandler().obtainMessage(Common.PLAYER_LEFT,number).sendToTarget();
			mPlayerContext.resetPlayer();
		}
	}
}
