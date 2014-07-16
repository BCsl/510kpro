package com.uc.fivetenkgame.player;

import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.util.Common;


public class ClientPlayer extends Player {

	public static ClientPlayer gInstance;
	public static ClientPlayer getInstance(){
		if( null == gInstance ){
			gInstance = new ClientPlayer();
		}
		
		return gInstance;
	}
	
	private ClientPlayer(){
		mNetworkManager = new ClientManager();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
	}
	
	public void initNetwork(String addr){
		((ClientManager)mNetworkManager).initNetwork(addr);
	}
	
	@Override
	public void handleMessage(String msg) {

		//������ӳɹ�
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			System.out.println(msg);
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

}
