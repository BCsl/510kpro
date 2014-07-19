package com.uc.fivetenkgame.network;


/**
 * 客户端网络管理类
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
	
	public void initNetwork(final String addr){
		new Thread(){
			public void run(){
				mTCPToServer.initNetwork(addr, NETWORK_PORT);
			}
		}.start();
	}
	
	@Override
	public void sendMessage(String msg) {
		mTCPToServer.sendMessage(msg);
	}

	@Override
	public void sendMessage(String msg, int num) {
		
	}

	
}
