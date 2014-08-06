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
import android.graphics.Paint;
import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * @author chensl@ucweb.com
 *
 * ÉÏÎç11:12:31 2014-7-30
 */
public class MainPlayerDrawer extends AbsMainPlayerInfoDrawer {
	protected final float BUTTON_BASE_HEIGHT;
	protected final float MAIN_CARDS_BASEY;
	protected final float MAIN_OUT_CARDS_BASEY;
	/**
	 * @param context
	 * @param screenHolder
	 * @param cardSizeHolder
	 */
	protected MainPlayerDrawer(Context context, ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
		BUTTON_BASE_HEIGHT = (float) mScreenHolder.height
				- mCardSizeHolder.width * 3;
		Paint paint = new Paint();
		paint.setTextSize(TEXT_SIZE);
		MAIN_CARDS_BASEY = (float) mScreenHolder.height
				- (float) mCardSizeHolder.width * 2 / 3
				- mCardSizeHolder.height;
		MAIN_OUT_CARDS_BASEY = (float) mScreenHolder.height
				- mCardSizeHolder.height - mCardSizeHolder.width * 3;
	}
	@Override
	protected void doDraw(Paint paint,boolean isMyTurn,int TimeRemind,String name,int cardNumber,int score,List<Card> cardList,List<Card> outList) {
		if (isMyTurn) {
			drawTime(String.valueOf(TimeRemind), paint,
					mScreenHolder.width / 2, BUTTON_BASE_HEIGHT);
		}
		drawPlayer(name, paint, 10.0f,
				(float) mScreenHolder.height - 10);
		drawScore(score,
				paint, paint.measureText(name) + TEXT_SIZE_SMALL,
				mScreenHolder.height - 10);
		drawCardsNumber(cardNumber, paint,
				mScreenHolder.width - 1.5f*paint.measureText(mContext.getResources().getString(R.string.cards_number)+String.valueOf(cardNumber)),
				mScreenHolder.height - 10);
//		drawCardsNumber(cardNumber,
//				paint, paint.measureText(name) + TEXT_SIZE_SMALL,
//				mScreenHolder.height - 10);
//		drawScore(score, paint,
//				mScreenHolder.width - 2 * paint.measureText(mContext.getResources().getString(R.string.score)+String.valueOf(score)),
//				mScreenHolder.height - 10);
		drawCards(cardList, MAIN_CARDS_BASEY,
				mCardSizeHolder.height / 2);
		drawOutList(outList,
				MAIN_OUT_CARDS_BASEY);
	}

}
