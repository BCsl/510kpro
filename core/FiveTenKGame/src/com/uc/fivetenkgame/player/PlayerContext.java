package com.uc.fivetenkgame.player;

import android.os.Handler;

import com.uc.fivetenkgame.common.ICommonMsgDecoder;
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
	 * 
	 * @param str 0Ϊʤ����ң�����Ϊ����ҷ���
	 */
	void gameOver(String[] str);
	
	/**
	 * ����handler
	 * @param handler
	 */
	void setHandler(Handler handler);
	
	/**
	 * �õ�handler
	 * @return Player�����handler
	 */
	public Handler getHandler();
	
	/**
	 * ���doneHandCards��ֵ���жϳ����Ƿ����
	 * @return
	 */
	public boolean doneHandCards();
	
	/**
	 * ����doneHandCards
	 * @param flag
	 */
	public void setDoneHandCards(boolean flag);
	
	/**
	 * ����myTurn
	 * @param flag
	 */
	public void setMyTurn(boolean flag);
	
	/**
	 * �жϸ�����Ƿ��Ǹþֵ�һ�����Ƶ���
	 * @return
	 */
	public boolean isFirstPlayer();
	
	/**
	 * �ж�����Ƿ�������
	 * @return
	 */
	public boolean hasCard();
	
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void reStartGame(String[] str);
	
	/**
	 * ���õ�ǰ�������
	 * @param palyerId
	 */
	public void setCurrentPlayer(int palyerId);
	/**
	 * ������ҷ�������
	 * @param playerId
	 */
	public void playerGiveUp(int playerId);
	
	/**
	 * �������״̬
	 */
	public void resetPlayer();
	
	/**
	 * 
	 * @return �����
	 */
	public String getPlayerName();
	
	/**
	 * ���������
	 * @param playerId
	 * @param playerName
	 */
	public void setPlayersName(int playerId,String playerName);
	
	public ICommonMsgDecoder getICommomDecoder();

    void setRestart(boolean isRestart);

    boolean isRestart();

    void initView();
}	
