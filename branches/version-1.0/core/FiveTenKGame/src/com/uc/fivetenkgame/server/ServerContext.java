package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.serverstate.ServerState;

/**
 * �������ӿ�
 * 
 * @author liuzd
 *
 */
public interface ServerContext {
	/**
	 * ��÷�����������ӿ�
	 * 
	 * @return NetworkManager����ӿ�
	 */
	public NetworkInterface getNetworkManager();
	
	/**
	 * ���÷�������״̬
	 * 
	 * @param state  ��ǰ״̬
	 */
	public void setState(ServerState state);
	
	/**
	 * ��÷�������handler����handler��Acitvity����
	 * 
	 * @return Activity hangler ������
	 */
	public Handler getHandler();
	
	/**
	 * ���÷�����handler
	 * 
	 * @param handler  Activity hangler ������
	 */
	public void setHandler(Handler handler);
	
	/**
	 * ���õ�ǰ�����
	 * 
	 * @param num  �����
	 */
	public void setClientNum(int num);
	
	/**
	 * ��õ�ǰ�����
	 * 
	 * @return  ��ǰ�����
	 */
	public int getClientNum();
	
	/**
	 * ����������ҵ�����
	 * 
	 * @param playerModelList  ������ҵ�����
	 */
	public void setPlayerModel(ArrayList<PlayerModel> playerModelList);
	
	/**
	 * ���������ҵ�����
	 * 
	 * @return ������ҵ�����
	 */
	public ArrayList<PlayerModel> getPlayerModel();
	
	/**
	 * ��õ�ǰ������ұ��
	 * 
	 * @return ��ǰ������ұ��
	 */
	public int getCurrentPlayerNumber();
	
	/**
	 * ���ó�����ұ��
	 * 
	 * @param CurrentPlayerNumber ������ұ��
	 */
	public void setCurrentPlayerNumber(int CurrentPlayerNumber) ;
	
	/**
	 * ��ñ��ַ���
	 * 
	 * @return ���ַ���
	 */
	public int getRoundScore() ;
	
	/**
	 * ���ñ��ַ���
	 * 
	 * @param mRoundScore ���ַ���
	 */
	public void setRoundScore(int mRoundScore) ;
	
	/**
	 * ��Ϣ����
	 * 
	 * @param msg Ҫ�������Ϣ
	 */
	public void handleMessage(String msg);
	
	public void resetServer();
}
