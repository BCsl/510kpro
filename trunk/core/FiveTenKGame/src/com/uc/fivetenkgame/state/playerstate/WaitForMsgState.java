package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

public class WaitForMsgState extends PlayerState{
	String TAG = "WaitForMsgState";
	boolean isFirstPlayCard = true;
	
	public WaitForMsgState(PlayerContext player){
		super(player);
	}
	
	@Override
	public void handle(String msg) {
		if( msg == null )
			return ;
		
		//得到出牌信息
		if( (msg.startsWith(Common.YOUR_TURN)) && 
				(Integer.parseInt(msg.substring(2,3)) == mPlayerContext.getPlayerNumber()) ){
			if(isFirstPlayCard){
				mPlayerContext.setFirstPlayer();
				isFirstPlayCard = false;
			}
			mPlayerContext.setState(new SelectCardState(mPlayerContext));
			mPlayerContext.handle(null);
			Log.i(TAG, msg);
		}
		//得到其他玩家出牌信息
		if( msg.startsWith(Common.PLAY_END) ){
			mPlayerContext.setState(new OthersPlayCardsState(mPlayerContext));
			mPlayerContext.handle(msg.substring(2, msg.length()));
			Log.i(TAG, msg);
		}	
		//得到回合结束信息
		if( msg.startsWith(Common.ROUND_END) ){
			mPlayerContext.setState(new RoundEndState(mPlayerContext));
			mPlayerContext.handle(msg.substring(2, msg.length()));
			Log.i(TAG, msg);
		}
		//得到游戏结束信息
		if( msg.startsWith(Common.GAME_OVER) ){
			mPlayerContext.setState(new GameOverState(mPlayerContext));
			mPlayerContext.handle(msg.substring(2,3));
			Log.i(TAG, msg);
		}	
	}
}
