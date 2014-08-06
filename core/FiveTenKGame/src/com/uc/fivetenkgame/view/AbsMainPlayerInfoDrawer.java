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
import android.graphics.Paint;
import android.widget.Toast;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardUtil;

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
		float space = (mScreenHolder.width)
				/ (float) (cardList.size() * TEXT_SIZE) >= 1 ? TEXT_SIZE
				: (mScreenHolder.width) / (float) (cardList.size() * TEXT_SIZE)
						* TEXT_SIZE;
		if (space < (float) mCardSizeHolder.width / 3 && isFirst) {
			isFirst = false;
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.tips),
					Toast.LENGTH_LONG).show();
		}
		float baseX = (float) mScreenHolder.width  - cardList.size() 
				* space;
		baseX/=2;
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
			temp = CardUtil.getBitmap(mContext,
					CardUtil.getResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
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

		float baseX = (float) mScreenHolder.width / 2 - outList.size() / 2
				* space;
		Card card = null;
		Bitmap temp = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(mCardSizeHolder.width, mCardSizeHolder.height);
			card.setLocation((int) (baseX + i * space), (int) baseY);
			temp = CardUtil.getBitmap(mContext,
					CardUtil.getResourceName(card.getCardId()));
			mCanvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
	}

	/**
	 * 画整体信息
	 * 
	 * @param paint
	 * @param ismyTurn
	 * @param TimeRemind
	 * @param name
	 * @param cardNumber
	 * @param score
	 * @param cardList
	 * @param outList
	 */
	protected abstract void doDraw(Paint paint, boolean ismyTurn,
			int TimeRemind, String name, int cardNumber, int score,
			List<Card> cardList, List<Card> outList);

}
