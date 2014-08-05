package com.uc.fivetenkgame.network.bluetooth;

/**
 * 蓝牙通信底层接口
 * 为实现LocalCommunication和SocketCommunication抽象出来的，使得ServerManager依赖接口实现多肽
 * 
 * @author liuzd
 *
 */
interface IBasicNetwork {
	public void sendMessage(String msg);
	public void receiveMessage(String msg);
	public void release();
}
