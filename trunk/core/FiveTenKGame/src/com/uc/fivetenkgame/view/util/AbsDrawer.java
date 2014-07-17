/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.util;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;

/**������Ļ���
 * @author chensl@ucweb.com
 *
 * ����4:33:39 2014-7-16
 */
public abstract class AbsDrawer {
	protected Canvas canvas;
	protected Context context;
	protected ScreenSizeHolder screenHolder;
	protected CardSizeHolder cardSizeHolder;
	protected float TEXT_SIZE, TEXT_SIZE_SMALL;
	public AbsDrawer(Context context,ScreenSizeHolder screenHolder, CardSizeHolder cardSizeHolder) {
		this.context=context;
		this.screenHolder = screenHolder;
		this.cardSizeHolder = cardSizeHolder;
		TEXT_SIZE=(float)cardSizeHolder.width*2/3;
		TEXT_SIZE_SMALL=(float)cardSizeHolder.width/2;
		
	}
	/**
	 * 
	 * @param canvas
	 */
	public void initCanvas(Canvas canvas){
		this.canvas=canvas;
	}
	/**
	 * ����(X,Y)������
	 * @param score	����
	 * @param paint
	 * @param x		
	 * @param y
	 */
	public  void drawScore(int score,Paint paint,float x,float y){
		paint.setTextSize(TEXT_SIZE_SMALL);
		paint.setColor(Color.rgb(255, 184, 15));
		canvas.drawText("������" + score, x, y, paint);
	}
	/**
	 *  ����(X,Y)��ʣ��������
	 * @param cardsNumber
	 * @param paint
	 * @param x
	 * @param y
	 */
	public final void drawCardsNumber(int cardsNumber,Paint paint,float x,float y){
		paint.setTextSize(TEXT_SIZE_SMALL);
		paint.setColor(Color.rgb(255, 184, 15));
		canvas.drawText("������" + cardsNumber, x, y, paint);
	}
	/**
	 *  ����(X,Y)�����
	 * @param playerId
	 * @param paint
	 * @param x
	 * @param y
	 */
	public  void drawPlayer(int playerId,Paint paint,float x,float y){
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(Color.rgb(255, 246, 143));
		canvas.drawText("���" + playerId, x, y, paint);
	}
}
