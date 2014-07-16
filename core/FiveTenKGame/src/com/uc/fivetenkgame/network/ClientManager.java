package com.uc.fivetenkgame.network;


/**
 * �ͻ������������
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
