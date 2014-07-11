package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 客户端TCP网络发送接收数据线程类
 * @author liuzd
 *
 */
public class TCPClient extends Thread {
	
	private static final int CLIENT_PORT = 8888;
	
	private ClientManager mClientManager;
	private Socket mClientSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private byte[] mBuffer;
	
	
	/*public static TCPClient gInstance;
	public static TCPClient getInstance(){
		if( null == gInstance ){
			gInstance = new TCPClient();
		}
		
		return gInstance;
	};
	
	private TCPClient(){
		mBuffer = new byte[1024];
	}*/
	
	public void initNetwork(ClientManager parent, String addr){
		//note : client not null
		try {
			mClientSocket = new Socket(addr, CLIENT_PORT);
			mOutputStream = mClientSocket.getOutputStream();
			mInputStream = mClientSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mBuffer = new byte[1024];
		mClientManager = parent;
	}

	@Override
	public void run() {
		
		try {
			mInputStream.read(mBuffer);
			mClientManager.receiveMessage(new String(mBuffer));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String msg){
		try {
			mOutputStream.write(msg.getBytes());
			mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
