package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * player初始化状态,向server发起连接
 * 
 * @author lm
 *
 */
public class InitState extends PlayerState {
	String TAG = "InitState";

	public InitState(PlayerContext context) {
		super(context);
	}

	/**
	 * 
	 * @param msg
	 * 				server ip
	 */
	@Override
	public void handle(String msg) {
		if(msg==null){//记录日志，此状态下不能为null
			Log.i("开始状态", "msg(ip)为null");
			
		}else{
			Log.i(TAG, msg);
			mPlayerContext.initNetwork(msg);//连接到server
			mPlayerContext.setState(new ConnectState(mPlayerContext));
			mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
		}
	}

}
