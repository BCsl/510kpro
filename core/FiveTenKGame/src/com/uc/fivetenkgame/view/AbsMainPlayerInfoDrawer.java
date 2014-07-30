/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.List;

import my.example.fivetenkgame.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.Toast;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardGenerator;

/**
 * @author chensl@ucweb.com
 * 
 *         下午5:03:12 2014-7-16
 */
public abstract class AbsMainPlayerInfoDrawer extends AbsDrawer {

	private String TAG = "MainPlayerInfoDrawer";
	private boolean isFirst = true;

	protected AbsMainPlayerInfoDrawer(Context context,
			ScreenSizeHolder screenHolder, CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
	}

	/**
	 * 以baseY为基准，水平画玩家手牌
	 * 
	 * @param cardList
	 * @param baseY
	 * @param cardIntent
	 */
	protected void drawCards(List<Card> cardList, float baseY, float cardIntent) {
		if (cardList == null || cardList.size() == 0)
			return;
		float space = (mScreenHolder.width - 10)
				/ (cardList.size() * TEXT_SIZE) > 1 ? TEXT_SIZE
				: (mScreenHolder.width - 10) / (cardList.size() * TEXT_SIZE)
						* TEXT_SIZE;
		float baseX = (float) mScreenHolder.width / 2 - cardList.size() / 2
				* space;
		Card card = null;
		Bitmap temp = null;
		for (int i = 0; i < cardList.size(); i++) {
			card = cardList.get(i);
			card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
			if (!card.isClicked())
				card.setLocation((int) (baseX + i * space), (int) baseY);
			else
				card.setLocation((int) (baseX + i * space),
						(int) (baseY - cardIntent));
			temp = CardGenerator.getBitmap(mContext,
					CardGenerator.cardResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
	}

	/**
	 * 以baseY为基准，水平方向上画按钮
	 * 
	 * @param paint
	 * @param baseY
	 */
	protected void drawButton(Paint paint, float baseY) {
		paint.setColor(Color.GRAY);
		paint.setTextSize(TEXT_SIZE);
		float baseLength = paint.measureText(mContext
				.getString(R.string.hand_cards));
		float leftButtonXEagle = mScreenHolder.width / 2 - 2
				* mCardSizeHolder.width;
		float rightButtonXEagle = mScreenHolder.width / 2 + 2
				* mCardSizeHolder.width;
		RectF rectLeft = new RectF(leftButtonXEagle, baseY - TEXT_SIZE,
				leftButtonXEagle + baseLength, baseY + 10);
		RectF rectRight = new RectF(rightButtonXEagle, baseY - TEXT_SIZE,
				rightButtonXEagle + baseLength, baseY + 10);
		mCanvas.drawRoundRect(rectLeft, 20f, 20f, paint);
		mCanvas.drawRoundRect(rectRight, 20f, 20f, paint);
		paint.setColor(Color.rgb(255, 48, 48));
		mCanvas.drawText(mContext.getString(R.string.hand_cards),
				leftButtonXEagle, baseY, paint);
		mCanvas.drawText(mContext.getString(R.string.give_up),
				rightButtonXEagle, baseY, paint);
	}

	/**
	 * 以baseY为基准，水平方向上画已出牌
	 * 
	 * @param outList
	 * @param baseY
	 */
	protected void drawOutList(List<Card> outList, float baseY) {
		if (outList == null || outList.size() == 0)
			return;
		float space = (float) (mScreenHolder.width - 10)
				/ (outList.size() * TEXT_SIZE_SMALL) > 1 ? TEXT_SIZE_SMALL
				: (mScreenHolder.width - 10)
						/ (outList.size() * TEXT_SIZE_SMALL) * TEXT_SIZE_SMALL;
		if (space < (float) mCardSizeHolder.width / 3 && isFirst) {
			isFirst = false;
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.tips),
					Toast.LENGTH_LONG).show();
		}

		float baseX = (float) mScreenHolder.width / 2 - outList.size() / 2
				* space;
		Card card = null;
		Bitmap temp = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
			card.setLocation((int) (baseX + i * space), (int) baseY);
			temp = CardGenerator.getBitmap(mContext,
					CardGenerator.cardResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
	}
	/**
	 * 画整体信息
	 * @param paint
	 * @param ismyTurn	
	 * @param TimeRemind
	 * @param name					
	 * @param cardNumber
	 * @param score
	 * @param cardList
	 * @param outList
	 */
	protected  abstract void doDraw(Paint paint,boolean ismyTurn,int TimeRemind,String name,int cardNumber,int score,List<Card> cardList,List<Card> outList); 

}
