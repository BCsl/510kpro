package com.uc.fivetenkgame.player;

import java.util.ArrayList;

import android.util.Log;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * player数据模型
 * 
 * @author fuyx
 *
 */

public class PlayerModel {

	private String TAG = "player model";
	private ArrayList<Card> mCardList;// 玩家手牌
	private int mScore;// 玩家分数
	private int mPlayerNumber;// 玩家序号
	private String mPlayerName;

	public PlayerModel() {
		mCardList = new ArrayList<Card>();
		mScore = 0;
		mPlayerNumber = 0;
	}

	public int getScore() {
		return mScore;
	}

	public ArrayList<Card> getCardList() {
		return mCardList;
	}

	public int getRemainCardsNum() {
		return mCardList.size();
	}

	public int getPlayerNumber() {
		return mPlayerNumber;
	}

	public String getPlayerName() {
		return mPlayerName;
	}

	public void setCardList(ArrayList<Card> cardList) {
		mCardList = cardList;
	}

	public void setScore(int score) {
		mScore = score;
	}

	public void setPlayerNumber(int playerNumber) {
		mPlayerNumber = playerNumber;
	}

	public void setPlayerName(String playerName) {
		mPlayerName = playerName;
		Log.i(TAG, "my player name: " + mPlayerName);
	}

}
