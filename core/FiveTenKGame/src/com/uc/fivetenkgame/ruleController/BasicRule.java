package com.uc.fivetenkgame.ruleController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 
 * 基本规则类
 * 
 * @author fuyx
 *
 */
public class BasicRule implements Rule {
	/**
	 * 实现接口方法，检测所选牌是否合法
	 * 
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 * @param isFirst
	 *            是否是第一个出牌玩家
	 */
	public int checkCards(List<Card> cardList1, List<Card> cardList2) {
		// BasicRule.setOrder(cardList1);
		// BasicRule.setOrder(cardList2);

		CardType cType = this.judgeType(cardList1);
		CardType cType2 = this.judgeType(cardList2);
		Log.i("basic rule", "非第一个打牌");
		Log.i("cardList1", cType.toString());
		Log.i("cardList2", cType2.toString());

		if (cType == CardType.c0)
			return 0;
		if (cType == CardType.ckk)
			return 1;
		if (cType2 == CardType.ckk)
			return 0;
		// 如果张数不同直接过滤
		if (cType != CardType.c4 && cType != CardType.c510k
				&& cType != CardType.c510K
				&& cardList1.size() != cardList2.size())
			return 0;
		// 比较我的出牌类型
		if (cType != CardType.c4 && cType != CardType.c510k
				&& cType != CardType.c510K && cType != cType2) {
			return 0;
		}
		// 比较出的牌是否要大
		// 510k和纯510K
		if (cType == CardType.c510K) {
			if (cType2 == CardType.c510K
					&& BasicRule.getColor(cardList1.get(0)) < BasicRule
							.getColor(cardList2.get(0))) {
				return 0;
			}
			return 1;
		} else if (cType == CardType.c510k) {
			if (cType2 == CardType.c510K)
				return 0;
			else
				return 1;
		}

		if (cType2 == CardType.c510K || cType2 == CardType.c510k)
			return 0;

		// 我是炸弹
		if (cType == CardType.c4) {
			if (cType2 != CardType.c4) {
				return 1;
			}
		}

		// 单牌,对子,3带,4炸弹
		if (cType == CardType.c1 || cType == CardType.c2
				|| cType == CardType.c3 || cType == CardType.c4) {
			if (BasicRule.getValue(cardList1.get(0)) <= BasicRule
					.getValue(cardList2.get(0))) {
				return 0;
			} else {
				return 1;
			}
		}
		// 顺子,连队，飞机裸
		if (cType == CardType.c123 || cType == CardType.c1122
				|| cType == CardType.c111222) {
			if (BasicRule.getValue(cardList1.get(0)) <= BasicRule
					.getValue(cardList2.get(0)))
				return 0;
			else
				return 1;
		}
		// 按重复多少排序
		// 3带1,3带2 ,飞机带单，双,4带1,2,只需比较第一个就行，独一无二的
		if (cType == CardType.c31 || cType == CardType.c32
				|| cType == CardType.c411 || cType == CardType.c422
				|| cType == CardType.c11122234 || cType == CardType.c1112223344) {
			List<Card> a1 = BasicRule.getOrder2(cardList1); // 我出的牌
			List<Card> a2 = BasicRule.getOrder2(cardList2);// 当前最大牌
			if (BasicRule.getValue(a1.get(0)) < BasicRule.getValue(a2.get(0)))
				return 0;
		}
		return 1;
	}

	public int countCardsScore(List<Card> cardList) {
		int score = 0;
		if (cardList == null)
			return 0;
		for (int count = cardList.size(), i = 0; i < count; i++) {
			score += getCardSocre(cardList.get(i));
		}
		return score;
	}

	public int firstPlayCards(List<Card> cardList) {
		Log.i("basic rule", "第一个打牌");
		if (judgeType(cardList) != CardType.c0)
			return 1;
		return 0;
	}

