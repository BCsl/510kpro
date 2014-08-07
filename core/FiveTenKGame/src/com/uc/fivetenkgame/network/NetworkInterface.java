package com.uc.fivetenkgame.network;


/**
 * ����ģ������ṩ�Ľӿ�
 * 
 * @author liuzd
 *
 */
public interface NetworkInterface {
	/**
	 * ͨ�����緢����Ϣ
	 * 
	 * @param msg ��Ҫ���͵���Ϣ
	 */
	public void sendMessage(String msg);
	
	/**
	 * ������յ�����Ϣ
	 * 
	 * @param ���յ�����Ϣ
	 */
	public void receiveMessage(String msg);
	
	/**
	 * ���ý�����Ϣ�ļ�����
	 * 
	 * @param onReceiveMessage ��Ҫ������Ϣ�ļ�����
	 */
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage);
	
	/**
	 * ��ʼ������
	 * �ͻ��˸���addr�������ӷ�������
	 * ��������ʼ����������������
	 * 
	 * @param addr �����ַ��������Ϊ��
	 */
	public void initNetwork(String addr);
	
	/**
	 * ��������
	 */
	public void reset();
}
