package com.uc.fivetenkgame.network.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.network.NetworkInterface;

/**
 * Socket通信线程类
 * 实现Socket数据的读写
 * 
 * @author liuzd
 *
 */
class SocketCommunicationThread extends Thread {

	private NetworkInterface mManager = null;
	private Socket mSocket = null;
	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	private byte[] mBuffer = null;

	public SocketCommunicationThread(NetworkInterface parent, Socket socket){
		mManager = parent;
		mSocket = socket;
		mBuffer = new byte[1024];
		
		try {
			mOutputStream = mSocket.getOutputStream();
			mInputStream = mSocket.getInputStream();			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void run() {
		while( true ){
			try {
				if( mInputStream != null  ){
					int len = mInputStream.read(mBuffer);
					//读到数据
					if( len > 1 && mBuffer != null ){
						String data = new String(mBuffer, 0, len);
						//拆分消息
						String []msgs = data.split(NetworkCommon.MESSAGE_END);
						for( String m : msgs)
							receiveMessage(m);
					}
					//链接中断
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
	}
	
	public void sendMessage(String msg){
		if( mOutputStream == null || mSocket.isClosed())
			return ;
		
		try {
			//加上消息尾
			msg = msg.concat(NetworkCommon.MESSAGE_END);
			mOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
			//mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveMessage(String msg){
		mManager.receiveMessage(msg);
	}
	
	public void release(){
		
		mBuffer = null;
		
		try {
			if( mOutputStream != null )
				mOutputStream.close();
			if( mInputStream != null )
				mInputStream.close();
			if( !mSocket.isClosed() )
				mSocket.close();
			
			mOutputStream = null;
			mInputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
