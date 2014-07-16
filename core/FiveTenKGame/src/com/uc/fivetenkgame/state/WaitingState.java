package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.server.ServerContext;

public class WaitingState extends ServerState {

	public WaitingState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
	}
	
}
