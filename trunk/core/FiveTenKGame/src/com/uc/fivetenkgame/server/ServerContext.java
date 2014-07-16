package com.uc.fivetenkgame.server;

import com.uc.fivetenkgame.network.NetworkManager;
import com.uc.fivetenkgame.state.ServerState;

public interface ServerContext {
	public NetworkManager getNetworkManager();
	public void setState(ServerState state);
}
