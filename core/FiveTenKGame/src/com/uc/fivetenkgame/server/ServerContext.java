package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.serverstate.ServerState;

/**
 * 服务器接口
 * 
 * @author liuzd
 *
 */
public interface ServerContext {
	/**
	 * 获得服务器的网络接口
	 * 
	 * @return NetworkManager网络接口
	 */
	public NetworkInterface getNetworkManager();
	
	/**
	 * 设置服务器的状态
	 * 
	 * @param state  当前状态
	 */
	public void setState(ServerState state);
	
	/**
	 * 获得服务器的handler，该handler由Acitvity设置
	 * 
	 * @return Activity hangler 的引用
	 */
	public Handler getHandler();
	
	/**
	 * 设置服务器handler
	 * 
	 * @param handler  Activity hangler 的引用
	 */
	public void setHandler(Handler handler);
	
	/**
	 * 设置当前玩家数
	 * 
	 * @param num  玩家数
	 */
	public void setClientNum(int num);
	
	/**
	 * 获得当前玩家数
	 * 
	 * @return  当前玩家数
	 */
	public int getClientNum();
	
	/**
	 * 设置所有玩家的数据
	 * 
	 * @param playerModelList  所有玩家的数据
	 */
	public void setPlayerModel(ArrayList<PlayerModel> playerModelList);
	
	/**
	 * 获得所有玩家的数据
	 * 
	 * @return 所有玩家的数据
	 */
	public ArrayList<PlayerModel> getPlayerModel();
	
	/**
	 * 获得当前出牌玩家编号
	 * 
	 * @return 当前出牌玩家编号
	 */
	public int getCurrentPlayerNumber();
	
	/**
	 * 设置出牌玩家编号
	 * 
	 * @param CurrentPlayerNumber 出牌玩家编号
	 */
	public void setCurrentPlayerNumber(int CurrentPlayerNumber) ;
	
	/**
	 * 获得本轮分数
	 * 
	 * @return 本轮分数
	 */
	public int getRoundScore() ;
	
	/**
	 * 设置本轮分数
	 * 
	 * @param mRoundScore 本轮分数
	 */
	public void setRoundScore(int mRoundScore) ;
	
	/**
	 * 消息处理
	 * 
	 * @param msg 要处理的消息
	 */
	public void handleMessage(String msg);
	
	public void resetServer();
}
