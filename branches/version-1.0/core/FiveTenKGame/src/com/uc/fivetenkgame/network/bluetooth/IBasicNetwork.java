package com.uc.fivetenkgame.network.bluetooth;

/**
 * ����ͨ�ŵײ�ӿ�
 * Ϊʵ��LocalCommunication��SocketCommunication��������ģ�ʹ��ServerManager�����ӿ�ʵ�ֶ���
 * 
 * @author liuzd
 *
 */
interface IBasicNetwork {
	public void sendMessage(String msg);
	public void receiveMessage(String msg);
	public void release();
}
