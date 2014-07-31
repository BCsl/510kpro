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
import android.util.Log;

import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * @author chensl@ucweb.com
 *
 * ÏÂÎç4:30:32 2014-7-31
 */
public class ButtonHistory implements IButton {
	private static ButtonHistory mButtonHistory;
	private Context mContext;
	private Canvas mCanvas;
	private float x;
	private float y;
	private String TAG ="ButtonHistory";

	private ButtonHistory(Context con, Canvas can, float x, float y) {
		mContext = con;
		mCanvas = can;
		this.x = x;
		this.y = y;
	}
	public static IButton getInstance(){
		if (mButtonHistory == null)
				new IllegalArgumentException("init first!!!");
			return mButtonHistory;
	}
	public static IButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mButtonHistory == null)
			mButtonHistory = new ButtonHistory(con, can, x, y);
		return mButtonHistory;
	}
	public void doDraw() {
		if(mCanvas!=null)
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext,
						CardUtil.BUTTON_HISTORY_NORMAL_NAME),x,y- CardUtil.getBitmap(mContext,
								CardUtil.BUTTON_HISTORY_NORMAL_NAME)
								.getHeight(), null);

	}
	
	public void onClick() {
		if(mCanvas!=null)
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext,
						CardUtil.BUTTON_HISTORY_PRESSED_NAME),x,y- CardUtil.getBitmap(mContext,
								CardUtil.BUTTON_HISTORY_PRESSED_NAME)
								.getHeight(), null);
	}
	@Override
	public boolean isClicked(float x, float y) {
		if(this.x< x && x< this.x+CardUtil.getBitmap(mContext,
				CardUtil.BUTTON_HISTORY_NORMAL_NAME).getWidth())
		{
			if(this.y>y&&y>this.y-CardUtil.getBitmap(mContext,
					CardUtil.BUTTON_HISTORY_NORMAL_NAME).getHeight())
			{
				Log.i(TAG , "isClicked");
				return true;
			}
		}
		return false;
	}

}

