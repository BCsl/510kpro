package com.uc.fivetenkgame.player;

import android.os.Handler;

import com.uc.fivetenkgame.state.playerstate.PlayerState;

/**
 * ��ҳ����ӿ�
 * @author fuyx
 *
 */
public interface PlayerContext {
	/**
	 * ���õ�ǰstate��handle����
	 * @param msg ����handle����Ϣ
	 */
	void handle(String msg);
	
	/**
	 * �������緽��
	 * @param addr server��IP��ַ
	 */
	void initNetwork(String addr);
	
	/**
	 * ����״̬����
	 * @param state 
	 */
	void setState(PlayerState state);
	
	/**
	 * ������Ϣ
	 * @param msg
	 */
	void sendMsg(String msg);
	
	/**
	 * �յ��غϽ����ź�ʱ�����ø÷���
	 * @param score
	 */
	void roundEndAction(String[] score);
	
	/**
	 * �յ�server��ҳ����ź�ʱ�����ø÷���
	 * @param outlist ��ҳ�����
	 * @param playernumber ������Һ�
	 * @param tablescore �������
	 * @param remaincards �����ʣ������
	 */
	void playCardsEndAction(String[] outlist, String playernumber,
			String tablescore, String[] remaincards);
	
	/**
	 * @return ��Ҫ������
	 */
	String getCardsToBePlayed();
	
	/**
	 * ������ҳ�ʼ����
	 * @param cards
	 */
	void setInitPlayerCards(String cards);
	
	/**
	 * ������Һ�
	 * @param playerNumber
	 */
	void setPlayerNumber(int playerNumber);
	
	/**
	 * 
	 * @return ��Һ�
	 */
	int getPlayerNumber();
	
	/**
	 * ��Ϸ����
	 * @param playerId Ӯ�Һ�
	 */
	void gameOver(int playerId);
	
	void setHandler(Handler handler);
	
	public Handler getHandler();
	
	public void setFirstPlayer();
}
