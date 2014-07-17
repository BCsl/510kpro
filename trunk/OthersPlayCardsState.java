package com.uc.fivetenkgame.state;

import java.util.List;
import android.R.integer;
import android.annotation.SuppressLint;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.entity.Card;

public class OthersPlayCardsState implements State{
	String TAG = "OthersPlayCardsState";
	Player mPlayer;
	String playerNumber, tableScore;
	String[] outList, remainCards;
	
	public OthersPlayCardsState(Player player,String[] outlist, String playernumber,
			String tablescore, String[] remaincards){
		mPlayer = player;
		playerNumber = playernumber;
		tableScore = tablescore;
		outList = outlist;
		remainCards = remaincards;
	}
	
	@SuppressLint("UseValueOf")
	@SuppressWarnings("null")
	@Override
	public void handle() {
		//设置outList
		List<Card> cardList = null;
		for(int i=0, count=remainCards.length; i<count; i++){
			cardList.add(new Card(outList[i]));
		}
		mPlayer.getIViewControler().setPlayersOutList(Integer.parseInt(playerNumber), cardList);
		
		//设置剩余牌数
		List<Integer> cardScore = null;
		cardScore.add(new Integer(remainCards[0]));
		cardScore.add(new Integer(remainCards[1]));
		cardScore.add(new Integer(remainCards[2]));
		mPlayer.getIViewControler().setCardNumber(cardScore);
		
		//设置当前分数
		mPlayer.getIViewControler().setGameScore(Integer.parseInt(tableScore));
		
		mPlayer.setState(new WaitForMsgState(mPlayer));
	}
}
