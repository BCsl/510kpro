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
	private int GIAVE_UP_TIME_LIMITE;		//���ֵĽ���λ,�統ǰ��һ���3�ˣ���������2�����ֽ�����

	public WaitingState(ServerContext context) {
		mServerContext = context;
		giveUpTimes = 0;
		GIAVE_UP_TIME_LIMITE = Common.TOTAL_PLAYER_NUM-1;
	}

	@Override
	public void handle(String msg) {
		if (msg.startsWith(Common.PLAY_CARDS)) {
			// ���ȸ��µ�ǰ���������Ϣ��Ȼ���ж���Ϸ�Ƿ����
			String str[] = msg.substring(2).trim().split(",");
			List<Card> cardList = getCardList(str);
			updateRoundScore(cardList);
			updatePlayerModle(cardList);
			sendToOtherPlayer(msg.substring(2).trim());
			if (isCurrentPlayerHasNoCards())
					playerFinalRoundOver();
			if (gameIsOver()) {
				GameEndState state = new GameEndState(mServerContext);
				mServerContext.setState(state);
				state.handle(Common.GAME_END);
			} else
				callNextPlayer();
			giveUpTimes = 0;
		} else if (msg.startsWith(Common.GIVE_UP)) {
			++giveUpTimes;
			giveUpAction();
			if (giveUpTimes == GIAVE_UP_TIME_LIMITE)
				roundOver();
			callNextPlayer();
		} else if(msg.startsWith(Common.GAME_PAUSE) || msg.startsWith(Common.GAME_RESUME) ||
				msg.startsWith(Common.GAME_EXIT)){
			Log.i("waitingState!!!","*** "+msg);
			mServerContext.getNetworkManager().sendMessage(msg);
		}

	}

	/**
	 * ��ǰ��Ұ�����ȫ�����꣬Ҳ��Ϊ���ֽ���
	 * 
	 * @return
	 */
	private boolean isCurrentPlayerHasNoCards() {
		if( mServerContext.getPlayerModel()
				.get(mServerContext.getCurrentPlayerNumber() - 1)
				.getRemainCardsNum() == 0){
			GIAVE_UP_TIME_LIMITE--;
			return true;
		}
		return false;
	}

	/**
	 * ��ǰ��ҵ����һ�ֽ�����û�ƣ�,����Ӧ�üӵ������Ƶ������
	 */
	private void playerFinalRoundOver() {
		PlayerModel player = mServerContext.getPlayerModel().get(
				mServerContext.getCurrentPlayerNumber() - 1);
		player.setScore(player.getScore() + mServerContext.getRoundScore());
		mServerContext.setRoundScore(0);
		StringBuilder res = new StringBuilder();
		res.append(Common.ROUND_END);
		for (PlayerModel temp : mServerContext.getPlayerModel())
			res.append(temp.getScore() + ",");
		mServerContext.getNetworkManager().sendMessage(
				res.deleteCharAt(res.length() - 1).toString());

	}
	/**
	 * ��ͨ�ı��ֽ�����ͳ�Ʒ�������ת�������ַ�������
	 */
	private void roundOver() {
		int nextPlayer = getNextPlayerId();
		PlayerModel player = mServerContext.getPlayerModel()
				.get(nextPlayer - 1);
		player.setScore(player.getScore() + mServerContext.getRoundScore());
		mServerContext.setRoundScore(0);
		StringBuilder res = new StringBuilder();
		res.append(Common.ROUND_END);
		for (PlayerModel temp : mServerContext.getPlayerModel())
			res.append(temp.getScore() + ",");
		mServerContext.getNetworkManager().sendMessage(
				res.deleteCharAt(res.length() - 1).toString());
		
	}

	/**
	 * ת����ǰ��ҷ������Ʋ���
	 */
	private void giveUpAction() {
		mServerContext.getNetworkManager().sendMessage(
				Common.GIVE_UP + mServerContext.getCurrentPlayerNumber());
	}


	/**
	 * ���ݵ�ǰ���ƣ����±��ַ���
	 * 
	 * @param cardList
	 */
	private void updateRoundScore(List<Card> cardList) {
		int add = 0;
		for (Card temp : cardList) {
			int id = Integer.valueOf(temp.getCardId());
			if (id == 54 || id == 55)
				continue;
			switch (id % 13) {
			case 5:
				add += 5;
				break;
			case 10:
			case 0:
				add += 10;
				break;
			default:
				add += 0;
				break;
			}
		}
		mServerContext.setRoundScore(mServerContext.getRoundScore() + add);
	}

	/**
	 * �ж���Ϸ�Ƿ����
	 * 
	 * @return
	 */
	private boolean gameIsOver() {
//		int i = 0;
//		for (PlayerModel temp : mServerContext.getPlayerModel())
//			if (temp.getRemainCardsNum() == 0)
//				Log.i(TAG, "�����Ƶ��������" + (++i));
//		return i >= 2 ? true : false;
		return GIAVE_UP_TIME_LIMITE<=0;			
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
		res.append(mServerContext.getCurrentPlayerNumber() + ",");
		res.append(substring + ",");
		res.append(mServerContext.getRoundScore() + ",");
		for (PlayerModel model : mServerContext.getPlayerModel())
			res.append(model.getRemainCardsNum() + ",");
		mServerContext.getNetworkManager().sendMessage(
				Common.PLAY_END + res.substring(0, res.length() - 1));
	}

	/**
	 * ����Ϣ�л�ȡ��������
	 * 
	 * @param str
	 * @return
	 */
	private List<Card> getCardList(String[] str) {
		List<Card> list = new ArrayList<Card>(str.length);
		for (int i = 0; i < str.length; i++) {
			list.add(new Card(str[i]));
		}
		return list;
	}

	/**
	 * �жϲ�ת����һ����ҳ���
	 */
	private void callNextPlayer() {
		int nextPlayer = getNextPlayerId();
		mServerContext.setCurrentPlayerNumber(nextPlayer);
		mServerContext.getNetworkManager().sendMessage(
				Common.YOUR_TURN + nextPlayer);
	}

	private int getNextPlayerId() {
		int nextPlayer = 0;
		if (mServerContext.getCurrentPlayerNumber() == 3)
			nextPlayer = mServerContext.getPlayerModel().get(0)
					.getRemainCardsNum() != 0 ? 1 : 2;
		else if (mServerContext.getCurrentPlayerNumber() == 1)
			nextPlayer = mServerContext.getPlayerModel().get(1)
					.getRemainCardsNum() != 0 ? 2 : 3;
		else if (mServerContext.getCurrentPlayerNumber() == 2)
			nextPlayer = mServerContext.getPlayerModel().get(2)
					.getRemainCardsNum() != 0 ? 3 : 1;
		Log.i(TAG, "ת����һ����ң�" + nextPlayer);
		return nextPlayer;
	}

}
