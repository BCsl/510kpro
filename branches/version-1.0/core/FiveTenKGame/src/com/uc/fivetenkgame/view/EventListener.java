/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view;

import java.util.List;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         ����5:23:29 2014-7-11
 */
public abstract class EventListener {

	/**
	 * ���Ʋ���(��Ҫ���й�����ж�)
	 * 
	 * @param handList           ׼�������� handListΪNULL����Ϊ����������
	 * @param timeOut            �Ƿ�ʱ��
	 * 
	 * @return ���Ƴɹ�����false
	 */
	public abstract boolean handCard(List<Card> handList, boolean timeOut);

}
