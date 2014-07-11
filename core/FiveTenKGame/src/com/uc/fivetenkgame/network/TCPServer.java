package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 服务器TCP网络发送接收数据线程类
 * @author liuzd
 *
 */
public class TCPServer extends Thread {
	
	private Socket mServerSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private byte[] mBuffer;
	
	/*
	public static TCPServer gInstance;
	public static TCPServer getInstance(){
		if( null == gInstance ){
			gInstance = new TCPServer();
		}
		
		return gInstance;
	}
	
	
	private TCPServer(){
		
	}
	*/
	
	public void initNetwork(Socket aSocket){
		mServerSocket = aSocket;
		try {
			mInputStream = mServerSocket.getInputStream();
			mOutputStream = mServerSocket.getOutputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mBuffer = new byte[1024];
	}

	@Override
	public void run() {
		try {
			mInputStream.read(mBuffer);
			System.out.println(mBuffer.toString());
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
