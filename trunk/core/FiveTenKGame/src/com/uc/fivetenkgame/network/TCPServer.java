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
public class TCPServer {
	
	private ServerManager mServerManager;
	private Socket mServerSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private byte[] mBuffer;
	
	public TCPServer(ServerManager parent, Socket aSocket){
		mServerSocket = aSocket;
		try {
			mInputStream = mServerSocket.getInputStream();
			mOutputStream = mServerSocket.getOutputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mBuffer = new byte[1024];
		mServerManager = parent;
		mThread.start();
	}
	
	private Thread mThread = new Thread(){
		@Override
		public void run() {
			
			while( true ){
				try {
					int len = mInputStream.read(mBuffer);
					System.out.println(mBuffer.toString());
					mServerManager.receiveMessage(new String(mBuffer, 0, len));
					
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
	
		}		
	};

	
	public void sendMessage(String msg){
		try {
			mOutputStream.write(msg.getBytes());
			mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void release(){
		
		mBuffer = null;
		
		try {
			mOutputStream.close();
			mInputStream.close();
			mServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
