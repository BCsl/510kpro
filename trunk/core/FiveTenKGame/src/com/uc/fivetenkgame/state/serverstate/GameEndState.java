package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.server.ServerContext;

public class GameEndState extends ServerState {

	public GameEndState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
	}
	
}
