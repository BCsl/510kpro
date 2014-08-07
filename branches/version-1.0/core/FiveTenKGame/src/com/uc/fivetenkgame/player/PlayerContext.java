package com.uc.fivetenkgame.player;

import android.content.Context;
import android.os.Handler;

import com.uc.fivetenkgame.state.playerstate.PlayerState;

/**
 * ��ҳ����ӿ�
 * 
 * @author fuyx
 *
 */
public interface PlayerContext {
	/**
	 * ���õ�ǰstate��handle����
	 * 
	 * @param msg
	 *            ����handle����Ϣ
	 */
	void handle(String msg);

	/**
	 * �������緽��
	 * 
	 * @param addr
	 *            server��IP��ַ
	 */
	void initNetwork(String addr);

	/**
	 * ����״̬����
	 * 
	 * @param state
	 */
	void setState(PlayerState state);

	/**
	 * ������Ϣ
	 * 
	 * @param msg
	 */
	void sendMsg(String msg);

	/**
	 * �յ��غϽ����ź�ʱ�����ø÷���
	 * 
	 * @param score
	 */
	void roundEndAction(String[] score);

	/**
	 * �յ�server��ҳ����ź�ʱ�����ø÷���
	 * 
	 * @param outlist
	 *            ��ҳ�����
	 * @param playernumber
	 *            ������Һ�
	 * @param tablescore
	 *            �������
	 * @param remaincards
	 *            �����ʣ������
	 */
	void playCardsEndAction(String[] outlist, String playernumber,
			String tablescore, String[] remaincards);

	/**
	 * @return ��Ҫ������
	 */
	String getCardsToBePlayed();

	/**
	 * ������ҳ�ʼ����
	 * 
	 * @param cards
	 */
	void setInitPlayerCards(String cards);

	/**
	 * ������Һ�
	 * 
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
	 * @param str
	 *            0Ϊʤ����ң�����Ϊ����ҷ���
	 */
	void gameOver(String[] str);

	/**
	 * ����handler
	 * 
	 * @param handler
	 */
	void setHandler(Handler handler);

	/**
	 * �õ�handler
	 * 
	 * @return Player�����handler
	 */
	public Handler getHandler();

	/**
	 * ����myTurn
	 * 
	 * @param flag
	 */
	public void setMyTurn(boolean flag);

	/**
	 * �жϸ�����Ƿ��Ǹþֵ�һ�����Ƶ���
	 * 
	 * @return
	 */
	public boolean isFirstPlayer();

	/**
	 * �ж�����Ƿ�������
	 * 
	 * @return
	 */
	public boolean hasCard();

	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void reStartGame(String[] str);

	/**
	 * ���õ�ǰ�������
	 * 
	 * @param palyerId
	 */
	public void setCurrentPlayer(int palyerId);

	/**
	 * ������ҷ�������
	 * 
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
	 * ���������Ϣ�������ӿ�
	 * @return
	 */
	
	public void setContext(Context context);
	
	public void playSound(int soundId);
	
	/**
	 * ���������
	 * 
	 * @param playerNames
	 */
	void setPlayersName(String[] playerNames);

	/**
	 * �����Ƿ������¿�ʼ����Ϸ
	 * @param isRestart
	 */
	void setRestart(boolean isRestart);
	
	/**
	 * ���isRestart����
	 * @return
	 */
	boolean isRestart();
	
	/**
	 * ��ʼ������
	 */
	void initView();
}
