package com.uc.fivetenkgame.ruleController;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 规则接口
 * @author fuyx
 *
 */
public interface Rule {
	/**  
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 * @param cardList1 the cards to be played
	 * @param cardList2 the cards the former player played
	 */
	int checkCards(List<Card> cardList1,List<Card> cardList2);
	
	/**
	 * 如果是一局中第一个打牌的人，则调用此方法
	 * @param cardList
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 */
	int firstPlayCards(List<Card> cardList);
	
	/**
	 * 
	 * @param cardList the cards to be played
	 * @return the score of a set of cards
	 */
	int countCardsScore(List<Card> cardList);
}

