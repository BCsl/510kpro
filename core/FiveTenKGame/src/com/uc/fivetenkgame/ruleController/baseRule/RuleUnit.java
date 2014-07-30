package com.uc.fivetenkgame.ruleController.baseRule;

import java.util.List;

import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

public interface RuleUnit {
	public int checkCards(cardType cType, List<Card> cardList1,
			cardType cType2, List<Card> cardList2);
}
