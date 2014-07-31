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
 *         上午10:11:28 2014-7-30
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
	 *            需要處理的事件
	 * @param view
	 *            被處理的View
	 */
	protected void handleTouchEvent(MotionEvent event, GameView view,
			List<Card> cardList) {
		// 只接受按下事件
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
		// 卡牌被点击
		if (card != null) {
			mApplication.playSound(SoundPoolCommon.SOUND_BUTTON_PRESS);
			Log.e(TAG, "被点击：" + card.getCardId());
			if (card.isClicked()) {
				card.setClick(false);
				mCardListToHand.remove(card);
			} else {
				card.setClick(true);
				mCardListToHand.add(card);

			}
			return;
		}
		// 出牌按钮被点击
		if (view.isMyTurn()
				&& ButtonHandCard.getInstance().isClicked(rawX, rawY)
				&& mCardListToHand.size() >= 0) {
			ButtonHandCard.getInstance().onClick();
			Log.e(TAG, "出牌：" + mCardListToHand.toString());
			if (mEventListener.handCard(mCardListToHand, false)) {
				// // 出牌成功
				view.getViewControler().setPlayersOutList(-1,
						new ArrayList<Card>(mCardListToHand));
				mCardListToHand.clear();
			}
			return;
		} else
			//放弃按钮
			if (view.isMyTurn()
				&& ButtonGiveUp.getInstance().isClicked(rawX, rawY)) {
			Log.e(TAG, "放弃出牌");
			for (Card temp : mCardListToHand) {
				temp.setClick(false);
			}
			mCardListToHand.clear();
			mEventListener.handCard(null, false);
			return;
		} else
			//历史按钮
			if (ButtonHistory.getInstance().isClicked(rawX, rawY)) {
				view.openHistoryDialog();
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
/*	private boolean buttonClick(int baseX, int screen_height, int card_width,
			float rawX, float rawY) {
		int button_buttom_y = screen_height - card_width * 3 + 10; // 按键在Y坐标上所能到达最大的Y值
		int button_top_y = button_buttom_y - card_width * 2 / 3; // 按键在Y坐标上所能到达最小的Y值
		Paint paint = new Paint();
		paint.setTextSize(card_width * 2 / 3);
		float baseLength = paint.measureText(mApplication.getResources()
				.getString(R.string.hand_cards));
		paint = null;
		// Log.e(TAG, "baseX:"+baseX+";baseLength:"+baseLength
		// +";button_buttom_y:"+button_buttom_y+";button_top_y"+button_top_y);
		if (rawY > button_top_y && rawY < button_buttom_y)
			if (rawX - baseX < baseLength && rawX - baseX > 0)
				return true;

		return false;
	}
	*/
	

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

	/**
	 * 检查是否超过出牌的有效时间
	 * 
	 * @param timeRemind
	 * @return true 超过出牌的有效时间 false 没有超出
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
