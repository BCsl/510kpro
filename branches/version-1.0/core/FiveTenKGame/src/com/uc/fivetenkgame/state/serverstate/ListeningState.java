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

	//保存玩家的名字
	public String[] playerNames;
	
	public ListeningState(ServerContext context) {
		mServerContext = context;
		playerNames = new String[Common.TOTAL_PLAYER_NUM];
	}

	@Override
	public void handle(String msg) {
		// 玩家链接成功
		if (msg.startsWith(Common.PLAYER_ACCEPTED)) {
			int clientNum = mServerContext.getClientNum();
			++clientNum;
			mServerContext.setClientNum(clientNum);

			if (clientNum <= Common.TOTAL_PLAYER_NUM) {
				mServerContext.getNetworkManager().sendMessage(
						Common.PLAYER_NUMBER_UPDATE + clientNum);
			}
//			else if (clientNum == Common.TOTAL_PLAYER_NUM) {
//				
//			}
		}
		else if (msg.startsWith(Common.GIVE_UP)) {
			Log.i("send game over in listeningState", msg);
			mServerContext.getNetworkManager().sendMessage(Common.GAME_OVER + msg.substring(2,3).trim());
			mServerContext.resetServer();
		}
		else if(msg.startsWith(Common.PLAYER_NAME)) {
			//保存玩家名字
			int playerNumber = Integer.valueOf(msg.substring(3, 4));
			playerNames[playerNumber] = msg.substring(5).trim();
			
			//达到所需的玩家人数开始游戏
			if( mServerContext.getClientNum() == Common.TOTAL_PLAYER_NUM ){
				StringBuilder sb = new StringBuilder();
				
				sb.append(Common.PLAYER_NAME);
				for(String name : playerNames){
					  sb.append(name + ',');
				}
				mServerContext.getNetworkManager().sendMessage(sb.deleteCharAt(sb.length()-1).toString());
				
				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		}
	}

}
