package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

public class WaitForMsgState extends PlayerState {
	String TAG = "WaitForMsgState";

	public WaitForMsgState(PlayerContext player) {
		super(player);
	}

	@Override
	public void handle(String msg) {
		if (msg == null)
			return;

		// 得到出牌信息
		if (msg.startsWith(Common.YOUR_TURN)
				&& msg.substring(2).equals(
						String.valueOf(mPlayerContext.getPlayerNumber()))) {
			mPlayerContext.setMyTurn(true);
			while (!mPlayerContext.doneHandCards()) {
			}
			String cards = mPlayerContext.getCardsToBePlayed();
			Log.i(TAG, "客户端出牌：" + cards);
			if (cards == null){
				mPlayerContext.sendMsg(Common.GIVE_UP);
			}
			else{
				mPlayerContext.sendMsg(Common.PLAY_CARDS + cards);
			}
			mPlayerContext.setMyTurn(false);
			mPlayerContext.setDoneHandCards(false);
		}
		// 得到其他玩家出牌信息
		if (msg.startsWith(Common.PLAY_END)) {
			msg = msg.substring(2, msg.length()).trim();
			String playerNumber = msg.substring(0, 1);

			String str[] = new String((msg.substring(2, msg.length())))
					.split(",");
			if (str == null)
				return;

			String[] outList = new String[str.length - 4];
			for (int i = 0, count = str.length - 4; i < count; i++) {
				outList[i] = str[i];
			}

			String tableScore = new String();
			tableScore = str[str.length - 4];

			String[] remainCards = new String[3];
			remainCards[0] = str[str.length - 3];
			remainCards[1] = str[str.length - 2];
			remainCards[2] = str[str.length - 1];
			Log.i(TAG, tableScore + " ？= " + str[str.length - 4]);

			mPlayerContext.playCardsEndAction(outList, playerNumber,
					tableScore, remainCards);
			mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
			Log.i(TAG, msg);
			return;
		}
		// 得到回合结束信息
		if (msg.startsWith(Common.ROUND_END)) {
			msg = msg.substring(2, msg.length()).trim();
			String[] playerScore = msg.split(",");
			mPlayerContext.roundEndAction(playerScore);
			Log.i(TAG, msg);
			return;
		}
		// 得到游戏结束信息
		if (msg.startsWith(Common.GAME_OVER)) {
			mPlayerContext.setState(new GameOverState(mPlayerContext));
			mPlayerContext.handle(msg.substring(2, 3));
			Log.i(TAG, msg);
			return;
		}
	}
}
