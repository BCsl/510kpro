package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.uc.fivetenkgame.network.ClientManager;
import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.ruleController.Rule;
import com.uc.fivetenkgame.state.InitState;
import com.uc.fivetenkgame.state.PlayerState;
import com.uc.fivetenkgame.state.State;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.EventListener;
import com.uc.fivetenkgame.view.util.IViewControler;

/**
 * 玩家类,
 * 修改：player类不再作为抽象类，与clientplayer类合并
 * @author fuyx
 *
 */
public class Player{
	protected boolean isGameOver = false;
	private Rule mRule;
	private State mState;
	//private OnReceiveMessageListener mReceiveMessage;
	
	//protected int currentPlayer;
	private PlayerModel mPlayerModel;
	private NetworkManager mNetworkManager;
	private Handler mHandler;
	//protected List<Card> currentPlayerOutList;
	protected List<Card> formerCardList;
	//protected int tableScore;
	private EventListener mEventListener = new EventListener() {
		
		@Override
		public boolean handCard(List<Card> handList) {
			if (mRule.checkCards(handList, formerCardList) == 1)
				return true;
			else
				return false;
		}
	};
	
	public void startPlay(String addr){
		setState(new InitState(gInstance, addr));
	}
	
	private IViewControler viewController;
	
	//设置规则
	public void setRule(Rule rule){
		mRule = rule;
	}
	
	//设置界面接口
	public void setIViewControler(IViewControler viewcontroler){
		viewController = viewcontroler;
	}
	
	//启动网络模块
	public void initNetwork(String addr){
		((ClientManager)mNetworkManager).initNetwork(addr);
	}
	
	public static Player gInstance;//唯一的Player实例
	
	//返回一个唯一的player实例
	public static  Player getInstance(){
		if( null == gInstance ){
			gInstance = new Player();
		}
		
		return gInstance;
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
	}
	
	public void setFormerCardList(List<Card> cardList){
		formerCardList = cardList;
	}
	
	public void setState(State state) {
		mState = state;
		mState.handle();
	}
	
	private Player(){
		mNetworkManager = ClientManager.getInstance();
	}
	
	public IViewControler getIViewControler(){
		return viewController;
	}
	
	public NetworkManager getNetworkManager(){
		return mNetworkManager;
	}
	
	public PlayerModel getPlayerModel(){
		return mPlayerModel;
	}
	
	public void setPlayerCards(String cards){
		String[] tCard = cards.split(",");
		ArrayList<Card> cardList = new ArrayList<Card>();
		for(int i=0, count=tCard.length; i<count; i++){
			cardList.add(new Card(tCard[i]));
		}
		mPlayerModel.setCardList(cardList);
	}
	
	/**
	 * 
	 * @return null, if no card to be played
	 */
	public String getCardsToBePlayed(){
		List<Card> outList = null;
		while( !(mEventListener.handCard(outList)) )
		{}
		if( outList == null )
			return null;
		StringBuffer sb = null;
		for(int i=0, count=outList.size(); i<count; i++){
			sb.append(outList.get(i).getCardId());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return new String(sb);
	}
	
	/*
	public void setCurrentPlayerOutList(String[] list){
		currentPlayerOutList.clear();
		for(int i=0, count=list.length; i<count; i++){
			currentPlayerOutList.add(new Card(list[i]));
		}
	}
	
	public List<Card> getCurrentPlayerOutList(){
		return currentPlayerOutList;
	}
	
	public void setTableScore(int score){
		tableScore = score;
	}
	
	public void setCurrentPlayer(int number){
		currentPlayer = number;
	}
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	public int getTableScore(){
		return tableScore;
	}
		
	public void setPlayNumber(String playerNumber){
		mPlayerModel.setPlayerNumber(Integer.parseInt(playerNumber));
	}
	*/
	
	public void playCards(List<Card> prePlayerCards){
		
	}
	
	/**
	 * 将玩家出的牌转换成网络数据传输格式发送
	 * 
	 * @param playCards 将要出的牌
	 */
	public void sendPlayCards(List<Card> playCards) {
		
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append(Common.PLAY_CARDS);
		for(Card card:playCards){
			strbuilder.append(card.getCardId());
			strbuilder.append(',');
		}
		
		mNetworkManager.sendMessage(strbuilder.toString());
	}
	
	/**
	 * 处理接收到的消息
	 * 
	 * @param msg 接收到的消息
	 * 
	 */
	
	/**
	 * 将从通信得到的String字符串转成card数组并存在playermodel中
	 */
	
	protected void setGameScore(int score){
		
	}
	
	protected void handCard(List<Card> cards){
		
	}
	
	protected void setCardNumber(List<Integer> cardNumber){
		
	}
	
	protected void setScroeList(List<Integer> scroeList){
		
	}
	
	
}
