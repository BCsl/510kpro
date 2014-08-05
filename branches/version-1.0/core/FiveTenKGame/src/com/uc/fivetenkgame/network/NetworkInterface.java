package com.uc.fivetenkgame.network;


/**
 * ����ģ������ṩ�Ľӿ�
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
