package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
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
		if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_STATE_CHANGE)) {// 有上一状态（ConnectState)跳转而来，暂不处理

		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.BEGIN_GAME)) {
			int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			if (mPlayerContext.getPlayerNumber() == playerNumber) {// 是自己的手牌,跳转到下一个状态waitForMsg
				mPlayerContext.getHandler().obtainMessage(NetworkCommon.START_GAME)
						.sendToTarget();
				String[] initCards = mCommonMsgDecoder.getCardsNumber(msg);
                StringBuilder cards = new StringBuilder();
                for (String card : initCards) {
                    cards.append(card);
				}
				mPlayerContext
						.setInitPlayerCards(cards.toString());
				Log.i("初始手牌：", cards.toString());
				mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
				mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
			}
		} else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_NUMBER_UPDATE)) {
			int playerNumber = mCommonMsgDecoder.getPlayerNumber(msg);
			mPlayerContext
					.getHandler()
					.obtainMessage(NetworkCommon.UPDATE_WAITING_PLAYER_NUM,
							playerNumber).sendToTarget();
		} 
//		else if (mCommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_OVER)) {
//			int number = mCommonMsgDecoder.getPlayerNumber(msg);
//			if (number != mPlayerContext.getPlayerNumber())
//				mPlayerContext.getHandler()
//						.obtainMessage(NetworkCommon.PLAYER_LEFT, number)
//						.sendToTarget();
//			mPlayerContext.resetPlayer();
//		}
	}
}
