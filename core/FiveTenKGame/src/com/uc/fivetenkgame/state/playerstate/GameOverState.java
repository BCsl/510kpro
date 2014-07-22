package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * 本次游戏结束，可选择退出或重玩
 * @author lm
 *
 */
public class GameOverState extends PlayerState {

	public GameOverState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg 赢家序号
	 */
	@Override
	public void handle(String msg) {
<<<<<<< .mine
		if(msg.startsWith(Common.GAME_OVER)){ //有上一状态（WaitForMsg）跳转而来，让玩家选择退出或重玩
			mPlayerContext.gameOver(Integer.parseInt(msg.substring(2, 3)));
=======
		if(msg==null){ //记录日志，此状态下不能为null
			 Log.i("PlayerGameOverState","msg为null");
			
		}else{
//			mPlayerContext.gameOver(Integer.parseInt(msg));
			mPlayerContext.getHandler().obtainMessage(Common.END_GAME, Integer.parseInt(msg)).sendToTarget();
>>>>>>> .r128
			Log.i("GameOverState", msg);
		}
	}

}
