package com.uc.fivetenkgame.player;

import java.util.ArrayList;
import java.util.List;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * player����ģ��
 * 
 * @author liuzd, fuyx
 *
 */

public class PlayerModel {
	
	private ArrayList<Card> mCardList;
	private int mScore;
	private int mPlayerNumber;//������
	
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