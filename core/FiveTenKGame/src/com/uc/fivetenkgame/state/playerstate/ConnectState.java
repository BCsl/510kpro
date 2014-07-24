package com.uc.fivetenkgame.state.playerstate;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.player.PlayerContext;

/**
 *  等待server的消息，确定是否连接成功
 * @author lm
 *
 */
public class ConnectState extends PlayerState {
	public ConnectState(PlayerContext context) {
		super(context);
		//mThread.start();
	}

	/**
	 * 
	 * @param msg
	 * 				成功信号或者失败信号
	 */
	@Override
	public void handle(String msg) {
		if(msg==null){//有上一状态（initState）跳转过来，暂不处理
			
		}else if (msg.startsWith(Common.PLAYER_ACCEPTED)) {// 连接成功，处理msg后跳转到等待开始状态
			Log.i("连接状态", msg);
			
			int playerNumber = Integer.parseInt(msg.substring(2,3).trim());
			mPlayerContext.setPlayerNumber(playerNumber);//设置玩家序号
			mPlayerContext.setState(new WaitForStartingState(mPlayerContext));
			mPlayerContext.handle(null);

		} else if (msg.startsWith(Common.PLAYER_REFUSED)) {
			// 连接失败，跳转到开始界面
			mPlayerContext.getHandler().obtainMessage(Common.HOST_FULL).sendToTarget();
		} else if (msg.startsWith(Common.GAME_OVER)){
			mPlayerContext.getHandler().obtainMessage(Common.PLAYER_LEFT).sendToTarget();
		}
	}
	
	private Thread mThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Log.i("TimeOut", null);
			mPlayerContext.getHandler().obtainMessage(Common.TIME_OUT).sendToTarget();;
			mPlayerContext.timeOutAction();
		}
	});
}
