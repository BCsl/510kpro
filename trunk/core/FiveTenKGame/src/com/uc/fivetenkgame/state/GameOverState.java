package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
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
	
	private OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener(){

		@Override
		public void reveiveMessage(String msg) {
			if( (msg.startsWith(Common.WINNING_PLAYER)) && 
					(Integer.parseInt(msg.substring(2)) == mPlayer.getPlayerModel().getPlayerNumber()) )
				mPlayer.setState(new WinningState());
		}
		
	};
}
