package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.server.ServerContext;

public class ListeningState extends ServerState {

	public ListeningState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
	}
	
}
