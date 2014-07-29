package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uc.fivetenkgame.network.util.Common;

/**
 * ������TCP���緢�ͽ��������߳���
 * @author liuzd
 *
 */
public class TCPServer {
	
	private ServerManager mServerManager;
	private Socket mServerSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private byte[] mBuffer;
	private int mNumber = 0;
	
	public TCPServer(ServerManager parent, Socket aSocket, int number){
		mNumber = number;
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
			
			while( mServerManager.getFlag(mNumber-1) ){
				try {
					if( mInputStream != null  ){
						int len = mInputStream.read(mBuffer);
						//��������
						if( len > 1 ){
							String data = new String(mBuffer, 0, len);
							//�����Ϣ
							String []msg = data.split(Common.MESSAGE_END);
							for( String m : msg)
								mServerManager.receiveMessage(m);
						}
						//�����ж�
						else if( len < 0 ){
							release();
							return;
						}
					}
					else{
						Thread.sleep(30);
					}
					
				} catch (SocketException e){
					release();
					return;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}
			release();
		}		
	};

	
	public void sendMessage(String msg){
		if( mOutputStream == null )
			return ;
		
		try {
			//������Ϣβ
			msg = msg.concat(Common.MESSAGE_END);
			mOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
			//mOutputStream.flush();
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
			
			mOutputStream = null;
			mInputStream = null;
			mServerManager.removePlayer(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
