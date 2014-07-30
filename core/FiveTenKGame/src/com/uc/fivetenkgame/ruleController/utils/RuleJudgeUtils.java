package com.uc.fivetenkgame.ruleController.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.uc.fivetenkgame.ruleController.utils.CardType.cardType;
import com.uc.fivetenkgame.view.entity.Card;

public class RuleJudgeUtils {
	// �ж�����
	public static cardType judgeType(List<Card> list) {
		// RuleJudgeUtils.setOrder(list);
		// ��Ϊ֮ǰ��������ԱȽϺ��ж�
		int len = list.size();
		// ˫��,��Ϊ���ӷ���
		if (len == 2 && RuleJudgeUtils.getColor(list.get(1)) == 5
				&& RuleJudgeUtils.getColor(list.get(0)) == 5)
			return CardType.cardType.ckk;

		if (len >= 4 && RuleJudgeUtils.judgeEveryCardsTheSame(list))
			return CardType.cardType.c4;

		// ����,���ӣ�3������4��һ��ը��
		if (len <= 4) { // �����һ����������ͬ��˵��ȫ����ͬ
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
			// ����һ����������ͬʱ,3��1
			if (len == 4
					&& ((RuleJudgeUtils.getValue(list.get(0)) == RuleJudgeUtils
							.getValue(list.get(len - 2))) || RuleJudgeUtils
							.getValue(list.get(1)) == RuleJudgeUtils.getValue(list
							.get(len - 1))))
				return CardType.cardType.c31;
			if (len == 3
					&& (((RuleJudgeUtils.getValue(list.get(0)) == 5 && (RuleJudgeUtils
							.getValue(list.get(1)) == 10 && (RuleJudgeUtils
							.getValue(list.get(2)) == 13)))) || (RuleJudgeUtils
							.getValue(list.get(2)) == 5 && (RuleJudgeUtils
							.getValue(list.get(1)) == 10 && (RuleJudgeUtils
							.getValue(list.get(0)) == 13))))) {
				if ((RuleJudgeUtils.getColor(list.get(0)) == RuleJudgeUtils.getColor(list
						.get(1)))
						&& (RuleJudgeUtils.getColor(list.get(2)) == RuleJudgeUtils
								.getColor(list.get(1)))) {
					return CardType.cardType.c510K;
				} else {
					return CardType.cardType.c510k;
				}
			}

			return CardType.cardType.c0;
		}
		// ��5������ʱ�����֣�3��2���ɻ���2˳��4��2�ȵ�
		if (len >= 5) {// ���ڰ���ͬ���������ִ���
			Card_index card_index = new Card_index();
			for (int i = 0; i < 8; i++)
				card_index.numberList[i] = new Vector<Integer>();
			// ����������ֳ���Ƶ��
			RuleJudgeUtils.getMax(card_index, list); // a[0,1,2,3]�ֱ��ʾ�ظ�1,2,3,4�ε���
			// 3��2 -----�غ��ظ�3�ε���
			if (card_index.numberList[2].size() == 1
					&& card_index.numberList[1].size() == 1 && len == 5)
				return CardType.cardType.c32;

			// 4��2(��,˫)
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

			// ����,��֤��������
			if ((RuleJudgeUtils.getColor(list.get(0)) != 5)
					&& (card_index.numberList[0].size() == len)
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len - 1)))
					&& ((RuleJudgeUtils.getValue(list.get(len - 1)) != 15) || (RuleJudgeUtils
							.getValue(list.get(0)) != 15)))
				return CardType.cardType.c123;
			// ����
			if (card_index.numberList[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len / 2 - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len / 2 - 1))))
				return CardType.cardType.c1122;
			// �ɻ�
			if (card_index.numberList[2].size() == len / 3
					&& (len % 3 == 0)
					&& ((RuleJudgeUtils.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == -(len / 3 - 1)) || (RuleJudgeUtils
							.getValue(list.get(0))
							- RuleJudgeUtils.getValue(list.get(len - 1)) == (len / 3 - 1))))
				return CardType.cardType.c111222;
			// �ɻ���n��,n/2��
			if (card_index.numberList[2].size() >= 2
					&& card_index.numberList[2].size() == len / 4
					&& (((Integer) (card_index.numberList[2].get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == len / 4 - 1) || ((Integer) (card_index.numberList[2]
							.get(len / 4 - 1))
							- (Integer) (card_index.numberList[2].get(0)) == -(len / 4 - 1)))
					&& len == card_index.numberList[2].size() * 4)
				return CardType.cardType.c11122234;

			// �ɻ���n˫
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

	// �趨�Ƶ�˳��
	// �������������˳���أ�����ʱû��
	private static void setOrder(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				// TODO Auto-generated method stub
				int a1 = RuleJudgeUtils.getColor(o1);// ��ɫ
				int a2 = RuleJudgeUtils.getColor(o2);
				int b1 = RuleJudgeUtils.getValue(o1);// ��ֵ
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

	// ����ֵ
	public static int getValue(Card card) {
		String cardName = RuleJudgeUtils.cardResourceName(card.getCardId());
		Log.i("getValue", cardName);
		int i = Integer.parseInt(cardName.substring(3, cardName.length()));
		// Log.i(cardName, String.valueOf(i));
		return i;
	}

	// ���ػ�ɫ
	public static int getColor(Card card) {
		return Integer.parseInt((RuleJudgeUtils.cardResourceName(card
				.getCardId())).substring(1, 2));
	}

	// �õ������ͬ��
	@SuppressWarnings("unchecked")
	private static void getMax(Card_index card_index, List<Card> list) {
		int count[] = new int[17];// 1-16����һ��,�����16��
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

	// �����ظ���������
	public static List<Card> getOrder2(List<Card> list) {
		List<Card> list2 = new Vector<Card>(list);
		List<Card> list3 = new Vector<Card>();
		// List<Integer> list4 = new Vector<Integer>();
		int len = list2.size();
		int a[] = new int[20];
		for (int i = 0; i < 20; i++)
			a[i] = 0;
		for (int i = 0; i < len; i++) {
			a[RuleJudgeUtils.getValue(list2.get(i))]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (a[j] > a[max])
					max = j;
			}

			for (int k = 0; k < len; k++) {
				if (RuleJudgeUtils.getValue(list2.get(k)) == max) {
					list3.add(list2.get(k));
				}
			}
			list2.remove(list3);
			a[max] = 0;
		}
		return list3;
	}

	// �ж������õ��ڲ���
	private static class Card_index {
		// numberList[i]����i+1����ͬ����
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

	// ����Ƶ���Դ��
	private static String cardResourceName(String cardId) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(cardId.trim());
		if (cardNO == 0)
			return "cardbg1";
		if (cardNO == 53)
			return "a5_16";
		if (cardNO == 54)
			return "a5_17";

		switch ((cardNO - 1) / 13) {
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
		// Log.i("card Resource Name length: ",
		// String.valueOf(prefix.length()));
		return prefix.toString().trim();
	}
}
