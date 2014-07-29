package com.uc.fivetenkgame.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.view.entity.Card;

public final class EventHandler {

	private String TAG = "EventListener";
	private List<Card> handList;
	private EventListener eventListener;

	public EventHandler(EventListener eventListener) {
		handList = new Vector<Card>();
		this.eventListener = eventListener;
	}

	/**
	 * @param event
	 *            需要處理的事件
	 * @param view
	 *            被處理的View
	 */
	protected final void handleTouchEvent(MotionEvent event, GameView view,
			List<Card> cardList) {
		// 只接受按下事件
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return;
		float rawX = event.getRawX();
		float rawY = event.getRawY();
		int CARD_WIDTH = view.cardSizeHolder.width;
		int CARD_HEIGHT = view.cardSizeHolder.height;
		int SCREEN_WIDTH = view.screenHolder.width;
		int SCREEN_HEIGHT = view.screenHolder.height;
		int CARD_INTENT = CARD_HEIGHT / 2;
		Card card = getCard(SCREEN_HEIGHT, CARD_WIDTH, CARD_HEIGHT,
				CARD_INTENT, rawX, rawY, cardList);
		// 卡牌被点击
		if (card != null) {
			((GameApplication) view.context.getApplicationContext())
					.playSound(Common.SOUND_BUTTON_PRESS);
			Log.e(TAG, "被点击：" + card.getCardId());
			if (card.isClicked()) {
				card.setClick(false);
				handList.remove(card);
			} else {
				card.setClick(true);
				handList.add(card);

			}
			return;
		}
		int leftButtonBaseX = SCREEN_WIDTH / 2 - 2 * CARD_WIDTH;
		int rightButtonBaseX = SCREEN_WIDTH / 2 + 2 * CARD_WIDTH;
		// 出牌按钮被点击
		if (view.isMyTurn()
				&& buttonClick(leftButtonBaseX, SCREEN_HEIGHT, CARD_WIDTH,
						rawX, rawY) && handList.size() >= 0) {
			Log.e(TAG, "出牌：" + handList.toString());
			if (eventListener.handCard(handList, false)) {
				// // 出牌成功
				view.getViewControler().setPlayersOutList(-1,
						new ArrayList<Card>(handList));
				((GameApplication) view.context.getApplicationContext())
						.playSound(Common.SOUND_OUTPUT_CARDS);
				// cardList.removeAll(handList);
				handList.clear();
			}
			return;
		}
		if (view.isMyTurn()
				&& buttonClick(rightButtonBaseX, SCREEN_HEIGHT, CARD_WIDTH,
						rawX, rawY)) {
			Log.e(TAG, "放弃出牌");

			for (Card temp : handList) {
				temp.setClick(false);
			}
			handList.clear();
			eventListener.handCard(null, false);
			return;
		}

	}

	/**
	 * 
	 * @param baseX
	 *            对应按钮左边缘的x坐标
	 * @param screen_height
	 * @param card_width
	 * @param rawX
	 * @param rawY
	 * @return 按钮是否被点击
	 */
	private boolean buttonClick(int baseX, int screen_height, int card_width,
			float rawX, float rawY) {
		int button_buttom_y = screen_height - card_width * 3 + 10; // 按键在Y坐标上所能到达最大的Y值
		int button_top_y = button_buttom_y - card_width * 2 / 3; // 按键在Y坐标上所能到达最小的Y值
		Paint paint = new Paint();
		paint.setTextSize(card_width * 2 / 3);
		float baseLength = paint.measureText("出牌");
		paint = null;
		// Log.e(TAG, "baseX:"+baseX+";baseLength:"+baseLength
		// +";button_buttom_y:"+button_buttom_y+";button_top_y"+button_top_y);
		if (rawY > button_top_y && rawY < button_buttom_y)
			if (rawX - baseX < baseLength && rawX - baseX > 0)
				return true;

		return false;
	}

	/**
	 * 根据坐标，获取被点击的卡牌
	 * 
	 * @param screen_height
	 * @param card_width
	 * @param card_height
	 * @param card_intent
	 *            卡牌点击后，Y方向上需要减小的量
	 * @param rawX
	 * @param rawY
	 * @param cardList
	 * @return 被点击的卡牌
	 */
	private Card getCard(int screen_height, int card_width, int card_height,
			int card_intent, float rawX, float rawY, List<Card> cardList) {
		float cards_buttom_y = (float) screen_height - card_width * 2 / 3; // 卡牌在Y坐标上能到达的最大的Y值
		float cards_top_y = (float) cards_buttom_y - card_height - card_intent; // 卡牌在Y坐标上能到达的最小的Y值
		float X_OFFEST_SMALL = (float) card_width * 3 / 4; // 卡牌在X方向上的偏移量
		if (rawY < cards_top_y || rawY > cards_buttom_y)
			return null;
		for (Card temp : cardList) {
			// 查询卡牌的符合范围
			if ((0 < rawX - temp.getX()) && (0 < rawY - temp.getY())
					&& (rawX - temp.getX() < X_OFFEST_SMALL)
					&& (rawY - temp.getY() < card_height)) {
				return temp;
			}
		}
		return null;
	}

	protected boolean checkForTimeOut(Context context, int timeRemind) {
		if (timeRemind < 4)
			switch (timeRemind) {
			case 3:
			case 2:
			case 1:
				((GameApplication) context.getApplicationContext())
						.playSound(Common.SOUND_SECOND_CALL);
				break;
			case 0:
				eventListener.handCard(null, true);
				return true;
			}
		return false;

	}

}
