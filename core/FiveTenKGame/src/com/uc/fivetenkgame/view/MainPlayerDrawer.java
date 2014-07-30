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
 * ����11:12:31 2014-7-30
 */
public class MainPlayerDrawer extends AbsMainPlayerInfoDrawer {
	protected float BUTTON_BASE_HEIGHT;
	protected float PLAYER_TEXT_LENGTH;
	protected float SCORE_TEXT_LENGTH;
	protected float MAIN_CARDS_BASEY;
	protected float MAIN_OUT_CARDS_BASEY;
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
		PLAYER_TEXT_LENGTH = paint.measureText(mContext.getResources().getString(R.string.player_text_length));
		paint.setTextSize(TEXT_SIZE_SMALL);
		SCORE_TEXT_LENGTH = paint.measureText(mContext.getResources().getString(R.string.score_text_length));
		MAIN_CARDS_BASEY = (float) mScreenHolder.height
				- (float) mCardSizeHolder.width * 2 / 3
				- mCardSizeHolder.height;
		MAIN_OUT_CARDS_BASEY = (float) mScreenHolder.height
				- mCardSizeHolder.height - mCardSizeHolder.width * 3;
		
	}
	@Override
	protected void doDraw(Paint paint,boolean ismyTurn,int TimeRemind,String name,int cardNumber,int score,List<Card> cardList,List<Card> outList) {
		if (ismyTurn) {
			drawButton(paint, BUTTON_BASE_HEIGHT);
			drawTime(String.valueOf(TimeRemind), paint,
					mScreenHolder.width / 2, BUTTON_BASE_HEIGHT);
		}
		drawPlayer(name, paint, 10.0f,
				(float) mScreenHolder.height - 5);
		drawCardsNumber(cardNumber,
				paint, PLAYER_TEXT_LENGTH + TEXT_SIZE_SMALL,
				mScreenHolder.height - 10);
		drawScore(score, paint,
				mScreenHolder.width - 2 * SCORE_TEXT_LENGTH,
				mScreenHolder.height - 10);
		drawCards(cardList, MAIN_CARDS_BASEY,
				mCardSizeHolder.height / 2);
		drawOutList(outList,
				MAIN_OUT_CARDS_BASEY);
	}

}
