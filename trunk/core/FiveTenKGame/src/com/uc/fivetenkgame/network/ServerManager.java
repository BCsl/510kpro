package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.uc.fivetenkgame.network.util.Common;

/**
 * 服务器网络管理类
 * @author liuzd
 *
 */
public class ServerManager extends NetworkManager{
		
	private ServerSocket mServerSocket;
	
	private TCPServer mFirstPlayer;
	private TCPServer mSecondPlayer;
	
	public ServerManager(){
		try {
			mServerSocket = new ServerSocket(NETWORK_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startListen(){
		
		try {
			//接收第一个玩家链接
			Socket socket = mServerSocket.accept();
			mFirstPlayer = new TCPServer(this, socket);
			mFirstPlayer.sendMessage(Common.PLAYER_ACCEPTED + Common.PLAYER1_NUM);
			mFirstPlayer.start();
			receiveMessage(Common.PLAYER_ACCEPTED);
			
			//接收第二个玩家链接
			socket = mServerSocket.accept();
			mSecondPlayer = new TCPServer(this, socket);
			mSecondPlayer.sendMessage(Common.PLAYER_ACCEPTED + Common.PLAYER2_NUM);
			mSecondPlayer.start();
			receiveMessage(Common.PLAYER_ACCEPTED);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		mFirstPlayer.sendMessage(msg);
		mSecondPlayer.sendMessage(msg);
	}

}
