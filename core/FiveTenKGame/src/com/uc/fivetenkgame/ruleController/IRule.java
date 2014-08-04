package com.uc.fivetenkgame.ruleController;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * ����ӿ�
 * @author fuyx
 *
 */
public interface IRule {
	/**  
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 * @param cardList1 the cards to be played
	 * @param cardList2 the cards the former player played
	 */
	int checkCards(List<Card> cardList1,List<Card> cardList2);
	
	/**
	 * �����һ���е�һ�����Ƶ��ˣ�����ô˷���
	 * @param cardList
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 */
	int firstPlayCards(List<Card> cardList);
	
	String getRuleName();
	
	boolean isBoom(List<Card> cardList);
}

