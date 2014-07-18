package com.uc.fivetenkgame.state.playerstate;

import java.util.List;

import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.player.PlayerContext;

public class RoundEndState extends PlayerState{
	String TAG = "RoundEndState";
	
	public RoundEndState(PlayerContext player){
		super(player);
	}
	
	@Override
	public void handle(String msg) {
		String[] playerScore = msg.split(",");
		mPlayerContext.roundEndAction(playerScore);
		mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
	}

}
