package com.uc.fivetenkgame.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uc.fivetenkgame.common.NetworkCommon;

/**
 * �ͻ���TCP���緢�ͽ��������߳���
 * @author liuzd
 *
 */
public class TCPClient{
	
	private ClientManager mClientManager = null;
	private Socket mClientSocket = null;
	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	private byte[] mBuffer = null;
	private boolean flag = true;
	
	public TCPClient(ClientManager parent){
		mBuffer = new byte[1024];
		mClientManager = parent;
		mThread.start();
	}
	
	public void initNetwork(final String addr, final int port){

		//note : client not null
		try {
			mClientSocket = new Socket(addr, port);
			mOutputStream = mClientSocket.getOutputStream();
			mInputStream = mClientSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * ����������Ϣ���߳�
	 * 
	 */
	private Thread mThread = new Thread(){
		@Override
		public void run() {
			
			
			while( flag ){
				try {
					if( mInputStream != null  ){
						int len = mInputStream.read(mBuffer);
						//��������
						if( len > 1 ){
							String data = new String(mBuffer, 0, len);
							//�����Ϣ
							String []msg = data.split(NetworkCommon.MESSAGE_END);
							for( String m : msg)
								mClientManager.receiveMessage(m);
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
			msg = msg.concat(NetworkCommon.MESSAGE_END);
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
			mClientSocket.close();
			
			mOutputStream = null;
			mInputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFlag(boolean bool){
		flag = bool;
	}
}