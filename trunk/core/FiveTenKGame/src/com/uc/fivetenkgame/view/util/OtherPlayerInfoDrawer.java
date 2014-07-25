/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.util;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 画其他玩家信息类（垂直方向上的布局）
 * 
 * @author chensl@ucweb.com
 * 
 *         下午6:42:47 2014-7-16
 */
public class OtherPlayerInfoDrawer extends AbsDrawer {

	public OtherPlayerInfoDrawer(Context context,
			ScreenSizeHolder screenHolder, CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
	}

	/**
	 * 根据(X,Y)画剩余牌数，同时以baseX为基准，画垂直方向上的只显示背面的牌
	 * 
	 * @param cardsNumber
	 * @param paint
	 * @param x
	 * @param y
	 * @param baseX
	 *            所有背景牌，都应该排列在的X坐标
	 */
	public void drawCardsNumber(int cardsNumber, Paint paint, float x, float y,
			float baseX) {
		super.drawCardsNumber(cardsNumber, paint, x, y);
		if (cardsNumber == 0)
			return;

		cardsNumber = cardsNumber > 15 ? 15 : cardsNumber;// 最多只画15张背面的牌
		float factor = (float) (screenHolder.height / 2 - cardSizeHolder.height - cardSizeHolder.width)
				/ (float) (cardsNumber / 2 * cardSizeHolder.height * 1 / 3);
		float baseSpace = (float) factor > 1 ? cardSizeHolder.height * 1 / 3
				: cardSizeHolder.height * 1 / 3 * factor;
		Card card = new Card(Card.CARD_BG_ID);
		card.setSize(cardSizeHolder.width, cardSizeHolder.height);
		Bitmap temp = null;
		for (int i = 0; i < cardsNumber; i++) {
			card.setLocation((int) baseX, (int) (i * baseSpace + 2 * TEXT_SIZE));
			temp = CardGenerator.getBitmap(context,
					CardGenerator.cardResourceName(card.getCardId()));
			canvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}

	}

	/**
	 * 以baseX为基准，画垂直方向上已出的牌
	 * 
	 * @param outList
	 * @param baseX
	 *            所有已出的牌，都应该排列在的X坐标
	 */
	public void drawOutList(List<Card> outList, float baseX) {
		if (outList == null || outList.size() == 0)
			return;
		float factor = (float) screenHolder.height
				/ (float) cardSizeHolder.height / 4 > 1 ? 1
				: (float) screenHolder.height / (float) cardSizeHolder.height
						/ 4;
		float baseSpace = (float) cardSizeHolder.height / 4 * factor;
		float baseY = (float) screenHolder.height / 2 - outList.size() / 2
				* baseSpace;
		Bitmap temp = null;
		Card card = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(cardSizeHolder.width, cardSizeHolder.height);
			card.setLocation((int) baseX, (int) (i * baseSpace + baseY));
			temp = CardGenerator.getBitmap(context,
					CardGenerator.cardResourceName(card.getCardId()));
			canvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
	}

	public void drawHandCardFlag(float left, float top) {
		canvas.drawBitmap(CardGenerator.getBitmap(context, "chupai"), left,
				top, null);
	}
}
