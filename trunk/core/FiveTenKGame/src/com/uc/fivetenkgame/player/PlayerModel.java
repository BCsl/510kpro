package com.uc.fivetenkgame.player;

import java.util.ArrayList;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * player数据模型
 * 
 * @author liuzd
 *
 */

public class PlayerModel {
	
	private ArrayList<Card> mCardList;
	private int mScore;
	private int mPlayerNumber;
	
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
	
	public void setPlayerNumber(int number){
		mPlayerNumber = number;
	}
	
	public int getPlayerNumber(){
		return mPlayerNumber;
	}
}
