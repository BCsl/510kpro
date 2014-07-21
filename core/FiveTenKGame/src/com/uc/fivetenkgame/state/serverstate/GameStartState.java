package com.uc.fivetenkgame.state.serverstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.view.entity.Card;

public class GameStartState extends ServerState{

	private Card []mCards;
	private final int TOTAL_CARD_NUM = 54;
	
	public GameStartState(ServerContext context){
		mServerContext = context;
		mCards = new Card[TOTAL_CARD_NUM];
	}
	
	@Override
	public void handle(String msg) {
		washCards();
		dealCards();
		sendCards();
		setFirstPlayer();
		mServerContext.setState(new WaitingState(mServerContext));
	}
	
	/**
	 * 随机洗牌
	 */
	private void washCards(){
		List<Card> cardlist = new ArrayList<Card>();
		
		for(int i = 0; i < TOTAL_CARD_NUM; ++i ){
			//牌的ID，1～54
			mCards[i] = new Card((i+1)+"");
			cardlist.add(mCards[i]);
		}

		Collections.shuffle(cardlist);
		
		int pos = 0;
		for(Card card : cardlist){
			mCards[pos++] = card;
		}
		
	}
	
	/**
	 * 发牌, 将洗好的牌分给各玩家
	 */
	private void dealCards(){
		ArrayList<Card> []playerCardList = new ArrayList[3];
		
		for(int i = 0; i < Common.TOTAL_PLAYER_NUM; ++i ){
			playerCardList[i] = new ArrayList<Card>();
		}
		
		//给各玩家添加洗好的牌
		for(int i = 0; i < TOTAL_CARD_NUM; ++i){
			playerCardList[i % 3].add(mCards[i]);
		}
		
		//服务器保存各玩家信息
		ArrayList<PlayerModel> players = new ArrayList<PlayerModel>();
		for(int i = 0; i < Common.TOTAL_PLAYER_NUM; ++i ){
			PlayerModel player = new PlayerModel();
			player.setCardList(playerCardList[i]);
			player.setPlayerNumber(i + 1);
			
			//排序
			Common.setOrder(player.getCardList());

			players.add(player);
		}
		mServerContext.setPlayerModel(players);
	}
	
	/**
	 * 发送开始游戏消息给各玩家，并发牌给各玩家
	 */
	private void sendCards(){
		ArrayList<PlayerModel> players = mServerContext.getPlayerModel();
		for(int i = 0; i < Common.TOTAL_PLAYER_NUM; ++i){
			PlayerModel player = players.get(i);
			StringBuilder sb = new StringBuilder();
			sb.append(Common.BEGIN_GAME);
			sb.append(player.getPlayerNumber() + ",");
			
			//牌号
			ArrayList<Card> cards = player.getCardList();
			for( Card card : cards){
				sb.append(card.getCardId());
				sb.append(",");
			}
			
			mServerContext.getNetworkManager().sendMessage(sb.deleteCharAt(sb.length()-1).toString(), player.getPlayerNumber());
			
		}
	}
	
	/**
	 * 设置第一个出牌的玩家
	 */
	private void setFirstPlayer(){
		Random random = new Random();
		int num = random.nextInt(Common.TOTAL_PLAYER_NUM) + 1;
		mServerContext.setCurrentPlayerNumber(num);
		mServerContext.getNetworkManager().sendMessage(Common.YOUR_TURN + num, num);
	}
}
