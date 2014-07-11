package com.uc.fivetenkgame.view.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;

import com.uc.fivetenkgame.view.entity.Card;

public interface IDrawer {
	/**
	 *  画玩家手上的牌
	 * @param canvas
	 * @param cardList
	 * @param con
	 */
		public void drawCards(Canvas canvas,List<Card> cardList,Context con);
		/**
		 *  画玩家
		 * @param canvas
		 * @param playerNO
		 */
		public void drawPlayers(Canvas canvas, int playerNO);
		/**
		 *  画玩家分数
		 * @param canvas
		 * @param scores
		 * @param playerNO
		 */
		public void drawPlayersScore(Canvas canvas,List<Integer> scores,int playerNO);
		/**
		 * 画本轮游戏分数
		 * @param canvas
		 * @param scores
		 */
		public void drawGameScore(Canvas canvas,int scores);
		/**
		 *  画玩家所出的牌
		 * @param con
		 * @param canvas
		 * @param outList
		 * @param playerNO
		 */
		public void drawOutList(Context con,Canvas canvas,Map<Integer,List<Card>> outList ,int playerNO);
		/**
		 * 画 出牌 ， 放弃 两个 button
		 * @param canvas
		 */
		public void drawButton(Canvas canvas);
		/**
		 *  画 各个玩家剩余牌数
		 * @param canvas
		 * @param cardNumber
		 * @param playerNO
		 * @param con
		 */
		public void drawCardNumber(Canvas canvas,List<Integer> cardNumber,int playerNO,Context con);
}
