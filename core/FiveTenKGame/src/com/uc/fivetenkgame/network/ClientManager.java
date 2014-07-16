package com.uc.fivetenkgame.network;


/**
 * 客户端网络管理类
 * 
 * @author liuzd
 *
 */
public class ClientManager extends NetworkManager{

	private TCPClient mTCPToServer;

	public ClientManager(){
		mTCPToServer = new TCPClient(this);
	}
	
	public void initNetwork(String addr){
		mTCPToServer.initNetwork(addr, NETWORK_PORT);
	}
	
	
	@Override
	public void sendMessage(String msg) {
		mTCPToServer.sendMessage(msg);
	}

	
}
