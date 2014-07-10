package com.uc.fivetenkgame.view.util;

import java.util.List;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

import android.content.Context;
import android.graphics.Canvas;

public interface IDrawer {
	// 画玩家手上的牌
		public void drawCards(Canvas canvas,List<Card> cardList,Context con);
		// 画玩家
		public void drawPlayers(Canvas canvas, int playerNO);
		// 画玩家分数
		public void drawPlayersScore(Canvas canvas,List<Integer> scores,int playerNO);
		//画本轮游戏分数
		public void drawGameScore(Canvas canvas,int scores);
		// 画玩家所出的牌
		public void drawOutList(Canvas canvas);
		// 画 出牌 ， 放弃 button
		public void drawButton(Canvas canvas);
		// 画 各个玩家剩余牌数
		public void drawCardNumber(Canvas canvas,List<Integer> cardNumber,int playerNO,Context con);
}
