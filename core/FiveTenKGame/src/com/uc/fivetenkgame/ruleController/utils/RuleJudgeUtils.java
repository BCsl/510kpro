package com.uc.fivetenkgame.ruleController.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

public class RuleJudgeUtils {
	// 判断牌型
	public static cardType judgeType(List<Card> list) {
		// RuleJudgeUtils.setOrder(list);
		// 因为之前排序过所以比较好判断
		int len = list.size();
		// 双王,化为对子返回
		if (len == 2 && RuleJudgeUtils.getColor(list.get(1)) == 5
				&& RuleJudgeUtils.getColor(list.get(0)) == 5)
			return CardType.cardType.ckk;

		if (len >= 4 && RuleJudgeUtils.judgeEveryCardsTheSame(list))
			return CardType.cardType.c4;

		// 单牌,对子，3不带，4个一样炸弹
		if (len <= 4) { // 如果第一个和最后个相同，说明全部相同
			if (list.size() > 0
					&& RuleJudgeUtils.getValue(list.get(0)) == RuleJudgeUtils
							.getValue(list.get(len - 1))) {
				switch (len) {
				case 1:
					return CardType.cardType.c1;
				case 2:
					return CardType.cardType.c2;
				case 3:
					return CardType.cardType.c3;
				}
			}
			// 当第一个和最后个不同时,3带1
			if (len == 4
					&& ((RuleJudgeUtils.getValue(list.get(0)) == RuleJudgeUtils
							.getValue(list.get(len - 2))) || RuleJudgeUtils
							.getValue(list.get(1)) == RuleJudgeUtils
							.getValue(list.get(len - 1))))
				return CardType.cardType.c31;
			if (len == 3
					&& (((RuleJudgeUtils.getValue(list.get(0)) == 5 && (RuleJudgeUtils
							.getValue(list.get(1)) == 10 && (RuleJudgeUtils
							.getValue(list.get(2)) == 13)))) || (RuleJudgeUtils
							.getValue(list.get(2)) == 5 && (RuleJudgeUtils
							.getValue(list.get(1)) == 10 && (RuleJudgeUtils
							.getValue(list.get(0)) == 13))))) {
				if ((RuleJudgeUtils.getColor(list.get(0)) == RuleJudgeUtils
						.getColor(list.get(1)))
						&& (RuleJudgeUtils.getColor(list.get(2)) == RuleJudgeUtils
								.getColor(list.get(1)))) {
					return CardType.cardType.c510K;
				} else {
					return CardType.cardType.c510k;
				}
			}

			return CardType.cardType.c0;
		}
		// 当5张以上时，连字，3带2，飞机，2顺，4带2等等
		if (len >= 5) {// 现在按相同数字最大出现次数
			Card_index card_index = new Card_index();
			for (int i = 0; i < 8; i++)
				card_index.numberList[i] = new Vector<Integer>();
			// 求出各种数字出现频率
			RuleJudgeUtils.getMax(card_index, list); // a[0,1,2,3]分别表示重复1,2,3,4次的牌
			// 3带2 -----必含重复3次的牌
			if (card_index.numberList[2].size() == 1
					&& card_index.numberList[1].size() == 1 && len == 5)
				return CardType.cardType.c32;

			// 4带2(单,双)
			if (card_index.numberList[3].size() == 1 && len == 6)
				return CardType.cardType.c411;
			if (card_index.numberList[3].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 8)
				return CardType.cardType.c422;
			if (card_index.numberList[4].size() == 1 && len == 7)
				return CardType.cardType.c411;
			if (card_index.numberList[4].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 9)
				return CardType.cardType.c422;
			if (card_index.numberList[5].size() == 1 && len == 8)
				return CardType.cardType.c411;
			if (card_index.numberList[5].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 10)
				return CardType.cardType.c422;
			if (card_index.numberList[6].size() == 1 && len == 9)
				return CardType.cardType.c411;
			if (card_index.numberList[6].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 11)
				return CardType.cardType.c422;
			if (card_index.numberList[7].size() == 1 && len == 10)
				return CardType.cardType.c411;
			if (card_index.numberList[7].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 12)
				return CardType.cardType.c422;

			// 单连,保证不存在王
			if ((RuleJudgeUtils.getColor(list.get(0)) != 5)
					&& (card_index.numberList[0].size() == len)
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len - 1)))
					&& ((RuleJudgeUtils.getValue(list.get(len - 1)) != 15) || (RuleJudgeUtils
							.getValue(list.get(0)) != 15)))
				return CardType.cardType.c123;
			// 连队
			if (card_index.numberList[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len / 2 - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len / 2 - 1))))
				return CardType.cardType.c1122;
			// 飞机
			if (card_index.numberList[2].size() == len / 3
					&& (len % 3 == 0)
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len / 3 - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len / 3 - 1))))
				return CardType.cardType.c111222;
			// 飞机带n单,n/2对
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 4
					&& (((Integer) (card_index.numberList[2].get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 4 - 1) || ((Integer) (card_index.numberList[2]
							.get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == -(len / 4 - 1)))
					&& len == card_index.numberList[2].size() * 4)
				return CardType.cardType.c11122234;

			// 飞机带n双
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 5
					&& card_index.numberList[2].size() == len / 5
					&& (((Integer) (card_index.numberList[2].get(len / 5 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 5 - 1) || ((Integer) (card_index.numberList[2]
							.get(len / 5 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == -(len / 5 - 1)))
					&& len == card_index.numberList[2].size() * 5)
				return CardType.cardType.c1112223344;

		}
		return CardType.cardType.c0;
	}

	// 设定牌的顺序
	// 服务器端已设好顺序呢，故暂时没用
	private static void setOrder(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				// TODO Auto-generated method stub
				int a1 = RuleJudgeUtils.getColor(o1);// 花色
				int a2 = RuleJudgeUtils.getColor(o2);
				int b1 = RuleJudgeUtils.getValue(o1);// 数值
				int b2 = RuleJudgeUtils.getValue(o2);
				int flag = 0;
				flag = b2 - b1;
				if (flag == 0)
					return a2 - a1;
				else {
					return flag;
				}
			}
		});
	}

	// 返回值
	public static int getValue(Card card) {
		String cardName = RuleJudgeUtils.cardResourceName(card.getCardId());
		// Log.i("getValue", cardName);
		int i = Integer.parseInt(cardName.substring(3, cardName.length()));
		// Log.i(cardName, String.valueOf(i));
		return i;
	}

	// 返回花色
	public static int getColor(Card card) {
		int color = Integer.parseInt((RuleJudgeUtils.cardResourceName(card
				.getCardId())).substring(1, 2));
		if (color == 1)
			return ResourseCommon.CLUBS;
		if (color == 2)
			return ResourseCommon.DIAMOND;
		return color;
	}

	// 得到最大相同数
	@SuppressWarnings("unchecked")
	private static void getMax(Card_index card_index, List<Card> list) {
		int count[] = new int[17];// 1-16各算一种,王算第16种
		for (int i = 0; i < 17; i++)
			count[i] = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			if (RuleJudgeUtils.getColor(list.get(i)) == 5)
				count[16]++;
			else
				count[RuleJudgeUtils.getValue(list.get(i)) - 1]++;
		}
		for (int i = 0; i < 17; i++) {
			switch (count[i]) {
			case 1:
				card_index.numberList[0].add(i + 1);
				break;
			case 2:
				card_index.numberList[1].add(i + 1);
				break;
			case 3:
				card_index.numberList[2].add(i + 1);
				break;
			case 4:
				card_index.numberList[3].add(i + 1);
				break;
			case 5:
				card_index.numberList[4].add(i + 1);
				break;
			case 6:
				card_index.numberList[5].add(i + 1);
				break;
			case 7:
				card_index.numberList[6].add(i + 1);
				break;
			case 8:
				card_index.numberList[7].add(i + 1);
				break;
			}
		}
	}

	/*
	 * // 按照重复次数排序 public static List<Card> getOrder2(List<Card> list) {
	 * List<Card> list2 = new Vector<Card>(list); List<Card> temp = new
	 * Vector<Card>();// 有相同后缀的牌 // List<Integer> list4 = new Vector<Integer>();
	 * int len = list2.size(); int number[] = new int[ResourseCommon.MAX_SUFFIX
	 * + 1];// 有相同后缀的牌的张数 for (int i = ResourseCommon.MIN_SUFFIX; i <=
	 * ResourseCommon.MAX_SUFFIX; i++) number[i] = 0; for (int i = 0; i < len;
	 * i++) { number[RuleJudgeUtils.getValue(list2.get(i))]++; } int index =
	 * ResourseCommon.MIN_SUFFIX;// 数量最多的有相同后缀的牌的位置 for (int i = 0; i < 20; i++)
	 * { index = ResourseCommon.MIN_SUFFIX; for (int j =
	 * ResourseCommon.MAX_SUFFIX; j >= ResourseCommon.MIN_SUFFIX; j--) { if
	 * (number[j] > number[index]) index = j; } for (int k = 0; k < len; k++) {
	 * if (RuleJudgeUtils.getValue(list2.get(k)) == index) {
	 * temp.add(list2.get(k)); } } list2.removeAll(temp); number[index] = 0; }
	 * return temp; }
	 */

	// 按照重复次数排序
	public static List getOrder2(List<Card> list) {
		List<Card> list2 = new Vector<Card>(list);// list的副本
		List<Card> temp = new Vector<Card>();// 有相同后缀的牌
		int len = list2.size();
		int number[] = new int[20];// 有相同后缀的牌的张数
		for (int i = 0; i < 20; i++)
			number[i] = 0;
		for (int i = 0; i < len; i++) {
			number[RuleJudgeUtils.getValue(list2.get(i))]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (number[j] > number[max])
					max = j;
			}

			for (int k = 0; k < len; k++) {
				if (RuleJudgeUtils.getValue(list2.get(k)) == max) {
					temp.add(list2.get(k));
				}
			}
			list2.remove(temp);
			number[max] = 0;
		}
		return temp;
	}

	// 判断牌型用的内部类
	private static class Card_index {
		// numberList[i]代表i+1张相同的牌
		@SuppressWarnings("rawtypes")
		List numberList[] = new Vector[8];
	}

	private static boolean judgeEveryCardsTheSame(List<Card> list) {
		for (int count = list.size(), i = 1; i < count; i++) {
			if (RuleJudgeUtils.getValue(list.get(i - 1)) != RuleJudgeUtils
					.getValue(list.get(i)))
				return false;
		}
		return true;
	}

	// 获得牌的资源名
	private static String cardResourceName(String cardId) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(cardId.trim());
		if (cardNO == 53)
			return ResourseCommon.LITTLE_JOKER;
		if (cardNO == 54)
			return ResourseCommon.BIG_JOKER;

		switch ((cardNO - 1) / 13) {
		case 0:
			prefix.append(ResourseCommon.DIAMOND_PREFIX);
			break;
		case 1:
			prefix.append(ResourseCommon.CLUBS_PREFIX);
			break;
		case 2:
			prefix.append(ResourseCommon.HEARTS_PREFIX);
			break;
		case 3:
			prefix.append(ResourseCommon.SPADE_PREFIX);
			break;
		default:
			throw new IllegalArgumentException(cardNO + "is not defined!");
		}
		cardNO %= 13;
		if (cardNO < 3)
			prefix.append(String.valueOf(cardNO + 13));
		else
			prefix.append(String.valueOf(cardNO));
		// Log.i("card Resource Name length: ",
		// String.valueOf(prefix.length()));
		return prefix.toString().trim();
	}
}
