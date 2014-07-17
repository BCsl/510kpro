package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.serverstate.InitState;
import com.uc.fivetenkgame.state.serverstate.ServerState;

/**
 * ��������
 * 
 * 
 * @author liuzd, fuyx
 *
 *
 */
public class Server implements ServerContext{
	
	private ArrayList<PlayerModel> mPlayerModels;
	private int mRoundScore;
	private int mClientNum;
	private int mCurrentPlayer;
	
	private NetworkManager mNetworkManager;
	private ServerState mState;
	private Handler mHandler;
	
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
		mState = new InitState(this);
	}
	
	protected OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		public void reveiveMessage(String msg) {
			handleMessage(msg);
		}
	};
	
	public void handleMessage(String msg) {
/*		
		//������ӳɹ�
		if( msg.startsWith(Common.PLAYER_ACCEPTED) ){
			++mClientNum;
			
			if( mClientNum == 1 ){
				//���µȴ���������
				mHandler.obtainMessage(Common.UPDATE_WAITING_PLAYER_NUM, 2).sendToTarget();
			}
			else if( mClientNum == 2 ){
				//�������������ʼ��Ϸ
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
	
	public Handler getHandler(){
		return mHandler;
	}

	@Override
	public int getClientNum() {
		return mClientNum;
	}

	@Override
	public void setClientNum(int num) {
		mClientNum = num;
	}

	@Override
	public void setPlayerModel(ArrayList<PlayerModel> playerModelList) {
		mPlayerModels = playerModelList;
	}

	@Override
	public ArrayList<PlayerModel> getPlayerModel() {
		return mPlayerModels;
	}

	@Override
	public int getCurrentPlayerNumber() {
		return mCurrentPlayer;
	}
	
}
