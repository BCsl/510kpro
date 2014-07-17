package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * player数据模型
 * 
 * @author liuzd, fuyx
 *
 */

public class PlayerModel {
	
	private ArrayList<Card> mCardList;
	private int mScore;
	private int mPlayerNumber;//玩家序号
	
	public PlayerModel(){
		mCardList = new ArrayList<Card>();
		mScore = 0;
	}
	
	public int getScore(){
		return mScore;
	}
	
	public ArrayList<Card> getCardList(){
		return mCardList;
	}
	
	public int getRemainCardsNum(){
		return mCardList.size();
	}
	
	public int getPlayerNumber(){
		return mPlayerNumber;
	}
	
	public void setCardList(ArrayList<Card> cardList){
		mCardList = cardList;
	}
	
	public void setScore(int score){
		mScore = score;
	}

	public void setPlayerNumber(int playerNumber){
		mPlayerNumber = playerNumber;
	}
}
