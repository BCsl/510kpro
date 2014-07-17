package com.uc.fivetenkgame.state.serverstate;

import java.util.Random;

import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.view.entity.Card;

public class GameStartState extends ServerState{

	private Card []mCards;
	private final int TOTAL_CARD_NUM = 54;
	
	public GameStartState(ServerContext context){
		mServerContext = context;
		mCards = new Card[TOTAL_CARD_NUM];
		washCards();
		dealCards();
		mServerContext.setState(new WaitingState(mServerContext));
	}
	
	@Override
	public void handle(String msg) {
		
	}
	
	/**
	 * ���ϴ��
	 */
	private void washCards(){
		
		for(int i = 0; i < TOTAL_CARD_NUM; ++i ){
			//�Ƶ�ID��1��54
			mCards[i] = new Card((i+1)+"");
		}
		
		//���������������100��
		for(int i = 0; i < 100; ++i){
			Random random = new Random();
			int aPos = random.nextInt(TOTAL_CARD_NUM);
			int bPos = random.nextInt(TOTAL_CARD_NUM);
			Card tCard = mCards[aPos];
			mCards[aPos] = mCards[bPos];
			mCards[bPos] = tCard;
		}
	}
	
	/**
	 * ����, ��ϴ�õ��Ʒַ��������
	 */
	private void dealCards(){
		
	}
}
