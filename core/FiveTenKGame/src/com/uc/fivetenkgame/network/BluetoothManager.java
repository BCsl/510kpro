package com.uc.fivetenkgame.network;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;

abstract class BluetoothManager implements NetworkInterface {
	//指定某一ID的蓝牙服务
	protected static final UUID BLUETOOTH_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	protected OnReceiveMessageListener mReceiveMessageListener;
	//本地蓝牙适配器设备
	protected BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	@Override
	public abstract void sendMessage(String msg);

	@Override
	public void setOnReceiveMessage(OnReceiveMessageListener onReceiveMessage) {
		mReceiveMessageListener = onReceiveMessage;
	}
	
	public void receiveMessage(String msg) {
		mReceiveMessageListener.reveiveMessage(msg);
	}

	@Override
	public abstract void initNetwork(String addr);

	@Override
	public void sendMessage(String msg, int num) {}

	@Override
	public abstract void reset();
}
