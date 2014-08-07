package com.uc.fivetenkgame.network.wifi;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 客户端网络管理类
 * 
 * @author liuzd
 *
 */
public class ClientManager extends NetworkManager{

	private SocketCommunicationThread mTCPToServer;

	private static ClientManager gInstance;
	public static ClientManager getInstance(){
		if( gInstance == null ){
			gInstance = new ClientManager();
		}
		
		return gInstance;
	}
	
	private ClientManager(){
	}
	
	public void initNetwork(final String addr){
		new Thread(){
			public void run(){
				try {
					Socket socket = new Socket(addr, NETWORK_PORT);
					mTCPToServer = new SocketCommunicationThread(ClientManager.this, socket);
					mTCPToServer.start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void sendMessage(String msg) {
		if( mTCPToServer != null )
			mTCPToServer.sendMessage(msg);
	}

	@Override
	public void reset() {
		mTCPToServer.release();
		mTCPToServer = null;
	}

	
}
