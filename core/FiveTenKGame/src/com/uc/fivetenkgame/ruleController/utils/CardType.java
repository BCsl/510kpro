package com.uc.fivetenkgame.ruleController.utils;

/**
 * 牌型定义
 * @author fuyx
 *
 */
public class CardType {
	// 牌型定义
	public static enum cardType {
		c1, // 单牌。
		c2, // 对子。
		c3, // 3不带。
		c4, // 炸弹。
		ckk, // 双王
		c31, // 3带1。
		c32, // 3带2。
		c411, // 4带2个单，或者一对
		c422, // 4带2对
		c123, // 连子。
		c1122, // 连队。
		c111222, // 飞机。
		c11122234, // 飞机带单排.
		c1112223344, // 飞机带对子.
		c510k, // 510K
		c510K, // 纯510K
		c0// 不能出牌
	}
}
