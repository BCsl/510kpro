package com.uc.fivetenkgame.state.playerstate;

import android.annotation.SuppressLint;
import android.util.Log;
import com.uc.fivetenkgame.player.PlayerContext;

public class OthersPlayCardsState extends PlayerState{
	String TAG = "OthersPlayCardsState";
	
	public OthersPlayCardsState(PlayerContext context){
		super(context);
	}
	
	@SuppressLint("UseValueOf")
	@Override
	public void handle(String msg) {
		String playerNumber = msg.substring(0,1);
		
		String str[] = new String( (msg.substring(2,msg.length())) ).split(",");
		if(str == null)
			return;
		
		String[] outList = new String[str.length-4];
		for(int i=0, count=str.length-4; i<count; i++){
			outList[i] = str[i];
		}
		
		String tableScore = new String();
		tableScore = str[str.length-4];
		
		String[] remainCards = new String[3];
		remainCards[0] = str[str.length-3];
		remainCards[1] = str[str.length-2];
		remainCards[2] = str[str.length-1];
		Log.i(TAG, tableScore+" £¿= "+str[str.length-4]);
		
		mPlayerContext.playCardsEndAction(outList, playerNumber, tableScore, remainCards);
		mPlayerContext.setState(new WaitForMsgState(mPlayerContext));
		
	}
}
