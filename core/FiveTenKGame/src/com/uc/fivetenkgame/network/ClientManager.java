package com.uc.fivetenkgame.network;


/**
 * �ͻ������������
 * 
 * @author liuzd
 *
 */
public class ClientManager extends NetworkManager{

	private TCPClient mTCPToServer;

	private static ClientManager gInstance;
	public static ClientManager getInstance(){
		if( gInstance == null ){
			gInstance = new ClientManager();
		}
		
		return gInstance;
	}
	
	private ClientManager(){
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
