package com.uc.fivetenkgame.view.util;

import java.util.List;

import android.view.MotionEvent;

import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;
/**
 * 
 * @author chensl@ucweb.com
 *
 * 下午5:23:29 2014-7-11
 */
public abstract class EventListener {
	/**
	 * @param event  需要處理的事件
	 * @param view  被處理的View
 	 */
	public void handleTouchEvent(MotionEvent event ,GameView view){
		float rawX=event.getRawX();
		float rawY=event.getRawY();
		int CARD_WIDTH=view.cardSizeHolder.width;
		int CARD_HEIGHT=view.cardSizeHolder.height;
		int SCREEN_WIDTH=view.screenHolder.width;
		int SCREEN_HEIGHT=view.screenHolder.height;
		
	
	}
	/**
	 * 打牌操作
	 * @param cardList		cardList為空，對應的動作為：放棄出牌；不為空，則出牌
	 */
	public  abstract void  handCard(List<Card> cardList);

}
