package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 服务器网络管理类
 * @author liuzd
 *
 */
public class ServerManager implements NetworkInterface{
	
	private static final int SERVER_PORT = 8888;
	
	private ServerSocket mServerSocket;
	
	private TCPServer mFirstPlayer;
	private TCPServer mSecondPlayer;
	
	public static ServerManager gInstance;
	public static ServerManager getInstance(){
		if( null == gInstance ){
			gInstance = new ServerManager();
		}
		
		return gInstance;
	}
	
	private ServerManager(){
		try {
			mServerSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startListen(){
		
		try {
			Socket socket = mServerSocket.accept();
			mFirstPlayer = new TCPServer();
			mFirstPlayer.initNetwork(socket);
			mFirstPlayer.sendMessage("1#2");
			
			socket = mServerSocket.accept();
			mSecondPlayer = new TCPServer();
			mSecondPlayer.initNetwork(socket);
			mSecondPlayer.sendMessage("1#3");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		mFirstPlayer.sendMessage(msg);
		mSecondPlayer.sendMessage(msg);
	}

	public void receiveMessage(String msg) {
		
		
	}

	@Override
	public void playCards(List<Card> playCards) {
		
	}

	
}
