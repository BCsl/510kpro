package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.player.Player;

public class GameOverState implements State{
	Player mPlayer;
	
	public GameOverState(Player player){
		mPlayer = player;
	}
	
	@Override
	public void handle() {
		//��ʱ����������
		mPlayer.setState(new EndState());
	}

}
