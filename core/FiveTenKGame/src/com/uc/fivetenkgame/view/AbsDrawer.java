/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import my.example.fivetenkgame.R;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 画界面的基类
 * 
 * @author chensl@ucweb.com
 * 
 *         下午4:33:39 2014-7-16
 */
public abstract class AbsDrawer {
	protected Canvas mCanvas;
	protected Context mContext;
	protected ScreenSizeHolder mScreenHolder;
	protected CardSizeHolder mCardSizeHolder;
	protected final float TEXT_SIZE, TEXT_SIZE_SMALL;
/**
 * 
 * @param context
 * @param screenHolder
 * @param cardSizeHolder
 */
	protected AbsDrawer(Context context, ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		this.mContext = context;
		this.mScreenHolder = screenHolder;
		this.mCardSizeHolder = cardSizeHolder;
		TEXT_SIZE = (float) cardSizeHolder.width * 2 / 3;
		TEXT_SIZE_SMALL = (float) cardSizeHolder.width / 2;
	}

	/**
	 * 
	 * @param canvas
	 */
	protected final void initCanvas(Canvas canvas) {
		this.mCanvas = canvas;
	}
	/**
	 * 根据(X,Y)画分数
	 * 
	 * @param score
	 *            分数
	 * @param paint
	 * @param x
	 * @param y
	 */
	protected final void drawScore(int score, Paint paint, float x, float y) {
		paint.setTextSize(TEXT_SIZE_SMALL);
		paint.setColor(Color.rgb(255, 184, 15));
		mCanvas.drawText(mContext.getResources().getString(R.string.score) + score, x, y, paint);
	}

	/**
	 * 根据(X,Y)画剩余牌数量
	 * 
	 * @param cardsNumber
	 * @param paint
	 * @param x
	 * @param y
	 */
	protected final void drawCardsNumber(int cardsNumber, Paint paint, float x,
			float y) {
		paint.setTextSize(TEXT_SIZE_SMALL);
		paint.setColor(Color.rgb(255, 184, 15));
		mCanvas.drawText(mContext.getResources().getString(R.string.cards_number) + cardsNumber, x, y, paint);
	}

	/**
	 * 根据(X,Y)画玩家
	 * 
	 * @param playerName
	 * @param paint
	 * @param x
	 * @param y
	 */
	protected final void drawPlayer(String playerName, Paint paint, float x, float y) {
		paint.setTextSize(TEXT_SIZE_SMALL);
		paint.setColor(Color.rgb(255, 246, 143));
		mCanvas.drawText(playerName, x, y, paint);
	}
	/**
	 * 画剩余时间
	 * @param timeRemind		
	 * @param paint
	 * @param x
	 * @param y
	 */
	protected final void drawTime(String timeRemind,Paint paint, float x, float y){
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(Color.rgb(255, 246, 143));
		mCanvas.drawText(timeRemind, x, y, paint);
	}
}
