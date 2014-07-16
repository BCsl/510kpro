package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.uc.fivetenkgame.network.util.Common;

/**
 * ���������������
 * @author liuzd
 *
 */
public class ServerManager extends NetworkManager{
	
	private ServerSocket mServerSocket;
	private ArrayList<TCPServer> mClientPlayers;
	private ArrayList<String> mPlayerIPs;
	
	public ServerManager(){
		try {
			mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mClientPlayers = new ArrayList<TCPServer>();
		mPlayerIPs = new ArrayList<String>();
	}
	
	public void startListen(){
		
		try {
			//�����������
			for( int i = 1; i <= Common.TOTAL_PLAYER_NUM; ++i ){
				Socket socket = mServerSocket.accept();
				TCPServer player = new TCPServer(this, socket);
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
	
	/**
	 * ������Ϣ���������
	 * 
	 */
	public void sendMessage(String msg) {
		for( TCPServer player : mClientPlayers ){
			player.sendMessage(msg);
		}
	}
}
