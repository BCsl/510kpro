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
 *         下午5:23:29 2014-7-11
 */
public abstract class EventListener {

	/**
	 * 出牌操作(需要进行规则的判断)
	 * 
	 * @param handList           准备出的牌 handList为NULL，则为放弃操作。
	 * @param timeOut            是否超时了
	 * 
	 * @return 出牌成功返回false
	 */
	public abstract boolean handCard(List<Card> handList, boolean timeOut);

}
