package com.uc.fivetenkgame.ruleController;

/**
 * ��������� 
 * @author fuyx
 *
 */
public class RuleManager {
	private Rule mRule;
	
	public Rule getRuleInstance(String rule){
		if(rule == "BasicRule")
			mRule=new BasicRule();
		return mRule;
	}
}
