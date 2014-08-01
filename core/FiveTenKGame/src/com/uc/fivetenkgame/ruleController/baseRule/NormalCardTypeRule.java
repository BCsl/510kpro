package com.uc.fivetenkgame.ruleController.baseRule;

import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.utils.CardType;
import com.uc.fivetenkgame.ruleController.utils.RuleJudgeUtils;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

public class NormalCardTypeRule implements RuleUnit {

	@Override
	public int checkCards(cardType cType, List<Card> cardList1,
			cardType cType2, List<Card> cardList2) {
		Log.i(this.getClass().getName(), this.getClass().getMethods()
				.toString());
		if (cType != CardType.cardType.c4 && cType != CardType.cardType.c510k
				&& cType != CardType.cardType.c510K
				&& cardList1.size() != cardList2.size())
			return 0;
		// 比较我的出牌类型
		if (cType != CardType.cardType.c4 && cType != CardType.cardType.c510k
				&& cType != CardType.cardType.c510K && cType != cType2) {
			return 0;
		}

		// 单牌,对子,3带,4炸弹
		if (cType == CardType.cardType.c1 || cType == CardType.cardType.c2
				|| cType == CardType.cardType.c3) {
			if (RuleJudgeUtils.getValue(cardList1.get(0)) <= RuleJudgeUtils
					.getValue(cardList2.get(0))) {
				return 0;
			} else {
				return 1;
			}
		}
		// 顺子,连队，飞机裸
		if (cType == CardType.cardType.c123 || cType == CardType.cardType.c1122
				|| cType == CardType.cardType.c111222) {
			if (RuleJudgeUtils.getValue(cardList1.get(0)) <= RuleJudgeUtils
					.getValue(cardList2.get(0)))
				return 0;
			else
				return 1;
		}
		// 按重复多少排序
		// 3带1,3带2 ,飞机带单，双,4带1,2,只需比较第一个就行，独一无二的
		if (cType == CardType.cardType.c31 || cType == CardType.cardType.c32
				|| cType == CardType.cardType.c411
				|| cType == CardType.cardType.c422
				|| cType == CardType.cardType.c11122234
				|| cType == CardType.cardType.c1112223344) {
			List<Card> a1 = RuleJudgeUtils.getOrder2(cardList1); // 我出的牌
			List<Card> a2 = RuleJudgeUtils.getOrder2(cardList2);// 当前最大牌
			Log.i(this.getClass().getMethods().toString(), "a1.get(0)="
					+ a1.get(0).getCardId());
			Log.i(this.getClass().getMethods().toString(), "a2.get(0)="
					+ a2.get(0).getCardId());
			if (RuleJudgeUtils.getValue(a1.get(0)) <= RuleJudgeUtils.getValue(a2
					.get(0)))
				return 0;
		}
		return 1;
	}
}
