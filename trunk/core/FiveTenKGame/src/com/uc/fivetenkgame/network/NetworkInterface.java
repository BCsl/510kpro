package com.uc.fivetenkgame.network;

import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;

/**
 * ����ģ������ṩ�Ľӿ�
 * 
 * @author liuzd
 *
 */
public interface NetworkInterface {
	public void sendMessage(String msg);
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage);
	public void initNetwork(String addr);
	public void sendMessage(String msg, int num);
	public void reset();
}
