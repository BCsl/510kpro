package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager implements NetworkManager{
	
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
				
				socket = mServerSocket.accept();
				mSecondPlayer = new TCPServer();
				mSecondPlayer.initNetwork(socket);
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendMessage(String msg) {
		mFirstPlayer.sendMessage(msg);
		mSecondPlayer.sendMessage(msg);
	}

	@Override
	public void receiveMessage(String msg) {
		
		
	}

}
