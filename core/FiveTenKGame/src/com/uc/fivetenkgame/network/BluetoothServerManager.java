package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.util.ArrayList;

import com.uc.fivetenkgame.common.NetworkCommon;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * 服务器蓝牙网络管理类
 * 
 * @author liuzd
 *
 */
public class BluetoothServerManager extends BluetoothManager {

	private static final String LOG_SERVER_MANAGER = "BluetoothServerManager :";
	//SDP服务名称
	private static final String SDP_RECORD_NAME = "Bluetooth Network";
	
	private BluetoothServerSocket mBluetoothServerSocket = null;
	private ArrayList<IBasicNetwork> mPlayerNetwork;
	private ListenThread mThread = null;
	
	private static BluetoothServerManager gInstance;
	public static BluetoothServerManager getInstance(){
		if( null == gInstance ){
			gInstance = new BluetoothServerManager();
		}
		
		return gInstance;
	}
	
	private BluetoothServerManager(){

	}
	
	public class ListenThread extends Thread{
		
		public void run(){
			try {
				//添加本地玩家
				Log.i(LOG_SERVER_MANAGER, "local player");
				IBasicNetwork locPlayer = new BluetoothLocalCommunication(BluetoothServerManager.this);
				mPlayerNetwork.add(locPlayer);
				locPlayer.sendMessage(NetworkCommon.PLAYER_ACCEPTED + NetworkCommon.SERVER_NUM);
				
				//添加远程蓝牙设备玩家
				for( int i = 2; i <= NetworkCommon.TOTAL_PLAYER_NUM; ++i ){
					
					BluetoothSocket socket = mBluetoothServerSocket.accept();
					
					Log.i(LOG_SERVER_MANAGER, "accepted player");
					IBasicNetwork player = new BluetoothSocketCommunication(BluetoothServerManager.this, socket);
					((BluetoothSocketCommunication)player).start();
					player.sendMessage(NetworkCommon.PLAYER_ACCEPTED + i);
					
					mPlayerNetwork.add(player);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void sendMessage(String msg) {

		Log.i(LOG_SERVER_MANAGER, "send :" + msg);
		for( IBasicNetwork player : mPlayerNetwork ){
			player.sendMessage(msg);
		}
		
	}

	@Override
	public void sendMessage(String msg, int playerNum) {
		
		Log.i(LOG_SERVER_MANAGER, "send to " + playerNum + " :" + msg);
		mPlayerNetwork.get(playerNum - 1).sendMessage(msg);
		
	}

	/**
	 * 监听蓝牙网络
	 * 
	 */
	@Override
	public void initNetwork(String addr) {
		
		try {
			mBluetoothServerSocket = 
					mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SDP_RECORD_NAME, BLUETOOTH_UUID);
		} catch (IOException e) {
			Log.i(LOG_SERVER_MANAGER, "sdp service record failed!");
			e.printStackTrace();
		}
		mPlayerNetwork = new ArrayList<IBasicNetwork>();
		
		mThread = new ListenThread();
		mThread.start();
	}

	@Override
	public void reset() {
		try {
			mBluetoothServerSocket.close();
			mBluetoothServerSocket = null;
			
			for( IBasicNetwork player : mPlayerNetwork ){
				player.release();
			}
			
			mPlayerNetwork.removeAll(mPlayerNetwork);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
