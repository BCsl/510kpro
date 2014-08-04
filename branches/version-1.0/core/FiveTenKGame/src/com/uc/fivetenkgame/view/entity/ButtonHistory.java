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
 *         下午4:30:32 2014-7-31
 */
public class ButtonHistory extends AbsButton {
	/**
	 * 以（x,y）为中心画历史记录按钮
	 * 
	 * @param con
	 * @param can
	 * @param x
	 * @param y
	 */
	public ButtonHistory(Context con, Canvas can, float x, float y) {
		super(con, x, y);
		HALF_OF_WIDTH = CardUtil.getBitmap(mContext,
				ResourseCommon.BUTTON_HISTORY_NORMAL).getWidth() / 2;
		HALF_OF_HEIGHT = CardUtil.getBitmap(mContext,
				ResourseCommon.BUTTON_HISTORY_NORMAL).getHeight() / 2;
	}

	private static AbsButton mButtonHistory;

	public static AbsButton getInstance() {
		if (mButtonHistory == null)
			throw new IllegalArgumentException("init first!!!");
		return mButtonHistory;
	}

	public static AbsButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mButtonHistory == null)
			mButtonHistory = new ButtonHistory(con, can, x, y);
		return mButtonHistory;
	}

	@Override
	protected void drawButtonOnPressedState(Canvas canvas) {
		// TODO Auto-generated method stub
		draw(canvas,CardUtil.getBitmap(mContext, ResourseCommon.BUTTON_HISTORY_PRESSED));

	}

	@Override
	protected void drawButtonOnNormalState(Canvas canvas) {
		// TODO Auto-generated method stub
		draw(canvas,CardUtil.getBitmap(mContext, ResourseCommon.BUTTON_HISTORY_NORMAL));

	}
}
