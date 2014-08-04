package com.uc.fivetenkgame.network;

/**
 * 蓝牙本地收发数据类
 * 
 * @author liuzd
 *
 */
public class BluetoothLocalCommunication implements IBasicNetwork{

	private BluetoothManager mBluetoothManager = null;
	
	private static BluetoothLocalCommunication gInstance;
	public static BluetoothLocalCommunication getInstance(){
		return gInstance;
	}
	
	public BluetoothLocalCommunication(BluetoothManager manager){
		mBluetoothManager = manager;
		gInstance = this;
	}
	
	@Override
	public void sendMessage(String msg) {
		BluetoothLocalClient.getInstance().receiveMessage(msg);
	}

	public void receiveMessage(String msg){
		mBluetoothManager.receiveMessage(msg);
	}
	
	@Override
	public void release() {
	}

}
