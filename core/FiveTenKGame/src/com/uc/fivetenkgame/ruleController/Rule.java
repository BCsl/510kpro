package com.uc.fivetenkgame.ruleController;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;


public interface Rule {
	int checkCards(List<Card> cardList1,List<Card> cardList2);
	//CardType judgeType(List<Card> cardList);
	int countCardsScore(List<Card> cardList);
}

