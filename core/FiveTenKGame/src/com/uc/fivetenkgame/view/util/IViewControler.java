/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.util;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * �������ݸ��¿�����
 * 
 * @author chensl@ucweb.com
 * 
 *         ����9:55:51 2014-7-16
 */
public interface IViewControler {
	/**
	 * ������Ϸ��ǰ����
	 * 
	 * @param score
	 *            ��Ϸ����
	 */
	public void setGameScore(int score);

	/**
	 * ���ø������ʣ��������
	 * 
	 * @param cardNumber
	 *            �����ʣ���Ƶ�����
	 */
	public void setCardNumber(List<Integer> cardNumber);

	/**
	 * ���ø�������Լ��ĵ÷�
	 * 
	 * @param scroeList
	 *            ��������Լ��ķ���
	 */
	public void setScroeList(List<Integer> scroeList);

	/**
	 * ���ø�����ҳ�����
	 * 
	 * @param number
	 *            ��Һ�
	 * @param list
	 *            ��ҳ�����
	 */
	public void setPlayersOutList(int playId, List<Card> list);

	/**
	 * �����������
	 * 
	 * @param cardList
	 *            ��������Լ�����
	 */
	public void setCards(List<Card> cards);

	/**
	 * �����Ƿ���Գ���
	 * 
	 * @param flag
	 *            �Ƿ���Գ����ź�
	 */
	public void setMyTurn(boolean flag);
	/**
	 * �����¼��ص���
	 * @param eventListener
	 */
	public void setEventListener(EventListener eventListener) ;
	/**
	 * ��Ϸ��������
	 */
	public void gameOver(int playId);
	/**
	 * ����û���չ���
	 */
	public void handCardFailed();
	/**
	 * �غϽ��������������ϵĿ���
	 */
	public void roundOver();
	/**
	 * ���õ�ǰ�������
	 */
	public void setCurrentPlayer(int playerId);
}
