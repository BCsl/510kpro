package com.uc.fivetenkgame.state.serverstate;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.player.PlayerModel;
import com.uc.fivetenkgame.server.ServerContext;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * 
 * @author chensl@ucweb.com
 * 
 *         下午4:59:13 2014-7-17
 */
public class WaitingState extends ServerState {
    private String TAG = "WaitingState";
    private int giveUpTimes;
    private int GIAVE_UP_TIME_LIMITE; // 本轮的结束位,如当前玩家还有3人，连续结束2次则本轮结束。
    
    private int[] noCardsPlayerNumbers;
    //缴纳分数值
    private static final int GIVE_SCORE = 30;
    
    public WaitingState(ServerContext context) {
        mServerContext = context;
        giveUpTimes = 0;
        GIAVE_UP_TIME_LIMITE = NetworkCommon.TOTAL_PLAYER_NUM - 1;
        noCardsPlayerNumbers = new int[NetworkCommon.TOTAL_PLAYER_NUM];
    }

    @Override
    public void handle(String msg) {
        Log.i(TAG, "server's msg is " + msg);
        if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAY_CARDS)) {
            // 首先更新当前出牌玩家信息，然后判断游戏是否结束
            String[] cards = CommonMsgDecoder.getCards(msg);
            List<Card> cardList = getCardList(cards);
            updateRoundScore(cardList);
            updatePlayerModle(cardList);
            sendToOtherPlayer(cards);
            if (isCurrentPlayerHasNoCards())
                playerFinalRoundOver();
            if (gameIsOver()) {
            	giveScoreToFirstPlayer();
                GameEndState state = new GameEndState(mServerContext);
                mServerContext.setState(state);
                state.handle(NetworkCommon.GAME_END);
            } else
                callNextPlayer();
            giveUpTimes = 0;
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {
            ++giveUpTimes;
            giveUpAction();
            if (giveUpTimes == GIAVE_UP_TIME_LIMITE)
                roundOver();
            callNextPlayer();
        } else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_PAUSE)
                || CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_RESUME)
                || CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)) {
            mServerContext.getNetworkManager().sendMessage(msg);
            if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_EXIT)) {
                mServerContext.resetServer();
                mServerContext.setState(new InitState(mServerContext));
            }
        }

    }

    /**
     * 当前玩家把手牌全部出完，也视为该轮结束
     * 
     * @return
     */
    private boolean isCurrentPlayerHasNoCards() {
        if (mServerContext.getPlayerModel()
                .get(mServerContext.getCurrentPlayerNumber() - 1)
                .getRemainCardsNum() == 0) {
        	//记录没牌的玩家号
        	noCardsPlayerNumbers[NetworkCommon.TOTAL_PLAYER_NUM - GIAVE_UP_TIME_LIMITE - 1]
        					= mServerContext.getCurrentPlayerNumber();
            GIAVE_UP_TIME_LIMITE--;
            return true;
        }
        return false;
    }

    /**
     * 当前玩家的最后一轮结束（没牌）,分数应该加到最后出牌的玩家上
     */
    private void playerFinalRoundOver() {
        PlayerModel player = mServerContext.getPlayerModel().get(
                mServerContext.getCurrentPlayerNumber() - 1);
        player.setScore(player.getScore() + mServerContext.getRoundScore());
        mServerContext.setRoundScore(0);
        StringBuilder res = new StringBuilder();
        res.append(NetworkCommon.ROUND_END);
        for (PlayerModel temp : mServerContext.getPlayerModel())
            res.append(temp.getScore() + ",");
        mServerContext.getNetworkManager().sendMessage(
                res.deleteCharAt(res.length() - 1).toString());

    }

    /**
     * 普通的本轮结束，统计分数，并转发，本轮分数清零
     */
    private void roundOver() {
        int nextPlayer = getNextPlayerId();
        PlayerModel player = mServerContext.getPlayerModel()
                .get(nextPlayer - 1);
        player.setScore(player.getScore() + mServerContext.getRoundScore());
        mServerContext.setRoundScore(0);
        StringBuilder res = new StringBuilder();
        res.append(NetworkCommon.ROUND_END);
        for (PlayerModel temp : mServerContext.getPlayerModel())
            res.append(temp.getScore() + ",");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mServerContext.getNetworkManager().sendMessage(
                res.deleteCharAt(res.length() - 1).toString());

    }

    /**
     * 转发当前玩家放弃出牌操作
     */
    private void giveUpAction() {
        mServerContext.getNetworkManager()
                .sendMessage(
                        NetworkCommon.GIVE_UP
                                + mServerContext.getCurrentPlayerNumber());
    }

    /**
     * 根据当前出牌，更新本轮分数
     * 
     * @param cardList
     */
    private void updateRoundScore(List<Card> cardList) {
       int add= countScore(cardList);
        mServerContext.setRoundScore(mServerContext.getRoundScore() + add);
    }
    
    
    /**
     * 获取对象牌中的分数
     * @param cardList
     * @return
     */
    private int countScore(List<Card> cardList) {
    	int res=0;
      for (Card temp : cardList) {
      int id = Integer.valueOf(temp.getCardId());
      if (id > 52)
          continue;
      switch (id % 13) {
      case 5:
    	  res += 5;
          break;
      case 10:
      case 0:
    	  res += 10;
          break;
      default:
    	  res += 0;
          break;
      }
  }
		return res;
	}

	/**
     * 判断游戏是否结束
     * 
     * @return
     */
    private boolean gameIsOver() {
        return GIAVE_UP_TIME_LIMITE <= 0;
    }

    /**
     * 更新当前出牌用户的个人信息,包括拥有的牌，拥有牌的数量
     * 
     * @param cardList
     */
    private void updatePlayerModle(List<Card> cardList) {
        PlayerModel model = mServerContext.getPlayerModel().get(
                mServerContext.getCurrentPlayerNumber() - 1);
        // model.getCardList().removeAll(cardList);
        Log.i(TAG, "to remove " + (mServerContext.getCurrentPlayerNumber() - 1)
                + " cards of:" + cardList);
        CardUtil.removeCards(model.getCardList(), cardList);
    }

    /**
     * 发送消息，通知其他玩家当前玩家出牌的信息，本轮分数，玩家牌数量
     */
    private void sendToOtherPlayer(String[] cards) {
        StringBuilder res = new StringBuilder();
        res.append(mServerContext.getCurrentPlayerNumber() + ",");
        for (String card : cards) {
            res.append(card).append(',');
        }
        res.append(mServerContext.getRoundScore() + ",");
        for (PlayerModel model : mServerContext.getPlayerModel())
            res.append(model.getRemainCardsNum() + ",");
        mServerContext.getNetworkManager().sendMessage(
                NetworkCommon.PLAY_END + res.substring(0, res.length() - 1));
    }

    /**
     * 从消息中获取所出的牌
     * 
     * @param str
     * @return
     */
    private List<Card> getCardList(String[] str) {
        List<Card> list = new ArrayList<Card>(str.length);
        for (int i = 0; i < str.length; i++) {
            list.add(new Card(str[i]));
        }
        return list;
    }

    /**
     * 判断并转发下一个玩家出牌
     */
    private void callNextPlayer() {
        int nextPlayer = getNextPlayerId();
        mServerContext.setCurrentPlayerNumber(nextPlayer);
        mServerContext.getNetworkManager().sendMessage(
                NetworkCommon.YOUR_TURN + nextPlayer);
    }

    private int getNextPlayerId() {
        int nextPlayer = 0;
        if (mServerContext.getCurrentPlayerNumber() == 3)
            nextPlayer = mServerContext.getPlayerModel().get(0)
                    .getRemainCardsNum() != 0 ? 1 : 2;
        else if (mServerContext.getCurrentPlayerNumber() == 1)
            nextPlayer = mServerContext.getPlayerModel().get(1)
                    .getRemainCardsNum() != 0 ? 2 : 3;
        else if (mServerContext.getCurrentPlayerNumber() == 2)
            nextPlayer = mServerContext.getPlayerModel().get(2)
                    .getRemainCardsNum() != 0 ? 3 : 1;
        Log.i(TAG, "转发下一个玩家：" + nextPlayer);
        return nextPlayer;
    }
    
    /**
     * 最后一名玩家给第一名玩家缴分
     * 
     */
    private void giveScoreToFirstPlayer(){
    	
    	int firstPlayer = noCardsPlayerNumbers[0];
    	int secondPlayer = noCardsPlayerNumbers[1];
    	ArrayList<PlayerModel> playerModels = mServerContext.getPlayerModel();
    	PlayerModel firstPlayerModle=playerModels.get(firstPlayer-1);
    	for (PlayerModel model : playerModels){
    		//最后一名玩家扣除相应分数
    		if( model.getPlayerNumber() != firstPlayer 
    				&& model.getPlayerNumber() != secondPlayer ){
    			//获取最后一个玩家手头上的牌的分数并附加到第一个玩家上
    			firstPlayerModle.setScore(firstPlayerModle.getScore() + countScore(model.getCardList()));
    		}
    	}
    }

}
