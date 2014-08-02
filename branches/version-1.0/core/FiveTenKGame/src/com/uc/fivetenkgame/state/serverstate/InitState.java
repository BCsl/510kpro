package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.server.ServerContext;

/**
 * ³õÊ¼×´Ì¬
 * 
 * @author liuzd
 *
 */
public class InitState extends ServerState {

	public InitState(ServerContext context){
		mServerContext = context;
	}
	
	@Override
	public void handle(String msg) {
		
		if(CommonMsgDecoder.checkMessage(msg, NetworkCommon.SERVER_LISTENING)){
			mServerContext.setState(new ListeningState(mServerContext));
		}
	}
	
}
