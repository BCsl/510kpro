/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.SoundPoolCommon;
import com.uc.fivetenkgame.view.entity.ButtonGiveUp;
import com.uc.fivetenkgame.view.entity.ButtonHandCard;
import com.uc.fivetenkgame.view.entity.ButtonHistory;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         ����10:11:28 2014-7-30
 */
public final class EventHandler {

	private final String TAG = "EventListener";
	private List<Card> mCardListToHand;
	private EventListener mEventListener;
	private GameApplication mApplication;

	public EventHandler(EventListener eventListener, Context context) {
		mCardListToHand = new Vector<Card>();
		mApplication = (GameApplication) context.getApplicationContext();
		this.mEventListener = eventListener;
	}

	/**
	 * @param event
	 *            ��Ҫ̎����¼�
	 * @param view
	 *            ��̎���View
	 */
	protected void handleTouchEvent(MotionEvent event, GameView view,
			List<Card> cardList) {
		// ֻ���ܰ����¼�
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return;
		float rawX = event.getRawX();
		float rawY = event.getRawY();
		int CARD_WIDTH = view.getCardSizeHolder().width;
		int CARD_HEIGHT = view.getCardSizeHolder().height;
		int SCREEN_HEIGHT = view.getScreenHolder().height;
		int CARD_INTENT = CARD_HEIGHT / 2;
		Card card = getCard(SCREEN_HEIGHT, CARD_WIDTH, CARD_HEIGHT,
				CARD_INTENT, rawX, rawY, cardList);
		// ���Ʊ����
		if (card != null) {
			mApplication.playSound(SoundPoolCommon.SOUND_BUTTON_PRESS);
			Log.e(TAG, "�������" + card.getCardId());
			if (card.isClicked()) {
				card.setClick(false);
				mCardListToHand.remove(card);
			} else {
				card.setClick(true);
				mCardListToHand.add(card);

			}
			return;
		}
		// ���ư�ť�����
		if (view.isMyTurn()
				&& ButtonHandCard.getInstance().isClicked(rawX, rawY)
				&& mCardListToHand.size() >= 0) {
			Log.e(TAG, "���ƣ�" + mCardListToHand.toString());
			if (mEventListener.handCard(mCardListToHand, false)) {
				// // ���Ƴɹ�
				view.getViewControler().setPlayersOutList(-1,
						new ArrayList<Card>(mCardListToHand));
				mCardListToHand.clear();
			}
			return;
		} else
			//������ť
			if (view.isMyTurn()
				&& ButtonGiveUp.getInstance().isClicked(rawX, rawY)) {
			Log.e(TAG, "��������");
			for (Card temp : mCardListToHand) {
				temp.setClick(false);
			}
			mCardListToHand.clear();
			mEventListener.handCard(null, false);
			return;
		} else
			//��ʷ��ť
			if (ButtonHistory.getInstance().isClicked(rawX, rawY)) {
				view.openHistoryDialog();
		}

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

	/**
	 * ����Ƿ񳬹����Ƶ���Чʱ��
	 * 
	 * @param timeRemind
	 * @return true �������Ƶ���Чʱ�� false û�г���
	 */
	protected boolean checkForTimeOut(int timeRemind) {
		if (timeRemind < 4)
			switch (timeRemind) {
			case 3:
			case 2:
			case 1:
				mApplication.playSound(SoundPoolCommon.SOUND_SECOND_CALL);
				break;
			case 0:
				mEventListener.handCard(null, true);
				return true;
			}
		return false;

	}

}
