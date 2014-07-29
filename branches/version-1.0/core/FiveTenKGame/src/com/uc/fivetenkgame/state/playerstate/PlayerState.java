package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.player.PlayerContext;
import com.uc.fivetenkgame.state.State;

public abstract class PlayerState implements State{

	protected PlayerContext mPlayerContext;
	
	public PlayerState(PlayerContext context){
		this.mPlayerContext = context;
	}
	@Override
	public String toString() {
		String name=this.getClass().getName();
		return "µ±Ç°×´Ì¬£º"+name.substring(name.lastIndexOf("."));
	}
}
