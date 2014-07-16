package com.uc.fivetenkgame.view.util;

import java.util.List;
import java.util.Map;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.Log;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         下午5:09:36 2014-7-9
 */
public class ThreePersonGameDrawer implements IDrawer {
	// private final String TAG = "ThreePersonGameDrawer";
	private int TEXT_SIZE, TEXT_SIZE_SMALL, TEXT_SIZE_BIG;
	private int CARD_WIDTH, CARD_HEIGHT;
	private int SCREEN_WIDTH, SCREEN_HEIGHT;

	public ThreePersonGameDrawer(ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {

		CARD_WIDTH = cardSizeHolder.width;
		CARD_HEIGHT = cardSizeHolder.height;
		SCREEN_WIDTH = screenHolder.width;
		SCREEN_HEIGHT = screenHolder.height;
		TEXT_SIZE = CARD_WIDTH * 2 / 3;
		TEXT_SIZE_SMALL = CARD_WIDTH / 2;
		TEXT_SIZE_BIG = CARD_WIDTH * 3 / 4;
	}

	public void drawButton(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(152, 251, 152));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(TEXT_SIZE);
		float baseLength = paint.measureText("出牌");
		float baseY = SCREEN_HEIGHT - CARD_WIDTH * 3;
		float leftButtonXEagle = SCREEN_WIDTH / 2 - 2 * CARD_WIDTH;
		float rightButtonXEagle = SCREEN_WIDTH / 2 + 2 * CARD_WIDTH;

		RectF rectLeft = new RectF(leftButtonXEagle, baseY - TEXT_SIZE,
				leftButtonXEagle + baseLength, baseY + 10);
		RectF rectRight = new RectF(rightButtonXEagle, baseY - TEXT_SIZE,
				rightButtonXEagle + baseLength, baseY + 10);
		canvas.drawRoundRect(rectLeft, 20f, 20f, paint);
		canvas.drawRoundRect(rectRight, 20f, 20f, paint);
		paint.setColor(Color.rgb(255, 48, 48));
		canvas.drawText("出牌", leftButtonXEagle, baseY, paint);
		canvas.drawText("放弃", rightButtonXEagle, baseY, paint);
	}

