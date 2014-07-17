package com.uc.fivetenkgame.state;

import java.util.List;

import com.uc.fivetenkgame.player.Player;

public class RoundEndState implements State{
	String TAG = "RoundEndState";
	Player mPlayer;
	String[] playerScore;
	
	public RoundEndState(Player player, String[] playerscore){
		mPlayer = player;
		playerScore = playerscore;
	}
	
	@Override
	public void handle() {
		List<Integer> score = null;
		score.add(new Integer(playerScore[0]));
		score.add(new Integer(playerScore[1]));
		score.add(new Integer(playerScore[2]));
		mPlayer.getIViewControler().setScroeList(score);
		mPlayer.setState(new WaitForMsgState(mPlayer));
	}

}
