package com.uc.fivetenkgame.common;

public class CommonMsgDecoder implements ICommonMsgDecoder {

    private int playerCount = 3;

    @Override
    public boolean checkMessage(String sourceMsg, String commonMsg) {
        return sourceMsg.startsWith(commonMsg);
    }

    @Override
    public int getPlayerNumber(String sourceMsg) {
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

    @Override
    public String[] getCards(String sourceMsg) {
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

    @Override
    public String[] getPlayerNames(String sourceMsg) {
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

    @Override
    public String[] getPlayerScores(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.ROUND_END)) {
            return sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return sourceMsg.substring(sourceMsg.indexOf(',') + 1).trim()
                    .split(",");
        }
        return null;
    }

    @Override
    public int getWinnerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        }
        return -1;
    }

    @Override
    public int getUpdatePlayerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NUMBER_UPDATE)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1).trim());
        }
        return -1;
    }

    @Override
    public int getTableScore(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String str[] = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");

            return Integer.parseInt(str[str.length - playerCount - 1]);
        }
        return -1;
    }

    @Override
    public String[] getRemainCardNumbers(String sourceMsg) {
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
