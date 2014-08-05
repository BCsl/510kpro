package com.uc.fivetenkgame.network.wifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;

/**
 * 服务器网络管理类
 * 
 * @author liuzd
 *
 */
public class ServerManager extends NetworkManager {

	private static String LOG_SERVER_TAG = "servermgr : ";
	
	private ServerSocket mServerSocket;
	private ArrayList<SocketCommunicationThread> mClientPlayers;
	private ArrayList<String> mPlayerIPs;

	private static ServerManager gInstance;

	public static ServerManager getInstance() {
		if (null == gInstance) {
			gInstance = new ServerManager();
		}

		return gInstance;
	}

	private ServerManager() {
		try {
			mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务器开始监听网络端口
	 */

	private class ListenThread extends Thread {

		public void run() {
			try {
				// 接收玩家链接
				for (int i = 1; i <= NetworkCommon.TOTAL_PLAYER_NUM; ++i) {

					Socket socket = mServerSocket.accept();

					Log.i(LOG_SERVER_TAG, "accept player :" + i);
					SocketCommunicationThread player = new SocketCommunicationThread
								(ServerManager.this, socket);
					player.start();
					mClientPlayers.add(player);
                    InetAddress ip = socket.getInetAddress();
                    mPlayerIPs.add(ip.toString());
					player.sendMessage(NetworkCommon.PLAYER_ACCEPTED + i);
					Thread.sleep(20);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private ListenThread mThread = null;

	public void startListen() {
		Log.i("ServerManager", "startListen");
		
		try {
			if (mServerSocket == null || mServerSocket.isClosed())
				mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mClientPlayers = new ArrayList<SocketCommunicationThread>();
		mPlayerIPs = new ArrayList<String>();
		
		Log.i("ServerManager", "start ");
		mThread = new ListenThread();
		mThread.start();
	}


	/**
	 * 发送消息给所有玩家
	 * 
	 */
	public void sendMessage(String msg) {
		Log.i(LOG_SERVER_TAG, "send :" + msg);
		for (SocketCommunicationThread player : mClientPlayers) {
			player.sendMessage(msg);
		}
	}

	/**
	 * 发消息给特定编号的玩家
	 * 
	 * @param msg
	 *            要发送的消息
	 * @param playerNum
	 *            玩家编号
	 */
	public void sendMessage(String msg, int playerNum) {
		Log.i(LOG_SERVER_TAG, "send to " + playerNum + " :" + msg);
		mClientPlayers.get(playerNum - 1).sendMessage(msg);
	}

	@Override
	public void initNetwork(String addr) {
		startListen();
	}

//	public void removePlayer(TCPServer player) {
//		mClientPlayers.remove(player);
//	}

	@Override
	public void reset() {

		//停止监听
		try {
			if( !mServerSocket.isClosed() )
				mServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//清除已有数据
		if( mPlayerIPs != null && !mPlayerIPs.isEmpty() )
			mPlayerIPs.removeAll(mPlayerIPs);
		if( mClientPlayers != null && !mClientPlayers.isEmpty() ){
			for(SocketCommunicationThread playerThread : mClientPlayers){
				playerThread.release();
				playerThread = null;
			}
		}
	}

}
