package com.uc.fivetenkgame.view.entity;

import com.uc.fivetenkgame.view.util.CardUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

public class ButtonGiveUp implements IButton{
	private String TAG=" ButtonGiveUp";
	private static ButtonGiveUp mGiveUpButton;
	private Context mContext;
	private Canvas mCanvas;
	private float x;
	private float y;

	private ButtonGiveUp(Context con, Canvas can, float x, float y) {
		mContext = con;
		mCanvas = can;
		this.x = x;
		this.y = y;
	}
	public static IButton getInstance(){
		if (mGiveUpButton == null)
				new IllegalArgumentException("init first!!!");
			return mGiveUpButton;
	}
	public static IButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mGiveUpButton == null)
			mGiveUpButton = new ButtonGiveUp(con, can, x, y);
		return mGiveUpButton;
	}
	@Override
	public void doDraw() {
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext,
						CardUtil.BUTTON_GIVEUP_NORMAL_NAME),x,y- CardUtil.getBitmap(mContext,
								CardUtil.BUTTON_GIVEUP_NORMAL_NAME)
								.getHeight(), null);
	}
	@Override
	public void onClick() {
		mCanvas.drawBitmap(
				CardUtil.getBitmap(mContext,
						CardUtil.BUTTON_GIVEUP_PRESSED_NAME),x,y- CardUtil.getBitmap(mContext,
								CardUtil.BUTTON_GIVEUP_PRESSED_NAME)
								.getHeight(), null);

	}
	@Override
	public boolean isClicked(float x, float y) {
		if(this.x< x && x< this.x+CardUtil.getBitmap(mContext,
				CardUtil.BUTTON_GIVEUP_NORMAL_NAME).getWidth())
		{
			if(this.y>y&&y>this.y-CardUtil.getBitmap(mContext,
					CardUtil.BUTTON_GIVEUP_NORMAL_NAME).getHeight())
			{
				Log.i(TAG, "isClicked");
				return true;
			}
		}
		
		return false;
	}

}
