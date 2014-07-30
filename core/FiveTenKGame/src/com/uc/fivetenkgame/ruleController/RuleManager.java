package com.uc.fivetenkgame.ruleController;

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
