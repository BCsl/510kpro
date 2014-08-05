package com.uc.fivetenkgame.view.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.uc.fivetenkgame.common.ResourseCommon;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * �����Ƶ����ת��Ϊ��Ӧ����Դ��
 * 
 * @author chensl@ucweb.com
 * 
 *         ����5:09:27 2014-7-9
 */
public class CardUtil {
	private  static String TAG="CardUtil";
	private static Map<String, Bitmap> CARDS_BITMAP = new HashMap<String, Bitmap>(75);//54+2+6+1+2
	/**
	 * 
	 * @param Id  ��Ƭid
	 * @return
	 */
	public static String getResourceName(String Id) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(Id.trim());
		if (cardNO == 0)
			return ResourseCommon.BACKGROUND;
		if (cardNO == 53)
			return ResourseCommon.LITTLE_JOKER;
		if (cardNO == 54)
			return ResourseCommon.BIG_JOKER;
		if (cardNO == 55)
			return ResourseCommon.PASS;
		switch ((cardNO - 1) / 13) {
		case 0:
			prefix.append(ResourseCommon.DIAMOND_PREFIX);
			break;
		case 1:
			prefix.append(ResourseCommon.CLUBS_PREFIX);
			break;
		case 2:
			prefix.append(ResourseCommon.HEARTS_PREFIX);
			break;
		case 3:
			prefix.append(ResourseCommon.SPADE_PREFIX);
			break;
		default:
		throw new IllegalArgumentException(cardNO + "is not defined!");
		}
		cardNO %= 13;
		if (cardNO < 3)
			prefix.append(String.valueOf(cardNO + 13));
		else
			prefix.append(String.valueOf(cardNO));
		return prefix.toString().trim();
	}
	/**
	 * 
	 * @param con
	 * @param resourceName		��Դ����
	 * @return
	 */
	public static Bitmap getBitmap(Context con, String resourceName) {
		if (CARDS_BITMAP.get(resourceName) == null) {
//			Log.i(TAG, resourceName +"�����ڣ�");
			CARDS_BITMAP.put(resourceName, BitmapFactory.decodeResource(
					con.getResources(),
					con.getResources().getIdentifier(resourceName, "drawable",
							con.getApplicationInfo().packageName)));
		}
		return CARDS_BITMAP.get(resourceName);
	}
	/**
	 * ����X(X>1)�������ϵ�������ͬ���ƾ���X����ͬ��ID������ÿ��ֻ��Ҫremoveһ�Ρ�
	 * @param cards				ԭ����CARDS
	 * @param cardToRemove		��Ҫ�������CARDS
	 */
	public static void  removeCards(List<Card> cards,List<Card> cardToRemove){
		int index;	
		for(Card temp:cardToRemove){
				 if((index=cards.indexOf(temp))!=-1){
					 Log.i(TAG, "remove index:" + index);
					 	cards.remove(index);
				 }
			}
		
	}

}
