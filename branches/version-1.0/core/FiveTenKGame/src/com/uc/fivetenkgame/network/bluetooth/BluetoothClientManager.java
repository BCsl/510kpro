package com.uc.fivetenkgame.network.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * 客户端蓝牙网络管理类
 * 
 * @author liuzd
 *
 */
public class BluetoothClientManager extends BluetoothManager {

	private static final String LOG_CLIENT_MANAGER = "BluetoothClientManager :";
	
	private BluetoothDevice mRemoteDevice = null;
	private BluetoothSocketCommunication mReadWriteThread = null;
	
	private static BluetoothClientManager gInstance = null;
	public static BluetoothClientManager getInstance(){
		if( null == gInstance ){
			gInstance = new BluetoothClientManager();
		}
		
		return gInstance;
	}
	
	private BluetoothClientManager(){
		
	}
	
	@Override
	public void initNetwork(final String addr) {
		Thread thread = new Thread(){
							public void run(){
								//利用服务器蓝牙设备的地址获得该远程蓝牙设备
								mRemoteDevice = mBluetoothAdapter.getRemoteDevice(addr);
								try {
									//创建与服务器设备连接的socket
									BluetoothSocket socket = mRemoteDevice.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
									
									mBluetoothAdapter.cancelDiscovery();
									socket.connect();
									mReadWriteThread = new BluetoothSocketCommunication(BluetoothClientManager.this, socket);
									mReadWriteThread.start();
									
								} catch (IOException e) {
									Log.i(LOG_CLIENT_MANAGER, "connect failed!");
									e.printStackTrace();
								}
							}
						};
		thread.start();
	}

	@Override
	public void reset() {
		mReadWriteThread.release();
	}

	@Override
	public void sendMessage(String msg) {
		if( mReadWriteThread != null )
			mReadWriteThread.sendMessage(msg);
	}

}
