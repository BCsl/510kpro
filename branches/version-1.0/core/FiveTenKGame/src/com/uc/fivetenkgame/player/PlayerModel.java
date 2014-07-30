package com.uc.fivetenkgame.player;

import java.util.ArrayList;

import android.util.Log;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * player����ģ��
 * 
 * @author fuyx
 *
 */

public class PlayerModel {

	private String TAG = "player model";
	private ArrayList<Card> mCardList;// �������
	private int mScore;// ��ҷ���
	private int mPlayerNumber;// ������
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
