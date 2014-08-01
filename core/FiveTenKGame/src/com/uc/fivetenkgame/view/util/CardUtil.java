package com.uc.fivetenkgame.view.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uc.fivetenkgame.view.entity.Card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * �����Ƶ����ת��Ϊ��Ӧ����Դ��
 * 
 * @author chensl@ucweb.com
 * 
 *         ����5:09:27 2014-7-9
 */
public class CardUtil {
	private final static String TAG="CardUtil";
	private static Map<String, Bitmap> CARDS_BITMAP = new HashMap<String, Bitmap>();
	
	
	public static String BUTTON_HANDCARD_NORMAL_NAME="button_handcard_normal";
	public static String BUTTON_HANDCARD_PRESSED_NAME="button_handcard_pressed";
	public static String BUTTON_GIVEUP_NORMAL_NAME="button_giveup_normal";
	public static String BUTTON_GIVEUP_PRESSED_NAME="button_giveup_pressed";
	public static String BUTTON_HISTORY_NORMAL_NAME="button_history_normal";
	public static String BUTTON_HISTORY_PRESSED_NAME="button_history_pressed";
	/**
	 * 
	 * @param Id  ��Ƭid
	 * @return
	 */
	public static String ResourceName(String Id) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(Id.trim());
		if (cardNO == 0)
			return "cardbg1";
		if (cardNO == 53)
			return "a5_16";
		if (cardNO == 54)
			return "a5_17";
		if (cardNO == 55)
			return "pass";
		switch ((cardNO - 1) / 13) {
		case 0:
			prefix.append("a2_");
			break;
		case 1:
			prefix.append("a1_");
			break;
		case 2:
			prefix.append("a3_");
			break;
		case 3:
			prefix.append("a4_");
			break;
		default:
			new IllegalArgumentException(cardNO + "is not defined!");
			break;
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
