package com.uc.fivetenkgame.player;

import android.os.Handler;

import com.uc.fivetenkgame.state.playerstate.PlayerState;

/**
 * 玩家场景接口
 * @author fuyx
 *
 */
public interface PlayerContext {
	/**
	 * 调用当前state的handle方法
	 * @param msg 传给handle的消息
	 */
	void handle(String msg);
	
	/**
	 * 启动网络方法
	 * @param addr server的IP地址
	 */
	void initNetwork(String addr);
	
	/**
	 * 设置状态方法
	 * @param state 
	 */
	void setState(PlayerState state);
	
	/**
	 * 发送消息
	 * @param msg
	 */
	void sendMsg(String msg);
	
	/**
	 * 收到回合结束信号时，调用该方法
	 * @param score
	 */
	void roundEndAction(String[] score);
	
	/**
	 * 收到server玩家出牌信号时，调用该方法
	 * @param outlist 玩家出的牌
	 * @param playernumber 出牌玩家号
	 * @param tablescore 桌面分数
	 * @param remaincards 个玩家剩余牌数
	 */
	void playCardsEndAction(String[] outlist, String playernumber,
			String tablescore, String[] remaincards);
	
	/**
	 * @return 将要出的牌
	 */
	String getCardsToBePlayed();
	
	/**
	 * 设置玩家初始手牌
	 * @param cards
	 */
	void setInitPlayerCards(String cards);
	
	/**
	 * 设置玩家号
	 * @param playerNumber
	 */
	void setPlayerNumber(int playerNumber);
	
	/**
	 * 
	 * @return 玩家号
	 */
	int getPlayerNumber();
	
	/**
	 * 
	 * @param str 0为胜利玩家，往后为各玩家分数
	 */
	void gameOver(String[] str);
	
	/**
	 * 设置handler
	 * @param handler
	 */
	void setHandler(Handler handler);
	
	/**
	 * 得到handler
	 * @return Player对象的handler
	 */
	public Handler getHandler();
	
	/**
	 * 获得doneHandCards的值，判断出牌是否结束
	 * @return
	 */
	public boolean doneHandCards();
	
	/**
	 * 设置doneHandCards
	 * @param flag
	 */
	public void setDoneHandCards(boolean flag);
	
	/**
	 * 设置myTurn
	 * @param flag
	 */
	public void setMyTurn(boolean flag);
	
	/**
	 * 判断该玩家是否是该局第一个出牌的人
	 * @return
	 */
	public boolean isFirstPlayer();
	
	public boolean hasCard();
	
	public void ReStartGame();
	/**
	 * 设置当前出牌玩家
	 * @param palyerId
	 */
	public void setCurrentPlayer(int palyerId);
	/**
	 * 处理玩家放弃操作
	 * @param playerId
	 */
	public void playerGiveUp(int playerId);
	
	public void resetPlayer();
	
	public String getPlayerName();
	
	public void setPlayersName(int playerId,String playerName);
}	
