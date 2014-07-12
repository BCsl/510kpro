package com.uc.fivetenkgame.player;

import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.util.Common;

/**
 * 服务器玩家
 * 
 * @author liuzd
 *
 */
public class ServerPlayer extends Player {

	private int mClientNum;
	
	public static ServerPlayer gInstance;
	public static ServerPlayer getInstance(){
		if( null == gInstance ){
			gInstance = new ServerPlayer();
		}
		
		return gInstance;
	}
	
	private ServerPlayer(){
		mNetworkManager = new ServerManager();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		
		mClientNum = 0;
		mPlayerNumber = Common.SERVER_NUM;
	}
	
	@Override
	public void handleMessage(String msg) {
		
		//玩家链接成功
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			++mClientNum;
			
			if( mClientNum == 1 ){
				//更新等待界面人数
				mHandler.obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM, 2).sendToTarget();
			}
			else if( mClientNum == 2 ){
				//够玩家人数，开始游戏
				mHandler.obtainMessage(Common.START_GAME).sendToTarget();
				mNetworkManager.sendMessage(Common.BEGIN_GAME);
			}
		}
		
		
	}
	

	public void startListen(){
		((ServerManager)mNetworkManager).startListen();
	}
}
