package com.uc.fivetenkgame.ruleController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardGenerator;

/**
 * 
 * ����������
 * @author fuyx
 *
 */
public class BasicRule implements Rule {
	/**  
	 * @return 0 if cardList1 is illegal, 1 if it is legal
	 * 
	 */
	public int checkCards(List<Card> cardList1, List<Card> cardList2) {
		BasicRule.setOrder(cardList1);
		BasicRule.setOrder(cardList2);
		CardType cType = this.judgeType(cardList1);
		CardType cType2 = this.judgeType(cardList2);
		// ���������ֱͬ�ӹ���
		if (cType != CardType.c4 && cardList1.size() != cardList2.size())
			return 0;
		// �Ƚ��ҵĳ�������
		if (cType != CardType.c4 && cType != cType2) {

			return 0;
		}
		// �Ƚϳ������Ƿ�Ҫ��
		// ����ը��
		if (cType == CardType.c4) {
			if (cardList1.size() == 2)
				return 1;
			if (cType2 != CardType.c4) {
				return 1;
			}
		}
		
		if(cType == CardType.c510K){
			if(cType2 == CardType.c4)
				return 0;
			else
				return 1;
		}
		
		if(cType == CardType.c510k){
			if( (cType2 == CardType.c4) || (cType2 == CardType.c510K))
				return 0;
			else return 1;
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
	
	public int countCardsScore(List<Card> cardList){
		int score = 0;
		if(cardList == null)
			return 0;
		for(int count = cardList.size(), i = 0; i < count; i++){
			score+=getCardSocre(cardList.get(i));
		}
		return score;
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
					&& ((BasicRule.getValue(list.get(0)) == 13 && (BasicRule
							.getValue(list.get(1)) == 10 && (BasicRule
							.getValue(list.get(2)) == 5))))) {
				if ((BasicRule.getValue(list.get(0)) == BasicRule.getValue(list
						.get(1)))
						&& (BasicRule.getValue(list.get(2)) == BasicRule
								.getValue(list.get(1))))
					return CardType.c510K;
				else
					return CardType.c510k;
			}

			return CardType.c0;
		}
		// ��5������ʱ�����֣�3��2���ɻ���2˳��4��2�ȵ�
		if (len >= 5) {// ���ڰ���ͬ���������ִ���
			Card_index card_index = new Card_index();
			for (int i = 0; i < 4; i++)
				card_index.a[i] = new Vector<Integer>();
			// ����������ֳ���Ƶ��
			BasicRule.getMax(card_index, list); // a[0,1,2,3]�ֱ��ʾ�ظ�1,2,3,4�ε���
			// 3��2 -----�غ��ظ�3�ε���
			if (card_index.a[2].size() == 1 && card_index.a[1].size() == 1
					&& len == 5)
				return CardType.c32;
			// 4��2(��,˫)
			if (card_index.a[3].size() == 1 && len == 6)
				return CardType.c411;
			if (card_index.a[3].size() == 1 && card_index.a[1].size() == 2
					&& len == 8)
				return CardType.c422;
			// ����,��֤��������
			if ((BasicRule.getColor(list.get(0)) != 5)
					&& (card_index.a[0].size() == len)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == len - 1))
				return CardType.c123;
			// ����
			if (card_index.a[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == (len / 2 - 1)))
				return CardType.c1122;
			// �ɻ�
			if (card_index.a[2].size() == len / 3
					&& (len % 3 == 0)
					&& (BasicRule.getValue(list.get(0))
							- BasicRule.getValue(list.get(len - 1)) == (len / 3 - 1)))
				return CardType.c111222;
			// �ɻ���n��,n/2��
			if (card_index.a[2].size() >= 2
					&& card_index.a[2].size() == len / 4
					&& ((Integer) (card_index.a[2].get(len / 4 - 1))
							- (Integer) (card_index.a[2].get(0)) == len / 4 - 1)
					&& len == card_index.a[2].size() * 4)
				return CardType.c11122234;

			// �ɻ���n˫
			if (card_index.a[2].size() >= 2
					&& card_index.a[2].size() == len / 5
					&& card_index.a[2].size() == len / 5
					&& ((Integer) (card_index.a[2].get(len / 5 - 1))
							- (Integer) (card_index.a[2].get(0)) == len / 5 - 1)
					&& len == card_index.a[2].size() * 5)
				return CardType.c1112223344;

		}
		return CardType.c0;
	}

	// �趨�Ƶ�˳��
	public static void setOrder(List<Card> list) {
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
	public static int getValue(Card card) {
		
		int i = Integer.parseInt(CardGenerator.cardResourceName(card.getCardId()).substring(
				3, card.getCardId().length()));
		return i;
	}

	// ���ػ�ɫ
	public static int getColor(Card card) {
		return Integer.parseInt(CardGenerator.cardResourceName(card.getCardId()).substring(1, 2));
	}

	// �õ������ͬ��
	public static void getMax(Card_index card_index, List<Card> list) {
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
				card_index.a[0].add(i + 1);
				break;
			case 2:
				card_index.a[1].add(i + 1);
				break;
			case 3:
				card_index.a[2].add(i + 1);
				break;
			case 4:
				card_index.a[3].add(i + 1);
				break;
			}
		}
	}

	// �����ظ���������
	public static List getOrder2(List<Card> list) {
		List<Card> list2 = new Vector<Card>(list);
		List<Card> list3 = new Vector<Card>();
		List<Integer> list4 = new Vector<Integer>();
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
	
	private int getCardSocre(Card card){
		int score;
		int cardNumber = BasicRule.getValue(card);
		switch(cardNumber){
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
	class Card_index {
		List a[] = new Vector[4];// ����
	}
	
	public enum CardType {
		c1,//���ơ�
		c2,//���ӡ�
		c3,//3������
		c4,//ը����
		c31,//3��1��
		c32,//3��2��
		c411,//4��2����������һ��
		c422,//4��2��
		c123,//���ӡ�
		c1122,//���ӡ�
		c111222,//�ɻ���
		c11122234,//�ɻ�������.
		c1112223344,//�ɻ�������.
		c510k,//510K
		c510K,//��510K
		c0//���ܳ���
	}
}