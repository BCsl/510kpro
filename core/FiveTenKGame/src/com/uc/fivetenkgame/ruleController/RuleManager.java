package com.uc.fivetenkgame.ruleController;

import com.uc.fivetenkgame.ruleController.ruleSet.BasicRule;

import android.util.Log;

public class RuleManager {
	private static BasicRule mBasicRule = new BasicRule();
	
	public Rule getRule(String ruleName){
		if(ruleName == mBasicRule.getRuleName())
			return mBasicRule;
		Log.e("get rule failed", "wrong rule name");
		return null;
	}
}
