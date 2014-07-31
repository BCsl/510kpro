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
 *         ÏÂÎç3:58:39 2014-7-31
 */
public class ButtonHandCard implements IButton {
	private final String TAG="HandCardButton";
	private static ButtonHandCard mHandCardButton;
	private Context mContext;
	private Canvas mCanvas;
	private float x;
	private float y;

	private ButtonHandCard(Context con, Canvas can, float x, float y) {
		mContext = con;
		mCanvas = can;
		this.x = x;
		this.y = y;
	}
	public static IButton getInstance(){
		if (mHandCardButton == null)
				new IllegalArgumentException("init first!!!");
			return mHandCardButton;
	}
	public static IButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mHandCardButton == null)
			mHandCardButton = new ButtonHandCard(con, can, x, y);
		return mHandCardButton;
	}
	public void doDraw() {
		if(mCanvas!=null)
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext,
						CardUtil.BUTTON_HANDCARD_NORMAL_NAME),x,y- CardUtil.getBitmap(mContext,
								CardUtil.BUTTON_HANDCARD_NORMAL_NAME)
								.getHeight(), null);

	}
	
	public void onClick() {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if(mCanvas!=null)
					mCanvas.drawBitmap(
							CardUtil.getBitmap(mContext,
									CardUtil.BUTTON_HANDCARD_PRESSED_NAME),x,y- CardUtil.getBitmap(mContext,
											CardUtil.BUTTON_HANDCARD_PRESSED_NAME)
											.getHeight(), null);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				doDraw();
			}
		}.start();
	}
	public boolean isClicked(float x,float y){
		if(this.x< x && x< this.x+CardUtil.getBitmap(mContext,
				CardUtil.BUTTON_HANDCARD_NORMAL_NAME).getWidth())
		{
			if(this.y>y&&y>this.y-CardUtil.getBitmap(mContext,
					CardUtil.BUTTON_HANDCARD_NORMAL_NAME).getHeight())
			{
				Log.i(TAG, "isClicked");
				return true;
			}
		}
		
		return false;
		
		
	}

}
