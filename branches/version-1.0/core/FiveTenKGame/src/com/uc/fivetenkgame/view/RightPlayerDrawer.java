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
 *         ÉÏÎç11:43:11 2014-7-30
 */
public class RightPlayerDrawer extends AbsOtherPlayerInfoDrawer {

	private final int RIGHT_CARDS_BASEX;
	private final float RIGHT_OUTCARDS_BASEX;
	
	private 	float nameLength;

	/**
	 * @param context
	 * @param screenHolder
	 * @param cardSizeHolder
	 */
	public RightPlayerDrawer(Context context, ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
		RIGHT_OUTCARDS_BASEX = mScreenHolder.width - 4 * mCardSizeHolder.width;
		RIGHT_CARDS_BASEX = mScreenHolder.width - 2 * mCardSizeHolder.width;
	}

	@Override
	protected void doDraw(Paint paint, String name, boolean isMyTurn,
			int timeRemind, int cardNumber, int score, List<Card> outList) {
		// TODO Auto-generated method stub
		if (isMyTurn) {
			drawTime(String.valueOf(timeRemind), paint, RIGHT_CARDS_BASEX
					- mCardSizeHolder.width + 10, mScreenHolder.height / 2);
			drawHandCardFlag(RIGHT_CARDS_BASEX - mCardSizeHolder.width + 10);
		}
		drawOutList(outList, RIGHT_OUTCARDS_BASEX);
		paint.setTextSize(TEXT_SIZE_SMALL);
		nameLength=paint.measureText(name);
		drawPlayer(name, paint, mScreenHolder.width - nameLength-10,
				TEXT_SIZE);
		drawCardsNumber(cardNumber, paint, mScreenHolder.width - paint.measureText(mContext.getString(R.string.cards_number)+String.valueOf(cardNumber)) - 20,
				2 * TEXT_SIZE,RIGHT_CARDS_BASEX);
		drawScore(score, paint, mScreenHolder.width
				- nameLength -paint.measureText(mContext.getString(R.string.score)+String.valueOf(score)) - 20, TEXT_SIZE);

	}

}
