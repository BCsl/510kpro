package com.uc.fivetenkgame.view.entity;

import android.content.Context;
import android.graphics.Canvas;

import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.view.util.CardUtil;

public class ButtonGiveUp extends AbsButton {
	private static AbsButton mGiveUpButton;

	public ButtonGiveUp(Context con, Canvas can, float x, float y) {
		super(con, x, y);
		HALF_OF_HEIGHT = CardUtil.getBitmap(mContext,
				ResourseCommon.BUTTON_GIVEUP_NORMAL).getHeight() / 2;
		HALF_OF_WIDTH = CardUtil.getBitmap(mContext,
				ResourseCommon.BUTTON_GIVEUP_NORMAL).getWidth() / 2;
	}

	public static AbsButton getInstance() {
		if (mGiveUpButton == null)
			throw new IllegalArgumentException("init first!!!");
		return mGiveUpButton;
	}

	public static AbsButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mGiveUpButton == null)
			mGiveUpButton = new ButtonGiveUp(con, can, x, y);
		return mGiveUpButton;
	}

	@Override
	protected void drawButtonOnPressedState(Canvas canvas) {
		draw(canvas,CardUtil.getBitmap(mContext, ResourseCommon.BUTTON_GIVEUP_PRESSED));

	}

	@Override
	protected void drawButtonOnNormalState(Canvas canvas) {
		draw(canvas,CardUtil.getBitmap(mContext, ResourseCommon.BUTTON_GIVEUP_NORMAL));

	}

}
