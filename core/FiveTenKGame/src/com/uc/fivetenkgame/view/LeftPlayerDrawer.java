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
 * ÉÏÎç11:33:40 2014-7-30
 */
public class LeftPlayerDrawer extends AbsOtherPlayerInfoDrawer {

	private final int LEFT_CARDS_BASEX;
	private final float LEFT_OUTCARDS_BASEX;
//	private final  int PLAYER_TEXT_LENGTH;
	/**
	 * @param context
	 * @param screenHolder
	 * @param cardSizeHolder
	 */
	protected LeftPlayerDrawer(Context context, ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
		LEFT_OUTCARDS_BASEX = 3 * mCardSizeHolder.width;
		LEFT_CARDS_BASEX = mCardSizeHolder.width;
//		Paint paint = new Paint();
//		paint.setTextSize(TEXT_SIZE_SMALL);
//		PLAYER_TEXT_LENGTH = (int) paint.measureText(mContext.getResources().getString(R.string.player_text_length));
		
	}
	@Override
	protected void doDraw(Paint paint,String name,boolean isMyTurn,int timeRemind,int cardNumber,int score,List<Card> outList){
		if (isMyTurn) {
			drawTime(String.valueOf(timeRemind), paint,
					LEFT_CARDS_BASEX + mCardSizeHolder.width + 10,
					mScreenHolder.height / 2);
			drawHandCardFlag(LEFT_CARDS_BASEX
					+ mCardSizeHolder.width + 10);
		}
		paint.setTextSize(TEXT_SIZE_SMALL);
		drawOutList(outList,
				LEFT_OUTCARDS_BASEX);
		drawPlayer(name, paint, 10, TEXT_SIZE);
		drawScore(score, paint,
				10, 2 * TEXT_SIZE);
		drawCardsNumber(cardNumber,
				paint, paint.measureText(name) + 10, TEXT_SIZE, LEFT_CARDS_BASEX);
		
	}

}
