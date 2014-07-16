package com.uc.fivetenkgame.player;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.ServerManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;

/**
 * ��������
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
	//������
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
		
		
	}
	

	public void startListen(){
		((ServerManager)mNetworkManager).startListen();
	}
}
