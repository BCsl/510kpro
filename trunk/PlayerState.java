package com.uc.fivetenkgame.state;

import com.uc.fivetenkgame.player.PlayerContext;

/**
 * �������ã�����ɾ��
 * @author fuyx
 *
 */
public abstract class PlayerState{
	/*
	protected static InitState initState = new InitState();
	protected static ConnectState connectState = new ConnectState();
	protected static WaitForStartState waitForStartState = new WaitForStartState();
	protected static WaitForMsgState waitForMsgState = new WaitForMsgState();
	protected static GameOverState gameOverState = new GameOverState();
	protected static OthersPlayCardsState othersPlayCardsState =
			new OthersPlayCardsState();
	protected static SelectCardState selectCardState = new SelectCardState();
	protected static RoundEndState roundEndState = new RoundEndState();
	protected static EndState endState = new EndState();
	*/
	
	protected PlayerContext mPlayerContext;
	public abstract void handle();
}
