package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 * player��ʼ��״̬,��server��������
 * 
 * @author lm
 *
 */
public class InitState extends PlayerState {
	String tag = "InitState";

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
	    Log.i(tag, "msg is " + msg);
		if(msg==null){//��¼��־����״̬�²���Ϊnull
			Log.i("��ʼ״̬", "msg(ip)Ϊnull");
		}else{
			mPlayerContext.initNetwork(msg);//���ӵ�server
			mPlayerContext.setState(new ConnectState(mPlayerContext));
			mPlayerContext.handle(NetworkCommon.PLAYER_STATE_CHANGE);
		}
	}

}
