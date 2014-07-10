package com.uc.fivetenkgame.view.util;

import android.util.Log;

/**
 * 
 * @author chensl@ucweb.com
 *
 * ÏÂÎç5:09:27 2014-7-9
 */
public class CardGenerator {
	public static String cardResourceName(String cardId){
		String res;
		String prefix = null;
		String suffix;
		int cardNO=Integer.valueOf(cardId.trim());
		if(cardNO == 0)
			return "cardbg1";
		if(cardNO==53)
				return "a5_16";
		if(cardNO==54)
				return "a5_17";
		
		switch(cardNO/14){
		case 0:prefix="a2_";break;
		case 1:prefix="a1_";break;
		case 2:prefix="a3_";break;
		case 3:prefix="a4_";break;
		default :new IllegalArgumentException(cardNO+"is not defined!");break;
		}
		cardNO%=13;
		if(cardNO<3)
			suffix=String.valueOf(cardNO+13);
		else
			suffix=String.valueOf(cardNO);
		res=prefix+suffix;
		
		Log.i("card resource name:", res);
		return res.trim();
	}

}
