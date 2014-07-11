package com.uc.fivetenkgame.network;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 客户端网络管理类
 * 
 * @author liuzd
 *
 */
public class ClientManager implements NetworkInterface{

	private TCPClient mTCPToServer;
	
	public static ClientManager gInstance;
	public static ClientManager getInstance(){
		if( null == gInstance){
			gInstance = new ClientManager();
		}
		
		return gInstance;
	}
	
	private ClientManager(){
		mTCPToServer = new TCPClient();
	}
	
	public void initNetwork(String addr){
		mTCPToServer.initNetwork(this, addr);
	}
	
	
	@Override
	public void sendMessage(String msg) {
		mTCPToServer.sendMessage(msg);
	}

	@Override
	public void receiveMessage(String msg) {
		
	}

	@Override
	public void playCards(List<Card> playCards) {
		
	}

	
}
