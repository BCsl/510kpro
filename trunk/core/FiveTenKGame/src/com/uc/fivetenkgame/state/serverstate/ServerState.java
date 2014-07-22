package com.uc.fivetenkgame.state.serverstate;

import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.state.State;

/**
 * 服务器状态的基类
 * 
 * @author liuzd
 *
 */
public class ServerState implements State{

	protected ServerContext mServerContext;
	
	/**
	 * 根据不同的消息进行状态跳转
	 * 
	 * @param msg 状态跳转条件
	 */
	@Override
	public void handle(String msg) {
		
	}

}