	public void drawPlayers(Canvas canvas, int playerNO) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 184, 15));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(TEXT_SIZE);
		float baseLength = paint.measureText("玩家2");
		canvas.drawText("玩家" + playerNO, 10, SCREEN_HEIGHT - 10, paint);
		if (playerNO == 1) {
			canvas.drawText("玩家2", SCREEN_WIDTH - baseLength - 10,
					CARD_WIDTH * 2 / 3, paint);
			canvas.drawText("玩家3", 10, CARD_WIDTH * 2 / 3, paint);
			return;
		}
		if (playerNO == 3) {
			canvas.drawText("玩家1", SCREEN_WIDTH - baseLength - 10,
					CARD_WIDTH * 2 / 3, paint);
			canvas.drawText("玩家2", 10, CARD_WIDTH * 2 / 3, paint);
			return;
		}
		canvas.drawText("玩家3", SCREEN_WIDTH - baseLength - 10,
				CARD_WIDTH * 2 / 3, paint);
		canvas.drawText("玩家1", 10, CARD_WIDTH * 2 / 3, paint);

	}

	public void drawPlayersScore(Canvas canvas, List<Integer> scores,
			int playerNO) {
		String text = "分数:";
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 246, 143));
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(TEXT_SIZE_SMALL);
		float baseLength = paint.measureText("分數：xxx");
		float baseHeight = 2 * TEXT_SIZE;
		canvas.drawText(text + scores.get(playerNO - 1), SCREEN_WIDTH - 2
				* baseLength, SCREEN_HEIGHT - 10, paint);
		if (playerNO == 1) {
			canvas.drawText(text + scores.get(1), SCREEN_WIDTH - 4 / 3
					* baseLength, baseHeight, paint);
			canvas.drawText(text + scores.get(2), 10, baseHeight, paint);
			return;
		}
		if (playerNO == 3) {
			canvas.drawText(text + scores.get(0), SCREEN_WIDTH - 4 / 3
					* baseLength, baseHeight, paint);
			canvas.drawText(text + scores.get(1), 10, baseHeight, paint);
			return;
		}
		canvas.drawText(text + scores.get(2),
				SCREEN_WIDTH - 4 / 3 * baseLength, baseHeight, paint);
		canvas.drawText(text + scores.get(0), 10, baseHeight, paint);

	}

	public void drawGameScore(Canvas canvas, int score) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 184, 15));
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(TEXT_SIZE_BIG);
		String str = "本轮分数：" + score;
		canvas.drawText(str, SCREEN_WIDTH / 2 - paint.measureText(str) / 2,
				CARD_WIDTH * 3 / 4, paint);
	}

	/**
	 * 画剩余牌数目，同时根据其他玩家剩余牌数量去画该玩家手牌
	 */
	public void drawCardNumber(Canvas canvas, List<Integer> cardNumber,
			int playerNO, Context con) {
		// Log.i(TAG, "画剩余张数");
		String text = "张数:";
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(255, 184, 15));
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(TEXT_SIZE);
		float baseLength = paint.measureText("玩家xx");
		paint.setTextSize(TEXT_SIZE_SMALL);
		canvas.drawText(text + cardNumber.get(playerNO - 1), TEXT_SIZE_SMALL
				+ baseLength, SCREEN_HEIGHT - 10, paint);

		if (playerNO == 1) {
			canvas.drawText(text + cardNumber.get(1), SCREEN_WIDTH - baseLength
					- paint.measureText(text + cardNumber.get(1)) - 20,
					CARD_WIDTH * 2 / 3, paint);
			canvas.drawText(text + cardNumber.get(2), 20 + baseLength,
					CARD_WIDTH * 2 / 3, paint);

			drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(1),
					SCREEN_WIDTH - 2 * CARD_WIDTH);
			drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(2),
					CARD_WIDTH);
			return;
		}
		if (playerNO == 3) {
			canvas.drawText(text + cardNumber.get(0), SCREEN_WIDTH - baseLength
					- paint.measureText(text + cardNumber.get(1)) - 20,
					CARD_WIDTH * 2 / 3, paint);
			canvas.drawText(text + cardNumber.get(1), 20 + baseLength,
					CARD_WIDTH * 2 / 3, paint);
			drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(0),
					SCREEN_WIDTH - 2 * CARD_WIDTH);
			drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(1),
					CARD_WIDTH);
			return;
		}
		canvas.drawText(text + cardNumber.get(2), SCREEN_WIDTH - baseLength
				- paint.measureText(text + cardNumber.get(1)) - 15, 10, paint);
		canvas.drawText(text + cardNumber.get(1), 15 + baseLength,
				CARD_WIDTH * 2 / 3, paint);

		drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(2), SCREEN_WIDTH
				- 2 * CARD_WIDTH);
		drawCardsBackgroundBaseOnX(con, canvas, cardNumber.get(1), CARD_WIDTH);
	}

	/**
	 * 根據x坐標，畫垂直方向上的只顯示背面的牌
	 * 
	 * @param con
	 * @param canvas
	 * @param num
	 * @param baseX
	 */
	private void drawCardsBackgroundBaseOnX(Context con, Canvas canvas, int num,
			int baseX) {
		if (num == 0)
			return;
		int heightBase = 2 * TEXT_SIZE;
		float factor = (float) (SCREEN_HEIGHT / 2 - CARD_HEIGHT - CARD_WIDTH)
				/ (float) (num / 2 * CARD_HEIGHT * 1 / 3);
		// Log.e(TAG, "剩余空间:"+(float)(SCREEN_HEIGHT/2-CARD_HEIGHT-CARD_WIDTH));
		// Log.e(TAG, "卡牌长:"+(float)(num/2*CARD_HEIGHT*1/3));
		// Log.e(TAG,"factor:"+factor);
		float baseSpace = factor > 1 ? CARD_HEIGHT * 1 / 3 : CARD_HEIGHT * 1
				/ 3 * factor;
		// Log.e(TAG,"baseSpace:"+baseSpace+";int baseSpace"+(int)baseSpace);
		for (int i = 0; i < num; i++) {
			Card card = new Card("0");
			card.setSize(CARD_WIDTH, CARD_HEIGHT);
			card.setLocation(baseX, (int) (i * baseSpace + heightBase));
			ApplicationInfo appInfo = con.getApplicationInfo();
			int id = con.getResources().getIdentifier(
					CardGenerator.cardResourceName(card.getCardId()),
					"drawable", appInfo.packageName);
			Bitmap temp = BitmapFactory.decodeResource(con.getResources(), id);
			canvas.drawBitmap(
					temp,
					card.getSRC(), card.getDST(), null);
			temp.recycle();
		}
	}

	/**
	 * 根據x坐標，畫垂直方向上牌
	 * 
	 * @param con
	 * @param canvas
	 * @param num
	 * @param cardList
	 */
	private  void drawCardsBaseOnX(Context con, Canvas canvas, int baseX,
			List<Card> cardList) {
		if (cardList == null || cardList.size() == 0)
			return;
		int num = cardList.size();
		int baseHeight = (SCREEN_HEIGHT - CARD_HEIGHT - 3 * CARD_WIDTH) / 2
				- num / 2 * CARD_HEIGHT / 3 + 2 * CARD_WIDTH;

		int baseSpace = CARD_HEIGHT / 4;
		// Log.e(TAG, "baseX"+baseX+";heightBase"+heightBase);
		for (int i = 0; i < num; i++) {
			Card card = cardList.get(i);
			card.setSize(CARD_WIDTH, CARD_HEIGHT);
			card.setLocation(baseX, i * baseSpace + baseHeight);
			ApplicationInfo appInfo = con.getApplicationInfo();
			int id = con.getResources().getIdentifier(
					CardGenerator.cardResourceName(card.getCardId()),
					"drawable", appInfo.packageName);
			Bitmap temp = BitmapFactory.decodeResource(con.getResources(), id);
			canvas.drawBitmap(
					temp,
					card.getSRC(), card.getDST(), null);
			temp.recycle();
		}

	}

	public void drawCards(Canvas canvas, List<Card> cardList, Context con) {
		int size = cardList.size();
		int height = SCREEN_HEIGHT - CARD_WIDTH * 2 / 3 - CARD_HEIGHT;
		int baseX = SCREEN_WIDTH / 2 - size / 2 * CARD_WIDTH * 3 / 4;
		int card_intent = CARD_HEIGHT / 2;
		for (int i = 0; i < size; i++) {
			Card card = cardList.get(i);
			card.setSize(CARD_WIDTH, CARD_HEIGHT);
			if (!card.isClicked())
				card.setLocation(baseX + i * CARD_WIDTH * 3 / 4, height);	//没有被点击的卡牌高度设为height
			else
				card.setLocation(baseX + i * CARD_WIDTH * 3 / 4, height		//被点击的卡牌高度设为height- card_intent
						- card_intent);

			ApplicationInfo appInfo = con.getApplicationInfo();
			int id = con.getResources().getIdentifier(
					CardGenerator.cardResourceName(card.getCardId()),
					"drawable", appInfo.packageName);
			Bitmap temp = BitmapFactory.decodeResource(con.getResources(), id);
			canvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
			temp.recycle();
			card=null;
		}
	}

	public void drawOutList(Context con, Canvas canvas,
			Map<Integer, List<Card>> outList, int playerNO) {
		if (outList == null || outList.size() == 0)
			return;

		List<Card> mainOutList = outList.get(playerNO - 1);
		List<Card> playerRightoutList;
		List<Card> playerLeftoutList;
		int mainBaseY = SCREEN_HEIGHT - CARD_HEIGHT - CARD_WIDTH * 3;
		int baseWidth = SCREEN_WIDTH / 2 - mainOutList.size() / 2 * CARD_WIDTH
				* 1 / 3;
		int leftXEgal = 3 * CARD_WIDTH;
		int rightXEgal = SCREEN_WIDTH - 4 * CARD_WIDTH;
		for (int i = 0; i < mainOutList.size(); i++) {
			Card card = mainOutList.get(i);
			card.setSize(CARD_WIDTH, CARD_HEIGHT);
			card.setLocation(baseWidth + i * CARD_WIDTH * 1 / 3, mainBaseY);
			ApplicationInfo appInfo = con.getApplicationInfo();
			int id = con.getResources().getIdentifier(
					CardGenerator.cardResourceName(card.getCardId()),
					"drawable", appInfo.packageName);
			Bitmap temp = BitmapFactory.decodeResource(con.getResources(), id);
			canvas.drawBitmap(
					temp,
					card.getSRC(), card.getDST(), null);
			temp.recycle();
		}
		if (playerNO == 1) {
			playerRightoutList = outList.get(1);
			playerLeftoutList = outList.get(2);
		} else if (playerNO == 3) {
			playerRightoutList = outList.get(0);
			playerLeftoutList = outList.get(1);
		} else {
			playerRightoutList = outList.get(2);
			playerLeftoutList = outList.get(0);
		}
		drawCardsBaseOnX(con, canvas, rightXEgal, playerRightoutList);
		drawCardsBaseOnX(con, canvas, leftXEgal, playerLeftoutList);
	}

}
