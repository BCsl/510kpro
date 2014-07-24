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
				// 更新等待界面人数
				// mServerContext.getHandler().obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM,
				// clientNum).sendToTarget();
				mServerContext.getNetworkManager().sendMessage(
						Common.PLAYER_NUMBER_UPDATE + clientNum);
			} else if (clientNum == Common.TOTAL_PLAYER_NUM) {
				// 够玩家人数，开始游戏
				// mServerContext.getHandler().obtainMessage(Common.START_GAME).sendToTarget();
				// mServerContext.getNetworkManager().sendMessage(Common.BEGIN_GAME);
				// mServerContext.getNetworkManager().sendMessage(Common.PLAYER_NUMBER_UPDATE
				// + clientNum);
				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		} else if (msg.startsWith(Common.GIVE_UP)) {
			Log.i("send give up in listeningState", msg);
			mServerContext.getNetworkManager().sendMessage(Common.GAME_OVER);
			mServerContext.resetServer();
		}
	}

}
