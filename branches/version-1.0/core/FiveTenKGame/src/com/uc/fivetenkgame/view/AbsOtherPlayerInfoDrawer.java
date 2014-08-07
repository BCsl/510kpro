/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * 画其他玩家信息类（垂直方向上的布局）
 * 
 * @author chensl@ucweb.com
 * 
 *         下午6:42:47 2014-7-16
 */
public abstract class AbsOtherPlayerInfoDrawer extends AbsDrawer {

	protected AbsOtherPlayerInfoDrawer(Context context,
			ScreenSizeHolder screenHolder, CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
	}

	/**
	 * 重载drawPlayer，设定Y为同一高度
	 * 
	 * @param playerName
	 * @param paint
	 * @param x
	 */
	protected void drawPlayer(String playerName, Paint paint, float x) {
		super.drawPlayer(playerName, paint, x, TEXT_SIZE);
	}

	/**
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	protected void drawIcon(Bitmap bitmap, float x) {
		mCanvas.drawBitmap(bitmap, x, TEXT_SIZE + 10, null);
	}

	/**
	 * 重载drawScore,设定Y为用以高度
	 * 
	 * @param score
	 * @param paint
	 * @param x
	 */
	protected void drawScore(int score, Paint paint, float x) {
		super.drawScore(score, paint, x, 3 * mCardSizeHolder.width + 10);
	}

	/**
	 * 重载drawCardsNumber，设定Y为用以高度
	 * 
	 * @param cardsNumber
	 * @param paint
	 * @param x
	 */
	protected void drawCardsNumber(int cardsNumber, Paint paint, float x) {
		super.drawCardsNumber(cardsNumber, paint, x, 3 * mCardSizeHolder.width
				+ TEXT_SIZE + 10);
	}

	/**
	 * 以baseX为基准，画垂直方向上已出的牌
	 * 
	 * @param outList
	 * @param baseX
	 *            所有已出的牌，都应该排列在的X坐标
	 */
	protected void drawOutList(List<Card> outList, float baseX) {
		if (outList == null || outList.size() == 0)
			return;
		float factor =  mScreenHolder.height
				/ ((float) mCardSizeHolder.height /4 *outList.size()) > 1 ? 1
				:  mScreenHolder.height / ((float) mCardSizeHolder.height /4 *outList.size());
		float space = (float) mCardSizeHolder.height /4 * factor;
		float baseY = (float) mScreenHolder.height - outList.size() 
				* space;
		baseY=baseY/2-mCardSizeHolder.width;
		Bitmap temp = null;
		Card card = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
			card.setLocation((int) baseX, (int) (i * space + baseY));
			temp = CardUtil.getBitmap(mContext,
					CardUtil.getResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
	}

	/**
	 * 画出牌标志
	 * 
	 * @param baseX
	 */
	protected void drawHandCardFlag(float baseX) {
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext, ResourseCommon.HANDCARD_FLAG),
				baseX, mCardSizeHolder.width + TEXT_SIZE_SMALL, null);
	}

	/**
	 * 画全部信息
	 * 
	 * @param paint
	 * @param name
	 * @param isMyTurn
	 * @param timeRemind
	 * @param cardNumber
	 * @param score
	 * @param outList
	 */
	protected abstract void doDraw(Paint paint, String name, boolean isMyTurn,
			int timeRemind, int cardNumber, int score, List<Card> outList);

}
