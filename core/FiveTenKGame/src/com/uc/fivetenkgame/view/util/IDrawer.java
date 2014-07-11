package com.uc.fivetenkgame.view.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;

import com.uc.fivetenkgame.view.entity.Card;

public interface IDrawer {
	/**
	 *  ��������ϵ���
	 * @param canvas
	 * @param cardList
	 * @param con
	 */
		public void drawCards(Canvas canvas,List<Card> cardList,Context con);
		/**
		 *  �����
		 * @param canvas
		 * @param playerNO
		 */
		public void drawPlayers(Canvas canvas, int playerNO);
		/**
		 *  ����ҷ���
		 * @param canvas
		 * @param scores
		 * @param playerNO
		 */
		public void drawPlayersScore(Canvas canvas,List<Integer> scores,int playerNO);
		/**
		 * ��������Ϸ����
		 * @param canvas
		 * @param scores
		 */
		public void drawGameScore(Canvas canvas,int scores);
		/**
		 *  �������������
		 * @param con
		 * @param canvas
		 * @param outList
		 * @param playerNO
		 */
		public void drawOutList(Context con,Canvas canvas,Map<Integer,List<Card>> outList ,int playerNO);
		/**
		 * �� ���� �� ���� ���� button
		 * @param canvas
		 */
		public void drawButton(Canvas canvas);
		/**
		 *  �� �������ʣ������
		 * @param canvas
		 * @param cardNumber
		 * @param playerNO
		 * @param con
		 */
		public void drawCardNumber(Canvas canvas,List<Integer> cardNumber,int playerNO,Context con);
}
