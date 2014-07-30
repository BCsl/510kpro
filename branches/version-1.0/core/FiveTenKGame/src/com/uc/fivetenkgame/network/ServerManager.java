package com.uc.fivetenkgame.network;

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

	private boolean[] flag = new boolean[3];
	private ServerSocket mServerSocket;
	private ArrayList<TCPServer> mClientPlayers;
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

		mClientPlayers = new ArrayList<TCPServer>();
		mPlayerIPs = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			flag[i] = true;
		}
	}

	/**
	 * 服务器开始监听网络端口
	 */

	private class MyThread extends Thread {

		public void run() {
			try {
				// 接收玩家链接
				for (int i = 1; i <= NetworkCommon.TOTAL_PLAYER_NUM; ++i) {
//					if (mServerSocket.isClosed())
//						mServerSocket = new ServerSocket(NETWORK_PORT);
					// return;
					Socket socket = mServerSocket.accept();

					Log.i(LOG_SERVER_TAG, "accept player :" + i);
					TCPServer player = new TCPServer(ServerManager.this,
							socket, i);
					flag[i - 1] = true;
					player.sendMessage(NetworkCommon.PLAYER_ACCEPTED + i);
					Thread.sleep(20);
					mClientPlayers.add(player);

					InetAddress ip = socket.getInetAddress();
					mPlayerIPs.add(ip.toString());
					receiveMessage(NetworkCommon.PLAYER_ACCEPTED);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private MyThread mThread = null;

	public void startListen() {
		Log.i("ServerManager", "startListen");
		
		try {
			if (mServerSocket == null || mServerSocket.isClosed())
				mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.i("ServerManager", "start ");
		mThread = new MyThread();
		mThread.start();
	}

	/*
	 * public void startListen(){ new Thread(){
	 * 
	 * @Override public void run(){ try { //接收玩家链接 for( int i = 1; i <=
	 * Common.TOTAL_PLAYER_NUM; ++i ){ if(mServerSocket.isClosed()) return;
	 * Socket socket = mServerSocket.accept(); Log.i(LOG_SERVER_TAG,
	 * "accept player :" + i); TCPServer player = new
	 * TCPServer(ServerManager.this, socket);
	 * player.sendMessage(Common.PLAYER_ACCEPTED + i); Thread.sleep(20);
	 * mClientPlayers.add(player);
	 * 
	 * InetAddress ip = socket.getInetAddress(); mPlayerIPs.add(ip.toString());
	 * receiveMessage(Common.PLAYER_ACCEPTED); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } catch
	 * (InterruptedException e) { e.printStackTrace(); } }
	 * 
	 * }.start();
	 * 
	 * }
	 */

	/**
	 * 发送消息给所有玩家
	 * 
	 */
	public void sendMessage(String msg) {
		Log.i(LOG_SERVER_TAG, "send :" + msg);
		for (TCPServer player : mClientPlayers) {
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

	public void removePlayer(TCPServer player) {
		mClientPlayers.remove(player);
	}

	@Override
	public void reset() {
		for (int i = 0; i < 3; i++) {
			flag[i] = false;
		}
		mPlayerIPs.removeAll(mPlayerIPs);
//		for (TCPServer player : mClientPlayers) {
//			player.release();
//		}
		
		try {
			mServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getFlag(int number) {
		return flag[number];
	}
}
