package com.uc.fivetenkgame.ruleController;

import com.uc.fivetenkgame.ruleController.ruleSet.BasicRule;

import android.util.Log;

/**
 * 规则管理器，管理静态规则对象
 * @author fuyx
 *
 */
public class RuleManager {
	private static BasicRule mBasicRule = new BasicRule();
	
	public IRule getRule(String ruleName){
		if(ruleName.equals(mBasicRule.getRuleName()))
			return mBasicRule;
		Log.e("get rule failed", "wrong rule name");
		return null;
	}
}
