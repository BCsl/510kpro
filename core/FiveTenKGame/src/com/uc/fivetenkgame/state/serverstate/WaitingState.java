package com.uc.fivetenkgame.state.serverstate;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         ����4:59:13 2014-7-17
 */
public class WaitingState extends ServerState {
	private String TAG = "WaitingState";
	private int giveUpTimes;

	public WaitingState(ServerContext context) {
		mServerContext = context;
		giveUpTimes = 0;
	}

	@Override
	public void handle(String msg) {
		Log.i(TAG, msg);
		if (msg.startsWith(Common.PLAY_CARDS)) {
			// ���ȸ��µ�ǰ���������Ϣ��Ȼ���ж���Ϸ�Ƿ����
			String str[] = msg.substring(2).split(",");
			List<Card> cardList = getCardList(str);
			updateRoundScore(cardList);
			updatePlayerModle(cardList);
			sendToOtherPlayer(msg.substring(2));
			if (gameIsOver())
				mServerContext.setState(new GameEndState(mServerContext));
			else
				nextPlayer();
			giveUpTimes = 0;
		} else if (msg.startsWith(Common.GIVE_UP)) {
			giveUpTimes++;
			if (giveUpTimes == 2) {
				roundOver();
			}
			nextPlayer();
		}

	}

	/**
	 * ���ֽ�����ͳ�Ʒ�������ת�������ַ�������
	 */
	private void roundOver() {
		int nextPlayer = 0;
		if(mServerContext.getCurrentPlayerNumber()==3)
			nextPlayer = 1;
		else
			nextPlayer = mServerContext.getCurrentPlayerNumber() + 1;
		
		PlayerModel player = mServerContext.getPlayerModel().get(nextPlayer - 1);
		player.setScore(player.getScore() + mServerContext.getRoundScore());
		mServerContext.setRoundScore(0);
		StringBuilder res = new StringBuilder();
		res.append(Common.ROUND_END);
		for (PlayerModel temp : mServerContext.getPlayerModel())
			res.append(temp.getScore());
		mServerContext.getNetworkManager().sendMessage(res.toString());
	}

	/**
	 * ���ݵ�ǰ���ƣ����±��ַ���
	 * 
	 * @param cardList
	 */
	private void updateRoundScore(List<Card> cardList) {
		int add = 0;
		for (Card temp : cardList) {
			switch (Integer.valueOf(temp.getCardId()) % 13) {
			case 5:
				add = 5;
				break;
			case 10:
				add = 10;
				break;
			case 0:
				add = 13;
				break;
			}
			mServerContext
					.setRoundScore(mServerContext.getRoundScore() + add);
		}
	}

	/**
	 * �ж���Ϸ�Ƿ����
	 * 
	 * @return
	 */
	private boolean gameIsOver() {
		int i = 0;
		for (PlayerModel temp : mServerContext.getPlayerModel())
			if (temp.getRemainCardsNum() == 0)
				i++;
		return i >= 2 ? true : false;
	}

	/**
	 * ���µ�ǰ�����û��ĸ�����Ϣ,����ӵ�е��ƣ�ӵ���Ƶ�����
	 * 
	 * @param cardList
	 */
	private void updatePlayerModle(List<Card> cardList) {

		PlayerModel model = mServerContext.getPlayerModel().get(
				mServerContext.getCurrentPlayerNumber() - 1);
		model.getCardList().removeAll(cardList);
	}

	/**
	 * ������Ϣ��֪ͨ������ҵ�ǰ��ҳ��Ƶ���Ϣ�����ַ��������������
	 */
	private void sendToOtherPlayer(String substring) {
		StringBuilder res = new StringBuilder();
		res.append(Common.PLAY_END);
		res.append(substring);
		res.append(mServerContext.getRoundScore());
		for (PlayerModel model : mServerContext.getPlayerModel())
			res.append(model.getRemainCardsNum());
		Log.i(TAG, res.toString());
		mServerContext.getNetworkManager().sendMessage(Common.PLAY_END + res);
	}

	/**
	 * ����Ϣ�л�ȡ��������
	 * 
	 * @param str
	 * @return
	 */
	private List<Card> getCardList(String[] str) {
		List<Card> list = new ArrayList<Card>(str.length - 1);
		for (int i = 1; i < str.length; i++)
			list.add(new Card(str[i]));
		return list;
	}

	/**
	 * �жϲ�ת����һ����ҳ���
	 */
	private void nextPlayer() {
		int nextPlayer = 0;
		if(mServerContext.getCurrentPlayerNumber()==3)
			nextPlayer = 1;
		else
			nextPlayer = mServerContext.getCurrentPlayerNumber() + 1;
		
		mServerContext.getNetworkManager().sendMessage(
				Common.YOUR_TURN + nextPlayer);
		mServerContext.setCurrentPlayerNumber(nextPlayer);
	}

}
