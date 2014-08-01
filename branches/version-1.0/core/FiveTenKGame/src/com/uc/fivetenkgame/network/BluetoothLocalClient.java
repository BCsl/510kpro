package com.uc.fivetenkgame.network;

public class BluetoothLocalClient extends BluetoothManager{

	private static BluetoothLocalClient gInstance = null;
	public static BluetoothLocalClient getInstance(){
		
		if( null == gInstance ){
			gInstance = new BluetoothLocalClient();
		}
		
		return gInstance;
	}
	
	private BluetoothLocalClient(){
		
	}
	
	@Override
	public void sendMessage(String msg) {
		BluetoothLocalCommunication.getInstance().receiveMessage(msg);
	}

	@Override
	public void initNetwork(String addr) {
		
	}

	@Override
	public void reset() {
	}

}
