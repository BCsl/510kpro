package com.uc.fivetenkgame.player;

import java.util.List;

import android.os.Handler;

import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * �����
 * ����������ͨ�ż���activityͨ��
 * 
 * @author liuzd
 *
 */
/**
 * 
 * �޸ģ�player�಻����Ϊ�����࣬��clientplayer��ϲ�
 * @author fuyx
 *
 */
public class Player {
	
	protected PlayerModel mPlayerModel;
	protected NetworkManager mNetworkManager;
	protected Handler mHandler;
	//������
	protected int mPlayerNumber;
	
	//Ψһ��Playerʵ��
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
	 * ����ҳ�����ת�����������ݴ����ʽ����
	 * 
	 * @param playCards ��Ҫ������
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
	 * ������յ�����Ϣ
	 * 
	 * @param msg ���յ�����Ϣ
	 * 
	 */
	public void handleMessage(String msg) {

		//������ӳɹ�
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			
		}
		//������ӱ��ܾ�
		else if( msg.startsWith(Common.PLAYER_REFUSED) ){
			
		}
		//��ʼ��Ϸ
		else if( msg.startsWith(Common.BEGIN_GAME) ){
			
		}
		//����
		else if( msg.startsWith(Common.SEND_CARDS) ){
			
		}
		//�ֵ�����ҳ���
		else if( msg.startsWith(Common.YOUR_TURN) ){
			
		}
		//������ҳ�����
		else if( msg.startsWith(Common.PLAYER_CARDS) ){
			
		}
		//���غ��������
		else if( msg.startsWith(Common.ROUND_SCORE) ){
			
		}
		//����ҵķ���
		else if( msg.startsWith(Common.PLAYERS_SCORE) ){
			
		}
		//���ʣ������
		else if( msg.startsWith(Common.PLAYERS_REMAIN_CARDS) ){
			
		}
		//��Ϸ����
		else if( msg.startsWith(Common.GAME_OVER) ){
			
		}
		//����ʤ�������
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
