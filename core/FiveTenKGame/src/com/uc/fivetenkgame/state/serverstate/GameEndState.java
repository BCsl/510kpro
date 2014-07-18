package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.server.ServerContext;

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
		int id = -1;
		for (int i = 0; i < mServerContext.getClientNum() - 1; i++) {
			id = mServerContext.getPlayerModel().get(i).getScore() > mServerContext
					.getPlayerModel().get(i + 1).getScore() ? mServerContext
					.getPlayerModel().get(i).getPlayerNumber() : mServerContext
					.getPlayerModel().get(i + 1).getPlayerNumber();
		}
		return id;
	}

}
