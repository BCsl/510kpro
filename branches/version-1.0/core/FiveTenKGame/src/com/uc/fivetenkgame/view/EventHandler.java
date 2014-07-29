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
	 *            ��Ҫ̎����¼�
	 * @param view
	 *            ��̎���View
	 */
	protected final void handleTouchEvent(MotionEvent event, GameView view,
			List<Card> cardList) {
		// ֻ���ܰ����¼�
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
		// ���Ʊ����
		if (card != null) {
			((GameApplication) view.context.getApplicationContext())
					.playSound(Common.SOUND_BUTTON_PRESS);
			Log.e(TAG, "�������" + card.getCardId());
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
		// ���ư�ť�����
		if (view.isMyTurn()
				&& buttonClick(leftButtonBaseX, SCREEN_HEIGHT, CARD_WIDTH,
						rawX, rawY) && handList.size() >= 0) {
			Log.e(TAG, "���ƣ�" + handList.toString());
			if (eventListener.handCard(handList, false)) {
				// // ���Ƴɹ�
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
			Log.e(TAG, "��������");

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
	 *            ��Ӧ��ť���Ե��x����
	 * @param screen_height
	 * @param card_width
	 * @param rawX
	 * @param rawY
	 * @return ��ť�Ƿ񱻵��
	 */
	private boolean buttonClick(int baseX, int screen_height, int card_width,
			float rawX, float rawY) {
		int button_buttom_y = screen_height - card_width * 3 + 10; // ������Y���������ܵ�������Yֵ
		int button_top_y = button_buttom_y - card_width * 2 / 3; // ������Y���������ܵ�����С��Yֵ
		Paint paint = new Paint();
		paint.setTextSize(card_width * 2 / 3);
		float baseLength = paint.measureText("����");
		paint = null;
		// Log.e(TAG, "baseX:"+baseX+";baseLength:"+baseLength
		// +";button_buttom_y:"+button_buttom_y+";button_top_y"+button_top_y);
		if (rawY > button_top_y && rawY < button_buttom_y)
			if (rawX - baseX < baseLength && rawX - baseX > 0)
				return true;

		return false;
	}

	/**
	 * �������꣬��ȡ������Ŀ���
	 * 
	 * @param screen_height
	 * @param card_width
	 * @param card_height
	 * @param card_intent
	 *            ���Ƶ����Y��������Ҫ��С����
	 * @param rawX
	 * @param rawY
	 * @param cardList
	 * @return ������Ŀ���
	 */
	private Card getCard(int screen_height, int card_width, int card_height,
			int card_intent, float rawX, float rawY, List<Card> cardList) {
		float cards_buttom_y = (float) screen_height - card_width * 2 / 3; // ������Y�������ܵ��������Yֵ
		float cards_top_y = (float) cards_buttom_y - card_height - card_intent; // ������Y�������ܵ������С��Yֵ
		float X_OFFEST_SMALL = (float) card_width * 3 / 4; // ������X�����ϵ�ƫ����
		if (rawY < cards_top_y || rawY > cards_buttom_y)
			return null;
		for (Card temp : cardList) {
			// ��ѯ���Ƶķ��Ϸ�Χ
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
