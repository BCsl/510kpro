package com.uc.fivetenkgame.state;

import android.util.Log;

import com.uc.fivetenkgame.network.util.Common;
import com.uc.fivetenkgame.network.util.OnReceiveMessageListener;
import com.uc.fivetenkgame.player.Player;

public class WaitForMsgState implements State{
	String TAG = "WaitForMsgState";
	Player mPlayer;
	
	public WaitForMsgState(Player player){
		mPlayer = player;
	}
	
	@Override
	public void handle() {
		mPlayer.getNetworkManager().setOnReceiveMessage(mReceiveMessage);
	}
	
	public OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {
		public void reveiveMessage(String msg) {
			//得到出牌信息
			if( (msg.startsWith(Common.YOUR_TURN)) && 
					(Integer.parseInt(msg.substring(2)) == mPlayer.getPlayerModel().getPlayerNumber()) ){
				mPlayer.setState(new SelectCardState());
				Log.i(TAG, msg);
			}
			//得到其他玩家出牌信息
			if( msg.startsWith(Common.PLAY_END) ){
				
				String str[] = new String(msg.substring(2,msg.length())).split(",");
				String[] outCards = null;
				for(int i=0, count=outCards.length; i<count; i++){
					outCards[i] = str[i];
				}
				
				String playerNumber = msg.substring(2);
				String tableScore = str[outCards.length];
				
				String[] playerRemaindCards = new String[3];
				playerRemaindCards[0] = str[str.length-3];
				playerRemaindCards[1] = str[str.length-2];
				playerRemaindCards[2] = str[str.length-1];
				Log.i(TAG, tableScore+" ？= "+str[str.length-4]);
				
				mPlayer.setState(new OthersPlayCardsState(mPlayer,outCards,playerNumber,
						tableScore, playerRemaindCards));
				Log.i(TAG, msg);
			}	
			//得到回合结束信息
			if( msg.startsWith(Common.ROUND_END) ){
				String str = msg.substring(2, msg.length());
				String[] playerScore = str.split(",");
				mPlayer.setState(new RoundEndState(mPlayer,playerScore));
				Log.i(TAG, msg);
			}
			//得到游戏结束信息
			if( msg.startsWith(Common.GAME_OVER) ){
				mPlayer.setState(new GameOverState(mPlayer));
				Log.i(TAG, msg);
			}	
		}
	};

}
