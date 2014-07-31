package com.uc.fivetenkgame.state.serverstate;

import java.io.File;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
/**
 * 
 * @author chensl@ucweb.com
 *
 *  ÏÂÎç4:59:13 2014-7-17
 */
public class GameEndState extends ServerState {

	private int playAgainNum = 0;
	

	public GameEndState(ServerContext mServerContext) {
		this.mServerContext=mServerContext;
	}

	@Override
	public void handle(String msg) {
		if (msg.equals(NetworkCommon.GAME_END)) {
			int id = winnerId();
			StringBuilder sb=new StringBuilder();
			sb.append(NetworkCommon.GAME_OVER);
			sb.append(id+",");
			for(PlayerModel model:mServerContext.getPlayerModel())
					sb.append(model.getScore()+",");
			mServerContext.getNetworkManager().sendMessage(sb.deleteCharAt(sb.length()-1).toString());
		}
		else if( msg.startsWith(NetworkCommon.PLAY_AGAIN) ){
			++playAgainNum;
			if( playAgainNum == NetworkCommon.TOTAL_PLAYER_NUM ){
			    mServerContext.getNetworkManager().sendMessage(NetworkCommon.PLAY_AGAIN);
				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		}
		else if( msg.startsWith(NetworkCommon.GAME_EXIT) ){
			mServerContext.getNetworkManager().sendMessage(NetworkCommon.GAME_EXIT);
			mServerContext.resetServer();
		}
	}

	private int winnerId() {
		int id = 0;
		for (int i = 1; i < mServerContext.getClientNum() ; i++) {
			id = mServerContext.getPlayerModel().get(id).getScore() > mServerContext
					.getPlayerModel().get(i ).getScore() ? id:i;
		}
		return id+1;
	}

}
