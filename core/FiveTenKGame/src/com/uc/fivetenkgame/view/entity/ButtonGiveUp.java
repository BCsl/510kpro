package com.uc.fivetenkgame.view.entity;

import com.uc.fivetenkgame.view.util.CardUtil;

import android.content.Context;
import android.graphics.Canvas;

public class ButtonGiveUp extends AbsButton {
	private static AbsButton mGiveUpButton;

	public ButtonGiveUp(Context con, Canvas can, float x, float y) {
		super(con, can, x, y);
		HALF_OF_HEIGHT = CardUtil.getBitmap(mContext,
				CardUtil.BUTTON_GIVEUP_NORMAL_NAME).getHeight() / 2;
		HALF_OF_WIDTH = CardUtil.getBitmap(mContext,
				CardUtil.BUTTON_GIVEUP_NORMAL_NAME).getWidth() / 2;
	}

	public static AbsButton getInstance() {
		if (mGiveUpButton == null)
			new IllegalArgumentException("init first!!!");
		return mGiveUpButton;
	}

	public static AbsButton getInstance(Context con, Canvas can, float x,
			float y) {
		if (mGiveUpButton == null)
			mGiveUpButton = new ButtonGiveUp(con, can, x, y);
		return mGiveUpButton;
	}

	@Override
	protected void drawButtonOnPressedState() {
		draw(CardUtil.getBitmap(mContext, CardUtil.BUTTON_GIVEUP_PRESSED_NAME));

	}

	@Override
	protected void drawButtonOnNormalState() {
		draw(CardUtil.getBitmap(mContext, CardUtil.BUTTON_GIVEUP_NORMAL_NAME));

	}

}
