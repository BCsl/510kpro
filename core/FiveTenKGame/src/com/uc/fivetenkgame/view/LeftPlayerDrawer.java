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
import android.graphics.Paint;
import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * @author chensl@ucweb.com
 *
 * ����11:33:40 2014-7-30
 */
public class LeftPlayerDrawer extends AbsOtherPlayerInfoDrawer {

	private final int LEFT_CARDS_BASEX;
	private final float LEFT_OUTCARDS_BASEX;
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

		
	}
	@Override
	protected void doDraw(Paint paint,String name,boolean isMyTurn,int timeRemind,int cardNumber,int score,List<Card> outList){
		if (isMyTurn) {
			drawTime(String.valueOf(timeRemind), paint,
					LEFT_CARDS_BASEX + 2*mCardSizeHolder.width ,
					mScreenHolder.height / 2);
			drawHandCardFlag(LEFT_CARDS_BASEX
					+ mCardSizeHolder.width + 10);
		}
		paint.setTextSize(TEXT_SIZE_SMALL);
		drawOutList(outList,
				LEFT_OUTCARDS_BASEX);
		drawPlayer(name, paint, 10, TEXT_SIZE);
		drawCardsNumber(cardNumber, paint,
				10, 2 * TEXT_SIZE,LEFT_CARDS_BASEX);
		drawScore(score,
				paint, paint.measureText(name) + 20, TEXT_SIZE);
	}

}