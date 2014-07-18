package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.player.PlayerContext;

/**
 * player��ʼ��״̬,��server��������
 * 
 * @author lm
 *
 */
public class InitState extends PlayerState {

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
		if(msg==null){//��¼��־����״̬�²���Ϊnull
			Log.i("playerInitState", "msgΪnull");
			
		}else{
			mPlayerContext.initNetwork(msg);//���ӵ�server
			mPlayerContext.setState(new ConnectState(mPlayerContext));
			mPlayerContext.handle(null);
		}
	}

}
