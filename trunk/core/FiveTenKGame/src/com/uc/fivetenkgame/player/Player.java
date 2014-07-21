package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkInterface;
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
 * 玩家类, 修改：player类不再作为抽象类，与clientplayer类合并
 * 
 * @author fuyx
 *
 */
@SuppressLint("UseValueOf")
public class Player implements PlayerContext {
	protected boolean isFirst = false;
	private Rule mRule;
	private State mState;
	private boolean doneHandCard=false;
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
	private List<Card> mHandList = new ArrayList<Card>();
	// protected int tableScore;
	protected EventListener mEventListener = new EventListener() {
		@Override
		public boolean handCard(List<Card> handList) {
			
			mHandList=new ArrayList<Card>();
			if(handList == null){
				mHandList=null;
				if(isFirst){
					//第一个玩家不能放弃出牌
					viewController.handCardFailed();
					return false;
				}
				setDoneHandCards(true);
				return true;
			}else if(handList.size() == 0){
				viewController.handCardFailed();
				return false;
			}else if (mRule.checkCards(handList, formerCardList,isFirst) == 1){
				mHandList.addAll(handList);
				setDoneHandCards(true);
				return true;
			}
			else{
				//不能什么牌都不点就出牌
				viewController.handCardFailed();
				return false;
			}
//			else{
//				//选牌大不过上家
//				viewController.handCardFailed();
//				return true;
//			}	
	}
	};
	
	public void startPlay(String addr) {
		setState(new InitState(gInstance));
		handle(addr);
	}
	
	public void setViewControler(IViewControler viewControler){
		this.viewController=viewControler;
	}
		
	// 设置规则
	private void setRule(Rule rule) {
		mRule = rule;
	}

	// 启动网络模块
	public void initNetwork(String addr) {
		mNetworkManager.initNetwork(addr);
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
	
	public Handler getHandler(){
		return mHandler;
	}
	
	public void setState(PlayerState state) {
		mState = state;
		// mState.handle();
	}

	public Player() {
		mNetworkManager = ClientManager.getInstance();
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		mPlayerModel = new PlayerModel();
		
		mRule = new BasicRule();
		//mPlayerModel.setCardList(null);
		//mPlayerModel.setPlayerNumber(-1);
		//mPlayerModel.setScore(0);
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
		if(mHandList == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = mHandList.size(); i < count; i++) {
			sb.append(mHandList.get(i).getCardId());
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
		List<Integer> score = new ArrayList<Integer>();
		score.add(new Integer(playerScore[0]));
		score.add(new Integer(playerScore[1]));
		score.add(new Integer(playerScore[2]));
		viewController.setScroeList(score);
		viewController.roundOver();
	}

	@Override
	public void playCardsEndAction(String[] outList, String playerNumber,
			String tableScore, String[] remainCards) {
		if(outList == null)
			return;
		
		// 设置outList
		List<Card> cardList = new ArrayList<Card>();
		for (int i = 0, count = outList.length; i < count; i++) {
			cardList.add(new Card(outList[i]));
		}
		viewController.setPlayersOutList(Integer.parseInt(playerNumber),
				cardList);
		if(cardList != null)
			formerCardList = cardList;

		// 设置剩余牌数
		List<Integer> cardScore = new ArrayList<Integer>();
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

	public void setEventListener(){
		viewController.setEventListener(mEventListener);
	}
	
	/**
	 * 初始化游戏界面
	 * 
	 */
	public void initView(){
		viewController.setCards(mPlayerModel.getCardList());
		//剩余牌数
		List<Integer> leftCardsNum = new ArrayList<Integer>();
		leftCardsNum.add(new Integer(18));
		leftCardsNum.add(new Integer(18));
		leftCardsNum.add(new Integer(18));
		viewController.setCardNumber(leftCardsNum);
	}

	@Override
	public void setFirstPlayer() {
		isFirst = true;
	}

	@Override
	public boolean doneHandCards() {
		return doneHandCard;
	}

	@Override
	public void setDoneHandCards(boolean flag) {
		// TODO Auto-generated method stub
		doneHandCard=flag;
	}

	@Override
	public void setMyTurn(boolean flag) {
		while(viewController==null){}
		viewController.setMyTurn(flag);
	}

	}
