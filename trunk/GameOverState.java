package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.player.Player;

public class GameOverState implements State{
	Player mPlayer;
	
	public GameOverState(Player player){
		mPlayer = player;
	}
	
	@Override
	public void handle() {
		//暂时不允许重玩
		mPlayer.setState(new EndState());
	}

}
