package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.network.util.Common;
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
			mServerContext.getNetworkManager().sendMessage(Common.GAME_OVER+id);
		}
		
	}

	private int winnerId() {
		int id = 0;
		for (int i = 1; i < mServerContext.getClientNum() ; i++) {
			id = mServerContext.getPlayerModel().get(id).getScore() > mServerContext
					.getPlayerModel().get(i ).getScore() ? mServerContext
					.getPlayerModel().get(id).getPlayerNumber() : mServerContext
					.getPlayerModel().get(i).getPlayerNumber();
		}
		return id;
	}

}
