package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.ruleController.BasicRule;
import com.uc.fivetenkgame.ruleController.Rule;
import com.uc.fivetenkgame.state.State;
import com.uc.fivetenkgame.state.playerstate.InitState;
import com.uc.fivetenkgame.state.playerstate.PlayerState;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;
import com.uc.fivetenkgame.view.util.IViewControler;

/**
 * �����, �޸ģ�player�಻����Ϊ�����࣬��clientplayer��ϲ�
 * 
 * @author fuyx
 * 
 */
@SuppressLint("UseValueOf")
public class Player implements PlayerContext {
	// protected boolean isFirst = false;
	private Rule mRule;
	private State mState;
	private boolean doneHandCard = false;
	private IViewControler viewController;
	private OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		@Override
		public void reveiveMessage(String msg) {
			Log.i("!!!player","... "+msg);
			if (msg.startsWith(Common.GAME_PAUSE) || msg.startsWith(Common.GAME_RESUME)
					|| msg.startsWith(Common.GAME_EXIT)){
				mHandler.obtainMessage(Common.GAME_STATE_CHANGE, msg)
						.sendToTarget(); //��activity����
				Log.i("!!!player","msg sent to target");
			}else{
				mState.handle(msg); //��״̬������
			}
		}
	};
	// protected int currentPlayer;
	private PlayerModel mPlayerModel;
	private NetworkInterface mNetworkManager;
	private Handler mHandler;
	// protected List<Card> currentPlayerOutList;
	private List<Card> formerCardList;
	private List<Card> mHandList;
	// protected int tableScore;
	private EventListener mEventListener = new EventListener() {
		@Override
		public boolean handCard(List<Card> handList) {
			mHandList = new ArrayList<Card>();
			if (formerCardList == null) {
				// ��һ�γ���
				if (handList == null) {
					// ��һ����Ҳ��ܷ�������
					viewController.handCardFailed();
					return false;
				} else if (mRule.firstPlayCards(handList) == 1) {
					mHandList.addAll(handList);
					mPlayerModel.getCardList().removeAll(handList);
					setDoneHandCards(true);
					Log.i("��ǰ������",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
					return true;
				} else {
					// ����ʲô�ƶ�����ͳ���
					viewController.handCardFailed();
					return false;
				}
			} else {
				Log.i("formerCardList", formerCardList.toString());
				if (handList == null) {
					mHandList = null;
					setDoneHandCards(true);
					Log.i("��ǰ������",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
					return true;
				} else if (handList.size() == 0) {
					viewController.handCardFailed();
					return false;
				} else if (mRule.checkCards(handList, formerCardList) == 1) {
					mHandList.addAll(handList);

					mPlayerModel.getCardList().removeAll(handList);

					Common.setOrder(mHandList);

					setDoneHandCards(true);
					Log.i("��ǰ������",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
					return true;
				} else {
					// ����ʲô�ƶ�����ͳ���
					viewController.handCardFailed();
					return false;
				}
			}
			// else{
			// //ѡ�ƴ󲻹��ϼ�
			// viewController.handCardFailed();
			// return true;
			// }
		}
	};

	public void timeOutAction() {
		setState(null);
	}

	public void startPlay(String addr) {
		setState(new InitState(gInstance));
		handle(addr);
	}

	public void setViewControler(IViewControler viewControler) {
		this.viewController = viewControler;
	}

	// ���ù���
	private void setRule(Rule rule) {
		mRule = rule;
	}

	// ��������ģ��
	public void initNetwork(String addr) {
		mNetworkManager.initNetwork(addr);
	}

	public static Player gInstance;// Ψһ��Playerʵ��

	// ����һ��Ψһ��playerʵ��
	public static Player getInstance() {
		if (null == gInstance) {
			gInstance = new Player();
		}

		return gInstance;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void setState(PlayerState state) {
		mState = state;
		// mState.handle();
	}

	private Player() {
		mNetworkManager = ClientManager.getInstance();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		mPlayerModel = new PlayerModel();

		setRule(new BasicRule());
		// mPlayerModel.setCardList(null);
		// mPlayerModel.setPlayerNumber(-1);
		// mPlayerModel.setScore(0);
	}

	public void setInitPlayerCards(String cards) {
		Log.i("��ʼ����:", cards);
		String[] tCard = cards.split(",");
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (int i = 0, count = tCard.length; i < count; i++) {
			cardList.add(new Card(tCard[i]));
		}
		mPlayerModel.setCardList(cardList);
	}

	/**
	 * 
	 * @return null, if no card to be played
	 */
	public String getCardsToBePlayed() {
		if (formerCardList == null) {
			if (mHandList == null) {
				Log.i("��һ�����Ƶ��˲��ܷ���", "��Ӧ�õ�����");
			}
		}
		if (mHandList == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = mHandList.size(); i < count; i++) {
			sb.append(mHandList.get(i).getCardId());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return new String(sb);
	}

	@Override
	public void sendMsg(String msg) {
		mNetworkManager.sendMessage(msg);
	}

	@SuppressLint("UseValueOf")
	@Override
	public void roundEndAction(String[] playerScore) {
		List<Integer> score = new ArrayList<Integer>();
		score.add(new Integer(playerScore[0]));
		score.add(new Integer(playerScore[1]));
		score.add(new Integer(playerScore[2]));
		formerCardList = null;
		mPlayerModel.setScore(Integer.parseInt(playerScore[mPlayerModel
				.getPlayerNumber() - 1]));
		viewController.setScroeList(score);
		viewController.roundOver();
	}

	@Override
	public void playCardsEndAction(String[] outList, String playerNumber,
			String tableScore, String[] remainCards) {
		if (outList == null)
			return;

		// ����outList
		List<Card> cardList = new ArrayList<Card>();
		for (int i = 0, count = outList.length; i < count; i++) {
			cardList.add(new Card(outList[i]));
		}
		viewController.setPlayersOutList(Integer.parseInt(playerNumber),
				cardList);
		if (cardList != null)
			formerCardList = cardList;

		// ����ʣ������
		List<Integer> cardScore = new ArrayList<Integer>();
		cardScore.add(Integer.valueOf(remainCards[0]));
		cardScore.add(Integer.valueOf(remainCards[1]));
		cardScore.add(Integer.valueOf(remainCards[2]));
		viewController.setCardNumber(cardScore);

		// ���õ�ǰ����
		viewController.setGameScore(Integer.parseInt(tableScore));
	}

	@Override
	public void handle(String msg) {
		mState.handle(msg);
	}

	@Override
	public void setPlayerNumber(int playerNumber) {
		mPlayerModel.setPlayerNumber(playerNumber);
	}

	@Override
	public int getPlayerNumber() {
		//Log.i("getPlayerNumber:",String.valueOf(mPlayerModel.getPlayerNumber()));
		return mPlayerModel.getPlayerNumber();
	}

	@Override
	public void gameOver(String[] str) {
		// viewController.gameOver(playerId);
		mHandler.obtainMessage(Common.END_GAME,str)
				.sendToTarget();
	}

	public void setEventListener() {
		viewController.setEventListener(mEventListener);
	}

	/**
	 * ��ʼ����Ϸ����
	 * 
	 */
	public void initView() {
		viewController.setCards(mPlayerModel.getCardList());
		// ʣ������
		List<Integer> leftCardsNum = new ArrayList<Integer>();
		leftCardsNum.add(new Integer(18));
		leftCardsNum.add(new Integer(18));
		leftCardsNum.add(new Integer(18));
		viewController.setCardNumber(leftCardsNum);
	}

	@Override
	public boolean doneHandCards() {
		return doneHandCard;
	}

	@Override
	public void setDoneHandCards(boolean flag) {
		// TODO Auto-generated method stub
		doneHandCard = flag;
	}

	@Override
	public void setMyTurn(boolean flag) {
		Log.i("setMyTurn", String.valueOf(flag));
		while (viewController == null) {
		}
		viewController.setMyTurn(flag);
	}

	@Override
	public boolean isFirstPlayer() {
		if (formerCardList == null)
			return true;
		else
			return false;
	}

	@Override
	public void ReStartGame() {
	}

	@Override
	public void setCurrentPlayer(int palyerId) {
		while (viewController == null)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		viewController.setCurrentPlayer(palyerId);
	}

	@Override
	public boolean hasCard() {
		if (mPlayerModel.getRemainCardsNum() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void playerGiveUp(int playerId) {
		List<Card> cardList = new ArrayList<Card>();
		cardList.add(new Card(Card.CARD_PASS_ID));
		viewController.setPlayersOutList(playerId, cardList);
	}

	@Override
	public void resetPlayer() {
		mPlayerModel = new PlayerModel();
		mNetworkManager.reset();
	}

}
