package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;
import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.network.OnReceiveMessageListener;
import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.serverstate.InitState;
import com.uc.fivetenkgame.state.serverstate.ServerState;

/**
 * 服务器类
 * 
 * 
 * @author liuzd
 *
 *
 */
public class Server implements ServerContext{
	
	private ArrayList<PlayerModel> mPlayerModels;
	private int mRoundScore;
	private int mClientNum;
	private int mCurrentPlayer;
	
	private NetworkInterface mNetworkManager;
	private ServerState mState;
	private Handler mHandler;
	
	private static Server gInstance;
	public static Server getInstance(){
		if( null == gInstance ){
			gInstance = new Server();
		}
		
		return gInstance;
	}
	
	private Server(){
		//mNetworkManager = ServerManager.getInstance();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		
		mClientNum = 0;
		mRoundScore=0;
//		mState = new InitState(this);
	}
	
	protected OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		public void reveiveMessage(String msg) {
			handleMessage(msg);
		}
	};
	public void handleMessage(String msg) {
		Log.i("server log", "receive :" + msg);
		mState.handle(msg);
	}
	
	/**
	 * 服务器开始监听网络，异步
	 * 
	 */
	public void startListen(){
		mState = new InitState(this);
		mState.handle(NetworkCommon.SERVER_LISTENING);
		mNetworkManager.initNetwork(null);
	}

	public void setNetworkManager(NetworkInterface networkInterface){
		mNetworkManager = networkInterface;
	}
	
	public NetworkInterface getNetworkManager() {
		return mNetworkManager;
	}
	
	public void setState(ServerState state){
		mState = state;
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
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

	public ArrayList<PlayerModel> getPlayerModel() {
		return mPlayerModels;
	}

	public int getCurrentPlayerNumber() {
		return mCurrentPlayer;
	}

	public int getRoundScore() {
		return mRoundScore;
	}

	public void setRoundScore(int mRoundScore) {
		this.mRoundScore = mRoundScore;
	}

	@Override
	public void setCurrentPlayerNumber(int CurrentPlayerNumber) {
		mCurrentPlayer=CurrentPlayerNumber;
	}

	@Override
	public void resetServer() {
		mClientNum = 0;
		mRoundScore=0;
		mState = new InitState(this);
		mNetworkManager.reset();
		Log.i("Server", "reset server");
	}
	
}
