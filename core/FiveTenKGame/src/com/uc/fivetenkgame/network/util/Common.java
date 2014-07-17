package com.uc.fivetenkgame.network.util;

/**
 * 公共类，一些常用数据定义
 * 
 * @author liuzd
 *
 */
public class Common {

	//玩家序号
	public static final int SERVER_NUM	= 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;
	
	//网络消息头标号定义
	public static final String PLAYER_ACCEPTED = "1#";//连接成功信号：1#+玩家序号
	public static final String PLAYER_REFUSED = "2#";//连接失败信号
	public static final String BEGIN_GAME = "3#";//游戏开始信号：3#+玩家序号+牌号+ ，+牌号....
	public static final String YOUR_TURN = "4#";//提醒玩家出牌信号
	public static final String PLAY_END = "5#";//出牌转发信号：5#+玩家号+牌号 + , + .... 
												//+桌面分数+玩家1牌数+玩家2牌数+玩家3牌数
	public static final String ROUND_END = "6#";//回合结束信号，更新玩家的分数：6#+玩家1分数+玩家2分数+玩家3分数
	public static final String GAME_OVER = "7#";//游戏结束信号：7#+胜利者
	public static final String WINNING_PLAYER = "8#";
	
	public static final String PLAY_CARDS = "9#";//自己出牌：12#+牌的序号 + , +.....	
	public static final String GIVE_UP = "0#";//自己放弃出牌
	
	//handler消息处理编号
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
}
