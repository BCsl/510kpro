package com.uc.fivetenkgame.ruleController.baseRule;

import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.utils.CardType;
import com.uc.fivetenkgame.ruleController.utils.RuleJudgeUtils;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

public class SpecialCardTypeRule implements RuleUnit {

	@Override
	public int checkCards(cardType cType, List<Card> cardList1,
			cardType cType2, List<Card> cardList2) {
		Log.i("basic rule", "�ǵ�һ������");
		Log.i("cardList1", cType.toString());
		Log.i("cardList2", cType2.toString());

		if (cType == CardType.cardType.c0)
			return 0;
		if (cType == CardType.cardType.ckk)
			return 1;
		if (cType2 == CardType.cardType.ckk)
			return 0;
		// ���������ֱͬ�ӹ���
		if (cType != CardType.cardType.c4 && cType != CardType.cardType.c510k
				&& cType != CardType.cardType.c510K
				&& cardList1.size() != cardList2.size())
			return 0;
		// �Ƚ��ҵĳ�������
		if (cType != CardType.cardType.c4 && cType != CardType.cardType.c510k
				&& cType != CardType.cardType.c510K && cType != cType2) {
			return 0;
		}

		// ��������4��ը��
		if (cType == CardType.cardType.c4 && cType2 != CardType.cardType.c4) {
			if (cardList1.size() > 4)
				return 1;
		}
		if (cType2 == CardType.cardType.c4 && cType != CardType.cardType.c4) {
			if (cardList2.size() > 4)
				return 0;
		}
		if (cType == CardType.cardType.c4 && cType2 == CardType.cardType.c4) {
			if (cardList1.size() > cardList2.size()) {
				return 1;
			} else if (cardList1.size() == cardList2.size()) {
				if (RuleJudgeUtils.getValue(cardList1.get(0)) > RuleJudgeUtils
						.getValue(cardList2.get(0)))
					return 1;
				return 0;
			}
			return 0;
		}

		// �Ƚϳ������Ƿ�Ҫ��
		// 510k�ʹ�510K
		if (cType == CardType.cardType.c510K) {
			if (cType2 == CardType.cardType.c510K
					&& RuleJudgeUtils.getColor(cardList1.get(0)) <= RuleJudgeUtils
							.getColor(cardList2.get(0))) {
				return 0;
			}
			return 1;
		} else if (cType == CardType.cardType.c510k) {
			if (cType2 == CardType.cardType.c510K)
				return 0;
			else
				return 1;
		}

		if (cType2 == CardType.cardType.c510K
				|| cType2 == CardType.cardType.c510k)
			return 0;

		// ��������4��ը��
		if (cType == CardType.cardType.c4)
			return 1;
		if (cType2 == CardType.cardType.c4)
			return 0;
		return 0;
	}

}