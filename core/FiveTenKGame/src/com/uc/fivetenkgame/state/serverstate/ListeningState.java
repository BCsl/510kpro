package com.uc.fivetenkgame.state.serverstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.server.ServerContext;

/**
 * 服务器监听状态
 * 
 * @author liuzd
 *
 */
public class ListeningState extends ServerState {

	// 保存玩家的名字
	public String[] playerNames;

	public ListeningState(ServerContext context) {
		mServerContext = context;
		playerNames = new String[NetworkCommon.TOTAL_PLAYER_NUM];
	}

	@Override
	public void handle(String msg) {
		// 玩家链接成功
		if (msg.startsWith(NetworkCommon.PLAYER_ACCEPTED)) {
			int clientNum = mServerContext.getClientNum();
			++clientNum;
			mServerContext.setClientNum(clientNum);

			if (clientNum <= NetworkCommon.TOTAL_PLAYER_NUM) {
				mServerContext.getNetworkManager().sendMessage(
						NetworkCommon.PLAYER_NUMBER_UPDATE + clientNum);
			}
			// else if (clientNum == Common.TOTAL_PLAYER_NUM) {
			//
			// }
		} else if (msg.startsWith(NetworkCommon.GIVE_UP)) {
			Log.i("send game over in listeningState", msg);
			mServerContext.getNetworkManager().sendMessage(
					NetworkCommon.GAME_OVER + msg.substring(2, 3).trim());
			mServerContext.resetServer();
		} else if (msg.startsWith(NetworkCommon.PLAYER_NAME)) {
			// 保存玩家名字
			int playerNumber = Integer.valueOf(msg.substring(3, 4));
			playerNames[playerNumber - 1] = msg.substring(5).trim();
			Log.i("add player name", "player name is: "
					+ playerNames[playerNumber - 1]);
			// 达到所需的玩家人数开始游戏
			if (mServerContext.getClientNum() == NetworkCommon.TOTAL_PLAYER_NUM) {
				StringBuilder sb = new StringBuilder();

				sb.append(NetworkCommon.PLAYER_NAME);
				for (String name : playerNames) {
					sb.append(name + ',');
				}
				mServerContext.getNetworkManager().sendMessage(
						sb.deleteCharAt(sb.length() - 1).toString());

				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		}
	}

}
