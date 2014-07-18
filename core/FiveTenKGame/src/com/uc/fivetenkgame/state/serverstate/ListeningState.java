package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.server.ServerContext;

public class ListeningState extends ServerState {

	public ListeningState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		//玩家链接成功
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			int clientNum = mServerContext.getClientNum();
			++clientNum;
			mServerContext.setClientNum(clientNum);
						
			if( clientNum < Common.TOTAL_PLAYER_NUM ){
				//更新等待界面人数
				//mServerContext.getHandler().obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM, clientNum).sendToTarget();
				mServerContext.getNetworkManager().sendMessage(Common.PLAYER_NUMBER_UPDATE+clientNum);
			}
			else if( clientNum == Common.TOTAL_PLAYER_NUM ){
				//够玩家人数，开始游戏
				//mServerContext.getHandler().obtainMessage(Common.START_GAME).sendToTarget();
				mServerContext.getNetworkManager().sendMessage(Common.BEGIN_GAME);
				mServerContext.setState(new GameStartState(mServerContext));
			}
			
			
		}
	}
	
}
