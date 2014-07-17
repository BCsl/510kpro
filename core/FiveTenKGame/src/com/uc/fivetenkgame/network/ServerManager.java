package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.uc.fivetenkgame.network.util.Common;

/**
 * ���������������
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
			//���յ�һ���������
			Socket socket = mServerSocket.accept();
			mFirstPlayer = new TCPServer(this, socket);
			mFirstPlayer.sendMessage(Common.PLAYER_ACCEPTED + Common.PLAYER1_NUM);
			mFirstPlayer.start();
			receiveMessage(Common.PLAYER_ACCEPTED);
			
			//���յڶ����������
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
