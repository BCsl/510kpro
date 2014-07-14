package com.uc.fivetenkgame.player;

import java.util.List;

import android.os.Handler;

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
public abstract class Player {
	
	protected PlayerModel mPlayerModel;
	protected NetworkManager mNetworkManager;
	protected Handler mHandler;
	//玩家序号
	protected int mPlayerNumber;
	
	public Player(){
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
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
	protected abstract void handleMessage(String msg);
	
	protected void setGameScore(int score){
		
	}
	
	protected void handCard(List<Card> cards){
		
	}
	
	protected void setCardNumber(List<Integer> cardNumber){
		
	}
	
	protected void setScroeList(List<Integer> scroeList){
		
	}
	
	
}