	// 判断牌型
	private CardType judgeType(List<Card> list) {
		// 因为之前排序过所以比较好判断
		int len = list.size();
		// 双王,化为对子返回
		if (len == 2 && BasicRule.getColor(list.get(1)) == 5)
			return CardType.c4;

		// *******判断510k和纯510k**************//

		// 单牌,对子，3不带，4个一样炸弹
		if (len <= 4) { // 如果第一个和最后个相同，说明全部相同
			if (list.size() > 0
					&& BasicRule.getValue(list.get(0)) == BasicRule
							.getValue(list.get(len - 1))) {
				switch (len) {
				case 1:
					return CardType.c1;
				case 2:
					return CardType.c2;
				case 3:
					return CardType.c3;
				case 4:
					return CardType.c4;
				}
			}
			// 当第一个和最后个不同时,3带1
			if (len == 4
					&& ((BasicRule.getValue(list.get(0)) == BasicRule
							.getValue(list.get(len - 2))) || BasicRule
							.getValue(list.get(1)) == BasicRule.getValue(list
							.get(len - 1))))
				return CardType.c31;
			if (len == 3
					&& ((BasicRule.getValue(list.get(0)) == 5 && (BasicRule
							.getValue(list.get(1)) == 10 && (BasicRule
							.getValue(list.get(2)) == 13))))) {
				if ((BasicRule.getValue(list.get(0)) == BasicRule.getValue(list
						.get(1)))
						&& (BasicRule.getValue(list.get(2)) == BasicRule
								.getValue(list.get(1)))) {
					return CardType.c510K;
				} else {
					return CardType.c510k;
				}
			}

			return CardType.c0;
		}
		// 当5张以上时，连字，3带2，飞机，2顺，4带2等等
		if (len >= 5) {// 现在按相同数字最大出现次数
			Card_index card_index = new Card_index();
			for (int i = 0; i < 4; i++)
				card_index.numberList[i] = new Vector<Integer>();
			// 求出各种数字出现频率
			BasicRule.getMax(card_index, list); // a[0,1,2,3]分别表示重复1,2,3,4次的牌
			// 3带2 -----必含重复3次的牌
			if (card_index.numberList[2].size() == 1
					&& card_index.numberList[1].size() == 1 && len == 5)
				return CardType.c32;
			// 4带2(单,双)
			if (card_index.numberList[3].size() == 1 && len == 6)
				return CardType.c411;
			if (card_index.numberList[3].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 8)
				return CardType.c422;
			// 单连,保证不存在王
			if ((BasicRule.getColor(list.get(0)) != 5)
					&& (card_index.numberList[0].size() == len)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len - 1))
					&& (BasicRule.getValue(list.get(len - 1)) != 15))
				return CardType.c123;
			// 连队
			if (card_index.numberList[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len / 2 - 1)))
				return CardType.c1122;
			// 飞机
			if (card_index.numberList[2].size() == len / 3
					&& (len % 3 == 0)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len / 3 - 1)))
				return CardType.c111222;
			// 飞机带n单,n/2对
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 4
					&& ((Integer) (card_index.numberList[2].get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 4 - 1)
					&& len == card_index.numberList[2].size() * 4)
				return CardType.c11122234;

			// 飞机带n双
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 5
					&& card_index.numberList[2].size() == len / 5
					&& ((Integer) (card_index.numberList[2].get(len / 5 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 5 - 1)
					&& len == card_index.numberList[2].size() * 5)
				return CardType.c1112223344;

		}
		return CardType.c0;
	}

	// 设定牌的顺序
	// 服务器端已设好顺序呢，故暂时没用
	private static void setOrder(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				// TODO Auto-generated method stub
				int a1 = BasicRule.getColor(o1);// 花色
				int a2 = BasicRule.getColor(o2);
				int b1 = BasicRule.getValue(o1);// 数值
				int b2 = BasicRule.getValue(o2);
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
	private static int getValue(Card card) {
		String cardName = BasicRule.cardResourceName(card.getCardId());
		int i = Integer.parseInt(cardName.substring(3, cardName.length()));
		Log.i(cardName, String.valueOf(i));
		return i;
	}

	// 返回花色
	private static int getColor(Card card) {
		return Integer.parseInt((BasicRule.cardResourceName(card.getCardId()))
				.substring(1, 2));
	}

	// 得到最大相同数
	private static void getMax(Card_index card_index, List<Card> list) {
		int count[] = new int[17];// 1-16各算一种,王算第16种
		for (int i = 0; i < 17; i++)
			count[i] = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			if (BasicRule.getColor(list.get(i)) == 5)
				count[16]++;
			else
				count[BasicRule.getValue(list.get(i)) - 1]++;
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
			}
		}
	}

	// 按照重复次数排序
	private static List<Card> getOrder2(List<Card> list) {
		List<Card> list2 = new Vector<Card>(list);
		List<Card> list3 = new Vector<Card>();
		// List<Integer> list4 = new Vector<Integer>();
		int len = list2.size();
		int a[] = new int[20];
		for (int i = 0; i < 20; i++)
			a[i] = 0;
		for (int i = 0; i < len; i++) {
			a[BasicRule.getValue(list2.get(i))]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (a[j] > a[max])
					max = j;
			}

			for (int k = 0; k < len; k++) {
				if (BasicRule.getValue(list2.get(k)) == max) {
					list3.add(list2.get(k));
				}
			}
			list2.remove(list3);
			a[max] = 0;
		}
		return list3;
	}

	// 获得某张手牌分数
	private static int getCardSocre(Card card) {
		int score;
		int cardNumber = BasicRule.getValue(card);
		switch (cardNumber) {
		case 5:
			score = 5;
			break;
		case 10:
			score = 10;
			break;
		case 13:
			score = 10;
			break;
		default:
			score = 0;
		}
		return score;
	}

	// 判断牌型用的
	private class Card_index {
		// numberList[i]代表i+1张相同的牌
		List numberList[] = new Vector[4];
	}

	private static String cardResourceName(String cardId) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(cardId.trim());
		if (cardNO == 0)
			return "cardbg1";
		if (cardNO == 53)
			return "a5_16";
		if (cardNO == 54)
			return "a5_17";

		switch (cardNO / 13) {
		case 0:
			prefix.append("a2_");
			break;
		case 1:
			prefix.append("a1_");
			break;
		case 2:
			prefix.append("a3_");
			break;
		case 3:
			prefix.append("a4_");
			break;
		default:
			new IllegalArgumentException(cardNO + "is not defined!");
			break;
		}
		cardNO %= 13;
		if (cardNO < 3)
			prefix.append(String.valueOf(cardNO + 13));
		else
			prefix.append(String.valueOf(cardNO));
		return prefix.toString().trim();
	}

	private static enum CardType {
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
