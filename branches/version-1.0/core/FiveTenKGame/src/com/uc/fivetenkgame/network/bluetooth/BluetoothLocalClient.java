package com.uc.fivetenkgame.network.bluetooth;

/**
 * ���ؿͻ����������������, �շ���Ϣͨ��ֱ�Ӻ�������ʵ��
 * 
 * @author liuzd
 *
 */
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
		//BluetoothServerManager.getInstance().addLocalPlayer();
	}

	@Override
	public void reset() {
	}

}
