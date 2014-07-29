/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.util;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.Toast;
import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * @author chensl@ucweb.com
 * 
 *         下午5:03:12 2014-7-16
 */
public class MainPlayerInfoDrawer extends AbsDrawer {

	private String TAG = "MainPlayerInfoDrawer";
	private boolean isFirst=true;

	public MainPlayerInfoDrawer(Context context, ScreenSizeHolder screenHolder,
			CardSizeHolder cardSizeHolder) {
		super(context, screenHolder, cardSizeHolder);
	}

	/**
	 * 以baseY为基准，水平画玩家手牌
	 * 
	 * @param cardList
	 * @param baseY
	 * @param cardIntent
	 */
	public void drawCards(List<Card> cardList, float baseY, float cardIntent) {
		if (cardList == null || cardList.size() == 0)
			return;
		float space = (screenHolder.width - 10) / (cardList.size() * TEXT_SIZE) > 1 ? TEXT_SIZE
				: (screenHolder.width - 10) / (cardList.size() * TEXT_SIZE)
						* TEXT_SIZE;
		float baseX = (float) screenHolder.width / 2 - cardList.size() / 2
				* space;
		Card card = null;
		Bitmap temp = null;
		for (int i = 0; i < cardList.size(); i++) {
			card = cardList.get(i);
			card.setSize(cardSizeHolder.width, cardSizeHolder.height);
			if (!card.isClicked())
				card.setLocation((int) (baseX + i * space), (int) baseY);
			else
				card.setLocation((int) (baseX + i * space),
						(int) (baseY - cardIntent));
			temp = CardGenerator.getBitmap(context,
					CardGenerator.cardResourceName(card.getCardId()));
			canvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
		card = null;
	}

	/**
	 * 以baseY为基准，水平方向上画按钮
	 * 
	 * @param paint
	 * @param baseY
	 */
	public void drawButton(Paint paint, float baseY) {
		paint.setColor(Color.rgb(152, 251, 152));
		paint.setTextSize(TEXT_SIZE);
		float baseLength = paint.measureText("出牌");
		float leftButtonXEagle = screenHolder.width / 2 - 2
				* cardSizeHolder.width;
		float rightButtonXEagle = screenHolder.width / 2 + 2
				* cardSizeHolder.width;
		RectF rectLeft = new RectF(leftButtonXEagle, baseY - TEXT_SIZE,
				leftButtonXEagle + baseLength, baseY + 10);
		RectF rectRight = new RectF(rightButtonXEagle, baseY - TEXT_SIZE,
				rightButtonXEagle + baseLength, baseY + 10);
		canvas.drawRoundRect(rectLeft, 20f, 20f, paint);
		canvas.drawRoundRect(rectRight, 20f, 20f, paint);
		paint.setColor(Color.rgb(255, 48, 48));
		canvas.drawText("出牌", leftButtonXEagle, baseY, paint);
		canvas.drawText("放弃", rightButtonXEagle, baseY, paint);
	}

	/**
	 * 以baseY为基准，水平方向上画已出牌
	 * 
	 * @param outList
	 * @param baseY
	 */
	public void drawOutList(List<Card> outList, float baseY) {
		if (outList == null || outList.size() == 0)
			return;

		float space = (float) (screenHolder.width - 10)
				/ (outList.size() * TEXT_SIZE_SMALL) > 1 ? TEXT_SIZE_SMALL
				: (screenHolder.width - 10)
						/ (outList.size() * TEXT_SIZE_SMALL) * TEXT_SIZE_SMALL;
			if(space<(float)cardSizeHolder.width/3 &&isFirst)	{
				isFirst=false;
				Toast.makeText(context,"为了能有更好的体验，请用分辨率跟高的手机进行游戏！",Toast.LENGTH_LONG).show();
			}
			
		float baseX = (float) screenHolder.width / 2 - outList.size() / 2
				* space;
		Card card = null;
		Bitmap temp = null;
		for (int i = 0; i < outList.size(); i++) {
			card = outList.get(i);
			card.setSize(cardSizeHolder.width, cardSizeHolder.height);
			card.setLocation((int) (baseX + i * space), (int) baseY);
			temp = CardGenerator.getBitmap(context,
					CardGenerator.cardResourceName(card.getCardId()));
			canvas.drawBitmap(temp, card.getSRC(), card.getDST(), null);
		}
	}

}
