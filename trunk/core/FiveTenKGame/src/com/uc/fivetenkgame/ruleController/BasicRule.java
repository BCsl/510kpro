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
 * ����������
 * 
 * @author fuyx
 *
 */
public class BasicRule implements Rule {
	/**
	 * ʵ�ֽӿڷ����������ѡ���Ƿ�Ϸ�
	 * 
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 * @param isFirst
	 *            �Ƿ��ǵ�һ���������
	 */
	public int checkCards(List<Card> cardList1, List<Card> cardList2) {
		// BasicRule.setOrder(cardList1);
		// BasicRule.setOrder(cardList2);

		CardType cType = this.judgeType(cardList1);
		CardType cType2 = this.judgeType(cardList2);
		Log.i("basic rule", "�ǵ�һ������");
		Log.i("cardList1", cType.toString());
		Log.i("cardList2", cType2.toString());

		if (cType == CardType.c0)
			return 0;
		if (cType == CardType.ckk)
			return 1;
		if (cType2 == CardType.ckk)
			return 0;
		// ���������ֱͬ�ӹ���
		if (cType != CardType.c4 && cType != CardType.c510k
				&& cType != CardType.c510K
				&& cardList1.size() != cardList2.size())
			return 0;
		// �Ƚ��ҵĳ�������
		if (cType != CardType.c4 && cType != CardType.c510k
				&& cType != CardType.c510K && cType != cType2) {
			return 0;
		}
		// �Ƚϳ������Ƿ�Ҫ��
		// 510k�ʹ�510K
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

		// ����ը��
		if (cType == CardType.c4) {
			if (cType2 != CardType.c4) {
				return 1;
			}
		}

		// ����,����,3��,4ը��
		if (cType == CardType.c1 || cType == CardType.c2
				|| cType == CardType.c3 || cType == CardType.c4) {
			if (BasicRule.getValue(cardList1.get(0)) <= BasicRule
					.getValue(cardList2.get(0))) {
				return 0;
			} else {
				return 1;
			}
		}
		// ˳��,���ӣ��ɻ���
		if (cType == CardType.c123 || cType == CardType.c1122
				|| cType == CardType.c111222) {
			if (BasicRule.getValue(cardList1.get(0)) <= BasicRule
					.getValue(cardList2.get(0)))
				return 0;
			else
				return 1;
		}
		// ���ظ���������
		// 3��1,3��2 ,�ɻ�������˫,4��1,2,ֻ��Ƚϵ�һ�����У���һ�޶���
		if (cType == CardType.c31 || cType == CardType.c32
				|| cType == CardType.c411 || cType == CardType.c422
				|| cType == CardType.c11122234 || cType == CardType.c1112223344) {
			List<Card> a1 = BasicRule.getOrder2(cardList1); // �ҳ�����
			List<Card> a2 = BasicRule.getOrder2(cardList2);// ��ǰ�����
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
		Log.i("basic rule", "��һ������");
		if (judgeType(cardList) != CardType.c0)
			return 1;
		return 0;
	}

	// �ж�����
	private CardType judgeType(List<Card> list) {
		// ��Ϊ֮ǰ��������ԱȽϺ��ж�
		int len = list.size();
		// ˫��,��Ϊ���ӷ���
		if (len == 2 && BasicRule.getColor(list.get(1)) == 5)
			return CardType.c4;

		// *******�ж�510k�ʹ�510k**************//

		// ����,���ӣ�3������4��һ��ը��
		if (len <= 4) { // �����һ����������ͬ��˵��ȫ����ͬ
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
			// ����һ����������ͬʱ,3��1
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
		// ��5������ʱ�����֣�3��2���ɻ���2˳��4��2�ȵ�
		if (len >= 5) {// ���ڰ���ͬ���������ִ���
			Card_index card_index = new Card_index();
			for (int i = 0; i < 4; i++)
				card_index.numberList[i] = new Vector<Integer>();
			// ����������ֳ���Ƶ��
			BasicRule.getMax(card_index, list); // a[0,1,2,3]�ֱ��ʾ�ظ�1,2,3,4�ε���
			// 3��2 -----�غ��ظ�3�ε���
			if (card_index.numberList[2].size() == 1
					&& card_index.numberList[1].size() == 1 && len == 5)
				return CardType.c32;
			// 4��2(��,˫)
			if (card_index.numberList[3].size() == 1 && len == 6)
				return CardType.c411;
			if (card_index.numberList[3].size() == 1
					&& card_index.numberList[1].size() == 2 && len == 8)
				return CardType.c422;
			// ����,��֤��������
			if ((BasicRule.getColor(list.get(0)) != 5)
					&& (card_index.numberList[0].size() == len)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len - 1))
					&& (BasicRule.getValue(list.get(len - 1)) != 15))
				return CardType.c123;
			// ����
			if (card_index.numberList[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len / 2 - 1)))
				return CardType.c1122;
			// �ɻ�
			if (card_index.numberList[2].size() == len / 3
					&& (len % 3 == 0)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == -(len / 3 - 1)))
				return CardType.c111222;
			// �ɻ���n��,n/2��
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 4
					&& ((Integer) (card_index.numberList[2].get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 4 - 1)
					&& len == card_index.numberList[2].size() * 4)
				return CardType.c11122234;

			// �ɻ���n˫
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

	// �趨�Ƶ�˳��
	// �������������˳���أ�����ʱû��
	private static void setOrder(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				// TODO Auto-generated method stub
				int a1 = BasicRule.getColor(o1);// ��ɫ
				int a2 = BasicRule.getColor(o2);
				int b1 = BasicRule.getValue(o1);// ��ֵ
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

	// ����ֵ
	private static int getValue(Card card) {
		String cardName = BasicRule.cardResourceName(card.getCardId());
		int i = Integer.parseInt(cardName.substring(3, cardName.length()));
		Log.i(cardName, String.valueOf(i));
		return i;
	}

	// ���ػ�ɫ
	private static int getColor(Card card) {
		return Integer.parseInt((BasicRule.cardResourceName(card.getCardId()))
				.substring(1, 2));
	}

	// �õ������ͬ��
	private static void getMax(Card_index card_index, List<Card> list) {
		int count[] = new int[17];// 1-16����һ��,�����16��
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

	// �����ظ���������
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

	// ���ĳ�����Ʒ���
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

	// �ж������õ�
	private class Card_index {
		// numberList[i]����i+1����ͬ����
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
		c1, // ���ơ�
		c2, // ���ӡ�
		c3, // 3������
		c4, // ը����
		ckk, // ˫��
		c31, // 3��1��
		c32, // 3��2��
		c411, // 4��2����������һ��
		c422, // 4��2��
		c123, // ���ӡ�
		c1122, // ���ӡ�
		c111222, // �ɻ���
		c11122234, // �ɻ�������.
		c1112223344, // �ɻ�������.
		c510k, // 510K
		c510K, // ��510K
		c0// ���ܳ���
	}
}
