package com.uc.fivetenkgame.ruleController.baseRule;

import java.util.List;

import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 规则单元接口
 * @author fuyx
 *
 */
public interface RuleUnit {
	/**
	 * 
	 * @param cType
	 * @param cardList1
	 * @param cType2
	 * @param cardList2
	 * @return -1, if still need to be checked
	 */
	public int checkCards(cardType cType, List<Card> cardList1,
			cardType cType2, List<Card> cardList2);
}
