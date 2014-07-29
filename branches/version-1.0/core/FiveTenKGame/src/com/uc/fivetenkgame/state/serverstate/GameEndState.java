package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
/**
 * 
 * @author chensl@ucweb.com
 *
 *  ÏÂÎç4:59:13 2014-7-17
 */
public class GameEndState extends ServerState {

	public GameEndState(ServerContext context) {
		mServerContext = context;
	}

	@Override
	public void handle(String msg) {
		if (msg.equals(Common.GAME_END)) {
			int id = winnerId();
			StringBuilder sb=new StringBuilder();
			sb.append(Common.GAME_OVER);
			sb.append(id+",");
			for(PlayerModel model:mServerContext.getPlayerModel())
					sb.append(model.getScore()+",");
			mServerContext.getNetworkManager().sendMessage(sb.deleteCharAt(sb.length()-1).toString());
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
