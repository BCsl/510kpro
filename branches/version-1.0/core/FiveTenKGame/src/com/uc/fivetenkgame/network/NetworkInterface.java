package com.uc.fivetenkgame.network;


/**
 * 网络模块对外提供的接口
 * 
 * @author liuzd
 *
 */
public interface NetworkInterface {
	public void sendMessage(String msg);
	public void receiveMessage(String msg);
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage);
	public void initNetwork(String addr);
	public void sendMessage(String msg, int num);
	public void reset();
}
