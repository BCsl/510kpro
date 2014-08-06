package com.uc.fivetenkgame.ruleController.baseRule;

import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.utils.CardType;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 用于第一个打牌的规则
 * @author fuyx
 *
 */
public class FirstPlayCardsRule implements RuleUnit {

	@Override
	public int checkCards(cardType cType, List<Card> cardList1,
			cardType cType2, List<Card> cardList2) {
		if (cType2 != null || cardList2 != null)
			return 0;
		Log.i(this.getClass().getName(), "牌型（第一个打牌）：" + cType.toString());
		if (cType != CardType.cardType.c0) {
			return 1;
		}
		return 0;
	}

}
