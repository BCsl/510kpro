package com.uc.fivetenkgame.player;

import java.util.List;

import android.os.Handler;

import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 玩家类
 * 负责与网络通信及与activity通信
 * 
 * @author liuzd
 *
 */
/**
 * 
 * 修改：player类不再作为抽象类，与clientplayer类合并
 * @author fuyx
 *
 */
public class Player {
	
	protected PlayerModel mPlayerModel;
	protected NetworkManager mNetworkManager;
	protected Handler mHandler;
	//玩家序号
	protected int mPlayerNumber;
	
	//唯一的Player实例
	public static Player gInstance;
	
	public static  Player getInstance(){
		if( null == gInstance ){
			gInstance = new Player();
		}
		
		return gInstance;
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
	}
	
	private Player(){
		mNetworkManager = new ClientManager();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
	}
	
	public void initNetwork(String addr){
		((ClientManager)mNetworkManager).initNetwork(addr);
	}
	
	public void playCards(List<Card> prePlayerCards){
		
	}
	
	/**
	 * 将玩家出的牌转换成网络数据传输格式发送
	 * 
	 * @param playCards 将要出的牌
	 */
	public void sendPlayCards(List<Card> playCards) {
		
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append(Common.PLAY_CARDS);
		for(Card card:playCards){
			strbuilder.append(card.getCardId());
			strbuilder.append(',');
		}
		
		mNetworkManager.sendMessage(strbuilder.toString());
	}
	
	protected OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		public void reveiveMessage(String msg) {
			handleMessage(msg);
		}
	};
	
	/**
	 * 处理接收到的消息
	 * 
	 * @param msg 接收到的消息
	 * 
	 */
	public void handleMessage(String msg) {

		//玩家链接成功
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			
		}
		//玩家连接被拒绝
		else if( msg.startsWith(Common.PLAYER_REFUSED) ){
			
		}
		//开始游戏
		else if( msg.startsWith(Common.BEGIN_GAME) ){
			
		}
		//发牌
		else if( msg.startsWith(Common.SEND_CARDS) ){
			
		}
		//轮到该玩家出牌
		else if( msg.startsWith(Common.YOUR_TURN) ){
			
		}
		//其他玩家出的牌
		else if( msg.startsWith(Common.PLAYER_CARDS) ){
			
		}
		//本回合桌面分数
		else if( msg.startsWith(Common.ROUND_SCORE) ){
			
		}
		//各玩家的分数
		else if( msg.startsWith(Common.PLAYERS_SCORE) ){
			
		}
		//玩家剩余牌数
		else if( msg.startsWith(Common.PLAYERS_REMAIN_CARDS) ){
			
		}
		//游戏结束
		else if( msg.startsWith(Common.GAME_OVER) ){
			
		}
		//本局胜利的玩家
		else if( msg.startsWith(Common.WINNING_PLAYER) ){
			
		}
		else{
			
		}
	}
	
	protected void setGameScore(int score){
		
	}
	
	protected void handCard(List<Card> cards){
		
	}
	
	protected void setCardNumber(List<Integer> cardNumber){
		
	}
	
	protected void setScroeList(List<Integer> scroeList){
		
	}
	
	
}
