package com.uc.fivetenkgame.view.util;

import java.util.ArrayList;
import java.util.List;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
/**
 * 
 * @author chensl@ucweb.com
 *
 * 下午5:09:36 2014-7-9
 */
public class ThreePersonGameDrawer implements IDrawer {
	private final String TAG="ThreePersonGameDrawer";
	private ScreenSizeHolder screenHolder;
	private CardSizeHolder cardSizeHolder;
	
	public ThreePersonGameDrawer(ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		this.screenHolder = screenHolder;
		this.cardSizeHolder = cardSizeHolder;
	}

	public void drawOutList(Canvas canvas) {

	}

	public void drawButton(Canvas canvas) {
		Paint paint =new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStyle(	Paint.Style.FILL);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(cardSizeHolder.width*2/3);
		canvas.drawText("出牌",screenHolder.width/2-2*cardSizeHolder.width,screenHolder.height-cardSizeHolder.height*3, paint);
		canvas.drawText("放弃",screenHolder.width/2+2*cardSizeHolder.width,screenHolder.height-cardSizeHolder.height*3, paint);

	}
	public void drawPlayers(Canvas canvas, int playerNO) {
		Log.i(TAG, "drawPlayers");
		Paint paint =new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStyle(	Paint.Style.FILL);
		paint.setTextSize(cardSizeHolder.width*2/3);
		float baseLength=paint.measureText("玩家2");
		canvas.drawText("玩家"+playerNO,10,screenHolder.height-10 , paint);
		if(playerNO == 1){
			canvas.drawText("玩家2",screenHolder.width-baseLength-10,cardSizeHolder.width*2/3, paint);
			canvas.drawText("玩家3",10,cardSizeHolder.width*2/3, paint);
			return ;
		}
		if(playerNO == 3){
			canvas.drawText("玩家1",screenHolder.width-baseLength-10,cardSizeHolder.width*2/3, paint);
			canvas.drawText("玩家2",10,cardSizeHolder.width*2/3, paint);
			return ;
		}
		canvas.drawText("玩家3",screenHolder.width-baseLength-10,cardSizeHolder.width*2/3, paint);
		canvas.drawText("玩家1",10,cardSizeHolder.width*2/3, paint);
		
		
	}

