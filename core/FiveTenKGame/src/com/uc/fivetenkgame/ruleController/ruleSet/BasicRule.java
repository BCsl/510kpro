package com.uc.fivetenkgame.ruleController.ruleSet;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.IRule;
import com.uc.fivetenkgame.ruleController.baseRule.FirstPlayCardsRule;
import com.uc.fivetenkgame.ruleController.baseRule.NormalCardTypeRule;
import com.uc.fivetenkgame.ruleController.baseRule.RuleUnit;
import com.uc.fivetenkgame.ruleController.baseRule.SpecialCardTypeRule;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.ruleController.utils.RuleJudgeUtils;
import com.uc.fivetenkgame.view.entity.Card;

public class BasicRule implements IRule {
	private List<RuleUnit> mRuleSet;
	private final String mRuleName = "BasicRule";
	private final int mCardPackNumber = 2;

	public BasicRule() {
		mRuleSet = new ArrayList<RuleUnit>();
		mRuleSet.add(new SpecialCardTypeRule());
		mRuleSet.add(new NormalCardTypeRule());
		mRuleSet.add(new FirstPlayCardsRule());
	}

	@Override
	public int checkCards(List<Card> cardList1, List<Card> cardList2) {
		cardType cType = RuleJudgeUtils.judgeType(cardList1);
		cardType cType2 = RuleJudgeUtils.judgeType(cardList2);
		Log.i(this.getClass().getName(), "�ǵ�һ������");
		Log.i(this.getClass().getName(), "card list 1 type:" + cType.toString());
		Log.i(this.getClass().getName(),
				"card list 2 type:" + cType2.toString());

		int result;
		for (RuleUnit ruleUnit : mRuleSet) {
			result = ruleUnit.checkCards(cType, cardList1, cType2, cardList2);
			if (result != -1)
				return result;
		}
		return 0;
	}

	@Override
	public int firstPlayCards(List<Card> cardList) {
		cardType cType = RuleJudgeUtils.judgeType(cardList);
		return mRuleSet.get(2).checkCards(cType, cardList, null, null);
	}

	@Override
	public String getRuleName() {
		return mRuleName;
	}

	@Override
	public boolean isBoom(List<Card> cardList) {
		cardType cType = RuleJudgeUtils.judgeType(cardList);
		if ((cType == cardType.c4) || (cType == cardType.c510k)
				|| (cType == cardType.c510K) || (cType == cardType.ckk))
			return true;
		return false;
	}

	@Override
	public int getCardPackNumber() {
		return mCardPackNumber;
	}
}
