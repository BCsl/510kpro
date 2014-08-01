package com.uc.fivetenkgame.ruleController.ruleSet;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.Rule;
import com.uc.fivetenkgame.ruleController.baseRule.FirstPlayCardsRule;
import com.uc.fivetenkgame.ruleController.baseRule.NormalCardTypeRule;
import com.uc.fivetenkgame.ruleController.baseRule.RuleUnit;
import com.uc.fivetenkgame.ruleController.baseRule.SpecialCardTypeRule;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.ruleController.utils.RuleJudgeUtils;
import com.uc.fivetenkgame.view.entity.Card;

public class BasicRule implements Rule {
	private List<RuleUnit> mRuleSet;
	private final String mRuleName = "BasicRule";

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
		Log.i(this.getClass().getName(), "card list 1 type:" + cType.toString());
		Log.i(this.getClass().getName(),
				"card list 2 type:" + cType2.toString());

		int result;
		for (RuleUnit ruleUnit : mRuleSet) {
			result = ruleUnit.checkCards(cType, cardList1, cType2, cardList2);
			if (result == 1)
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
}