	public void drawPlayersScore(Canvas canvas, 
			List<Integer> scores,int playerNO) {
		String text="分数:";
		Paint paint =new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(	Paint.Style.FILL_AND_STROKE);
		paint.setTextSize(cardSizeHolder.width*2/3);
		float baseLength=paint.measureText("分數：xxx");
		canvas.drawText(text+scores.get(playerNO-1),screenHolder.width-4/3*baseLength,screenHolder.height-10 , paint);
		if(playerNO == 1){
			canvas.drawText(text+scores.get(1),screenHolder.width-4/3*baseLength,4/3*cardSizeHolder.width, paint);
			canvas.drawText(text+scores.get(2),10,4/3*cardSizeHolder.width, paint);
			return ;
		}
		if(playerNO == 3){
			canvas.drawText(text+scores.get(0),screenHolder.width-4/3*baseLength,4/3*cardSizeHolder.width, paint);
			canvas.drawText(text+scores.get(1),10,4/3*cardSizeHolder.width, paint);
			return ;
		}
		canvas.drawText(text+scores.get(2),screenHolder.width-4/3*baseLength,4/3*cardSizeHolder.width, paint);
		canvas.drawText(text+scores.get(0),10,4/3*cardSizeHolder.width, paint);
		
	}
	public void drawGameScore(Canvas canvas,int score) {
		Paint paint =new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(	Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(cardSizeHolder.width*3/4);
		String str="本轮分数："+score;
		canvas.drawText(str,screenHolder.width/2-paint.measureText(str)/2,cardSizeHolder.width*3/4 , paint);
	}
	
	public void drawCardNumber(Canvas canvas, 
			List<Integer> cardNumber, int playerNO,Context con) {
		String text="张数:";
		Paint paint =new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStyle(	Paint.Style.FILL);
		paint.setTextSize(cardSizeHolder.width*2/3);
		float baseLength=paint.measureText("玩家x");
		canvas.drawText(text+cardNumber.get(playerNO-1),20+baseLength,screenHolder.height-10 , paint);
		if(playerNO == 1){
			canvas.drawText(text+cardNumber.get(1),screenHolder.width-baseLength-paint.measureText(text+cardNumber.get(1))-20,cardSizeHolder.width*2/3 , paint);
			canvas.drawText(text+cardNumber.get(2),20+baseLength,cardSizeHolder.width*2/3 , paint);
			drawCardsOnRight(cardNumber.get(1), canvas, con);
			drawCardsOnLeft(cardNumber.get(2), canvas, con);
			return ;
		}
		if(playerNO == 3){
			canvas.drawText(text+cardNumber.get(0),screenHolder.width-baseLength-paint.measureText(text+cardNumber.get(1))-20,cardSizeHolder.width*2/3 , paint);
			canvas.drawText(text+cardNumber.get(1),20+baseLength,cardSizeHolder.width*2/3 , paint);
			drawCardsOnRight(cardNumber.get(0), canvas, con);
			drawCardsOnLeft(cardNumber.get(1), canvas, con);
			return ;
		}
		canvas.drawText(text+cardNumber.get(2),screenHolder.width-baseLength-paint.measureText(text+cardNumber.get(1))-15,10, paint);
		canvas.drawText(text+cardNumber.get(1),15+baseLength,cardSizeHolder.width*2/3 , paint);
		drawCardsOnRight(cardNumber.get(2), canvas, con);
		drawCardsOnLeft(cardNumber.get(1), canvas, con);
	}

	public void drawCards(Canvas canvas,
			List<Card> cardList,Context con) {
		int size= cardList.size();
		int height=screenHolder.height-cardSizeHolder.height*2;
		int width=screenHolder.width/2-size/2 * cardSizeHolder.width*3/4;
		Log.i(TAG, "drawCards,size=:"+size+";width="+width +";height="+height);
		for(int i=0;i<size;i++)
		{	
		Card card =cardList.get(i);	
		card.setLocation(width+i*cardSizeHolder.width*3/4,height );
		ApplicationInfo appInfo = con.getApplicationInfo();
		int id = con.getResources().getIdentifier(CardGenerator.cardResourceName(card.getCardId()), "drawable",appInfo.packageName);
		canvas.drawBitmap(BitmapFactory.decodeResource(con.getResources(), id),card.getSRC(), card.getDST(), null);
		}
	}
/**
 * 根據左邊玩家剩餘牌的數量去畫左邊的牌
 * @param num
 * @param canvas
 * @param con
 */
	public void drawCardsOnLeft(int num,Canvas canvas,Context con){
		int width= cardSizeHolder.width;
		int heightBase=(screenHolder.height-4*cardSizeHolder.height)/2-num/2*cardSizeHolder.width*2/3;
		Log.i(TAG, "num"+num+"width"+width+";height"+heightBase);
			for(int i=0;i<num;i++){
				Card card=new Card(cardSizeHolder.width, cardSizeHolder.height, "0");
				card.setLocation(width, i*cardSizeHolder.width*2/3+heightBase);
				ApplicationInfo appInfo = con.getApplicationInfo();
				int id = con.getResources().getIdentifier(CardGenerator.cardResourceName(card.getCardId()), "drawable",appInfo.packageName);
				canvas.drawBitmap(BitmapFactory.decodeResource(con.getResources(), id),card.getSRC(), card.getDST(), null);
			}
		
	}
	/**
	 * 根據右邊玩家剩餘牌的數量去畫右邊的牌
	 * @param num	
	 * @param canvas
	 * @param con
	 */
	public void drawCardsOnRight(int num,Canvas canvas,Context con){
		int width= screenHolder.width-2*cardSizeHolder.width;
		int heightBase=(screenHolder.height-4*cardSizeHolder.height)/2-num/2*cardSizeHolder.width*2/3;
		Log.i(TAG, "num"+num+"width"+width+";height"+heightBase);
			for(int i=0;i<num;i++){
				Card card=new Card(cardSizeHolder.width, cardSizeHolder.height, "0");
				card.setLocation(width, i*cardSizeHolder.width*2/3+heightBase);
				ApplicationInfo appInfo = con.getApplicationInfo();
				int id = con.getResources().getIdentifier(CardGenerator.cardResourceName(card.getCardId()), "drawable",appInfo.packageName);
				canvas.drawBitmap(BitmapFactory.decodeResource(con.getResources(), id),card.getSRC(), card.getDST(), null);
			}
		
	}



}
