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
 * �����������Ϣ�ࣨ��ֱ�����ϵĲ��֣�
 * 
 * @author chensl@ucweb.com
 * 
 *         ����6:42:47 2014-7-16
 */
public abstract class AbsOtherPlayerInfoDrawer extends AbsDrawer {

	protected AbsOtherPlayerInfoDrawer(Context context,
			ScreenSizeHolder screenHolder, CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
	}

	/**
	 * ����drawPlayer���趨YΪͬһ�߶�
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
	 * ����drawScore,�趨YΪ���Ը߶�
	 * 
	 * @param score
	 * @param paint
	 * @param x
	 */
	protected void drawScore(int score, Paint paint, float x) {
		super.drawScore(score, paint, x, 3 * mCardSizeHolder.width + 10);
	}

	/**
	 * ����drawCardsNumber���趨YΪ���Ը߶�
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
	 * ��baseXΪ��׼������ֱ�������ѳ�����
	 * 
	 * @param outList
	 * @param baseX
	 *            �����ѳ����ƣ���Ӧ�������ڵ�X����
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
	 * �����Ʊ�־
	 * 
	 * @param baseX
	 */
	protected void drawHandCardFlag(float baseX) {
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext, ResourseCommon.HANDCARD_FLAG),
				baseX, mCardSizeHolder.width + TEXT_SIZE_SMALL, null);
	}

	/**
	 * ��ȫ����Ϣ
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
