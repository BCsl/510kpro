package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.uc.fivetenkgame.network.util.Common;

/**
 * 服务器网络管理类
 * @author liuzd
 *
 */
public class ServerManager extends NetworkManager{
	
	private ServerSocket mServerSocket;
	private ArrayList<TCPServer> mClientPlayers;
	private ArrayList<String> mPlayerIPs;
	
	private static ServerManager gInstance;
	public static ServerManager getInstance(){
		if( null == gInstance ){
			gInstance = new ServerManager();
		}
		
		return gInstance;
	}
	
	private ServerManager(){
		try {
			mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mClientPlayers = new ArrayList<TCPServer>();
		mPlayerIPs = new ArrayList<String>();
	}
	
	/**
	 * 服务器开始监听网络端口
	 */
	public void startListen(){
		
		new Thread(){
			
			@Override
			public void run(){
				try {
					//接收玩家链接
					for( int i = 1; i <= Common.TOTAL_PLAYER_NUM; ++i ){
						Socket socket = mServerSocket.accept();
						TCPServer player = new TCPServer(ServerManager.this, socket);
						player.sendMessage(Common.PLAYER_ACCEPTED + i);
						mClientPlayers.add(player);
						
						InetAddress ip = socket.getInetAddress();
						mPlayerIPs.add(ip.toString());
						receiveMessage(Common.PLAYER_ACCEPTED);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			
		}.start();
		
	}
	
	/**
	 * 发送消息给所有玩家
	 * 
	 */
	public void sendMessage(String msg) {
		for( TCPServer player : mClientPlayers ){
			player.sendMessage(msg);
		}
	}

	@Override
	public void initNetwork(String addr) {
		startListen();
	}
}
