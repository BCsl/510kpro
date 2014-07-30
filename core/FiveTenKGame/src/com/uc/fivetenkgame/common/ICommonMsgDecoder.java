package com.uc.fivetenkgame.common;

public interface ICommonMsgDecoder {

    /**
     * 
     * @param sourceMsg 想要判断的信息，源信息
     * @param commonMsg 消息标志
     * @return sourceMsg属于commonMsg标志，返回true，否则false
     */
    boolean checkMessage(String sourceMsg, String commonMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 玩家序号,若不包含序号，返回-1
     */
    int getPlayerNumber(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 牌号（玩家出的牌，初始牌等），若不包含，返回null
     */
    String[] getCardsNumber(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 玩家名字（1个或多个），若不包含，返回null
     */
    String[] getPlayerNames(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 玩家分数（回合结束或游戏结束），若不包含，返回null
     */
    String[] getPlayerScores(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 游戏结束时的赢家序号，只有GAME_OVER信号支持，其他信号则返回-1
     */
    int getWinnerNumber(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 目前已加入游戏的玩家数，只有PLAYER_NUMBER_UPDATE信号支持，其他信号返回-1
     */
    int getUpdatePlayerNumber(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 目前一轮的桌面分数，只有PLAY_END信号支持，其他信号返回-1
     */
    int getTableScore(String sourceMsg);
    
    /**
     * 
     * @param sourceMsg 源信息
     * @return 各玩家剩余的牌数，只有PLAY_END信号支持，其他信号返回null
     */
    String[] getRemainCards(String sourceMsg);
}
