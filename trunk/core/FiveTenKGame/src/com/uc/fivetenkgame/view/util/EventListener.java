package com.uc.fivetenkgame.view.util;

import java.util.List;

import android.view.MotionEvent;

import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;
/**
 * 
 * @author chensl@ucweb.com
 *
 * ����5:23:29 2014-7-11
 */
public abstract class EventListener {
	/**
	 * @param event  ��Ҫ̎����¼�
	 * @param view  ��̎���View
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
	 * ���Ʋ���
	 * @param cardList		cardList��գ������Ą����飺�ŗ����ƣ�����գ��t����
	 */
	public  abstract void  handCard(List<Card> cardList);

}
