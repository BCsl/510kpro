package com.uc.fivetenkgame.network.bluetooth;

import com.uc.fivetenkgame.network.NetworkInterface;

/**
 * ���������շ�������
 * 
 * @author liuzd
 *
 */
class BluetoothLocalCommunication implements IBasicNetwork{

	private NetworkInterface mBluetoothManager = null;
	
	private static BluetoothLocalCommunication gInstance;
	public static BluetoothLocalCommunication getInstance(){
		return gInstance;
	}
	
	public BluetoothLocalCommunication(NetworkInterface manager){
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
