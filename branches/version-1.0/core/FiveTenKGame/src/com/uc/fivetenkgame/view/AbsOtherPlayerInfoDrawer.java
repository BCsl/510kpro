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
	 * ����(X,Y)��ʣ��������ͬʱ��baseXΪ��׼������ֱ�����ϵ�ֻ��ʾ�������
	 * 
	 * @param cardsNumber
	 * @param paint
	 * @param x
	 * @param y
	 * @param baseX
	 *            ���б����ƣ���Ӧ�������ڵ�X����
	 */
	protected final void drawCardsNumber(int cardsNumber, Paint paint, float x, float y,
			float baseX) {
		super.drawCardsNumber(cardsNumber, paint, x, y);
		if (cardsNumber == 0)
			return;
		cardsNumber = cardsNumber > 14 ? 14 : cardsNumber;// ���ֻ��15�ű������
		float factor = (float) (mScreenHolder.height / 2 - mCardSizeHolder.height - mCardSizeHolder.width)
				/ (float) (cardsNumber / 2 * mCardSizeHolder.height * 1 / 3);
		float baseSpace = (float) factor > 1 ? mCardSizeHolder.height * 1 / 3
				: mCardSizeHolder.height * 1 / 3 * factor;
		Card card = new Card(Card.CARD_BG_ID);
		card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
		Bitmap temp = null;
		for (int i = 0; i < cardsNumber; i++) {
			card.setLocation((int) baseX, (int) (i * baseSpace +  mCardSizeHolder.width+TEXT_SIZE_SMALL));
			temp = CardUtil.getBitmap(mContext,
					CardUtil.ResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
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
		float factor = (float) mScreenHolder.height
				/ (float) mCardSizeHolder.height / 4 > 1 ? 1
				: (float) mScreenHolder.height / (float) mCardSizeHolder.height
						/ 4;
		float baseSpace = (float) mCardSizeHolder.height / 4 * factor;
		float baseY = (float) mScreenHolder.height / 2 - outList.size() / 2
				* baseSpace;
		Bitmap temp = null;
		Card card = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
			card.setLocation((int) baseX, (int) (i * baseSpace + baseY));
			temp = CardUtil.getBitmap(mContext,
					CardUtil.ResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
	}
/**
 * �����Ʊ�־
 * @param baseX
 */
	protected void drawHandCardFlag(float baseX) {
		mCanvas.drawBitmap(CardUtil.getBitmap(mContext, ResourseCommon.HANDCARD_FLAG), baseX,
				 mCardSizeHolder.width+TEXT_SIZE_SMALL, null);
	}
	
	/**
	 * ��ȫ����Ϣ
	 * @param paint
	 * @param name
	 * @param isMyTurn
	 * @param timeRemind
	 * @param cardNumber
	 * @param score
	 * @param outList
	 */
	protected abstract void doDraw(Paint paint,String name,boolean isMyTurn,int timeRemind,int cardNumber,int score,List<Card> outList);

}
