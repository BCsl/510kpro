package com.uc.fivetenkgame.network;


/**
 * 网络模块对外提供的接口
 * 
 * @author liuzd
 *
 */
public interface NetworkInterface {
	/**
	 * 通过网络发送消息
	 * 
	 * @param msg 将要发送的消息
	 */
	public void sendMessage(String msg);
	
	/**
	 * 处理接收到的消息
	 * 
	 * @param 接收到的消息
	 */
	public void receiveMessage(String msg);
	
	/**
	 * 设置接收消息的监听器
	 * 
	 * @param onReceiveMessage 将要处理消息的监听器
	 */
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage);
	
	/**
	 * 初始化网络
	 * 客户端根据addr参数链接服务器；
	 * 服务器则开始监听网络链接请求
	 * 
	 * @param addr 网络地址，服务器为空
	 */
	public void initNetwork(String addr);
	
	/**
	 * 重设网络
	 */
	public void reset();
}
