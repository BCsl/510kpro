package com.uc.fivetenkgame.common;
/**
 * 用来匹配和解析Common信息
 * @author lm
 *
 */
public class CommonMsgDecoder{

    private static int playerCount = 3;

    /**
     * 判断信号的标志是否一样
     * @param sourceMsg 想要判断的信息，源信息
     * @param commonMsg 消息标志
     * @return sourceMsg属于commonMsg标志，返回true，否则false
     */
    public static boolean checkMessage(String sourceMsg, String commonMsg) {
        return sourceMsg.startsWith(commonMsg);
    }

    /**
     * 获得玩家序号
     * @param sourceMsg 源信息
     * @return 玩家序号,若不包含序号，返回-1
     */
    public static int getPlayerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_ACCEPTED)
                || checkMessage(sourceMsg, NetworkCommon.GIVE_UP)
                || checkMessage(sourceMsg, NetworkCommon.YOUR_TURN)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.BEGIN_GAME)
                || checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NAME)
                && !sourceMsg.substring(sourceMsg.indexOf(',') + 1).contains(
                        ",")) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            if (sourceMsg.contains(",")) { // 包含赢家号和各玩家分数
                return Integer.parseInt(sourceMsg.substring(
                        sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(','))
                        .trim());
            } else { // 包含退出玩家的序号
                return Integer.parseInt(sourceMsg.substring(
                        sourceMsg.indexOf('#') + 1).trim());
            }
        }
        return -1;
    }

    /**
     * 获得牌号
     * @param sourceMsg 源信息
     * @return 牌号（玩家出的牌，初始牌等），若不包含，返回null
     */
    public static String[] getCards(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.BEGIN_GAME)) {
            return sourceMsg.substring(sourceMsg.indexOf(',') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAY_CARDS)) {
            return sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String msg = sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim();

            String str[] = new String((msg.substring(msg.indexOf(',') + 1)))
                    .split(",");

            String[] outList = new String[str.length - 4];
            for (int i = 0, count = str.length - 4; i < count; i++) {
                outList[i] = str[i];
            }

            return outList;
        }
        return null;
    }

    /**
     * 获得玩家名字
     * @param sourceMsg 源信息
     * @return 玩家名字（1个或多个），若不包含，返回null
     */
    public static String[] getPlayerNames(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NAME)) {
            String[] names = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");
            if (names.length > 2) {// player接收
                return names;
            } else if (names.length == 2) {// server接收
                String[] singleName = new String[1];
                singleName[0] = names[1];
                return singleName;
            }
        }
        return null;
    }

    /**
     * 获得各玩家分数
     * @param sourceMsg 源信息
     * @return 玩家分数（回合结束或游戏结束），若不包含，返回null
     */
    public static String[] getPlayerScores(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.ROUND_END)) {
            return sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return sourceMsg.substring(sourceMsg.indexOf(',') + 1).trim()
                    .split(",");
        }
        return null;
    }

    /**
     * 获得赢家序号
     * @param sourceMsg 源信息
     * @return 游戏结束时的赢家序号，只有GAME_OVER信号支持，其他信号则返回-1
     */
    public static int getWinnerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        }
        return -1;
    }

    /**
     * 获得当前已加入的玩家数
     * @param sourceMsg 源信息
     * @return 目前已加入游戏的玩家数，只有PLAYER_NUMBER_UPDATE信号支持，其他信号返回-1
     */
    public static int getUpdatePlayerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NUMBER_UPDATE)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1).trim());
        }
        return -1;
    }

    /**
     * 获得桌面分数
     * @param sourceMsg 源信息
     * @return 目前一轮的桌面分数，只有PLAY_END信号支持，其他信号返回-1
     */
    public static int getTableScore(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String str[] = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");

            return Integer.parseInt(str[str.length - playerCount - 1]);
        }
        return -1;
    }

    /**
     * 获得各玩家剩余牌数
     * @param sourceMsg 源信息
     * @return 各玩家剩余的牌数，只有PLAY_END信号支持，其他信号返回null
     */
    public static String[] getRemainCardNumbers(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String str[] = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");
            String[] remainCards = new String[playerCount];
            for (int i = 0; i < playerCount; i++) {
                remainCards[i] = str[str.length - playerCount + i];
            }
            return remainCards;
        }
        return null;
    }

}
