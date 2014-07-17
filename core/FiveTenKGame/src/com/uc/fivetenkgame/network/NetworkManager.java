package com.uc.fivetenkgame.network;


import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;


/**
 * 网络通信抽象类
 * @author liuzd
 *
 */
public abstract class NetworkManager {

	public static final int NETWORK_PORT = 8888;
	protected OnReceiveMessageListener mReceiveMessageListener;
	
	public abstract void sendMessage(String msg);
	
	public void receiveMessage(String msg) {
		mReceiveMessageListener.reveiveMessage(msg);
	}
	
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage) {
		mReceiveMessageListener = onReceiveMessage;
	}
}
