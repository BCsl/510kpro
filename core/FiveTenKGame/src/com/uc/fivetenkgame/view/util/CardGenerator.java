package com.uc.fivetenkgame.view.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 把手牌的序号转换为对应的资源名
 * 
 * @author chensl@ucweb.com
 * 
 *         下午5:09:27 2014-7-9
 */
public class CardGenerator {
	private static Map<String,Bitmap> CARDS_BITMAP=new HashMap<String, Bitmap>();
	public static String cardResourceName(String cardId) {
		StringBuilder prefix = new StringBuilder();
		int cardNO = Integer.valueOf(cardId.trim());
		if (cardNO == 0)
			return "cardbg1";
		if (cardNO == 53)
			return "a5_16";
		if (cardNO == 54)
			return "a5_17";

		switch ((cardNO-1)/ 13) {
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

	public static Bitmap getBitmap(Context con, String resourceName) {
		String TAG="getBitmap";
		if(CARDS_BITMAP.get(resourceName)==null)
		{	
		Log.i(TAG, "resourceName name 不存在！");
			CARDS_BITMAP.put(resourceName,  BitmapFactory.decodeResource(
				con.getResources(),
				con.getResources().getIdentifier(resourceName, "drawable",
						con.getApplicationInfo().packageName)));
		}
		return CARDS_BITMAP.get(resourceName);
	}


}
