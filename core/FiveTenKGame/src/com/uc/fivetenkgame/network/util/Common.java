package com.uc.fivetenkgame.network.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 公共类，一些常用数据定义
 * 
 * @author liuzd
 *
 */
public class Common {

	// 玩家序号
	public static final int SERVER_NUM = 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;

	//网络消息头标号定义
	public static final String PLAYER_ACCEPTED = "1#";//连接成功信号：1#+当前玩家序号
	public static final String PLAYER_REFUSED = "2#";//连接失败信号:2#
	//public static final String PLAYER_NUMBER_UPDATE = "10#";
	public static final String BEGIN_GAME = "3#";//游戏开始信号：3#+玩家序号+牌号+ ，+牌号....
	public static final String YOUR_TURN = "4#";//提醒玩家出牌信号：4#+玩家序号
	public static final String PLAY_END = "5#";//出牌转发信号：5#+玩家号+牌号 + , + .... //+桌面分数+玩家1牌数+玩家2牌数+玩家3牌数（后4位）
	public static final String ROUND_END = "6#";//回合结束信号，更新玩家的分数：6#+玩家1分数+玩家2分数+玩家3分数
	public static final String GAME_OVER = "7#";//游戏结束信号：7#+胜利者
	public static final String PLAYER_NUMBER_UPDATE = "8#";//更新等待玩家人数：8#+等待玩家人数
	
	public static final String PLAY_CARDS = "9#";//自己出牌：9#+玩家号+牌的序号 + , +.....	
	public static final String GIVE_UP = "0#";//自己放弃出牌0#+玩家号
	
	// handler消息处理编号
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	public static final int END_GAME = 3;
	public static final int HOST_FULL = 4;
	public static final int TIME_OUT = 5;
	
	// 服务器状态改变内部消息
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	// 总玩家人数
	public static final int TOTAL_PLAYER_NUM = 3;
	
	//消息数据尾
	public static final String MESSAGE_END = "#@";
	
	public static void setOrder(List<Card> list){
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				int a1 = (Integer.valueOf(o1.getCardId()) - 1 ) / 13; // 花色
				int a2 = (Integer.valueOf(o2.getCardId()) - 1 ) / 13;
				
				int b1 = (Integer.valueOf(o1.getCardId()) - 1 ) % 13;// 数值
				int b2 = (Integer.valueOf(o2.getCardId()) - 1 ) % 13;

				//有joker的情况
				if( a1 == 4 || a2 == 4 ){
					if( a2 == a1 )
						return b2 - b1;
					return a2 - a1;
				}
				
				//2,1 情况
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
