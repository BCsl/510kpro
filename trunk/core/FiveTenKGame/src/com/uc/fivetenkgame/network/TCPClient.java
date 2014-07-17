package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * �ͻ���TCP���緢�ͽ��������߳���
 * @author liuzd
 *
 */
public class TCPClient{
	
	private ClientManager mClientManager;
	private Socket mClientSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private byte[] mBuffer;
	
	public TCPClient(ClientManager parent){
		mBuffer = new byte[1024];
		mClientManager = parent;
	}
	
	public void initNetwork(String addr, int port){
		//note : client not null
		try {
			mClientSocket = new Socket(addr, port);
			mOutputStream = mClientSocket.getOutputStream();
			mInputStream = mClientSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mThread.start();
	}

	/**
	 * ����������Ϣ���߳�
	 * 
	 */
	private Thread mThread = new Thread(){
		@Override
		public void run() {
			while( true ){
				try {
					
					int len = mInputStream.read(mBuffer);
					mClientManager.receiveMessage(new String(mBuffer, 0, len));
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
			mClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
