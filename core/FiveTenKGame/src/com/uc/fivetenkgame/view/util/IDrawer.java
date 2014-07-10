package com.uc.fivetenkgame.view.util;

import java.util.List;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

import android.content.Context;
import android.graphics.Canvas;

public interface IDrawer {
	// ��������ϵ���
		public void drawCards(Canvas canvas,List<Card> cardList,Context con);
		// �����
		public void drawPlayers(Canvas canvas, int playerNO);
		// ����ҷ���
		public void drawPlayersScore(Canvas canvas,List<Integer> scores,int playerNO);
		//��������Ϸ����
		public void drawGameScore(Canvas canvas,int scores);
		// �������������
		public void drawOutList(Canvas canvas);
		// �� ���� �� ���� button
		public void drawButton(Canvas canvas);
		// �� �������ʣ������
		public void drawCardNumber(Canvas canvas,List<Integer> cardNumber,int playerNO,Context con);
}
