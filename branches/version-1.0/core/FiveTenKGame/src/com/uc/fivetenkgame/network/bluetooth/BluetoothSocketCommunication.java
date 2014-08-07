package com.uc.fivetenkgame.network.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import android.bluetooth.BluetoothSocket;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.network.NetworkInterface;

/**
 * �����ײ�Socketͨ����
 * ʵ���������ݵĶ�д
 * 
 * @author liuzd
 *
 */
class BluetoothSocketCommunication extends Thread implements IBasicNetwork{
	//�����С
	private static final int BUFFER_SIZE = 1024;
	
	private NetworkInterface mBluetoothManager = null;
	private BluetoothSocket mBluetoothSocket = null;
	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	private byte[] mBuffer = null;
	
	public BluetoothSocketCommunication(NetworkInterface manager, BluetoothSocket socket){
		mBluetoothManager = manager;
		mBluetoothSocket = socket;
		
		try {
			mOutputStream = mBluetoothSocket.getOutputStream();
			mInputStream = mBluetoothSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mBuffer = new byte[BUFFER_SIZE];
	}
	
	@Override
	public void run() {
		while( mBuffer != null ){
			try {
				if( mInputStream != null  ){
					int len = mInputStream.read(mBuffer);
					//��������
					if( len > 1 && mBuffer != null ){
						String data = new String(mBuffer, 0, len);
						//�����Ϣ
						String []msg = data.split(NetworkCommon.MESSAGE_END);
						for( String m : msg)
							receiveMessage(m);
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
	}

	public void sendMessage(String msg){
		if( mOutputStream == null || !mBluetoothSocket.isConnected())
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
	
	public void receiveMessage(String msg){
		mBluetoothManager.receiveMessage(msg);
	}
	
	public void release(){
		
		mBuffer = null;
		
		try {
			if( mOutputStream != null )
				mOutputStream.close();
			if( mInputStream != null )
				mInputStream.close();
			mBluetoothSocket.close();
			
			mOutputStream = null;
			mInputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
