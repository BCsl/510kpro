package com.uc.fivetenkgame.network;

import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * 网络通信接口
 * @author liuzd
 *
 */
public interface NetworkInterface {

	public void sendMessage(String msg);
	public void receiveMessage(String msg);
	public void playCards(List<Card> playCards);
}
