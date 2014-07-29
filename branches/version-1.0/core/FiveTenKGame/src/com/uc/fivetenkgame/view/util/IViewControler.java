/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.util;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 界面数据更新控制器
 * 
 * @author chensl@ucweb.com
 * 
 *         上午9:55:51 2014-7-16
 */
public interface IViewControler {
	/**
	 * 设置游戏当前分数
	 * 
	 * @param score
	 *            游戏分数
	 */
	public void setGameScore(int score);

	/**
	 * 设置各个玩家剩余牌数量
	 * 
	 * @param cardNumber
	 *            个玩家剩余牌的数量
	 */
	public void setCardNumber(List<Integer> cardNumber);

	/**
	 * 设置各个玩家自己的得分
	 * 
	 * @param scroeList
	 *            各个玩家自己的分数
	 */
	public void setScroeList(List<Integer> scroeList);

	/**
	 * 设置各个玩家出的牌
	 * 
	 * @param number
	 *            玩家号
	 * @param list
	 *            玩家出的牌
	 */
	public void setPlayersOutList(int playId, List<Card> list);

	/**
	 * 设置玩家手牌
	 * 
	 * @param cardList
	 *            各个玩家自己的牌
	 */
	public void setCards(List<Card> cards);

	/**
	 * 设置是否可以出牌
	 * 
	 * @param flag
	 *            是否可以出牌信号
	 */
	public void setMyTurn(boolean flag);
	/**
	 * 设置事件回调器
	 * @param eventListener
	 */
	public void setEventListener(EventListener eventListener) ;
	/**
	 * 游戏结束操作
	 */
	public void gameOver(int playId);
	/**
	 * 出牌没按照规则
	 */
	public void handCardFailed();
	/**
	 * 回合结束，清理桌面上的卡牌
	 */
	public void roundOver();
	/**
	 * 设置当前出牌玩家
	 */
	public void setCurrentPlayer(int playerId);
}
