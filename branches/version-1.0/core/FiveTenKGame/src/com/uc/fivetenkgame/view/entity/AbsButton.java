/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.entity;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * @author chensl@ucweb.com
 * 
 *         上午10:09:39 2014-8-1
 */
public abstract class AbsButton {
	private final String TAG = getClass().getSimpleName();
	protected Context mContext;
	protected Canvas mCanvas;
	protected float x;
	protected float y;
	protected boolean isClick;
	private Timer timer;
	protected float HALF_OF_WIDTH;
	protected float HALF_OF_HEIGHT;

	protected AbsButton(Context con, Canvas can, float x, float y) {
		mContext = con;
		mCanvas = can;
		this.x = x;
		this.y = y;
		timer = new Timer();
		HALF_OF_WIDTH=0;
		HALF_OF_HEIGHT=0;
	}
	/**
	 * 根据isClick的状态来画
	 */
	public  void doDraw(){
		if(mCanvas == null)
			return ;
		if(!isClick){
			drawButtonOnNormalState();
		}
		else{
			drawButtonOnPressedState();
		}
			
	}

	/**
	 * 根据已有参数（位置，大小）画bitmap
	 * @param bitmap
	 */
	protected final void draw(Bitmap bitmap) {
		if(mCanvas==null)
			return ;
			mCanvas.drawBitmap(bitmap, x - HALF_OF_WIDTH, y - HALF_OF_HEIGHT,
					null);
	}

	private final void onClick() {
		if (isClick == false) {
			isClick = true;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isClick = false;
				}
			}, 300);
		}
	}

	/**
	 * 判断是否被点击
	 * 
	 * @param x
	 * @param y
	 * @return true：被点击 false：没有被点击
	 */
	public boolean isClicked(float x, float y) {
		if (this.x - HALF_OF_WIDTH < x && x < this.x + HALF_OF_WIDTH) {
			if (this.y + HALF_OF_HEIGHT > y && y > this.y - HALF_OF_HEIGHT) {
				onClick();
				Log.i(TAG, "isClicked");
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 画点击状态下的Button
	 */
	protected abstract void drawButtonOnPressedState() ;
	/**
	 * 画正常状态下的Button
	 */
	protected abstract void drawButtonOnNormalState() ;

}
