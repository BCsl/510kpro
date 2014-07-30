package com.uc.fivetenkgame.state.serverstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.util.OredrUtil;
import com.uc.fivetenkgame.view.entity.Card;

/**
 * ��Ϸ��ʼ״̬ ������ϴ�ơ����ơ�ȷ�����ȳ������
 * 
 * @author liuzd
 *
 */
public class GameStartState extends ServerState {

	private Card[] mCards;
	private final int TOTAL_CARD_NUM = 54;
	private final int CARD_PAIRS = 2;

	public GameStartState(ServerContext context) {
		mServerContext = context;
		mCards = new Card[TOTAL_CARD_NUM * CARD_PAIRS];
	}

	@Override
	public void handle(String msg) {
		/*
		 * new Handler().postDelayed(new Runnable() { public void run() {
		 * 
		 * } }, 500);
		 */
		washCards();
		dealCards();
		sendCards();
		setFirstPlayer();
		mServerContext.setState(new WaitingState(mServerContext));
	}

	/**
	 * ���ϴ��
	 */
	private void washCards() {
		List<Card> cardlist = new ArrayList<Card>();
		for (int j = 0; j < CARD_PAIRS; j++)
			for (int i = 0; i < TOTAL_CARD_NUM; ++i) {
				// �Ƶ�ID��1��54
				mCards[i + j * TOTAL_CARD_NUM] = new Card((i + 1) + "");

				cardlist.add(mCards[i]);
			}

		Collections.shuffle(cardlist);

		int pos = 0;
		for (Card card : cardlist) {
			mCards[pos++] = card;
		}

	}

	/**
	 * ����, ��ϴ�õ��Ʒָ������
	 */
	private void dealCards() {
		ArrayList<Card>[] playerCardList = new ArrayList[3];

		for (int i = 0; i < NetworkCommon.TOTAL_PLAYER_NUM; ++i) {
			playerCardList[i] = new ArrayList<Card>();
		}

		// ����������ϴ�õ���
		for (int i = 0; i < TOTAL_CARD_NUM * CARD_PAIRS; ++i) {
			playerCardList[i % 3].add(mCards[i]);
		}

		// ����������������Ϣ
		ArrayList<PlayerModel> players = new ArrayList<PlayerModel>();
		for (int i = 0; i < NetworkCommon.TOTAL_PLAYER_NUM; ++i) {
			PlayerModel player = new PlayerModel();
			player.setCardList(playerCardList[i]);
			player.setPlayerNumber(i + 1);
			// ����
			OredrUtil.setOrder(player.getCardList());
			players.add(player);
		}
		mServerContext.setPlayerModel(players);
	}

	/**
	 * ���Ϳ�ʼ��Ϸ��Ϣ������ң������Ƹ������
	 */
	private void sendCards() {
		ArrayList<PlayerModel> players = mServerContext.getPlayerModel();
		for (int i = 0; i < NetworkCommon.TOTAL_PLAYER_NUM; ++i) {
			PlayerModel player = players.get(i);
			StringBuilder sb = new StringBuilder();
			sb.append(NetworkCommon.BEGIN_GAME);
			sb.append(player.getPlayerNumber() + ",");

			// �ƺ�
			ArrayList<Card> cards = player.getCardList();
			for (Card card : cards) {
				sb.append(card.getCardId());
				sb.append(",");
			}

			mServerContext.getNetworkManager().sendMessage(
					sb.deleteCharAt(sb.length() - 1).toString());

		}
	}

	/**
	 * ���õ�һ�����Ƶ����
	 */
	private void setFirstPlayer() {
		Random random = new Random();
		final int num = random.nextInt(NetworkCommon.TOTAL_PLAYER_NUM) + 1;
		mServerContext.setCurrentPlayerNumber(num);
		// Log.i("first time to call Next Player", "wait a second");
		// for (int i = 0; i < 1000000000; i++){}
		// Log.i("first time to call Next Player", "end waiting");
		Timer mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mServerContext.getNetworkManager().sendMessage(
						NetworkCommon.YOUR_TURN + num);
			}
		}, 1000);
	}
}
