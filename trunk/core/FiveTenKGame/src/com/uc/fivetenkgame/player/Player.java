package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.os.Handler;
import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.ruleController.Rule;
import com.uc.fivetenkgame.state.State;
import com.uc.fivetenkgame.state.playerstate.PlayerState;
import com.uc.fivetenkgame.state.playerstate.WaitForMsgState;
import com.uc.fivetenkgame.state.serverstate.InitState;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;
import com.uc.fivetenkgame.view.util.IViewControler;

/**
 * 玩家类, 修改：player类不再作为抽象类，与clientplayer类合并
 * 
 * @author fuyx
 *
 */
@SuppressLint("UseValueOf")
public class Player implements PlayerContext {
	protected boolean isGameOver = false;
	private Rule mRule;
	private State mState;
	private IViewControler viewController;
	private OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		@Override
		public void reveiveMessage(String msg) {
			mState.handle(msg);
		}
	};

	// protected int currentPlayer;
	private PlayerModel mPlayerModel;
	private NetworkInterface mNetworkManager;
	private Handler mHandler;
	// protected List<Card> currentPlayerOutList;
	protected List<Card> formerCardList;
	// protected int tableScore;
	private EventListener mEventListener = new EventListener() {

		@Override
		public boolean handCard(List<Card> handList) {
			if (mRule.checkCards(handList, formerCardList) == 1)
				return true;
			else
				return false;
		}
	};

	public void startPlay(String addr) {
		// setState(new InitState(gInstance, addr));
	}

	// 设置规则
	private void setRule(Rule rule) {
		mRule = rule;
	}

	// 启动网络模块
	public void initNetwork(String addr) {
		((ClientManager) mNetworkManager).initNetwork(addr);
	}

	public static Player gInstance;// 唯一的Player实例

	// 返回一个唯一的player实例
	public static Player getInstance() {
		if (null == gInstance) {
			gInstance = new Player();
		}

		return gInstance;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void setState(PlayerState state) {
		mState = state;
		// mState.handle();
	}

	private Player() {
		mNetworkManager = ClientManager.getInstance();
	}

	public void setInitPlayerCards(String cards) {
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
		List<Card> outList = null;
		while (!(mEventListener.handCard(outList))) {
			viewController.handCardFailed();
		}
		if (outList == null)
			return null;
		StringBuffer sb = null;
		for (int i = 0, count = outList.size(); i < count; i++) {
			sb.append(outList.get(i).getCardId());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return new String(sb);
	}

	/*
	 * public void setCurrentPlayerOutList(String[] list){
	 * currentPlayerOutList.clear(); for(int i=0, count=list.length; i<count;
	 * i++){ currentPlayerOutList.add(new Card(list[i])); } }
	 * 
	 * public List<Card> getCurrentPlayerOutList(){ return currentPlayerOutList;
	 * }
	 * 
	 * public void setTableScore(int score){ tableScore = score; }
	 * 
	 * public void setCurrentPlayer(int number){ currentPlayer = number; }
	 * 
	 * public int getCurrentPlayer(){ return currentPlayer; }
	 * 
	 * public int getTableScore(){ return tableScore; }
	 * 
	 * public void setPlayNumber(String playerNumber){
	 * mPlayerModel.setPlayerNumber(Integer.parseInt(playerNumber)); }
	 */

	@Override
	public void sendMsg(String msg) {
		mNetworkManager.sendMessage(msg);
	}

	@SuppressLint("UseValueOf")
	@Override
	public void roundEndAction(String[] playerScore) {
		List<Integer> score = null;
		score.add(new Integer(playerScore[0]));
		score.add(new Integer(playerScore[1]));
		score.add(new Integer(playerScore[2]));
		viewController.setScroeList(score);
	}

	@Override
	public void playCardsEndAction(String[] outList, String playerNumber,
			String tableScore, String[] remainCards) {
		// 设置outList
		List<Card> cardList = null;
		for (int i = 0, count = remainCards.length; i < count; i++) {
			cardList.add(new Card(outList[i]));
		}
		viewController.setPlayersOutList(Integer.parseInt(playerNumber),
				cardList);
		formerCardList = cardList;

		// 设置剩余牌数
		List<Integer> cardScore = null;
		cardScore.add(Integer.valueOf(remainCards[0]));
		cardScore.add(Integer.valueOf(remainCards[1]));
		cardScore.add(Integer.valueOf(remainCards[2]));
		viewController.setCardNumber(cardScore);

		// 设置当前分数
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
		return mPlayerModel.getPlayerNumber();
	}

	@Override
	public void gameOver(int playerId) {
		viewController.gameOver(playerId);
	}

}
