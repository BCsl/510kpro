package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.server.ServerContext;

public class GameStartState extends ServerState{

	public GameStartState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
	}
	
}
