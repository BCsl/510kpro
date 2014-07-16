package com.uc.fivetenkgame.player;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;

/**
 * 服务器类
 * 
 * 
 * @author liuzd, fuyx
 *
 *
 */
public class Server{
	protected PlayerModel mPlayerModel;
	protected NetworkManager mNetworkManager;
	protected Handler mHandler;
	//玩家序号
	protected int mPlayerNumber;
	private int mClientNum;
	
	public static Server gInstance;
	public static Server getInstance(){
		if( null == gInstance ){
			gInstance = new Server();
		}
		
		return gInstance;
	}
	
	private Server(){
		mNetworkManager = new ServerManager();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		
		mClientNum = 0;
		mPlayerNumber = Common.SERVER_NUM;
	}
	
	protected OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		public void reveiveMessage(String msg) {
			handleMessage(msg);
		}
	};
	
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
