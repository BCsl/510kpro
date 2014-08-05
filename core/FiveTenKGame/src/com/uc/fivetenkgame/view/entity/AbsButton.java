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
 *         ����10:09:39 2014-8-1
 */
public abstract class AbsButton {
	private final String TAG = getClass().getSimpleName();
	protected Context mContext;
	protected float x;
	protected float y;
	protected boolean isClick;
	private Timer timer;
	protected final float HALF_OF_WIDTH;
	protected final float HALF_OF_HEIGHT;

	/**
	 * 
	 * @param con
	 * @param can
	 * @param x
	 * @param y
	 * @param halfWidth
	 *            ��Դ���һ��Ĵ�С
	 * @param halfHeight
	 *            ��Դ�߶�һ��Ĵ�С
	 */
	public AbsButton(Context con, float x, float y,float halfOfWidth,float halfOfHeight) {
		mContext = con;
		this.x = x;
		this.y = y;
		timer = new Timer();
		HALF_OF_WIDTH = halfOfWidth;
		HALF_OF_HEIGHT =halfOfHeight;
	}

	/**
	 * ����isClick��״̬����
	 */
	public  void doDraw(Canvas canvas){
		if(canvas == null)
			return ;
		if(!isClick){
			drawButtonOnNormalState( canvas, x - HALF_OF_WIDTH,y - HALF_OF_HEIGHT);
		}
		else{
			drawButtonOnPressedState( canvas,x - HALF_OF_WIDTH,y - HALF_OF_HEIGHT);
		}
			
	}

	/**
	 * �������в�����λ�ã���С����bitmap
	 * 
	 * @param bitmap
	 */
	public final void draw(Canvas canvas, Bitmap bitmap) {
		if (canvas == null)
			return;
		canvas.drawBitmap(bitmap, x - HALF_OF_WIDTH, y - HALF_OF_HEIGHT, null);
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
	 * �ж��Ƿ񱻵��
	 * 
	 * @param x
	 * @param y
	 * @return true������� false��û�б����
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
	 * �����״̬�µ�Button
	 */
	public abstract void drawButtonOnPressedState(Canvas canvas,float x,float y);

	/**
	 * ������״̬�µ�Button
	 */
	public abstract void drawButtonOnNormalState(Canvas canvas,float x,float y);

}
