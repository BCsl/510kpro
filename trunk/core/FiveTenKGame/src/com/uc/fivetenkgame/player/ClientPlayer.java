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

		//玩家链接成功
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			System.out.println(msg);
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

}
