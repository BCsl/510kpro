/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * @author chensl@ucweb.com
 *
 * ����9:59:43 2014-7-30
 */
public class OredrUtil {
	public static void setOrder(List<Card> list){
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				int a1 = (Integer.valueOf(o1.getCardId()) - 1 ) / 13; // ��ɫ
				int a2 = (Integer.valueOf(o2.getCardId()) - 1 ) / 13;
				
				int b1 = (Integer.valueOf(o1.getCardId()) - 1 ) % 13;// ��ֵ
				int b2 = (Integer.valueOf(o2.getCardId()) - 1 ) % 13;

				//��joker�����
				if( a1 == 4 || a2 == 4 ){
					if( a2 == a1 )
						return b2 - b1;
					return a2 - a1;
				}
				
				//2,1 ���
				if( b2 < 2 )
					b2 += 13;
				if( b1 < 2 )
					b1 += 13;
				
				if (b2 - b1 == 0)
					return a2 - a1;
				else {
					return b2 - b1;
				}
			}
		});
	}
}
