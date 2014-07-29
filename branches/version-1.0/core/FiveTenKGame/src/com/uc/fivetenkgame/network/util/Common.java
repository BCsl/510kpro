package com.uc.fivetenkgame.network.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static final String BEGIN_GAME = "3#";//游戏开始信号：3#+玩家序号+牌号+ ，+牌号....
	public static final String YOUR_TURN = "4#";//提醒玩家出牌信号：4#+玩家序号
	public static final String PLAY_END = "5#";//出牌转发信号：5#+玩家号+牌号 + , + .... //+桌面分数+玩家1牌数+玩家2牌数+玩家3牌数（后4位）
	public static final String ROUND_END = "6#";//回合结束信号，更新玩家的分数：6#+玩家1分数+玩家2分数+玩家3分数
	public static final String GAME_OVER = "7#";//游戏结束信号：7#+胜利者+各玩家分数
	public static final String PLAYER_NUMBER_UPDATE = "8#";//更新等待玩家人数：8#+等待玩家人数
	
	public static final String PLAY_CARDS = "9#";//自己出牌：9#+玩家号+牌的序号 + , +.....	
	public static final String GIVE_UP = "0#";//自己放弃出牌0#+玩家号
	
	public static final String GAME_PAUSE = "10#";//某个玩家暂停游戏，所有玩家暂停
	public static final String GAME_RESUME = "11#";//所有玩家恢复游戏
	public static final String GAME_EXIT = "12#";//某个玩家退出游戏，所有玩家退出

	public static final String PLAYER_NAME = "13#";//1.server接收：13#+玩家序号+玩家名字    2.player接收：13#玩家1名字+玩家2名字+玩家3名字
	public static final String PLAY_AGAIN = "14#"; //重玩消息
		
	// handler消息处理编号
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	public static final int END_GAME = 3;
	public static final int HOST_FULL = 4;
	public static final int TIME_OUT = 5;
	public static final int GAME_STATE_CHANGE = 6;//和GAME_PAUSE,GAME_RESUME，GAME_EXIT匹配
	public static final int PLAYER_LEFT = 7;
	
	// 服务器状态改变内部消息
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	// 总玩家人数
	public static final int TOTAL_PLAYER_NUM = 3;
	
	//消息数据尾
	public static final String MESSAGE_END = "#@";
	
	//sharepreference相关
	public static final String TABLE_PLAYERS="players";
	public static final String TABLE_SETTING="setting";
	public static final String SP_QRCODE_FLAG="qrcode";
	public static final String SP_MUSIC_FLAG="music";
	
	public static final int SOUND_BUTTON_PRESS=0;
	public static final int SOUND_GAME_START=1;
	public static final int SOUND_WIN = 2;
	public static final int SOUND_FAILD =3;
	public static final int SOUND_SECOND_CALL =4;
//	public static final int SOUND_SECOND_CALL_1 =5;
//	public static final int SOUND_SECOND_CALL_2 =6;
	public static final int SOUND_OUTPUT_CARDS =7;
	public static final int SOUND_PASS =8;
	public static final int SOUND_BEEP =9;
	
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
	/**
	 * 检查IP是否有效
	 * 
	 * @param ipaddr 输入的IP字符串
	 * @return   是否有效
	 */
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
		}
}
