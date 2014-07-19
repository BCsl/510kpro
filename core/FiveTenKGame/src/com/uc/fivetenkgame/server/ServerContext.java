package com.uc.fivetenkgame.server;

import java.util.ArrayList;

import android.os.Handler;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.state.serverstate.ServerState;

public interface ServerContext {
	public NetworkManager getNetworkManager();
	public void setState(ServerState state);
	public Handler getHandler();
	public void setClientNum(int num);
	public int getClientNum();
	public void setPlayerModel(ArrayList<PlayerModel> playerModelList);
	public ArrayList<PlayerModel> getPlayerModel();
	public int getCurrentPlayerNumber();
	public void setCurrentPlayerNumber(int CurrentPlayerNumber) ;
	public int getRoundScore() ;
	public void setRoundScore(int mRoundScore) ;
	public void handleMessage(String msg);
}
