/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.entity;

import android.content.Context;
import android.graphics.Canvas;
import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * @author chensl@ucweb.com
 * 
 *         ÏÂÎç3:58:39 2014-7-31
 */
public class ButtonHandCard extends AbsButton {
	private static AbsButton mHandCardButton;
	public ButtonHandCard(Context con, Canvas can, float x, float y) {
		super(con, can, x, y);
		HALF_OF_WIDTH=CardUtil.getBitmap(mContext,ResourseCommon.BUTTON_HANDCARD_NORMAL).getWidth()/2;
		HALF_OF_HEIGHT= CardUtil.getBitmap(mContext,ResourseCommon.BUTTON_HANDCARD_NORMAL).getHeight()/2;
	}
	public static AbsButton getInstance(){
		if (mHandCardButton == null)
				throw new IllegalArgumentException("init button first!!!");
			return mHandCardButton;
	}
	public static AbsButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mHandCardButton == null)
			mHandCardButton = new ButtonHandCard(con, can, x, y);
		return mHandCardButton;
	}
	@Override
	protected void drawButtonOnPressedState() {
		// TODO Auto-generated method stub
		draw(CardUtil.getBitmap(mContext,ResourseCommon.BUTTON_HANDCARD_PRESSED));
	}

	@Override
	protected void drawButtonOnNormalState() {
		// TODO Auto-generated method stub
		draw(CardUtil.getBitmap(mContext,ResourseCommon.BUTTON_HANDCARD_NORMAL));
	}

}
