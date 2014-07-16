package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.InitState;
import com.uc.fivetenkgame.state.ServerState;

/**
 * 服务器类
 * 
 * 
 * @author liuzd, fuyx
 *
 *
 */
public class Server implements ServerContext{
	
	private ArrayList<PlayerModel> mPlayerModels;
	private int []mPlayerScores;
	private int mRoundScore;
	private int mClientNum;
	
	private NetworkManager mNetworkManager;
	private Handler mHandler;
	private ServerState mState;
	
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
		mState = new InitState();
	}
	
	protected OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		public void reveiveMessage(String msg) {
			handleMessage(msg);
		}
	};
	
	public void handleMessage(String msg) {
/*		
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
*/
		mState.handle(msg);
		
	}
	

	public void startListen(){
		mState.handle(Common.SERVER_LISTENING);
		
		((ServerManager)mNetworkManager).startListen();
	}

	@Override
	public NetworkManager getNetworkManager() {
		return mNetworkManager;
	}
	
	public void setState(ServerState state){
		mState = state;
	}
	
	
}
