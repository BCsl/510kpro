package com.uc.fivetenkgame.state.serverstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.server.ServerContext;

/**
 * 服务器监听状态
 * 
 * @author liuzd
 *
 */
public class ListeningState extends ServerState {

	public ListeningState(ServerContext context) {
		mServerContext = context;
	}

	@Override
	public void handle(String msg) {
		// 玩家链接成功
		if (msg.startsWith(Common.PLAYER_ACCEPTED)) {
			int clientNum = mServerContext.getClientNum();
			++clientNum;
			mServerContext.setClientNum(clientNum);

			if (clientNum < Common.TOTAL_PLAYER_NUM) {
				mServerContext.getNetworkManager().sendMessage(
						Common.PLAYER_NUMBER_UPDATE + clientNum);
			} else if (clientNum == Common.TOTAL_PLAYER_NUM) {
				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		} else if (msg.startsWith(Common.GIVE_UP)) {
			Log.i("send game over in listeningState", msg);
			mServerContext.getNetworkManager().sendMessage(Common.GAME_OVER + msg.substring(2,3).trim());
			mServerContext.resetServer();
		}
	}

}
