package com.uc.fivetenkgame.view.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         下午5:23:29 2014-7-11
 */
public abstract class EventListener {
	private String TAG = "EventListener";
	private List<Card> handList;

	public EventListener() {
		handList = new Vector<Card>();
	}

	/**
	 * @param event
	 *            需要處理的事件
	 * @param view
	 *            被處理的View
	 */
	public final  void handleTouchEvent(MotionEvent event, GameView view,
			List<Card> cardList) {
		//只接受按下事件
//		if(event.getAction()!=MotionEvent.ACTION_UP)
//			return ;
		float rawX = event.getRawX();
		float rawY = event.getRawY();
		// Log.e(TAG, "x" + rawX + ";y" + rawY);
		int CARD_WIDTH = view.cardSizeHolder.width;
		int CARD_HEIGHT = view.cardSizeHolder.height;
		int SCREEN_WIDTH = view.screenHolder.width;
		int SCREEN_HEIGHT = view.screenHolder.height;
		int CARD_INTENT = CARD_HEIGHT / 2;
		Card card = getCard(SCREEN_HEIGHT, CARD_WIDTH, CARD_HEIGHT,
				CARD_INTENT, rawX, rawY, cardList);
		//卡牌被点击
		if (card != null) {
		Log.e(TAG, "被点击：" + card.getCardId());
			if (card.isClicked()) {
				card.setClick(false);
				handList.remove(card);
			} 
			else {
				card.setClick(true);
				handList.add(card);
			}
			return ;
		}
		Log.e(TAG, "test");
		int leftButtonBaseX = SCREEN_WIDTH / 2 - 2 * CARD_WIDTH;
		int rightButtonBaseX = SCREEN_WIDTH / 2 + 2 * CARD_WIDTH;
		//出牌按钮被点击
		if (view.isMyTurn()
				&& buttonClick(leftButtonBaseX, SCREEN_HEIGHT, CARD_WIDTH,
						rawX, rawY) && handList.size()>=0) {
			Log.e(TAG, "出牌：" + handList.toString());
			if(handCard(handList)){
				//出牌成功
				view.getViewControler().setPlayersOutList(-1, new ArrayList<Card>(handList));
				cardList.removeAll(handList);
//				view.setMyTurn(false);
				handList.clear();
			}
			else
				Toast.makeText(view.getContext(), "请按规则出牌喔",Toast.LENGTH_SHORT ).show();
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
			handCard(null);
//			view.setMyTurn(false);
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

	/**	出牌操作(需要进行规则的判断)
	 * 
	 * @param handList	准备出的牌    handList为空，则为放弃操作。
	 * 
	 * @return	出牌成功返回false
	 */
	public abstract boolean handCard(List<Card> handList);

}
