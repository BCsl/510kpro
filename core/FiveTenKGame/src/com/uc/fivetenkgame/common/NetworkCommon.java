package com.uc.fivetenkgame.common;

/**
 * 公共类，一些常用数据定义
 * 
 * @author liuzd
 * 
 */
public class NetworkCommon {

	// 玩家序号
	public static final int SERVER_NUM = 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;

	// 网络消息头标号定义
	//单信号
	public static final String PLAYER_REFUSED = "2#";// 连接失败信号:
	public static final String GAME_PAUSE = "10#";// 某个玩家暂停游戏，所有玩家暂停
	public static final String GAME_RESUME = "11#";// 所有玩家恢复游戏
	public static final String GAME_EXIT = "12#";// 某个玩家退出游戏，所有玩家退出
	public static final String PLAY_AGAIN = "14#"; // 重玩消息
	//只包含玩家序号
	public static final String GIVE_UP = "0#";// 自己放弃出牌0#+玩家号
	public static final String PLAYER_ACCEPTED = "1#";// 连接成功信号：1#+当前玩家序号
	public static final String YOUR_TURN = "4#";// 提醒玩家出牌信号：4#+玩家序号
	//包含玩家序号和牌号
	public static final String BEGIN_GAME = "3#";// 游戏开始信号：3#+玩家序号+牌号+ ，+牌号....
	public static final String PLAY_CARDS = "9#";// 自己出牌：9#+玩家号+牌号 + , +.....
	//包含玩家号和玩家名字
	public static final String PLAYER_NAME = "13#";// 1.server接收：13#+玩家序号+玩家名字 2.player接收：13#玩家1名字+玩家2名字+玩家3名字
	//包含各玩家分数
	public static final String ROUND_END = "6#";// 回合结束信号，更新玩家的分数：6#+玩家1分数+玩家2分数+玩家3分数
	//包含赢家号，各玩家分数
	public static final String GAME_OVER = "7#";// 游戏结束信号：7#+胜利者+玩家1分数+玩家2分数+玩家3分数
	//包含等待玩家人数
	public static final String PLAYER_NUMBER_UPDATE = "8#";// 更新等待玩家人数：8#+等待玩家人数
	//包含玩家号，牌号，桌面分数，玩家剩余牌数
    public static final String PLAY_END = "5#";// 出牌转发信号：5#+玩家号+牌号 + , + ....+桌面分数+玩家1牌数+玩家2牌数+玩家3牌数（后4位）

    
	// handler消息处理编号
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	public static final int END_GAME = 3;
	public static final int HOST_FULL = 4;
	public static final int TIME_OUT = 5;
	public static final int GAME_STATE_CHANGE = 6;// 和GAME_PAUSE,GAME_RESUME，GAME_EXIT匹配
	public static final int PLAYER_LEFT = 7;

	//player状态改变内部消息
	public static final String PLAYER_STATE_CHANGE = "0@";
	// 服务器状态改变内部消息
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	// 总玩家人数
	public static final int TOTAL_PLAYER_NUM = 3;

	// 消息数据尾
	public static final String MESSAGE_END = "#@";

}
