package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.server.ServerContext;

public class InitState extends ServerState {

	public InitState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
		if( msg.startsWith(Common.SERVER_LISTENING) ){
			mServerContext.setState(new ListeningState(mServerContext));
		}
	}
	
}
