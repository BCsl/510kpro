package com.uc.fivetenkgame.network;

import com.uc.fivetenkgame.player.Player;


/**
 * �ͻ������������
 * 
 * @author fuyx
 *
 */
public class ClientManager extends NetworkManager{

	private TCPClient mTCPToServer;
	private static ClientManager gInstance;//Ψһ��ClientManagerʵ��
	
	public static ClientManager getInstance(){
		if( null == gInstance ){
			gInstance = new ClientManager();
		}
		
		return gInstance;
	}

	private ClientManager(){
		mTCPToServer = new TCPClient(this);
	}
	
	public void initNetwork(String addr){
		mTCPToServer.initNetwork(addr, NETWORK_PORT);
		mTCPToServer.start();
	}
	
	
	@Override
	public void sendMessage(String msg) {
		mTCPToServer.sendMessage(msg);
	}

	
}
